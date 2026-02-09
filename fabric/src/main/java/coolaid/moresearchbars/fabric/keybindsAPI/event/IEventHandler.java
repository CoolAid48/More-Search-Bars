package coolaid.moresearchbars.fabric.keybindsAPI.event;

public interface IEventHandler<T, U> {

    U handle(T event);

}