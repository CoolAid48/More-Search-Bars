package coolaid.moresearchbars.keybindsAPI.events;

import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import net.minecraft.client.input.MouseButtonEvent;

public interface IKeyEntryMouseReleasedEvent {

    IKeyEntry getEntry();

    MouseButtonEvent event();

    boolean isHandled();

    void setHandled(boolean handled);

}