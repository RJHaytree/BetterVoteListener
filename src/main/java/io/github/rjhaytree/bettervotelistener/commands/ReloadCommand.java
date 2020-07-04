package io.github.rjhaytree.bettervotelistener.commands;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.config.ConfigManager;
import io.github.rjhaytree.bettervotelistener.utilities.ChatUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class ReloadCommand implements CommandExecutor {
    private BetterVoteListener plugin;

    public ReloadCommand(BetterVoteListener plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        for (String s: ConfigManager.LINKS) {
            plugin.getLogger().info(s);
        }

        ConfigManager.reload();

        for (String s: ConfigManager.LINKS) {
            plugin.getLogger().info(s);
        }

        ChatUtils.sendMessage(src, Text.of("Configuration reloaded. Not all options are reloaded, however, and a restart may be necessary."));
        return CommandResult.success();
    }
}
