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
        for (String s: ConfigManager.LINKS) {
            plugin.getLogger().info(s);
        }

        ConfigManager.reload();

        for (String s: ConfigManager.LINKS) {
            plugin.getLogger().info(s);
        }
    }
}
