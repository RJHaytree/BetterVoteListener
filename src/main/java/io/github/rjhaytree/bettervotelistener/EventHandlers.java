package io.github.rjhaytree.bettervotelistener;

import io.github.rjhaytree.bettervotelistener.config.ConfigManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;

public class EventHandlers {
    private BetterVoteListener plugin;

    public EventHandlers(BetterVoteListener plugin) {
        this.plugin = plugin;
    }

    @Listener
    public void reload(GameReloadEvent event) {
        ConfigManager.reload();
    }
}
