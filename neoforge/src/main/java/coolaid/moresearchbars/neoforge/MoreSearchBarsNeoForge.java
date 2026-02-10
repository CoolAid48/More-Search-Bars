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
import coolaid.moresearchbars.config.SearchBarConfigScreen;
import coolaid.moresearchbars.neoforge.keybindsAPI.event.ClientEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(MoreSearchBars.MOD_ID)
public final class MoreSearchBarsNeoForge {
    public MoreSearchBarsNeoForge(IEventBus modEventBus) {

        modEventBus.addListener(this::init);

        // Auto-Config for NeoForge's built-in mods menu
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, parentScreen) -> new SearchBarConfigScreen(parentScreen)        );

        // Run our common setup.
        MoreSearchBars.init();
    }

    private void init(final FMLClientSetupEvent event) {

        NeoForge.EVENT_BUS.register(new ClientEventHandler());
    }
}
