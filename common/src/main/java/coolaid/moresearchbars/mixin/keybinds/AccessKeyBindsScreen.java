package coolaid.moresearchbars.mixin.keybinds;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBindsScreen.class)
public interface AccessKeyBindsScreen {

    @Accessor("keyBindsList")
    KeyBindsList moresearchbars$getKeyBindsList();

    @Accessor("keyBindsList")
    void moresearchbars$setKeyBindsList(KeyBindsList newList);

    @Accessor("resetButton")
    Button moresearchbars$getResetButton();

    @Accessor("resetButton")
    void moresearchbars$setResetButton(Button resetButton);

}