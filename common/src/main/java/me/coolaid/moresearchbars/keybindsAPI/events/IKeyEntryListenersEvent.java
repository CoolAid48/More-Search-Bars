package me.coolaid.moresearchbars.keybindsAPI.events;

import me.coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import net.minecraft.client.gui.components.events.GuiEventListener;

import java.util.List;

public interface IKeyEntryListenersEvent {

    List<GuiEventListener> getListeners();

    IKeyEntry getEntry();

}