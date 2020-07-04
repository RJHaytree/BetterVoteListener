package io.github.rjhaytree.bettervotelistener.commands;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

public class LeaderBoardCommand implements CommandExecutor {
    private BetterVoteListener plugin;

    public LeaderBoardCommand(BetterVoteListener plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        return null;
    }
}
