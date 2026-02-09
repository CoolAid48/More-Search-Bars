package coolaid.moresearchbars.mixin.keybinds;

import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.keybindsClient.NewKeyBindsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Minecraft.class)
public class OpenGuiMixin {

    @Shadow
    public Screen screen;

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    private Screen upgradeControlScreen(Screen opened) {
        if (opened != null
                && KeyBindsScreen.class.equals(opened.getClass())
                && (MoreSearchBars.CONFIG == null || MoreSearchBars.CONFIG.enableKeybindsSearch)) {
            return new NewKeyBindsScreen(this.screen, Minecraft.getInstance().options);
        }
        return opened;
    }
}