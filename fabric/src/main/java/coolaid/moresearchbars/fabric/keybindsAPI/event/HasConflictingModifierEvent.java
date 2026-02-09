package coolaid.moresearchbars.fabric.keybindsAPI.event;

import coolaid.moresearchbars.keybindsAPI.events.IHasConflictingModifierEvent;
import net.minecraft.client.KeyMapping;

/**
 * Fired to check if a {@link KeyMapping} conflicts with another {@link KeyMapping}.
 */
public class HasConflictingModifierEvent implements IHasConflictingModifierEvent {

    private final KeyMapping thisMapping;
    private final KeyMapping otherMapping;

    public HasConflictingModifierEvent(KeyMapping thisMapping, KeyMapping otherMapping) {

        this.thisMapping = thisMapping;
        this.otherMapping = otherMapping;
    }

    @Override
    public KeyMapping thisMapping() {

        return thisMapping;
    }

    @Override
    public KeyMapping otherMapping() {

        return otherMapping;
    }


}