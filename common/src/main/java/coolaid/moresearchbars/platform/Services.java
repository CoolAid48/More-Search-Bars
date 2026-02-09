package coolaid.moresearchbars.platform;

import coolaid.moresearchbars.MoreSearchBars;

import java.util.ServiceLoader;

public class Services {

    public static final IEventHelper EVENT = load(IEventHelper.class);
    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);

    public static <T> T load(Class<T> classssss) {

        final T loadedService = ServiceLoader.load(classssss)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + classssss.getName()));
        MoreSearchBars.LOGGER.debug("Loaded {} for service {}", loadedService, classssss);
        return loadedService;
    }

}