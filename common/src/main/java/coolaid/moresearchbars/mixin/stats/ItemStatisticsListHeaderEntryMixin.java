package coolaid.moresearchbars.mixin.stats;

import coolaid.moresearchbars.util.INamedStatEntry;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net.minecraft.client.gui.screens.achievement.StatsScreen$ItemStatisticsList$HeaderEntry")
public class ItemStatisticsListHeaderEntryMixin implements INamedStatEntry {

    @Override
    public boolean moresearchbars$matchesSelection(String selection) {
        return true;
    }
}