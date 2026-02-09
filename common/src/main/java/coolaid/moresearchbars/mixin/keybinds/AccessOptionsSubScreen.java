package coolaid.moresearchbars.mixin.keybinds;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(OptionsSubScreen.class)
public interface AccessOptionsSubScreen {

    @Accessor("lastScreen")
    Screen moresearchbars$getLastScreen();

}