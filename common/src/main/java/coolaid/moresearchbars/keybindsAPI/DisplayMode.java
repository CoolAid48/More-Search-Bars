package coolaid.moresearchbars.keybindsAPI;

import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import coolaid.moresearchbars.mixin.keybinds.AccessKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;

import java.util.function.Predicate;

public enum DisplayMode {
    ALL(keyEntry -> true), NONE(keyEntry -> keyEntry.getKey().isUnbound()), CONFLICTING(keyEntry -> {

        for(KeyMapping key : Minecraft.getInstance().options.keyMappings) {
            if(!key.getName().equals(keyEntry.getKey().getName()) && !key.isUnbound()) {
                if(((AccessKeyMapping) key).moresearchbars$getKey()
                        .getValue() == ((AccessKeyMapping) keyEntry.getKey()).moresearchbars$getKey().getValue()) {
                    return true;
                }
            }
        }
        return false;
    });


    private final Predicate<IKeyEntry> predicate;

    DisplayMode(Predicate<IKeyEntry> predicate) {

        this.predicate = predicate;
    }

    public Predicate<KeyBindsList.Entry> getPredicate() {

        return entry -> entry instanceof IKeyEntry keyEntry && predicate.test(keyEntry);
    }
}