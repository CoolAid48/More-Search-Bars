package coolaid.moresearchbars.config;

import coolaid.moresearchbars.MoreSearchBars;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class SearchBarConfigScreen extends Screen {

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;

    private final Screen parent;
    private final SearchBarConfig config;
    private StringWidget titleWidget;

    public SearchBarConfigScreen(Screen parent) {
        super(Component.translatable("moresearchbars.config.title"));
        this.parent = parent;
        this.config = MoreSearchBars.CONFIG;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4;
        int spacing = 24;

        this.addRenderableWidget(createToggle(
                centerX - BUTTON_WIDTH / 2,
                startY,
                Component.translatable("moresearchbars.config.multiplayer"),
                () -> this.config.enableMultiplayerSearch,
                value -> this.config.enableMultiplayerSearch = value
        ));

        this.addRenderableWidget(createToggle(
                centerX - BUTTON_WIDTH / 2,
                startY + spacing,
                Component.translatable("moresearchbars.config.gamerules"),
                () -> this.config.enableGameRuleSearch,
                value -> this.config.enableGameRuleSearch = value
        ));

        this.addRenderableWidget(createToggle(
                centerX - BUTTON_WIDTH / 2,
                startY + spacing * 2,
                Component.translatable("moresearchbars.config.stats"),
                () -> this.config.enableStatsSearch,
                value -> this.config.enableStatsSearch = value
        ));

        this.addRenderableWidget(createToggle(
                centerX - BUTTON_WIDTH / 2,
                startY + spacing * 3,
                Component.translatable("moresearchbars.config.keybinds"),
                () -> this.config.enableKeybindsSearch,
                value -> this.config.enableKeybindsSearch = value
        ));

        this.addRenderableWidget(
                Button.builder(Component.translatable("moresearchbars.config.done"), button -> this.onClose())
                        .bounds(centerX - BUTTON_WIDTH / 2, startY + spacing * 4 + 12, BUTTON_WIDTH, BUTTON_HEIGHT)
                        .build()
        );
    }

    @Override
    public void onClose() {
        ConfigManager.save(this.config);
        this.minecraft.setScreen(this.parent);
    }

    private Button createToggle(int x, int y, Component label, BooleanSupplier getter, Consumer<Boolean> setter) {
        Button button = Button.builder(Component.empty(), pressed -> {
                    boolean newValue = !getter.getAsBoolean();
                    setter.accept(newValue);
                    pressed.setMessage(buildToggleLabel(label, newValue));
                })
                .bounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT)
                .build();
        button.setMessage(buildToggleLabel(label, getter.getAsBoolean()));
        return button;
    }

    private Component buildToggleLabel(Component label, boolean enabled) {
        Component value = enabled
                ? Component.translatable("moresearchbars.config.on").withStyle(ChatFormatting.GREEN)
                : Component.translatable("moresearchbars.config.off").withStyle(ChatFormatting.RED);
        return Component.empty().append(label).append(Component.literal(": ")).append(value);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        // Draws background and widgets
        graphics.fillGradient(0, 0, this.width, this.height, 0xC0101010, 0xD0101010);
        super.render(graphics, mouseX, mouseY, delta);

        // Screen title component
        int textWidth = this.font.width(title) + 25; // Include +25 to fit bold formatting
        Component title = Component.translatable("moresearchbars.config.title").withStyle(ChatFormatting.BOLD);
        titleWidget = new StringWidget(
                (this.width - textWidth) / 2, 10, textWidth, 9, title, this.font
        );
        this.addRenderableWidget(titleWidget);
    }
}