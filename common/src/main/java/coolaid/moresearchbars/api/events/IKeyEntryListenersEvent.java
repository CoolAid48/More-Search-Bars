package coolaid.moresearchbars.api.events;

import coolaid.moresearchbars.api.entries.IKeyEntry;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.List;

public interface IKeyEntryListenersEvent {

    List<GuiEventListener> getListeners();

    IKeyEntry getEntry();

}