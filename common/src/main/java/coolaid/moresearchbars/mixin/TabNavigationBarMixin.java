package coolaid.moresearchbars.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import coolaid.moresearchbars.MoreSearchBars;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TabNavigationBar.class)
public class TabNavigationBarMixin {

    @Shadow
    @Final
    private ImmutableList<Tab> tabs;

    @WrapOperation(
            method = "selectTab(IZ)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/tabs/TabManager;setCurrentTab(Lnet/minecraft/client/gui/components/tabs/Tab;Z)V"
            )
    )
    private void wrapSelectTab(
            TabManager instance,
            Tab tab,
            boolean playClickSound,
            Operation<Void> original
    ) {

        // Block tab switching while typing
        if (MoreSearchBars.isSearchingServers) {
            return;
        }

        MoreSearchBars.selectedTab = tabs.indexOf(tab);

        original.call(instance, tab, playClickSound);
    }

    @WrapOperation(
            method = "setFocused(Lnet/minecraft/client/gui/components/events/GuiEventListener;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/tabs/TabManager;setCurrentTab(Lnet/minecraft/client/gui/components/tabs/Tab;Z)V"
            )
    )
    private void wrapSetFocused(
            TabManager instance,
            Tab tab,
            boolean playClickSound,
            Operation<Void> original
    ) {

        // Block focus stealing
        if (MoreSearchBars.isSearchingServers) {
            return;
        }

        MoreSearchBars.selectedTab = tabs.indexOf(tab);

        original.call(instance, tab, playClickSound);
    }
}
