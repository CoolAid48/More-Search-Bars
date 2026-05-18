package me.coolaid.moresearchbars.mixin.gamerules;

import me.coolaid.moresearchbars.util.GameRuleSearchAccess;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Locale;

@Mixin(EditGameRulesScreen.GameRuleEntry.class)
public abstract class GameRuleEntryMixin implements GameRuleSearchAccess {

    @Unique
    private String moresearchbars$searchText = "";

    @Inject(method = "<init>", at = @At("TAIL"))
    private void moresearchbars$captureLabel(EditGameRulesScreen screen, List<FormattedCharSequence> tooltip, Component label, CallbackInfo ci) {
        moresearchbars$appendGameRuleSearchText(label.getString());
    }

    @Override
    public boolean moresearchbars$matchesGameRuleSearch(String query) {
        return moresearchbars$searchText.contains(query);
    }

    @Override
    public boolean moresearchbars$isGameRuleCategory() {
        return false;
    }

    @Override
    public void moresearchbars$appendGameRuleSearchText(String text) {
        if (text == null || text.isBlank()) {
            return;
        }
        moresearchbars$searchText = (moresearchbars$searchText + " " + text).toLowerCase(Locale.ROOT);
    }
}
