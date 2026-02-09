package coolaid.moresearchbars.neoforge.keybindsAPI.event;

import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryListenersEvent;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.neoforged.bus.api.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * GetKeyEntryListenersEvent is called to get the values for {@link IKeyEntry#children()}.
 * Allowing for mods to add more listeners.
 */
public class KeyEntryListenersEvent extends Event implements IKeyEntryListenersEvent {

    private final IKeyEntry entry;

    private final List<GuiEventListener> listeners;

    public KeyEntryListenersEvent(IKeyEntry entry) {

        this.entry = entry;
        this.listeners = new ArrayList<>();

        getListeners().add(entry.getBtnChangeKeyBinding());
        getListeners().add(entry.getBtnResetKeyBinding());
    }


    public List<GuiEventListener> getListeners() {

        return listeners;
    }

    public IKeyEntry getEntry() {

        return entry;
    }

}
