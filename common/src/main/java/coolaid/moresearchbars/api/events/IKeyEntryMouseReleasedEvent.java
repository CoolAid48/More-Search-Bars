package coolaid.moresearchbars.api.events;

import coolaid.moresearchbars.api.entries.IKeyEntry;
import net.minecraft.client.input.MouseButtonEvent;

public interface IKeyEntryMouseReleasedEvent {

    IKeyEntry getEntry();

    MouseButtonEvent event();

    boolean isHandled();

    void setHandled(boolean handled);

}