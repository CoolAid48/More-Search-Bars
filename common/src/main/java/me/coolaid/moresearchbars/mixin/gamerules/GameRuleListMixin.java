package me.coolaid.moresearchbars.mixin.gamerules;

import me.coolaid.moresearchbars.MoreSearchBars;
import me.coolaid.moresearchbars.util.GameRuleSearchAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(EditGameRulesScreen.RuleList.class)
public abstract class GameRuleListMixin extends ContainerObjectSelectionList<EditGameRulesScreen.RuleEntry> implements GameRuleSearchAccess {

    @Unique
    private final List<EditGameRulesScreen.RuleEntry> moresearchbars$allEntries = new ArrayList<>();

    public GameRuleListMixin(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void moresearchbars$captureEntries(EditGameRulesScreen screen, GameRules gameRules, CallbackInfo ci) {
        moresearchbars$captureEntries();
    }

    @Unique
    private void moresearchbars$captureEntries() {
        moresearchbars$allEntries.clear();
        moresearchbars$allEntries.addAll(this.children());
        moresearchbars$applyFilter();
    }

    @Override
    public void moresearchbars$applyFilter() {
        String query = MoreSearchBars.getGameRulesSearchString();

        this.clearEntries();

        EditGameRulesScreen.RuleEntry currentCategory = null;
        boolean categoryAdded = false;
        boolean categoryMatches = false;

        for (EditGameRulesScreen.RuleEntry entry : moresearchbars$allEntries) {
            if (query.isEmpty()) {
                this.addEntry(entry);
                continue;
            }

            GameRuleSearchAccess searchEntry = (GameRuleSearchAccess) entry;
            if (searchEntry.moresearchbars$isGameRuleCategory()) {
                currentCategory = entry;
                categoryMatches = searchEntry.moresearchbars$matchesGameRuleSearch(query);
                categoryAdded = false;
                if (categoryMatches) {
                    this.addEntry(entry);
                    categoryAdded = true;
                }
                continue;
            }

            if (categoryMatches || searchEntry.moresearchbars$matchesGameRuleSearch(query)) {
                if (currentCategory != null && !categoryAdded) {
                    this.addEntry(currentCategory);
                    categoryAdded = true;
                }
                this.addEntry(entry);
            }
        }

        this.setScrollAmount(0);
    }
}
