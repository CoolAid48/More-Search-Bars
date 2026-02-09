package coolaid.moresearchbars;

import coolaid.moresearchbars.keybindsAPI.entries.ICategoryEntry;
import coolaid.moresearchbars.keybindsAPI.entries.IInputEntry;
import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Locale;

public class Constants {

    public static final MutableComponent COMPONENT_CONTROLS_RESET = Component.translatable("moresearchbars.controls.reset");
    public static final MutableComponent COMPONENT_CONTROLS_RESET_ALL = Component.translatable("moresearchbars.controls.resetAll");
    public static final MutableComponent COMPONENT_OPTIONS_CONFIRM_RESET = Component.translatable("moresearchbars.keybinds.confirmReset");
    public static final MutableComponent COMPONENT_OPTIONS_SHOW_NONE = Component.translatable("moresearchbars.keybinds.showNone");
    public static final MutableComponent COMPONENT_OPTIONS_SHOW_ALL = Component.translatable("moresearchbars.keybinds.showAll");
    public static final MutableComponent COMPONENT_OPTIONS_SHOW_CONFLICTS = Component.translatable("moresearchbars.keybinds.showConflicts");


    public static boolean matchesKeybindSearch(KeyBindsList.Entry entry, String searchText) {
        if (searchText == null || searchText.isBlank()) {
            return true;
        }
        String query = searchText.toLowerCase(Locale.ROOT);
        if (entry instanceof ICategoryEntry cat) {
            if (contains(cat.category().label().getString(), query)) {
                return true;
            }
        }
        if (entry instanceof IKeyEntry key) {
            if (contains(key.categoryName().getString(), query)) {
                return true;
            }
            if (!key.getKey().isUnbound() && contains(key.getKey().getTranslatedKeyMessage().getString(), query)) {
                return true;
            }
            if (contains(key.getKeyDesc().getString(), query)) {
                return true;
            }
        }
        if (entry instanceof IInputEntry input) {
            return contains(input.getInput().getName(), query);
        }
        return false;
    }

    private static boolean contains(String candidate, String query) {
        return candidate != null && candidate.toLowerCase(Locale.ROOT).contains(query);
    }
}