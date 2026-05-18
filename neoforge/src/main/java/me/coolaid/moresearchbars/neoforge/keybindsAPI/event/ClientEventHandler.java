package me.coolaid.moresearchbars.neoforge.keybindsAPI.event;

import me.coolaid.moresearchbars.keybindsClient.NewKeyBindsScreen;
import me.coolaid.moresearchbars.mixin.keybinds.AccessOptionsSubScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ClientEventHandler {

    @SubscribeEvent
    public void openGui(ScreenEvent.Opening event) {

        try {
            if(event.getScreen() instanceof KeyBindsScreen gui && !(event.getScreen() instanceof NewKeyBindsScreen)) {
                event.setNewScreen(new NewKeyBindsScreen(((AccessOptionsSubScreen) gui).moresearchbars$getLastScreen(), Minecraft.getInstance().options));
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}