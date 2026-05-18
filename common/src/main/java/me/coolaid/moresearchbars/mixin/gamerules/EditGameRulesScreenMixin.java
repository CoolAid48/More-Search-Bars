package me.coolaid.moresearchbars.mixin.gamerules;

import me.coolaid.moresearchbars.MoreSearchBars;
import me.coolaid.moresearchbars.util.GameRuleSearchAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditGameRulesScreen.class)
public abstract class EditGameRulesScreenMixin extends Screen {

    @Shadow
    @Final
    HeaderAndFooterLayout layout;

    @Shadow
    private EditGameRulesScreen.RuleList ruleList;

    @Unique
    private EditBox moresearchbars$gameRuleSearchField;

    protected EditGameRulesScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void moresearchbars$clearSearch(CallbackInfo ci) {
        MoreSearchBars.setGameRulesSearchString("");
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addTitleHeader(Lnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/Font;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void moresearchbars$addSearchField(CallbackInfo ci) {
        if (!moresearchbars$isGameRulesSearchEnabled()) {
            return;
        }

        int searchWidth = 200;
        moresearchbars$gameRuleSearchField = new EditBox(
                this.font,
                (this.width - searchWidth) / 2,
                0,
                searchWidth,
                20,
                Component.translatable("moresearchbars.editbox.search")
        );
        moresearchbars$gameRuleSearchField.setHint(Component.translatable("moresearchbars.editbox.search")
                .withStyle(ChatFormatting.GRAY)
                .withStyle(ChatFormatting.ITALIC));
        moresearchbars$gameRuleSearchField.setResponder(this::moresearchbars$filterGameRules);

        this.layout.setHeaderHeight(58);
        this.layout.addToHeader(moresearchbars$gameRuleSearchField, settings -> settings
                .alignHorizontallyCenter()
                .paddingTop(4));
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void moresearchbars$focusSearchField(CallbackInfo ci) {
        if (moresearchbars$gameRuleSearchField == null) {
            return;
        }

        this.setInitialFocus(moresearchbars$gameRuleSearchField);
        moresearchbars$gameRuleSearchField.setFocused(true);
        moresearchbars$filterGameRules(moresearchbars$gameRuleSearchField.getValue());
    }

    @Inject(method = "onClose", at = @At("HEAD"))
    private void moresearchbars$resetSearchOnClose(CallbackInfo ci) {
        MoreSearchBars.setGameRulesSearchString("");
    }

    @Unique
    private boolean moresearchbars$isGameRulesSearchEnabled() {
        return MoreSearchBars.CONFIG == null || MoreSearchBars.CONFIG.enableGameRulesSearch;
    }

    @Unique
    private void moresearchbars$filterGameRules(String text) {
        MoreSearchBars.setGameRulesSearchString(text);
        if (this.ruleList != null) {
            ((GameRuleSearchAccess) this.ruleList).moresearchbars$applyFilter();
        }
    }
}
