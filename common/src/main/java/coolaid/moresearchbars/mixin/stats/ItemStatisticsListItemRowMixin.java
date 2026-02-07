package coolaid.moresearchbars.mixin.stats;

import coolaid.moresearchbars.util.INamedStatEntry;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Locale;

@Mixin(targets = "net.minecraft.client.gui.screens.achievement.StatsScreen$ItemStatisticsList$ItemRow")
public class ItemStatisticsListItemRowMixin implements INamedStatEntry {

    @Shadow
    @Final
    private Item item;

    @Override
    public boolean moresearchbars$matchesSelection(String selection) {
        return item.getName(item.getDefaultInstance()).getString().toLowerCase(Locale.ROOT).contains(selection);
    }
}