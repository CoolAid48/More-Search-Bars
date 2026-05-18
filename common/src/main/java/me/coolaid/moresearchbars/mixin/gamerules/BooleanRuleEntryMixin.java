package me.coolaid.moresearchbars.mixin.gamerules;

import me.coolaid.moresearchbars.util.GameRuleSearchAccess;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(EditGameRulesScreen.BooleanRuleEntry.class)
public abstract class BooleanRuleEntryMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void moresearchbars$captureRuleName(EditGameRulesScreen screen, Component label, List<FormattedCharSequence> tooltip, String name, GameRules.BooleanValue rule, CallbackInfo ci) {
        ((GameRuleSearchAccess) this).moresearchbars$appendGameRuleSearchText(name);
    }
}
