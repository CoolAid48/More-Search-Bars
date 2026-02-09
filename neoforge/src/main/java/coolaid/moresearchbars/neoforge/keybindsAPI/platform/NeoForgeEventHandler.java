package coolaid.moresearchbars.neoforge.keybindsAPI.platform;

import com.mojang.datafixers.util.Either;
import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryListenersEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseClickedEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseReleasedEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryRenderEvent;
import coolaid.moresearchbars.neoforge.keybindsAPI.event.KeyEntryListenersEvent;
import coolaid.moresearchbars.neoforge.keybindsAPI.event.KeyEntryMouseClickedEvent;
import coolaid.moresearchbars.neoforge.keybindsAPI.event.KeyEntryMouseReleasedEvent;
import coolaid.moresearchbars.neoforge.keybindsAPI.event.KeyEntryRenderEvent;
import coolaid.moresearchbars.platform.IEventHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.util.Unit;
import net.neoforged.neoforge.common.NeoForge;

import java.util.List;

public class NeoForgeEventHandler implements IEventHelper {

    @Override
    public Either<IKeyEntryListenersEvent, List<GuiEventListener>> fireKeyEntryListenersEvent(IKeyEntry entry) {

        return Either.left(NeoForge.EVENT_BUS.post(new KeyEntryListenersEvent(entry)));
    }

    @Override
    public Either<IKeyEntryMouseClickedEvent, Boolean> fireKeyEntryMouseClickedEvent(IKeyEntry entry, MouseButtonEvent event, boolean doubleClick) {

        return Either.left(NeoForge.EVENT_BUS.post(new KeyEntryMouseClickedEvent(entry, event, doubleClick)));
    }

    @Override
    public Either<IKeyEntryMouseReleasedEvent, Boolean> fireKeyEntryMouseReleasedEvent(IKeyEntry entry, MouseButtonEvent event) {

        return Either.left(NeoForge.EVENT_BUS.post(new KeyEntryMouseReleasedEvent(entry, event)));
    }

    @Override
    public Either<IKeyEntryRenderEvent, Unit> fireKeyEntryRenderEvent(IKeyEntry entry, GuiGraphics guiGraphics, int y, int x, int rowLeft, int rowWidth, boolean hovered, float partialTicks) {

        return Either.left(NeoForge.EVENT_BUS.post(new KeyEntryRenderEvent(entry, guiGraphics, y, x, rowLeft, rowWidth, hovered, partialTicks)));
    }

}