/*
 * DISCLAIMER & ATTRIBUTION
 *
 * This mod heavily adapts and modifies code and behavior from jaredlll08's "Controlling" mod,
 * which is under the MIT License.
 *
 * Original copyright and license notices are preserved where applicable.
 * This project is not affiliated with or endorsed by the original authors
 * All modifications are the responsibility of this project.
 */

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
