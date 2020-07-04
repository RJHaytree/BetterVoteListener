package io.github.rjhaytree.bettervotelistener.utilities;

import io.github.rjhaytree.bettervotelistener.PluginInfo;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.List;

public class ChatUtils {
    public static void sendWarning(CommandSource src, Text msg) {
        src.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.RED, msg));
    }

    public static void sendMessage(CommandSource src, Text msg) {
        src.sendMessage(Text.of(PluginInfo.PLUGIN_PREFIX, TextColors.GREEN, msg));
    }

    public static void sendPagination(CommandSource src, Text title, List<Text> contents) {
        PaginationList.builder()
                .title(Text.of(TextColors.GREEN, TextStyles.BOLD, title))
                .contents(contents)
                .padding(Text.of(TextColors.DARK_GRAY, TextStyles.STRIKETHROUGH, "-"))
                .sendTo(src);
    }
}
