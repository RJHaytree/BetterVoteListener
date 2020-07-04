package io.github.rjhaytree.bettervotelistener.managers;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.managers.model.VoteData;
import org.spongepowered.api.entity.living.player.Player;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class VoteManager {
    private BetterVoteListener plugin;
    private HashMap<UUID, VoteData> voteDataCache;

    public VoteManager(BetterVoteListener plugin) {
        this.plugin = plugin;
        this.voteDataCache = new HashMap<>();
    }

    /**
     * Load a player into cache from storage. If a player does not yet exists, add the player
     * to storage, and then load into cache. We can assume that if the player has no data, they have
     * not yet voted.
     *
     * @param player Player to add.
     */
    public void loadPlayerToCache(Player player) {
        try {
            if (plugin.getStorage().checkIfPlayerExists(player).get()) {
                VoteData vd = plugin.getStorage().getPlayer(player).get();
                voteDataCache.put(player.getUniqueId(), vd);
            }
            else {
                plugin.getStorage().addPlayer(player);
                VoteData vd = new VoteData(1, LocalDateTime.now());
                voteDataCache.put(player.getUniqueId(), vd);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve a player's VoteData. This is from cache, and the
     * presence of the player's data should already be validated. using:
     * {@link #isPlayerInCache(UUID)}
     *
     * @param player The player to query.
     * @return an instance of their VoteData.
     */
    public VoteData getVoteData(Player player) {
        return voteDataCache.get(player.getUniqueId());
    }

    /**
     * Retrieve a player's VoteData. This is from cache, and the
     * presence of the player's data should already be validated. using:
     * {@link #isPlayerInCache(UUID)}
     *
     * @param uuid The player's UUID to query.
     * @return an instance of their VoteData.
     */
    public VoteData getVoteData(UUID uuid) {
        return voteDataCache.get(uuid);
    }

    /**
     * This method is used to see whether the player already had data in the
     * cache, and more importantly, whether they don't. If the player does not
     * have cached data, it means they have no data to load from storage.
     *
     * @param uuid player's uuid to query.
     * @return Whether the player has cached data.
     */
    public boolean isPlayerInCache(UUID uuid) {
        return voteDataCache.containsKey(uuid);
    }

    /**
     * Retrieving the player's total vote count. The player must be cached for this to work.
     * @param player The player to query.
     * @return The number of votes.
     */
    public Integer getVoteCount(Player player) {
        int count = 0;

        if (isPlayerInCache(player.getUniqueId())) {
            count = getVoteData(player).getVoteCount();
        }

        return count;
    }
}
