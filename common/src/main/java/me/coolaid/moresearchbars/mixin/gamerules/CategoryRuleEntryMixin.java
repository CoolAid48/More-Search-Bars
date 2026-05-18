package me.coolaid.moresearchbars.mixin.gamerules;

import me.coolaid.moresearchbars.Constants;
import me.coolaid.moresearchbars.util.GameRuleSearchAccess;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EditGameRulesScreen.CategoryRuleEntry.class)
public abstract class CategoryRuleEntryMixin implements GameRuleSearchAccess {

    @Shadow
    @Final
    Component label;

    @Unique
    private String moresearchbars$searchText;

    @Override
    public boolean moresearchbars$matchesGameRuleSearch(String query) {
        if (moresearchbars$searchText == null) {
            moresearchbars$searchText = Constants.normalizeSearchText(label.getString());
        }
        return moresearchbars$searchText.contains(query);
    }

    @Override
    public boolean moresearchbars$isGameRuleCategory() {
        return true;
    }
}
