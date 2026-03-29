package coolaid.moresearchbars.fabric.keybindsAPI.platform;

import com.mojang.blaze3d.platform.InputConstants;
import coolaid.moresearchbars.keybindsClient.NewKeyBindsScreen;
import coolaid.moresearchbars.platform.IPlatformHelper;
import coolaid.moresearchbars.fabric.keybindsAPI.event.*;
import net.minecraft.util.Util;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import net.minecraft.client.input.KeyEvent;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

public class FabricPlatformHelper implements IPlatformHelper {

    private final Map<NewKeyBindsScreen, KeySelectionState> keySelectionStates = new WeakHashMap<>();

    private KeySelectionState getOrCreateState(NewKeyBindsScreen screen) {

        return keySelectionStates.computeIfAbsent(screen, ignored -> new KeySelectionState());
    }

    private void clearState(NewKeyBindsScreen screen) {

        keySelectionStates.remove(screen);
    }

    @Override
    public Path getConfigDir() {

        return net.fabricmc.loader.api.FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public boolean hasConflictingModifier(KeyMapping keybinding, KeyMapping other) {

        return Events.HAS_CONFLICTING_MODIFIERS_EVENT.invoker()
                .handle(new HasConflictingModifierEvent(keybinding, other));

    }

    @Override
    public void setKey(Options options, KeyMapping keybinding, InputConstants.Key key) {

        boolean handled = Events.SET_KEY_EVENT.invoker().handle(new SetKeyEvent(options, keybinding, key));
        if(!handled || !keybinding.getDefaultKey().equals(key)) {
            IPlatformHelper.super.setKey(options, keybinding, key);
        } else {
            KeyMapping.resetMapping();
        }
        applyAmecsBoundModifiersIfPresent(keybinding);
    }

    @Override
    public void setToDefault(Options options, KeyMapping keybinding) {

        boolean handled = Events.SET_TO_DEFAULT_EVENT.invoker().handle(new SetToDefaultEvent(options, keybinding));
        if(!handled || !keybinding.isDefault()) {
            IPlatformHelper.super.setToDefault(options, keybinding);
        } else {
            KeyMapping.resetMapping();
        }
        resetAmecsBoundModifiersIfPresent(keybinding);
    }

    @Override
    public boolean isKeyCodeModifier(InputConstants.Key key) {

        return Events.IS_KEY_CODE_MODIFIER_EVENT.invoker().handle(new IsKeyCodeModifierEvent(key));

    }

    @Override
    public void handleKeyPress(NewKeyBindsScreen screen, Options options, KeyEvent event) {

        if (screen.selectedKey == null) {
            return;
        }
        KeySelectionState state = getOrCreateState(screen);
        if (event.isEscape()) {
            setKey(options, screen.selectedKey, InputConstants.UNKNOWN);
            finalizeSelection(screen, state);
            return;
        }

        InputConstants.Key pressed = InputConstants.getKey(event);
        if (isModifierKey(pressed)) {
            state.pressedModifier = pressed;
            state.modifierHeldDown = true;
            setKey(options, screen.selectedKey, pressed);
            screen.getKeyBindsList().resetMappingAndUpdateButtons();
            return;
        }
        setKey(options, screen.selectedKey, pressed);
        finalizeSelection(screen, state);
    }

    @Override
    public boolean handleKeyReleased(NewKeyBindsScreen screen, Options options, KeyEvent event) {

        if (screen.selectedKey == null) {
            return false;
        }
        KeySelectionState state = getOrCreateState(screen);
        if (event.isEscape()) {
            setKey(options, screen.selectedKey, InputConstants.UNKNOWN);
            finalizeSelection(screen, state);
            return true;
        }

        InputConstants.Key released = InputConstants.getKey(event);
        if (state.modifierHeldDown && state.pressedModifier.equals(released)) {
            setKey(options, screen.selectedKey, state.pressedModifier);
            finalizeSelection(screen, state);
            return true;
        }
        return false;
    }

    @Override
    public void cancelPendingSelection(NewKeyBindsScreen screen) {

        clearState(screen);
    }

    private void finalizeSelection(NewKeyBindsScreen screen, KeySelectionState state) {

        state.reset();
        screen.selectedKey = null;
        screen.lastKeySelection = Util.getMillis();
        screen.getKeyBindsList().resetMappingAndUpdateButtons();
    }

    private static final class KeySelectionState {

        private InputConstants.Key pressedModifier = InputConstants.UNKNOWN;
        private boolean modifierHeldDown;

        private void reset() {

            this.pressedModifier = InputConstants.UNKNOWN;
            this.modifierHeldDown = false;
        }
    }

    private boolean isModifierKey(InputConstants.Key key) {

        if (isKeyCodeModifier(key)) {
            return true;
        }
        return switch (key.getValue()) {
            case GLFW.GLFW_KEY_LEFT_SHIFT,
                 GLFW.GLFW_KEY_RIGHT_SHIFT,
                 GLFW.GLFW_KEY_LEFT_CONTROL,
                 GLFW.GLFW_KEY_RIGHT_CONTROL,
                 GLFW.GLFW_KEY_LEFT_ALT,
                 GLFW.GLFW_KEY_RIGHT_ALT,
                 GLFW.GLFW_KEY_LEFT_SUPER,
                 GLFW.GLFW_KEY_RIGHT_SUPER -> true;
            default -> false;
        };
    }

    private void applyAmecsBoundModifiersIfPresent(KeyMapping keybinding) {

        AmecsReflection reflection = AmecsReflection.INSTANCE;
        if (!reflection.available) {
            return;
        }
        try {
            if (keybinding.isUnbound()) {
                reflection.resetBoundModifiers.invoke(null, keybinding);
                return;
            }
            Object bound = reflection.getBoundModifiers.invoke(null, keybinding);
            Object currentlyPressed = reflection.getCurrentlyPressed.invoke(null);
            reflection.copyFrom.invoke(bound, currentlyPressed);
            reflection.cleanup.invoke(bound, keybinding);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private void resetAmecsBoundModifiersIfPresent(KeyMapping keybinding) {

        AmecsReflection reflection = AmecsReflection.INSTANCE;
        if (!reflection.available) {
            return;
        }
        try {
            reflection.resetBoundModifiers.invoke(null, keybinding);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static final class AmecsReflection {

        private static final AmecsReflection INSTANCE = new AmecsReflection();

        private final boolean available;
        private final Method getBoundModifiers;
        private final Method resetBoundModifiers;
        private final Method getCurrentlyPressed;
        private final Method copyFrom;
        private final Method cleanup;

        private AmecsReflection() {

            Method getBoundModifiersMethod = null;
            Method resetBoundModifiersMethod = null;
            Method getCurrentlyPressedMethod = null;
            Method copyFromMethod = null;
            Method cleanupMethod = null;
            boolean loaded = false;
            try {
                Class<?> apiClass = Class.forName("de.siphalor.amecs.key_modifiers.api.AmecsKeyModifiersApi");
                Class<?> combinationClass = Class.forName("de.siphalor.amecs.key_modifiers.api.AmecsKeyModifierCombination");
                getBoundModifiersMethod = apiClass.getMethod("getBoundModifiers", KeyMapping.class);
                resetBoundModifiersMethod = apiClass.getMethod("resetBoundModifiers", KeyMapping.class);
                getCurrentlyPressedMethod = combinationClass.getMethod("getCurrentlyPressed");
                copyFromMethod = combinationClass.getMethod("copyFrom", combinationClass);
                cleanupMethod = combinationClass.getMethod("cleanup", KeyMapping.class);
                loaded = true;
            } catch (ReflectiveOperationException ignored) {
            }
            this.available = loaded;
            this.getBoundModifiers = getBoundModifiersMethod;
            this.resetBoundModifiers = resetBoundModifiersMethod;
            this.getCurrentlyPressed = getCurrentlyPressedMethod;
            this.copyFrom = copyFromMethod;
            this.cleanup = cleanupMethod;
        }
    }

}