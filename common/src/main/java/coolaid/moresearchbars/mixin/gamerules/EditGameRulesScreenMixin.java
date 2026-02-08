package coolaid.moresearchbars.mixin.gamerules;

import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.util.GameRuleListMixinInvoker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditGameRulesScreen.class)
public abstract class EditGameRulesScreenMixin extends Screen {

    @Shadow private EditGameRulesScreen.RuleList ruleList;
    @Unique private EditBox moresearchbars$ruleSearchField;

    protected EditGameRulesScreenMixin(Component title) {
        super(title);
    }

    @Unique
    private void moresearchbars$repositionElements() {
        if (this.moresearchbars$ruleSearchField == null || this.ruleList == null) return;

        int boxWidth = 200;
        int searchX = (this.width - boxWidth) / 2;
        int searchY = 24;
        this.moresearchbars$ruleSearchField.setPosition(searchX, searchY);

        int gap = 8;
        int bottomPadding = 40;
        int listY = this.moresearchbars$ruleSearchField.getY() + this.moresearchbars$ruleSearchField.getHeight() + gap;

        this.ruleList.setPosition(this.ruleList.getX(), listY);
        this.ruleList.setHeight(this.height - listY - bottomPadding);

        // Reset scroll when repositioning
        this.ruleList.setScrollAmount(0);
    }

    @Inject(method = "repositionElements", at = @At("TAIL"))
    private void onReposition(CallbackInfo ci) {
        this.moresearchbars$repositionElements();
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {

        if (!MoreSearchBars.CONFIG.enableGameruleSearch) {
            return;
        }

        if (this.ruleList != null) {
            ((GameRuleListMixinInvoker)this.ruleList).moresearchbars$captureEntries();
        }

        this.moresearchbars$ruleSearchField = new EditBox(
                this.font,
                0, 0, 200, 20,
                Component.literal("")
        );

        moresearchbars$ruleSearchField.setMaxLength(255);
        moresearchbars$ruleSearchField.setHint(
                Component.literal("Search...").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)
        );
        this.moresearchbars$ruleSearchField.setResponder(this::moresearchbars$onSearch);

        // Add to screen and run repositioning
        this.addRenderableWidget(this.moresearchbars$ruleSearchField);
        this.moresearchbars$repositionElements();
        this.setInitialFocus(this.moresearchbars$ruleSearchField);

        // Reset scroll when screen is initialized
        if (this.ruleList != null) {
            this.ruleList.setScrollAmount(0);
        }
    }

    @Unique
    private void moresearchbars$onSearch(String text) {
        MoreSearchBars.setGameRuleSearchString(text);
        if (this.ruleList != null) {
            ((GameRuleListMixinInvoker)this.ruleList).moresearchbars$applyFilter();
            // Reset scroll after filtering
            this.ruleList.setScrollAmount(0);
        }
    }
}