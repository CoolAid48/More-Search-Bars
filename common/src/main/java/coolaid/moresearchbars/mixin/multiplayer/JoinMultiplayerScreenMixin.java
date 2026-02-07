package coolaid.moresearchbars.mixin.multiplayer;

import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.util.ServerSelectionListMixinInvoker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(JoinMultiplayerScreen.class)
public abstract class JoinMultiplayerScreenMixin extends Screen {

    @Shadow private ServerSelectionList serverSelectionList;
    @Shadow private ServerList servers;
    @Unique private EditBox moresearchbars$serverSearchField;

    protected JoinMultiplayerScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci) {
        int boxWidth = 200;
        int boxHeight = 20;
        int x = (this.width - boxWidth) / 2;
        int y = 24;

        moresearchbars$serverSearchField = new EditBox(
                this.font, x, y, boxWidth, boxHeight, Component.literal("")
        );

        moresearchbars$serverSearchField.setMaxLength(255);
        moresearchbars$serverSearchField.setHint(
                Component.literal("Search...").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)
        );
        moresearchbars$serverSearchField.setResponder(this::filterServersBySearch);

        // Add the widget to the screen and set the search bar as the initial focus
        this.addWidget(moresearchbars$serverSearchField);
        this.addRenderableWidget(moresearchbars$serverSearchField);
        this.setInitialFocus(moresearchbars$serverSearchField);

        repositionServerList();

        if (servers != null && serverSelectionList != null) {
            serverSelectionList.updateOnlineServers(servers);
        }
    }

    @Unique
    private void repositionServerList() {
        if (moresearchbars$serverSearchField == null || serverSelectionList == null) return;

        int gap = 8;
        int bottomPadding = 60;
        int listY = moresearchbars$serverSearchField.getY() + moresearchbars$serverSearchField.getHeight() + gap;
        serverSelectionList.setPosition(serverSelectionList.getX(), listY);
        serverSelectionList.setHeight(this.height - listY - bottomPadding);
    }

    @Unique
    private void filterServersBySearch(String text) {
        if (serverSelectionList == null || servers == null) return;

        MoreSearchBars.setServerSearchString(text);
        ((ServerSelectionListMixinInvoker)serverSelectionList).moresearchbars$applyFilter();
        serverSelectionList.setScrollAmount(0);
    }

    @Inject(method = "repositionElements", at = @At("TAIL"))
    private void onResize(CallbackInfo ci) {
        if (moresearchbars$serverSearchField == null) return;

        int boxWidth = 200;
        int x = (this.width - boxWidth) / 2;
        moresearchbars$serverSearchField.setPosition(x, 24);

        repositionServerList();
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void onRemoved(CallbackInfo ci) {
        MoreSearchBars.setServerSearchString("");
    }
}