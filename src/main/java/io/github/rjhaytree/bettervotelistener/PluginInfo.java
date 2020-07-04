package io.github.rjhaytree.bettervotelistener;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class PluginInfo {
    public static final String ID = "bettervotelistener";
    public static final String NAME = "BetterVoteListener";
    public static final String VERSION = "1.0";
    public static final String DESCRIPTION = "A vote listener designed for Sponge servers.";
    public static final Text PLUGIN_PREFIX = Text.of(TextColors.DARK_GRAY, "[", TextColors.GREEN, "V", TextColors.DARK_GREEN, "ote", TextColors.DARK_GRAY, "] ", TextColors.RESET);
    public static final String AUTHOR = "RyanJH";
}
