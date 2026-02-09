package coolaid.moresearchbars.mixin.stats;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import coolaid.moresearchbars.MoreSearchBars;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.layouts.SpacerElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StatsScreen.class)
public abstract class StatsScreenMixin extends Screen {

    @Shadow
    public abstract void onStatsUpdated();

    @Shadow
    private boolean isLoading;

    @Shadow
    @Nullable
    private TabNavigationBar tabNavigationBar;

    @Unique
    private EditBox moresearchbars$searchField;

    public StatsScreenMixin(Component text) {
        super(text);
    }

    @Unique
    private boolean isStatsSearchEnabled() {
        return MoreSearchBars.CONFIG == null || MoreSearchBars.CONFIG.enableStatsSearch;
    }

    // Clears the search query on screen init.
    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {

        // Config toggle is handled in isStatsSearchEnabled()
        MoreSearchBars.setStatsSearchString("");
    }

    @WrapOperation(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addToFooter(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"
            )
    )

    private <T extends LayoutElement> T wrapAddDoneButton(
            HeaderAndFooterLayout instance, T child, Operation<T> original
    ) {
        if (!isStatsSearchEnabled()) {
            return original.call(instance, child);
        }

        int height = 20;
        int fieldWidth = 120;
        moresearchbars$searchField = new EditBox(font, fieldWidth, height, Component.empty());
        moresearchbars$searchField.setCanLoseFocus(false);
        moresearchbars$searchField.setFocused(true);
        moresearchbars$searchField.setHint(Component.literal("Search Statistics..."));

        LinearLayout layout = new LinearLayout(
                width,
                Math.max(height, child.getHeight()),
                LinearLayout.Orientation.HORIZONTAL
        );
        layout.addChild(moresearchbars$searchField);
        layout.addChild(new SpacerElement(4, height));

        if (child instanceof LinearLayout l) {
            l.visitChildren(layout::addChild);
        } else {
            if (child instanceof Button b)
                b.setWidth(Math.max(120, b.getWidth() / 2));
            layout.addChild(child);
        }

        original.call(instance, layout);
        return null;
    }

    // Directs keyboard and mouse inputs into the search field.
    @Override
    public boolean keyPressed(KeyEvent event) {
        if (isStatsSearchEnabled() && moresearchbars$searchField != null && moresearchbars$searchField.keyPressed(event)) {
            moresearchbars$refresh();
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (isStatsSearchEnabled() && moresearchbars$searchField.charTyped(event)) {
            moresearchbars$refresh();
            return true;
        }
        return super.charTyped(event);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return super.mouseClicked(event, doubleClick);
    }

    @Unique
    private void moresearchbars$refresh() {
        MoreSearchBars.setStatsSearchString(moresearchbars$searchField.getValue());
        int selected = MoreSearchBars.selectedTab;
        isLoading = true;
        onStatsUpdated();
        if (selected >= 0 && tabNavigationBar != null)
            tabNavigationBar.selectTab(selected, false);
    }
}