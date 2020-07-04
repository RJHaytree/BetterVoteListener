package io.github.rjhaytree.bettervotelistener.commands;

import io.github.rjhaytree.bettervotelistener.BetterVoteListener;
import io.github.rjhaytree.bettervotelistener.utilities.Permissions;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

public class CommandRegistrar {
    private BetterVoteListener plugin;

    public CommandRegistrar(BetterVoteListener plugin) {
        this.plugin = plugin;
        register();
    }

    private void register() {
        CommandSpec count = CommandSpec.builder()
                .executor(new CountCommand(plugin))
                .permission(Permissions.USER_COUNT_PERM)
                .description(Text.of("View your number of votes."))
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .build();

        CommandSpec add = CommandSpec.builder()
                .executor(new AddCommand(plugin))
                .permission(Permissions.ADMIN_ADD_PERM)
                .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))), GenericArguments.onlyOne(GenericArguments.integer(Text.of("votes"))))
                .description(Text.of("Add votes to a player."))
                .build();

        CommandSpec leaderboard = CommandSpec.builder()
                .executor(new LeaderBoardCommand(plugin))
                .permission(Permissions.USER_LEADERBOARD_PERM)
                .description(Text.of("See who has voted the most."))
                .build();

        CommandSpec reload = CommandSpec.builder()
                .executor(new ReloadCommand(plugin))
                .permission(Permissions.ADMIN_RELOAD_PERM)
                .description(Text.of("Reload the configuration."))
                .build();

        CommandSpec set = CommandSpec.builder()
                .executor(new SetCommand(plugin))
                .permission(Permissions.ADMIN_SET_PERM)
                .arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))), GenericArguments.onlyOne(GenericArguments.integer(Text.of("votes"))))
                .description(Text.of("Set a user's vote count."))
                .build();

        CommandSpec vote = CommandSpec.builder()
                .executor(new VoteCommand(plugin))
                .permission(Permissions.USER_BASE_PERM)
                .child(count, "count")
                .child(add, "add")
                .child(leaderboard, "leaderboard", "top", "board")
                .child(reload, "reload")
                .child(set, "set")
                .description(Text.of("Vote for this server on vote listings to gain rewards."))
                .build();

        Sponge.getCommandManager().register(plugin, vote, "vote");
    }
}
