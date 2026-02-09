package coolaid.moresearchbars.fabric.api.event;

public interface IEventHandler<T, U> {

    U handle(T event);

}