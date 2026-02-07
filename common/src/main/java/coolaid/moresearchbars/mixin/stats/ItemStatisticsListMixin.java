package coolaid.moresearchbars.mixin.stats;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.util.INamedStatEntry;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.gui.screens.achievement.StatsScreen$ItemStatisticsList")
public class ItemStatisticsListMixin {

    @WrapOperation(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/achievement/StatsScreen$ItemStatisticsList;addEntry(Lnet/minecraft/client/gui/components/AbstractSelectionList$Entry;)I"
            )
    )
    private int wrapAddEntry(
            StatsScreen.ItemStatisticsList instance,
            AbstractSelectionList.Entry<?> entry,
            Operation<Integer> original
    ) {
        if (((INamedStatEntry) entry).moresearchbars$matchesSelection(MoreSearchBars.getStatsSearchString())) {
            return original.call(instance, entry);
        }
        return 0; // Ignored
    }
}