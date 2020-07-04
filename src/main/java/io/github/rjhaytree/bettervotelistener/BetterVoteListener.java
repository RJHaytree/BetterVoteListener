package io.github.rjhaytree.bettervotelistener;

import com.google.inject.Inject;
import io.github.rjhaytree.bettervotelistener.api.VoteService;
import io.github.rjhaytree.bettervotelistener.commands.CommandRegistrar;
import io.github.rjhaytree.bettervotelistener.config.ConfigManager;
import io.github.rjhaytree.bettervotelistener.managers.VoteManager;
import io.github.rjhaytree.bettervotelistener.storage.Storage;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;
import java.nio.file.Path;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, description = PluginInfo.DESCRIPTION, authors = PluginInfo.AUTHOR, version = PluginInfo.VERSION)
public class BetterVoteListener {

    @Inject
    private Logger logger;

    @Inject
    private PluginContainer container;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private File defaultConfig;

    private static BetterVoteListener instance;
    private CommandRegistrar registrar;
    private Storage storage;
    private VoteManager manager;

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        instance = this;

        // Init and load configuration.
        ConfigManager.init(defaultConfig);
        ConfigManager.load();

        registrar = new CommandRegistrar(this);
        storage = new Storage(this);
        manager = new VoteManager(this);

        // Provider service.
        Sponge.getServiceManager().setProvider(this, VoteService.class, new BetterVoteService(this));

        Sponge.getEventManager().registerListeners(this, new EventHandlers(this));
    }

    public static BetterVoteListener getInstance() {
        return instance;
    }

    public PluginContainer getContainer() {
        return this.container;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public Storage getStorage() {
        return this.storage;
    }

    public VoteManager getVoteManager() {
        return this.manager;
    }
}
