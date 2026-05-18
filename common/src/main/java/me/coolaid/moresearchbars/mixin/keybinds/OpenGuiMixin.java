package me.coolaid.moresearchbars.mixin.keybinds;

import me.coolaid.moresearchbars.MoreSearchBars;
import me.coolaid.moresearchbars.keybindsClient.NewKeyBindsScreen;
import me.coolaid.moresearchbars.platform.Services;
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
                && Services.PLATFORM.shouldUseCustomKeybindScreen()
                && (MoreSearchBars.CONFIG == null || MoreSearchBars.CONFIG.enableKeybindsSearch)) {
            return new NewKeyBindsScreen(this.screen, Minecraft.getInstance().options);
        }
        return opened;
    }
}