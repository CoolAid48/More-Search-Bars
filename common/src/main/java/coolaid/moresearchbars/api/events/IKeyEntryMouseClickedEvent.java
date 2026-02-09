package coolaid.moresearchbars.api.events;

import coolaid.moresearchbars.api.entries.IKeyEntry;
import net.minecraft.client.input.MouseButtonEvent;

public interface IKeyEntryMouseClickedEvent {

    IKeyEntry getEntry();

    MouseButtonEvent event();

    boolean doubleClick();

    boolean isHandled();

    void setHandled(boolean handled);

}