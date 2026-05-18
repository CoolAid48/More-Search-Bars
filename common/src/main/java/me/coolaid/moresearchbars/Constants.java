package me.coolaid.moresearchbars;

import me.coolaid.moresearchbars.keybindsAPI.entries.ICategoryEntry;
import me.coolaid.moresearchbars.keybindsAPI.entries.IInputEntry;
import me.coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public class Constants {

    public static final MutableComponent COMPONENT_OPTIONS_RESET = Component.translatable("moresearchbars.keybinds.reset");

    public static boolean matchesKeybindSearch(KeyBindsList.Entry entry, String searchText) {
        return matchesNormalizedKeybindSearch(entry, normalizeSearchText(searchText));
    }

    public static boolean matchesNormalizedKeybindSearch(KeyBindsList.Entry entry, String query) {
        if (query == null || query.isEmpty()) {
            return true;
        }
        if (entry instanceof ICategoryEntry cat) {
            if (containsNormalized(cat.category().label().getString(), query)) {
                return true;
            }
        }
        if (entry instanceof IKeyEntry key) {
            if (containsNormalized(key.categoryName().getString(), query)) {
                return true;
            }
            if (!key.getKey().isUnbound() && containsNormalized(key.getKey().getTranslatedKeyMessage().getString(), query)) {
                return true;
            }
            if (containsNormalized(key.getKeyDesc().getString(), query)) {
                return true;
            }
        }
        if (entry instanceof IInputEntry input) {
            return containsNormalized(input.getInput().getName(), query);
        }
        return false;
    }

    public static String normalizeSearchText(String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return "";
        }
        return searchText.toLowerCase(Locale.ROOT);
    }

    public static boolean containsNormalized(String candidate, String query) {
        return candidate != null && candidate.toLowerCase(Locale.ROOT).contains(query);
    }
}
