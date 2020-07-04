package io.github.rjhaytree.bettervotelistener.storage;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.config.ConfigManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.sql.SqlService;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLConnection {
    private static SqlService sql = Sponge.getServiceManager().provideUnchecked(SqlService.class);

    /**
     * Get an SQL connection for the selected storage type.
     * @return
     */
    public static Connection getSqlConnection() {
        try {
            return sql.getDataSource(getUrl()).getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getUrl() {
        if (ConfigManager.STORAGE_METHOD.toLowerCase().equals("mysql")) {
            return "jdbc:mysql://" + ConfigManager.MYSQL_HOST +
                    "/" + ConfigManager.MYSQL_DB +
                    "?user=" + ConfigManager.MYSQL_USER +
                    "&password=" + ConfigManager.MYSQL_PASS;
        }
        else {
            return "jdbc:h2:./" + BetterVoteListener.getInstance().getContainer().getId() + File.separator + "votelistener.db";
        }
    }
}
