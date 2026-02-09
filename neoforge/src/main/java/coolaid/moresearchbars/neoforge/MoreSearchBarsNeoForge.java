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

package coolaid.moresearchbars.neoforge;

import coolaid.moresearchbars.MoreSearchBars;
import net.neoforged.fml.common.Mod;

@Mod(MoreSearchBars.MOD_ID)
public final class MoreSearchBarsNeoForge {
    public MoreSearchBarsNeoForge() {

        // Run our common setup.
        MoreSearchBars.init();
    }
}
