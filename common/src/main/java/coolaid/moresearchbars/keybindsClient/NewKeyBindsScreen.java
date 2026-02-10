package coolaid.moresearchbars.keybindsClient;

import com.google.common.base.Suppliers;
import coolaid.moresearchbars.Constants;
import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.keybindsAPI.DisplayMode;
import coolaid.moresearchbars.mixin.keybinds.AccessKeyBindsScreen;
import coolaid.moresearchbars.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NewKeyBindsScreen extends KeyBindsScreen {

    private EditBox search;
    private DisplayMode displayMode;
    private Button buttonNone;
    private Button buttonConflicting;
    private final DisplayableBoolean confirmingReset = new DisplayableBoolean(false, Constants.COMPONENT_OPTIONS_CONFIRM_RESET, Constants.COMPONENT_CONTROLS_RESET_ALL);


    public NewKeyBindsScreen(Screen screen, Options settings) {
        super(screen, settings);
        this.layout.setHeaderHeight(48);
        this.layout.setFooterHeight(84);
    }

    @Override
    protected void init() {
        super.init();
        if (isSearchEnabled() && this.search != null) {
            this.search.moveCursor(0, false);
        }
    }

    @Override
    protected void addTitle() {
        if (!isSearchEnabled()) {
            super.addTitle();
            return;
        }
        int searchX = 200;
        int centerX = this.width / 2;
        this.search = new EditBox(font, centerX - searchX / 2, 20, searchX, Button.DEFAULT_HEIGHT, Component.translatable("moresearchbars.editbox.search"));
        this.search.setHint(Component.translatable("moresearchbars.editbox.search").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        this.search.setResponder(this::filterKeys);

        LinearLayout header = this.layout.addToHeader(LinearLayout.vertical(), layoutSettings -> layoutSettings.paddingVertical(8));
        header.addChild(new StringWidget(this.title, this.font), LayoutSettings::alignHorizontallyCenter);
        header.addChild(this.search, layoutSettings -> layoutSettings.paddingVertical(4));
        setInitialFocus(this.search);
    }

    @Override
    protected void addContents() {
        Supplier<NewKeyBindsList> newKeyList = Suppliers.memoize(() -> new NewKeyBindsList(this, this.minecraft));
        getAccess().moresearchbars$setKeyBindsList(newKeyList.get());
        this.layout.addToContents(getKeyBindsList());
        displayMode = DisplayMode.ALL;
    }

    @Override
    protected void addFooter() {
        int btnWidth = 150;

        // Buttons
        this.resetButton(Button.builder(confirmingReset.currentDisplay(), PRESS_RESET).size(btnWidth, Button.DEFAULT_HEIGHT).build());
        resetButton().active = canReset();

        this.buttonNone = Button.builder(Constants.COMPONENT_OPTIONS_SHOW_NONE, PRESS_NONE).size(btnWidth, Button.DEFAULT_HEIGHT).build();
        this.buttonConflicting = Button.builder(Constants.COMPONENT_OPTIONS_SHOW_CONFLICTS, PRESS_CONFLICTING).size(btnWidth, Button.DEFAULT_HEIGHT).build();

        Button doneButton = Button.builder(CommonComponents.GUI_DONE, btn -> this.onClose()).size(btnWidth, Button.DEFAULT_HEIGHT).build();

        // Grid Layout
        GridLayout grid = this.layout.addToFooter(new GridLayout());
        grid.rowSpacing(4);
        grid.columnSpacing(8);

        // Manage the rows and add a buffer to push everything down. 2 columns wide and 24 pixels tall.
        GridLayout.RowHelper rowHelper = grid.createRowHelper(2);
        rowHelper.addChild(net.minecraft.client.gui.layouts.SpacerElement.height(24), 2);

        // 4 buttons (2 top and 2 bottom)
        rowHelper.addChild(this.buttonNone);
        rowHelper.addChild(this.buttonConflicting);
        rowHelper.addChild(resetButton());
        rowHelper.addChild(doneButton);
    }

    @Override
    protected void repositionElements() {
        super.repositionElements();
        resetButton().active = canReset();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mxPos, int myPos, float partialTicks) {
        super.render(guiGraphics, mxPos, myPos, partialTicks);
    }

    public Button resetButton() {
        return this.getAccess().moresearchbars$getResetButton();
    }

    public void resetButton(Button button) {
        this.getAccess().moresearchbars$setResetButton(button);
    }

    public void filterKeys() {
        if (search == null) {
            return;
        }
        filterKeys(search.getValue());
    }

    public void filterKeys(String lastSearch) {
        CustomList list = getCustomList();

        list.clearEntries();
        getKeyBindsList().setScrollAmount(0);
        if (lastSearch.isEmpty() && displayMode == DisplayMode.ALL) {
            for (KeyBindsList.Entry allEntry : getCustomList().getAllEntries()) {
                list.addEntryInternal(allEntry);
            }
            return;
        }

        Predicate<KeyBindsList.Entry> extraPredicate = entry -> true;

        if (list instanceof NewKeyBindsList) {
            extraPredicate = displayMode.getPredicate();
        }
        for (KeyBindsList.Entry entry : list.getAllEntries()) {
            if (extraPredicate.test(entry) && Constants.matchesKeybindSearch(entry, lastSearch)) {
                list.addEntryInternal(entry);
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseScrolled(double xpos, double ypos, double xDelta, double yDelta) {
        return super.mouseScrolled(xpos, ypos, xDelta, yDelta);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (search == null) {
            return super.keyPressed(event);
        }
        if (!search.isFocused() && this.selectedKey == null) {
            if (event.hasControlDown() && event.key() == GLFW.GLFW_KEY_F) {
                search.setFocused(true);
                return true;
            }
        }
        if (search.isFocused()) {
            if (event.isEscape()) {
                search.setFocused(false);
                return true;
            }
        }
        if (this.selectedKey != null) {
            Services.PLATFORM.handleKeyPress(this, this.options, event);
            return true;
        }
        return super.keyPressed(event);
    }

    private boolean isSearchEnabled() {
        return MoreSearchBars.CONFIG == null || MoreSearchBars.CONFIG.enableKeybindsSearch;
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        if (Services.PLATFORM.handleKeyReleased(this, this.options, event)) {
            return true;
        }
        return super.keyReleased(event);
    }

    private CustomList getCustomList() {
        if (this.getKeyBindsList() instanceof CustomList cl) {
            return cl;
        }
        throw new IllegalStateException("keyBindsList('%s') was not an instance of CustomList!".formatted(this.getKeyBindsList().getClass()));
    }

    public KeyBindsList getKeyBindsList() {
        return getAccess().moresearchbars$getKeyBindsList();
    }

    private AccessKeyBindsScreen getAccess() {
        return ((AccessKeyBindsScreen) this);
    }

    private boolean canReset() {
        for (KeyMapping key : this.options.keyMappings) {
            if (!key.isDefault()) {
                return true;
            }
        }
        return false;
    }

    private final Button.OnPress PRESS_RESET = btn -> {
        NewKeyBindsScreen screen = NewKeyBindsScreen.this;
        Minecraft minecraft = Objects.requireNonNull(screen.minecraft);

        if (!confirmingReset.toggle()) {
            for (KeyMapping keybinding : minecraft.options.keyMappings) {
                Services.PLATFORM.setToDefault(minecraft.options, keybinding);
            }
            getKeyBindsList().resetMappingAndUpdateButtons();
        }
        btn.setMessage(confirmingReset.currentDisplay());
    };

    private final Button.OnPress PRESS_NONE = btn -> {
        if (displayMode == DisplayMode.NONE) {
            buttonNone.setMessage(Constants.COMPONENT_OPTIONS_SHOW_NONE);
            displayMode = DisplayMode.ALL;
        } else {
            displayMode = DisplayMode.NONE;
            buttonNone.setMessage(Constants.COMPONENT_OPTIONS_SHOW_ALL);
            buttonConflicting.setMessage(Constants.COMPONENT_OPTIONS_SHOW_CONFLICTS);
        }
        filterKeys();
    };

    private final Button.OnPress PRESS_CONFLICTING = btn -> {
        if (displayMode == DisplayMode.CONFLICTING) {
            buttonConflicting.setMessage(Constants.COMPONENT_OPTIONS_SHOW_CONFLICTS);
            displayMode = DisplayMode.ALL;
        } else {
            displayMode = DisplayMode.CONFLICTING;
            buttonConflicting.setMessage(Constants.COMPONENT_OPTIONS_SHOW_ALL);
            buttonNone.setMessage(Constants.COMPONENT_OPTIONS_SHOW_NONE);
        }
        filterKeys();
    };
}