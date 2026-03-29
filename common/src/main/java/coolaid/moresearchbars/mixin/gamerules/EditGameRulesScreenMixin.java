package coolaid.moresearchbars.mixin.gamerules;

import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.util.GameRuleListMixinInvoker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.EditWorldScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EditWorldScreen.class)
public abstract class EditGameRulesScreenMixin extends Screen {

    @Unique
    private GameRuleListMixinInvoker moresearchbars$ruleList;
    @Unique
    private EditBox moresearchbars$ruleSearchField;

    protected EditGameRulesScreenMixin(Component title) {
        super(title);
    }

    @Unique
    private void moresearchbars$cacheRuleList() {
        if (this.moresearchbars$ruleList != null) {
            return;
        }

        for (GuiEventListener child : this.children()) {
            if (child instanceof GameRuleListMixinInvoker invoker && child instanceof AbstractSelectionList<?>) {
                this.moresearchbars$ruleList = invoker;
                break;
            }
        }
    }

    @Unique
    private void moresearchbars$repositionElements() {
        this.moresearchbars$cacheRuleList();

        if (this.moresearchbars$ruleSearchField == null || this.moresearchbars$ruleList == null) {
            return;
        }

        int boxWidth = 200;
        int searchX = (this.width - boxWidth) / 2;
        int searchY = 24;
        this.moresearchbars$ruleSearchField.setPosition(searchX, searchY);

        int gap = 8;
        int bottomPadding = 40;
        int listY = this.moresearchbars$ruleSearchField.getY() + this.moresearchbars$ruleSearchField.getHeight() + gap;

        this.moresearchbars$ruleList.moresearchbars$setPosition(this.moresearchbars$ruleList.moresearchbars$getX(), listY);
        this.moresearchbars$ruleList.moresearchbars$setHeight(this.height - listY - bottomPadding);
        this.moresearchbars$ruleList.moresearchbars$setScrollAmount(0);
    }

    @Inject(method = "repositionElements()V", at = @At("TAIL"))
    private void moresearchbars$onReposition(CallbackInfo ci) {
        this.moresearchbars$repositionElements();
    }

    @Inject(method = "init()V", at = @At("TAIL"))
    private void moresearchbars$onInit(CallbackInfo ci) {
        if (MoreSearchBars.CONFIG == null || !MoreSearchBars.CONFIG.enableGameRuleSearch) {
            return;
        }

        this.moresearchbars$cacheRuleList();

        if (this.moresearchbars$ruleList != null) {
            this.moresearchbars$ruleList.moresearchbars$captureEntries();
        }

        this.moresearchbars$ruleSearchField = new EditBox(this.font, 0, 0, 200, 20, Component.empty());
        this.moresearchbars$ruleSearchField.setMaxLength(255);
        this.moresearchbars$ruleSearchField.setHint(Component.translatable("moresearchbars.editbox.search")
                .withStyle(ChatFormatting.GRAY)
                .withStyle(ChatFormatting.ITALIC));
        this.moresearchbars$ruleSearchField.setResponder(this::moresearchbars$onSearch);

        this.addRenderableWidget(this.moresearchbars$ruleSearchField);
        this.moresearchbars$repositionElements();
        this.setInitialFocus(this.moresearchbars$ruleSearchField);

        if (this.moresearchbars$ruleList != null) {
            this.moresearchbars$ruleList.moresearchbars$setScrollAmount(0);
        }
    }

    @Unique
    private void moresearchbars$onSearch(String text) {
        MoreSearchBars.setGameRuleSearchString(text);

        if (this.moresearchbars$ruleList == null) {
            this.moresearchbars$cacheRuleList();
        }

        if (this.moresearchbars$ruleList != null) {
            this.moresearchbars$ruleList.moresearchbars$applyFilter();
            this.moresearchbars$ruleList.moresearchbars$setScrollAmount(0);
        }
    }
}