package coolaid.moresearchbars.keybindsAPI.events;

import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import net.minecraft.client.input.MouseButtonEvent;

public interface IKeyEntryMouseClickedEvent {

    IKeyEntry getEntry();

    MouseButtonEvent event();

    boolean doubleClick();

    boolean isHandled();

    void setHandled(boolean handled);

}