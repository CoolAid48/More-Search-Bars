package coolaid.moresearchbars.keybindsAPI.events;

import net.minecraft.client.KeyMapping;

public interface IHasConflictingModifierEvent {

    KeyMapping thisMapping();

    KeyMapping otherMapping();

}