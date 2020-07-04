package io.github.rjhaytree.bettervotelistener.config;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    private static CommentedConfigurationNode config;
    private static ConfigurationLoader<CommentedConfigurationNode> loader;
    private static File configFile;

    // Load config options to ensure we don't need to constantly query the file.
    // Storage configuration.
    public static String STORAGE_METHOD;
    public static String MYSQL_HOST;
    public static String MYSQL_USER;
    public static String MYSQL_PASS;
    public static String MYSQL_DB;

    // Vote links
    public static List<String> LINKS;

    // Notification system.
    public static Boolean REMIND_ON_JOIN;
    public static String REMIND_JOIN_MESSAGE;

    // Vote rewards.
    public static Boolean LV_ENABLED;
    public static Boolean RANDOM_REWARDS;
    public static List<String> BASE_COMMANDS;
    public static List<String> LUCKY_COMMANDS;

    public static void init(File defaultConfig) {
        configFile = defaultConfig;
        loader = HoconConfigurationLoader.builder().setPath(defaultConfig.toPath()).build();
    }

    public static void load() {
        try {
            if (!configFile.exists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
                config = loader.load();
                populate();
                loader.save(config);
            }

            config = loader.load();
            loadValues();
        }
        catch (IOException | ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    private static void populate() throws ObjectMappingException {
        // General
        config.getNode("general", "storage-method").setValue("h2");
        config.getNode("general", "storage-method").setComment("Storage methods currently supported: h2, mysql.");

        // MySQL
        config.getNode("mysql", "hostname").setValue("127.0.0.1");
        config.getNode("mysql", "username").setValue("root");
        config.getNode("mysql", "password").setValue("");
        config.getNode("mysql", "database").setValue("bvotelistener_db");
        config.getNode("mysql").setComment("This section is only utilised when the 'mysql' \nstorage method is selected");

        config.getNode("vote-links").setValue(new TypeToken<List<String>>() {
        }, Arrays.asList(
                "https://example.com",
                "https://ftbservers.com",
                "https://pixelmonservers.com"
        ));

        // Notifications
        config.getNode("notifications", "remind-on-join").setValue(true);
        config.getNode("notifications", "remind-on-join").setComment("true or false.");
        config.getNode("notifications", "remind-on-join-message").setValue("You have not voted today! be sure to vote using /vote.");
        config.getNode("notifications").setComment("Notification settings. Determines whether players are reminded to vote on join.");

        // Rewards
        config.getNode("vote-rewards", "lucky-vote-enabled").setValue(false);
        config.getNode("vote-rewards", "lucky-vote-enabled").setComment("Enabling this will allow rewards to be drawn from the lucky-rewards list.\nThe probability of a player being lucky 10%.");
        config.getNode("vote-rewards", "random-rewards-enabled").setValue(false);
        config.getNode("vote-rewards", "base-rewards").setValue(new TypeToken<List<String>>() {
        }, Arrays.asList(
                "give %player% minecraft:diamond 2",
                "eco add %player% 500"
                ));
        config.getNode("vote-rewards", "lucky-rewards").setValue(new TypeToken<List<String>>() {
        }, Arrays.asList(
                "give %player% minecraft:diamond_block 2",
                "eco add %player% 1500"
        ));
    }

    private static void loadValues() throws ObjectMappingException {
        STORAGE_METHOD = config.getNode("general", "storage-method").getString();

        MYSQL_HOST = config.getNode("mysql", "hostname").getString();
        MYSQL_USER = config.getNode("mysql", "username").getString();
        MYSQL_PASS = config.getNode("mysql", "password").getString();
        MYSQL_DB = config.getNode("mysql", "database").getString();

        LINKS = config.getNode("vote-links").getList(TypeToken.of(String.class));

        REMIND_ON_JOIN = config.getNode("notifications", "remind-on-join").getBoolean();
        REMIND_JOIN_MESSAGE = config.getNode("notifications", "remind-on-join-message").getString();

        LV_ENABLED = config.getNode("vote-rewards", "lucky-vote-enabled").getBoolean();
        RANDOM_REWARDS = config.getNode("vote-rewards", "random-rewards-enabled").getBoolean();
        BASE_COMMANDS = config.getNode("vote-rewards", "base-rewards").getList(TypeToken.of(String.class));
        LUCKY_COMMANDS = config.getNode("vote-rewards", "lucky-rewards").getList(TypeToken.of(String.class));
    }

    /**
     * Not all variables should be exposed to a reload, especially those that
     * are involved in caching and database operations. Therefore, it has been decided that
     * only vote links and the 'join' options be exposed/refreshed on reload.
     */
    private static void variableReload() throws IOException, ObjectMappingException {
        // Reload config through the loader.
        config = loader.load();

        // Cache refreshed variables.
        LINKS = config.getNode("vote-links").getList(TypeToken.of(String.class));
        REMIND_ON_JOIN = config.getNode("notifications", "remind-on-join").getBoolean();
        REMIND_JOIN_MESSAGE = config.getNode("notifications", "remind-on-join-message").getString();
    }

    /**
     * Reload select configuration options.
     */
    public static void reload() {
        try {
            variableReload();
        }
        catch (ObjectMappingException | IOException e) {
            e.printStackTrace();
        }
    }
}
