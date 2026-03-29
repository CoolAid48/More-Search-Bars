package coolaid.moresearchbars.keybindsClient;

import com.google.common.collect.ImmutableList;
import coolaid.moresearchbars.Constants;
import coolaid.moresearchbars.keybindsAPI.entries.ICategoryEntry;
import coolaid.moresearchbars.keybindsAPI.entries.IKeyEntry;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryListenersEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseClickedEvent;
import coolaid.moresearchbars.keybindsAPI.events.IKeyEntryMouseReleasedEvent;
import coolaid.moresearchbars.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.options.controls.KeyBindsList;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.CommonColors;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;

public class NewKeyBindsList extends CustomList {

    private static final int HEADER_HEIGHT = 48;
    private static final int FOOTER_HEIGHT = 54;

    private final KeyBindsScreen controlsScreen;
    private final Minecraft mc;
    private int maxListLabelWidth;

    public NewKeyBindsList(KeyBindsScreen controls, Minecraft mcIn) {

        super(controls, mcIn);
        this.setY(HEADER_HEIGHT);
        this.setHeight(controls.height - HEADER_HEIGHT - FOOTER_HEIGHT);
        this.controlsScreen = controls;
        this.mc = mcIn;
        this.clearEntries();
        allEntries = new ArrayList<>();
        KeyMapping[] bindings = ArrayUtils.clone(mcIn.options.keyMappings);
        Arrays.sort(bindings);
        KeyMapping.Category lastCategory = null;

        for(KeyMapping keybinding : bindings) {
            KeyMapping.Category category = keybinding.getCategory();
            if(!category.equals(lastCategory)) {
                lastCategory = category;
                if(!isHidden(category.label())) {
                    addEntry(new NewKeyBindsList.CategoryEntry(category));
                }
            }

            Component component = Services.PLATFORM.getKeyName(keybinding);
            int width = mcIn.font.width(component);
            if(width > this.maxListLabelWidth) {
                this.maxListLabelWidth = width;
            }
            if(!isHidden(category.label())) {
                addEntry(new NewKeyBindsList.KeyEntry(keybinding, component));
            }
        }

    }

    private boolean isHidden(Component component) {

        if(component.getContents() instanceof TranslatableContents tc) {
            return tc.getKey().endsWith(".hidden");
        }
        return false;
    }

    private void moresearchbars$clearSearchFocus() {
        if (this.controlsScreen instanceof NewKeyBindsScreen newScreen) {
            newScreen.moresearchbars$clearSearchFocus();
        }
    }

    private void moresearchbars$cancelPendingSelection() {
        if (this.controlsScreen instanceof NewKeyBindsScreen newScreen) {
            newScreen.moresearchbars$cancelPendingSelection();
        } else {
            this.controlsScreen.selectedKey = null;
        }
    }

    @Override
    public int getBottom() {

        return this.getY() + this.getHeight();
    }

    @Override
    public void resetMappingAndUpdateButtons() {

        super.resetMappingAndUpdateButtons();
        for (KeyBindsList.Entry entry : this.getAllEntries()) {
            if (entry instanceof KeyEntry keyEntry) {
                keyEntry.refreshEntry();
            }
        }
    }

    public class CategoryEntry extends Entry implements ICategoryEntry {

        private final KeyMapping.Category category;
        private final Component categoryName;

        public CategoryEntry(KeyMapping.Category category) {

            this.category = category;
            this.categoryName = category.label();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {

            int x = NewKeyBindsList.this.width / 2 - NewKeyBindsList.this.minecraft.font.width(this.categoryName) / 2;
            graphics.text(NewKeyBindsList.this.minecraft.font, this.categoryName, x, this.getContentBottom() - 9 - 1, CommonColors.WHITE);
        }

        public List<? extends NarratableEntry> narratables() {

            return List.of();
        }

        @Override
        public List<? extends GuiEventListener> children() {

            return List.of();
        }

        @Override
        protected void refreshEntry() {

        }

        public Component categoryName() {

            return categoryName;
        }

        @Override
        public KeyMapping.Category category() {

            return this.category;
        }

    }

    public class KeyEntry extends KeyBindsList.Entry implements IKeyEntry {

        private final KeyMapping key;
        private final Component keyDesc;
        private final Button btnChangeKeyBinding;
        private final Button btnResetKeyBinding;

        private boolean hasCollision;

        private final Component categoryName;

        public KeyEntry(final KeyMapping key, final Component keyDesc) {

            this.key = key;
            this.keyDesc = keyDesc;
            this.btnChangeKeyBinding = Button.builder(this.keyDesc, (btn) -> {
                        NewKeyBindsList.this.moresearchbars$clearSearchFocus();
                        NewKeyBindsList.this.moresearchbars$cancelPendingSelection();
                        NewKeyBindsList.this.controlsScreen.selectedKey = key;
                        NewKeyBindsList.this.resetMappingAndUpdateButtons();
                    })
                    .bounds(0, 0, 75, 20)
                    .createNarration(supp -> key.isUnbound() ? Component.translatable("narrator.controls.unbound", keyDesc) : Component.translatable("narrator.controls.bound", keyDesc, supp.get()))
                    .build();

            this.btnResetKeyBinding = Button.builder(Constants.COMPONENT_OPTIONS_RESET, btn -> {
                        NewKeyBindsList.this.moresearchbars$clearSearchFocus();
                        NewKeyBindsList.this.moresearchbars$cancelPendingSelection();
                        Services.PLATFORM.setToDefault(minecraft.options, key);
                        NewKeyBindsList.this.resetMappingAndUpdateButtons();
                    }).bounds(0, 0, 50, 20)
                    .createNarration(supp -> Component.translatable("narrator.controls.reset", keyDesc))
                    .build();

            this.categoryName = this.key.getCategory().label();
            refreshEntry();
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {

            refreshEntry();
            Services.EVENT.fireKeyEntryRenderEvent(this, graphics, this.getContentX(), this.getContentY(), getRowLeft(), getRowWidth(), hovered, partialTicks);

            int resetKeyX = NewKeyBindsList.this.scrollBarX() - this.btnResetKeyBinding.getWidth() - 10;
            int top = this.getContentY() - 2;
            this.btnResetKeyBinding.setPosition(resetKeyX, top);
            this.btnResetKeyBinding.extractRenderState(graphics, mouseX, mouseY, partialTicks);

            this.btnChangeKeyBinding.setPosition(resetKeyX - 5 - this.btnChangeKeyBinding.getWidth(), top);
            this.btnChangeKeyBinding.extractRenderState(graphics, mouseX, mouseY, partialTicks);

            Font font = NewKeyBindsList.this.mc.font;
            graphics.text(font, this.keyDesc, this.getContentX(), this.getContentYMiddle() - 9 / 2, CommonColors.WHITE);

            if(this.hasCollision) {
                int markerWidth = 3;
                int minX = this.btnChangeKeyBinding.getX() - 6;
                graphics.fill(minX, this.getContentY() - 1, minX + markerWidth, this.getContentBottom(), CommonColors.YELLOW);
            }
        }

        @Override
        public void renderContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTicks) {
            this.extractContent(graphics, mouseX, mouseY, hovered, partialTicks);
        }

        public List<GuiEventListener> children() {
            List<GuiEventListener> listeners = new ArrayList<>(2);
            listeners.add(this.btnChangeKeyBinding);
            listeners.add(this.btnResetKeyBinding);

            List<GuiEventListener> eventListeners = Services.EVENT.fireKeyEntryListenersEvent(this)
                    .map(IKeyEntryListenersEvent::getListeners, UnaryOperator.identity());

            for (GuiEventListener listener : eventListeners) {
                if (listener != this.btnChangeKeyBinding && listener != this.btnResetKeyBinding) {
                    listeners.add(listener);
                }
            }

            return listeners;
        }

        public List<? extends NarratableEntry> narratables() {

            return ImmutableList.of(this.btnChangeKeyBinding, this.btnResetKeyBinding);
        }


        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            NewKeyBindsList.this.moresearchbars$clearSearchFocus();

            if(Services.EVENT.fireKeyEntryMouseClickedEvent(this, event, doubleClick)
                    .map(IKeyEntryMouseClickedEvent::isHandled, UnaryOperator.identity())) {
                return true;
            }
            return super.mouseClicked(event, doubleClick);
        }

        @Override
        public boolean mouseReleased(MouseButtonEvent event) {

            if(Services.EVENT.fireKeyEntryMouseReleasedEvent(this, event)
                    .map(IKeyEntryMouseReleasedEvent::isHandled, UnaryOperator.identity())) {
                return true;
            }

            return super.mouseReleased(event);
        }

        public KeyMapping getKey() {

            return key;
        }

        public Component getKeyDesc() {

            return keyDesc;
        }

        public Component categoryName() {

            return categoryName;
        }

        public Button getBtnResetKeyBinding() {

            return btnResetKeyBinding;
        }

        public Button getBtnChangeKeyBinding() {

            return btnChangeKeyBinding;
        }

        @Override
        protected void refreshEntry() {

            this.btnChangeKeyBinding.setMessage(this.key.getTranslatedKeyMessage());
            this.btnResetKeyBinding.active = !this.key.isDefault();
            this.hasCollision = false;
            MutableComponent duplicates = Component.empty();
            if(!this.key.isUnbound()) {
                KeyMapping[] mappings = NewKeyBindsList.this.minecraft.options.keyMappings;

                for(KeyMapping mapping : mappings) {
                    if(mapping != this.key && this.key.same(mapping) || Services.PLATFORM.hasConflictingModifier(key, mapping)) {
                        if(this.hasCollision) {
                            duplicates.append(", ");
                        }

                        this.hasCollision = true;
                        duplicates.append(Services.PLATFORM.getKeyName(mapping));
                    }
                }
            }
            MutableComponent tooltip = categoryName.copy();
            if(this.hasCollision) {
                this.btnChangeKeyBinding.setMessage(Component.literal("[ ")
                        .append(this.btnChangeKeyBinding.getMessage().copy().withStyle(ChatFormatting.WHITE))
                        .append(" ]")
                        .withStyle(ChatFormatting.YELLOW));
                tooltip.append(CommonComponents.NEW_LINE);
                tooltip.append(Component.translatable("controls.keybinds.duplicateKeybinds", duplicates));
            }
            this.btnChangeKeyBinding.setTooltip(Tooltip.create(tooltip));

            if(NewKeyBindsList.this.controlsScreen.selectedKey == this.key) {
                this.btnChangeKeyBinding.setMessage(Component.literal("> ")
                        .append(this.btnChangeKeyBinding.getMessage()
                                .copy()
                                .withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE))
                        .append(" <")
                        .withStyle(ChatFormatting.YELLOW));
            }

        }
    }
}