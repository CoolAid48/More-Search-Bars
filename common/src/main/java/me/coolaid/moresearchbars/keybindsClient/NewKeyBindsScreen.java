package me.coolaid.moresearchbars.keybindsClient;

import me.coolaid.moresearchbars.Constants;
import me.coolaid.moresearchbars.keybindsAPI.entries.ICategoryEntry;
import me.coolaid.moresearchbars.mixin.keybinds.AccessKeyBindsScreen;
import me.coolaid.moresearchbars.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class NewKeyBindsScreen extends KeyBindsScreen {

    private EditBox search;

    public NewKeyBindsScreen(Screen screen, Options settings) {
        super(screen, settings);
        this.layout.setHeaderHeight(48);
    }

    @Override
    protected void init() {
        super.init();
        if (this.search != null) {
            this.search.moveCursor(0, false);
        }
    }

    @Override
    protected void addTitle() {
        int searchWidth = 200;
        int centerX = this.width / 2;
        this.search = new EditBox(font, centerX - searchWidth / 2, 20, searchWidth, Button.DEFAULT_HEIGHT,
                Component.translatable("moresearchbars.editbox.search"));
        this.search.setHint(Component.translatable("moresearchbars.editbox.search")
                .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        this.search.setResponder(this::filterKeys);

        LinearLayout header = this.layout.addToHeader(LinearLayout.vertical(),
                layoutSettings -> layoutSettings.paddingVertical(8));
        header.addChild(new StringWidget(this.title, this.font), LayoutSettings::alignHorizontallyCenter);
        header.addChild(this.search, layoutSettings -> layoutSettings.paddingVertical(4));
        setInitialFocus(this.search);
    }

    @Override
    protected void addContents() {
        NewKeyBindsList newKeyList = new NewKeyBindsList(this, this.minecraft);
        getAccess().moresearchbars$setKeyBindsList(newKeyList);
        this.layout.addToContents(newKeyList);
    }

    public void filterKeys() {
        if (search == null) return;
        filterKeys(search.getValue());
    }

    public void filterKeys(String query) {
        CustomList list = getCustomList();
        String normalizedQuery = Constants.normalizeSearchText(query);
        list.clearEntries();
        getKeyBindsList().setScrollAmount(0);

        if (normalizedQuery.isEmpty()) {
            for (KeyBindsList.Entry entry : list.getAllEntries()) {
                list.addEntryInternal(entry);
            }
            return;
        }

        KeyBindsList.Entry currentCategory = null;
        boolean categoryAdded = false;
        for (KeyBindsList.Entry entry : list.getAllEntries()) {
            if (entry instanceof ICategoryEntry) {
                currentCategory = entry;
                categoryAdded = false;
                continue;
            }
            if (Constants.matchesNormalizedKeybindSearch(entry, normalizedQuery)) {
                if (currentCategory != null && !categoryAdded) {
                    list.addEntryInternal(currentCategory);
                    categoryAdded = true;
                }
                list.addEntryInternal(entry);
            }
        }
    }

    void moresearchbars$clearSearchFocus() {
        if (this.search != null && this.search.isFocused()) {
            this.search.setFocused(false);
        }
    }

    void moresearchbars$cancelPendingSelection() {
        this.selectedKey = null;
        Services.PLATFORM.cancelPendingSelection(this);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (search == null) return super.keyPressed(event);
        if (!search.isFocused() && this.selectedKey == null
                && event.hasControlDown() && event.key() == GLFW.GLFW_KEY_F) {
            search.setFocused(true);
            return true;
        }
        if (search.isFocused() && event.isEscape()) {
            search.setFocused(false);
            return true;
        }
        if (this.selectedKey != null) {
            Services.PLATFORM.handleKeyPress(this, this.options, event);
            return true;
        }
        return super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (Services.PLATFORM.handleKeyReleased(this, this.options, event)) return true;
        return super.keyReleased(event);
    }

    public KeyBindsList getKeyBindsList() {
        return getAccess().moresearchbars$getKeyBindsList();
    }

    private CustomList getCustomList() {
        if (this.getKeyBindsList() instanceof CustomList cl) return cl;
        throw new IllegalStateException(
                "keyBindsList('%s') is not a CustomList".formatted(this.getKeyBindsList().getClass()));
    }

    private AccessKeyBindsScreen getAccess() {
        return (AccessKeyBindsScreen) this;
    }
}
