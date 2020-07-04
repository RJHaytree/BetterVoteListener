package io.github.rjhaytree.bettervotelistener.storage;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.PluginInfo;
import io.github.rjhaytree.bettervotelistener.config.ConfigManager;
import io.github.rjhaytree.bettervotelistener.managers.model.VoteData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Storage {
    private BetterVoteListener plugin;

    // Table creation queries.
    private final String CREATE_PLAYERS_TBL = "CREATE TABLE IF NOT EXISTS `tbl_players`(`id` INT NOT NULL AUTO_INCREMENT, `uuid` VARCHAR(36) NOT NULL, `total` INT NOT NULL, `last_vote` DATETIME NOT NULL, PRIMARY KEY (`id`), UNIQUE (`uuid`))";
    private final String CREATE_UNCLAIMED_TBL = "CREATE TABLE IF NOT EXISTS `tbl_unclaimed`(`id` INT NOT NULL AUTO_INCREMENT, `uuid` VARCHAR(36) NOT NULL, `count` INT NOT NULL, PRIMARY KEY (`id`), UNIQUE (`uuid`))";

    // Data manipulation queries.
    private final String ADD_PLAYER = "INSERT INTO `tbl_players` VALUES (NULL, ?, ?, NOW())";
    private final String ADD_UNCLAIMED_VOTE = "INSERT INTO `tbl_unclaimed` VALUES (NULL, ?, 1)";
    private final String VOID_UNCLAIMED_VOTES = "DELETE FROM `tbl_unclaimed` WHERE `uuid` = ?";
    private final String GET_PLAYER_DATA = "SELECT * FROM `tbl_players` WHERE `uuid` = ?";
    private final String GET_UNCLAIMED_VOTES = "SELECT COUNT(*) FROM `tbl_unclaimed` WHERE `uuid` = ?";
    private final String ADD_VOTE_TO_PLAYER = "UPDATE `tbl_players` SET `total` = ?, `last_vote` = ? WHERE `uuid` = ?";
    private final String CHECK_IF_PLAYER_EXISTS = "SELECT * FROM `tbl_players` WHERE `uuid` = ?";
    private final String GET_ALL_VOTES = "SELECT `total` FROM `tbl_players`";

    public Storage(BetterVoteListener plugin) {
        this.plugin = plugin;

        attemptTableCreation();
        if (ConfigManager.STORAGE_METHOD.toLowerCase().equals("mysql")) {
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "MySQL storage initialized successfully."));
        }
        else {
            Sponge.getServer().getConsole().sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, "H2 storage initialized successfully."));
        }
    }

    /**
     * Attempt to construct database tables. Only used on startup.
     */
    private void attemptTableCreation() {
        try (Connection connection = SQLConnection.getSqlConnection()) {
            PreparedStatement ps = connection.prepareStatement(CREATE_PLAYERS_TBL);
            ps.execute();

            ps = connection.prepareStatement(CREATE_UNCLAIMED_TBL);
            ps.execute();
        }
        catch (SQLException e) {
            plugin.getLogger().error("Database table could not be created!");
            e.printStackTrace();
        }
    }

    /**
     * Check whether a specific player exists in storage.
     * If a player has no existing data, it means they haven't voted since
     * this plugin was installed.
     *
     * @param player Player being queried.
     * @return Whether the player has existing data.
     */
    public CompletableFuture<Boolean> checkIfPlayerExists(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            boolean exists = false;
            try (Connection connection = SQLConnection.getSqlConnection()) {
                PreparedStatement ps = connection.prepareStatement(CHECK_IF_PLAYER_EXISTS);
                ps.setString(1, player.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) exists = true;
            }
            catch (SQLException e) {
                plugin.getLogger().error("Failed to check if player already exists!");
                e.printStackTrace();
            }

            return false;
        });
    }

    /**
     * Check whether a specific player exists in storage.
     * If a player has no existing data, it means they haven't voted since
     * this plugin was installed.
     *
     * @param uuid The UUID being queried.
     * @return Whether a player with this UUID has existing data.
     */
    public CompletableFuture<Boolean> checkIfPlayerExists(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            boolean exists = false;
            try (Connection connection = SQLConnection.getSqlConnection()) {
                PreparedStatement ps = connection.prepareStatement(CHECK_IF_PLAYER_EXISTS);
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) exists = true;
            }
            catch (SQLException e) {
                plugin.getLogger().error("Failed to check if player already exists!");
                e.printStackTrace();
            }

            return false;
        });
    }

    /**
     * Create a new entry in the database for a specific player.
     * This method should only be used when a player has voted for the
     * first time. In order to check this, see:
     * {@link #checkIfPlayerExists(UUID)} or {@link #checkIfPlayerExists(Player)}.
     *
     * @param player
     * @return
     */
    public CompletableFuture<Void> addPlayer(Player player) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = SQLConnection.getSqlConnection()) {
                PreparedStatement ps = connection.prepareStatement(ADD_PLAYER);
                ps.setString(1, player.getUniqueId().toString());
                ps.setInt(2, 1);
                ps.execute();
            }
            catch (SQLException e) {
                plugin.getLogger().error("Player could not be added to storage!");
                e.printStackTrace();
            }
        });
    }

    /**
     * Get a player's existing VoteData from storage. The player must have an existing records.
     * To check this, see:
     * {@link #checkIfPlayerExists(UUID)} or {@link #checkIfPlayerExists(Player)}.
     *
     * @param player The player being queried.
     * @return an instance of the player's VoteData.
     */
    public CompletableFuture<VoteData> getPlayer(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = SQLConnection.getSqlConnection()) {
                PreparedStatement ps = connection.prepareStatement(GET_PLAYER_DATA);
                ps.setString(1, player.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();

                // Assume only 1 row will be returned despite iterating using `while`.
                while (rs.next()) {
                    int votes = rs.getInt(3);
                    LocalDateTime date = rs.getTimestamp(4).toLocalDateTime();
                    return new VoteData(votes, date);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });
    }

    /**
     * Get a player's vote count. If the player is online, an instance of their VoteData should be in cache,
     * meaning this method is unnecessary.
     *
     * @param uuid The uuid of the offline player.
     * @return The number of votes.
     */
    public CompletableFuture<Integer> getOfflinePlayerVotes(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            int total = 0;

            try (Connection connection = SQLConnection.getSqlConnection()) {
                PreparedStatement ps = connection.prepareStatement(GET_PLAYER_DATA);
                ps.setString(1, uuid.toString());
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    total = rs.getInt(3);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            return total;
        });
    }

    /**
     * This method will return the total number of votes that have occurred since the plugin was installed.
     * This operation may be substantial depending on the size of the database. However, the operation is being conducted async.
     * Despite this, I would not recommend using this method a great deal if you value your server.
     *
     * @return The total number of votes.
     */
    public CompletableFuture<Integer> getTotalStoredVotes() {
        return CompletableFuture.supplyAsync(() -> {
            int total = 0;

            try (Connection connection = SQLConnection.getSqlConnection()) {
                PreparedStatement ps = connection.prepareStatement(GET_ALL_VOTES);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    total += rs.getInt(1);
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            return total;
        });
    }
}
