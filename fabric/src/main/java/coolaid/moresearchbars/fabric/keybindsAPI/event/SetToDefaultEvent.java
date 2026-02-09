package coolaid.moresearchbars.fabric.keybindsAPI.event;

import coolaid.moresearchbars.keybindsAPI.events.ISetToDefaultEvent;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

/**
 * Fired when a key is set to the default, either through the individual reset button or the global reset button.
 */
public class SetToDefaultEvent implements ISetToDefaultEvent {

    private final Options options;
    private final KeyMapping mapping;

    public SetToDefaultEvent(Options options, KeyMapping mapping) {

        this.options = options;
        this.mapping = mapping;
    }

    @Override
    public Options options() {

        return options;
    }

    @Override
    public KeyMapping mapping() {

        return mapping;
    }

}