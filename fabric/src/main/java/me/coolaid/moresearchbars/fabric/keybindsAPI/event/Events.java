package me.coolaid.moresearchbars.fabric.keybindsAPI.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.util.Unit;

import java.util.ArrayList;
import java.util.List;

public class Events {

    public static final Event<IEventHandler<KeyEntryListenersEvent, List<GuiEventListener>>> KEY_ENTRY_LISTENERS_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> {
        if (listeners.length == 0) {
            return event.getListeners();
        }

        List<GuiEventListener> eventListeners = new ArrayList<>();
        for (IEventHandler<KeyEntryListenersEvent, List<GuiEventListener>> handler : listeners) {
            eventListeners.addAll(handler.handle(event));
        }
        return List.copyOf(eventListeners);
    });
    public static final Event<IEventHandler<KeyEntryMouseClickedEvent, Boolean>> KEY_ENTRY_MOUSE_CLICKED_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> {
        if (listeners.length == 0) {
            return event.isHandled();
        }
        return anyHandled(listeners, event);
    });
    public static final Event<IEventHandler<KeyEntryMouseReleasedEvent, Boolean>> KEY_ENTRY_MOUSE_RELEASED_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> {
        if (listeners.length == 0) {
            return event.isHandled();
        }
        return anyHandled(listeners, event);
    });
    public static final Event<IEventHandler<KeyEntryRenderEvent, Unit>> KEY_ENTRY_RENDER_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> {
        for (IEventHandler<KeyEntryRenderEvent, Unit> handler : listeners) {
            handler.handle(event);
        }
        return Unit.INSTANCE;
    });
    public static final Event<IEventHandler<HasConflictingModifierEvent, Boolean>> HAS_CONFLICTING_MODIFIERS_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> anyHandled(listeners, event));

    public static final Event<IEventHandler<IsKeyCodeModifierEvent, Boolean>> IS_KEY_CODE_MODIFIER_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> anyHandled(listeners, event));

    public static final Event<IEventHandler<SetToDefaultEvent, Boolean>> SET_TO_DEFAULT_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> anyHandled(listeners, event));

    public static final Event<IEventHandler<SetKeyEvent, Boolean>> SET_KEY_EVENT = EventFactory.createArrayBacked(IEventHandler.class, listeners -> event -> anyHandled(listeners, event));

    private static <T> boolean anyHandled(IEventHandler<T, Boolean>[] handlers, T event) {
        for (IEventHandler<T, Boolean> handler : handlers) {
            if (handler.handle(event)) {
                return true;
            }
        }
        return false;
    }
}
