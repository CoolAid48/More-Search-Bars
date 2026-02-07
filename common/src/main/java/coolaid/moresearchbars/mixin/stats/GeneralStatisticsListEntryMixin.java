package coolaid.moresearchbars.mixin.stats;

import coolaid.moresearchbars.util.INamedStatEntry;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Locale;

@Mixin(targets = "net.minecraft.client.gui.screens.achievement.StatsScreen$GeneralStatisticsList$Entry")
public class GeneralStatisticsListEntryMixin implements INamedStatEntry {

    @Shadow
    @Final
    private Component statDisplay;

    @Override
    public boolean moresearchbars$matchesSelection(String selection) {
        return statDisplay.getString().toLowerCase(Locale.ROOT).contains(selection);
    }
}