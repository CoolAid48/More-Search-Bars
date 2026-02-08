package coolaid.moresearchbars.fabric;

import coolaid.moresearchbars.MoreSearchBars;
import net.fabricmc.api.ClientModInitializer;

public final class MoreSearchBarsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.

        // Run our common setup.
        MoreSearchBars.init();
    }
}
