package coolaid.moresearchbars.fabric.keybindsAPI.platform;

import com.mojang.datafixers.util.Either;
import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryListenersEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseClickedEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseReleasedEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryRenderEvent;
import coolaid.moresearchbars.platform.IEventHelper;
import coolaid.moresearchbars.fabric.keybindsAPI.event.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Unit;

import java.util.List;

public class FabricEventHandler implements IEventHelper {

    @Override
    public Either<IKeyEntryListenersEvent, List<GuiEventListener>> fireKeyEntryListenersEvent(IKeyEntry entry) {
        KeyEntryListenersEvent event = new KeyEntryListenersEvent(entry);
        if(FabricLoader.getInstance().isModLoaded("fabric")) {
            return Either.right(Events.KEY_ENTRY_LISTENERS_EVENT.invoker().handle(event));
        }

        return Either.right(event.getListeners());
    }

    @Override
    public Either<IKeyEntryMouseClickedEvent, Boolean> fireKeyEntryMouseClickedEvent(IKeyEntry entry, MouseButtonEvent event, boolean doubleClick) {
        KeyEntryMouseClickedEvent clickEvent = new KeyEntryMouseClickedEvent(entry, event, doubleClick);
        if(FabricLoader.getInstance().isModLoaded("fabric")) {
            return Either.right(Events.KEY_ENTRY_MOUSE_CLICKED_EVENT.invoker().handle(clickEvent));
        }

        return Either.right(clickEvent.isHandled());
    }

    @Override
    public Either<IKeyEntryMouseReleasedEvent, Boolean> fireKeyEntryMouseReleasedEvent(IKeyEntry entry, MouseButtonEvent event) {
        KeyEntryMouseReleasedEvent releaseEvent = new KeyEntryMouseReleasedEvent(entry, event);
        if(FabricLoader.getInstance().isModLoaded("fabric")) {
            return Either.right(Events.KEY_ENTRY_MOUSE_RELEASED_EVENT.invoker().handle(releaseEvent));
        }

        return Either.right(releaseEvent.isHandled());
    }

    @Override
    public Either<IKeyEntryRenderEvent, Unit> fireKeyEntryRenderEvent(IKeyEntry entry, GuiGraphics guiGraphics, int y, int x, int rowLeft, int rowWidth, boolean hovered, float partialTicks) {

        if(FabricLoader.getInstance().isModLoaded("fabric")) {
            return Either.right(Events.KEY_ENTRY_RENDER_EVENT.invoker()
                    .handle(new KeyEntryRenderEvent(entry, guiGraphics, x, y, rowLeft, rowWidth, hovered, partialTicks)));
        }
        return Either.right(Unit.INSTANCE);
    }
}