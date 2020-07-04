package io.github.rjhaytree.bettervotelistener.commands;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.config.ConfigManager;
import io.github.rjhaytree.bettervotelistener.utilities.ChatUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class VoteCommand implements CommandExecutor {
    private BetterVoteListener plugin;

    public VoteCommand(BetterVoteListener plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            ChatUtils.sendWarning(src, Text.of("You must be a player to run this command"));
            return CommandResult.empty();
        }

        Player player = (Player) src;
        List<Text> contents = new ArrayList<>();

        // Iterate over vote-links in config, adding them to the contexts ArrayList.
        for (String s : ConfigManager.LINKS) {
            try {
                contents.add(Text.builder().append(Text.of(TextColors.GREEN, " " + s)).onClick(TextActions.openUrl(new URL(s))).onHover(TextActions.showText(Text.of("Vote on this link!"))).build());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        // Leave line for organisation purposes.
        contents.add(Text.of(" "));

        // Check if player has voted previously (They would be loaded into the cache.)
        if (plugin.getVoteManager().isPlayerInCache(player.getUniqueId())) {
            contents.add(Text.builder().append(Text.of(TextColors.GREEN, " Your current vote count: ")).append(Text.of(TextColors.GOLD, Text.of(plugin.getVoteManager().getVoteData(player.getUniqueId()).getVoteCount()))).build());
        }
        else {
            contents.add(Text.builder().append(Text.of(TextColors.GREEN, " Your current vote count: ")).append(Text.of(TextColors.GOLD, Text.of(0))).build());
        }

        ChatUtils.sendPagination(src, Text.builder().append(Text.of(TextColors.DARK_GREEN, TextStyles.BOLD, "Vote ")).append(Text.of(TextColors.GREEN, TextStyles.BOLD, "Links")).build(), contents);

        return CommandResult.success();
    }
}
