package io.github.rjhaytree.bettervotelistener.commands;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.utilities.ChatUtils;
import io.github.rjhaytree.bettervotelistener.utilities.Permissions;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class CountCommand implements CommandExecutor {
    private BetterVoteListener plugin;

    public CountCommand(BetterVoteListener plugin) {
        this.plugin = plugin;
    }


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src.hasPermission(Permissions.ADMIN_COUNT_PERM) && args.getOne("player").isPresent()) {
            Player player = args.<Player>getOne("player").get();
            ChatUtils.sendMessage(src, Text.builder().append(Text.of(TextColors.GREEN, player.getName() + "'s vote count: ")).append(Text.of(TextColors.GOLD, Text.of(plugin.getVoteManager().getVoteCount(player)))).build());
            return CommandResult.success();
        }

        if (!(src instanceof Player)) {
            ChatUtils.sendWarning(src, Text.of("You must be a player to run this command"));
            return CommandResult.empty();
        }

        Player player = (Player) src;

        // Check if player has voted previously (They would be loaded into the cache.)
        if (plugin.getVoteManager().isPlayerInCache(player.getUniqueId())) {
            ChatUtils.sendMessage(src, Text.builder().append(Text.of(TextColors.GREEN, "Your current vote count: ")).append(Text.of(TextColors.GOLD, Text.of(plugin.getVoteManager().getVoteData(player.getUniqueId()).getVoteCount()))).build());
        }
        else {
            ChatUtils.sendMessage(src, Text.builder().append(Text.of(TextColors.GREEN, "Your current vote count: ")).append(Text.of(TextColors.GOLD, Text.of(0))).build());
        }

        return CommandResult.success();
    }
}
