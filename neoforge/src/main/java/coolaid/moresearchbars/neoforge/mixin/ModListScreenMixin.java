package coolaid.moresearchbars.neoforge.mixin;

import java.util.Comparator;
import java.util.List;
import net.neoforged.neoforge.client.gui.ModListScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ModListScreen.class)
public class ModListScreenMixin {
    @Redirect(
            method = "tick",
            at = @At(value = "INVOKE", target = "Ljava/util/List;sort(Ljava/util/Comparator;)V")
    )
    private void moresearchbars$guardImmutableSort(List<?> list, Comparator<?> comparator) {
        try {
            list.sort((Comparator) comparator);
        } catch (UnsupportedOperationException ignored) {
            // NeoForge's mod list screen can attempt to sort an immutable list; ignore to prevent crash.
        }
    }
}