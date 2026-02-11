package coolaid.moresearchbars.platform;

import com.mojang.blaze3d.platform.InputConstants;
import coolaid.moresearchbars.keybindsClient.NewKeyBindsScreen;
import coolaid.moresearchbars.mixin.keybinds.AccessKeyMapping;
import net.minecraft.util.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import java.nio.file.Path;

public interface IPlatformHelper {

    default Path getConfigDir() {

        throw new IllegalStateException("Platform helper must provide config directory.");
    }

    default boolean hasConflictingModifier(KeyMapping keybinding, KeyMapping other) {

        return false;
    }

    default void setKey(Options options, KeyMapping keybinding, InputConstants.Key key) {

        keybinding.setKey(key);
    }

    default void setToDefault(Options options, KeyMapping keybinding) {

        keybinding.setKey(keybinding.getDefaultKey());
    }

    default boolean isKeyCodeModifier(InputConstants.Key key) {

        return false;
    }

    default Component getKeyName(KeyMapping mapping) {

        return Component.translatable(mapping.getName());
    }

    default void handleKeyPress(NewKeyBindsScreen screen, Options options, KeyEvent event) {

        if(screen.selectedKey != null) {
            if(event.isEscape()) {
                Services.PLATFORM.setKey(options, screen.selectedKey, InputConstants.UNKNOWN);
            } else {
                Services.PLATFORM.setKey(options, screen.selectedKey, InputConstants.getKey(event));
            }
            if(!Services.PLATFORM.isKeyCodeModifier(((AccessKeyMapping) screen.selectedKey).moresearchbars$getKey())) {
                screen.selectedKey = null;
            }
            screen.lastKeySelection = Util.getMillis();
            screen.getKeyBindsList().resetMappingAndUpdateButtons();
        }
    }

    default boolean handleKeyReleased(NewKeyBindsScreen screen, Options options, KeyEvent event) {

        return false;
    }

}