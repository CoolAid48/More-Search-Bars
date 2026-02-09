package coolaid.moresearchbars.platform;

import com.mojang.datafixers.util.Either;
import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryListenersEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseClickedEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseReleasedEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryRenderEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Unit;

import java.util.List;

public interface IEventHelper {

    Either<IKeyEntryListenersEvent, List<GuiEventListener>> fireKeyEntryListenersEvent(IKeyEntry entry);

    Either<IKeyEntryMouseClickedEvent, Boolean> fireKeyEntryMouseClickedEvent(IKeyEntry entry, MouseButtonEvent event, boolean doubleClick);

    Either<IKeyEntryMouseReleasedEvent, Boolean> fireKeyEntryMouseReleasedEvent(IKeyEntry entry, MouseButtonEvent event);

    Either<IKeyEntryRenderEvent, Unit> fireKeyEntryRenderEvent(IKeyEntry entry, GuiGraphics stack, int x, int y, int rowLeft, int rowWidth, boolean hovered, float partialTicks);

}