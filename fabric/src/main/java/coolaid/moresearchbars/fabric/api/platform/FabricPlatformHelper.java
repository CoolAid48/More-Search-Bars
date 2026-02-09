package coolaid.moresearchbars.fabric.api.platform;

import com.mojang.blaze3d.platform.InputConstants;
import coolaid.moresearchbars.api.platform.IPlatformHelper;
import coolaid.moresearchbars.fabric.api.event.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public boolean hasConflictingModifier(KeyMapping keybinding, KeyMapping other) {

        if(FabricLoader.getInstance().isModLoaded("fabric")) {
            return Events.HAS_CONFLICTING_MODIFIERS_EVENT.invoker()
                    .handle(new HasConflictingModifierEvent(keybinding, other));
        }
        return IPlatformHelper.super.hasConflictingModifier(keybinding, other);

    }

    @Override
    public void setKey(Options options, KeyMapping keybinding, InputConstants.Key key) {

        boolean handled = FabricLoader.getInstance().isModLoaded("fabric")
                && Events.SET_KEY_EVENT.invoker().handle(new SetKeyEvent(options, keybinding, key));
        if(!handled) {
            IPlatformHelper.super.setKey(options, keybinding, key);
        }
    }

    @Override
    public void setToDefault(Options options, KeyMapping keybinding) {

        boolean handled = FabricLoader.getInstance().isModLoaded("fabric")
                && Events.SET_TO_DEFAULT_EVENT.invoker().handle(new SetToDefaultEvent(options, keybinding));
        if(!handled) {
            IPlatformHelper.super.setToDefault(options, keybinding);
        }
    }

    @Override
    public boolean isKeyCodeModifier(InputConstants.Key key) {

        if(FabricLoader.getInstance().isModLoaded("fabric")) {
            return Events.IS_KEY_CODE_MODIFIER_EVENT.invoker().handle(new IsKeyCodeModifierEvent(key));
        }
        return IPlatformHelper.super.isKeyCodeModifier(key);

    }

}