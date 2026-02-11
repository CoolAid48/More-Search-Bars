package coolaid.moresearchbars.mixin.gamerules;

import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.util.GameRuleListMixinInvoker;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.worldselection.EditGameRulesScreen;
import net.minecraft.world.level.gamerules.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mixin(EditGameRulesScreen.RuleList.class)
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class GameRuleListMixin extends AbstractSelectionList implements GameRuleListMixinInvoker {

    @Unique
    private final List<AbstractSelectionList.Entry<?>> moresearchbars$allEntries = new ArrayList<>();

    public GameRuleListMixin(net.minecraft.client.Minecraft minecraft, int i, int j, int k, int l) {
        super(minecraft, i, j, k, l);
    }

    @Override
    public void moresearchbars$captureEntries() {
        moresearchbars$allEntries.clear();
        moresearchbars$allEntries.addAll(this.children());
    }

    @Override
    public void moresearchbars$applyFilter() {
        String filterText = MoreSearchBars.getGameRuleSearchString();
        String query = (filterText == null) ? "" : filterText.toLowerCase(Locale.ROOT).trim();

        this.clearEntries();

        if (query.isEmpty()) {
            moresearchbars$allEntries.forEach(this::moresearchbars$addEntry);
            return;
        }

        // Track current category and if it has any visible rules
        AbstractSelectionList.Entry<?> currentCategory = null;
        List<AbstractSelectionList.Entry<?>> visibleRulesInCurrentCategory = new ArrayList<>();

        for (AbstractSelectionList.Entry<?> entry : moresearchbars$allEntries) {
            // If this is a category header, add the previous category if it had visible rules, then start tracking the new category
            if (moresearchbars$isCategoryEntry(entry)) {
                if (currentCategory != null && !visibleRulesInCurrentCategory.isEmpty()) {
                    this.addEntry((AbstractSelectionList.Entry) currentCategory);
                    visibleRulesInCurrentCategory.forEach(this::moresearchbars$addEntry);
                    visibleRulesInCurrentCategory.clear();
                }

                currentCategory = entry;
                continue;
            }

            boolean matches = false;

            // Try 3 different approaches to get the rule name
            // 1: Try to get the rule key directly
            // 2: Try to get the widget's text directly
            // 3: use toString (basically the last resort, holy jank)

            try {
                for (java.lang.reflect.Field field : entry.getClass().getDeclaredFields()) {
                    if (field.getType().equals(GameRules.class) ||
                            field.getType().getName().contains("GameRules$Key")) {
                        field.setAccessible(true);
                        Object keyObj = field.get(entry);
                        if (keyObj != null) {
                            String ruleId = keyObj.toString().toLowerCase(Locale.ROOT);
                            if (ruleId.contains(query)) {
                                matches = true;
                                break;
                            }

                            // Also try to get the translation
                            String translationKey = "gamerule." + ruleId;
                            String translated = net.minecraft.network.chat.Component.translatable(translationKey).getString()
                                    .toLowerCase(Locale.ROOT);
                            if (translated.contains(query)) {
                                matches = true;
                                break;
                            }
                        }
                    }
                }

                if (!matches) {
                    for (java.lang.reflect.Field field : entry.getClass().getDeclaredFields()) {
                        if (net.minecraft.client.gui.components.AbstractWidget.class.isAssignableFrom(field.getType())) {
                            field.setAccessible(true);
                            Object widget = field.get(entry);
                            if (widget instanceof net.minecraft.client.gui.components.AbstractWidget abstractWidget) {
                                // Get the message/text from the widget
                                for (java.lang.reflect.Field widgetField : abstractWidget.getClass().getDeclaredFields()) {
                                    if (widgetField.getType().equals(net.minecraft.network.chat.Component.class)) {
                                        widgetField.setAccessible(true);
                                        Object message = widgetField.get(abstractWidget);
                                        if (message != null) {
                                            String text = message.toString().toLowerCase(Locale.ROOT);
                                            if (text.contains(query)) {
                                                matches = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (!matches) {
                    String entryText = entry.toString().toLowerCase(Locale.ROOT);
                    matches = entryText.contains(query);
                }

            } catch (Exception e) {
                // Fallback to simple toString check
                String entryText = entry.toString().toLowerCase(Locale.ROOT);
                matches = entryText.contains(query);
            }

            // If the rule matches, add it to current category's visible rules
            if (matches && currentCategory != null) {
                visibleRulesInCurrentCategory.add(entry);
            }
        }

        // Add the last category if it had visible rules
        if (currentCategory != null && !visibleRulesInCurrentCategory.isEmpty()) {
            this.addEntry((AbstractSelectionList.Entry) currentCategory);
            visibleRulesInCurrentCategory.forEach(this::moresearchbars$addEntry);
        }

        // Reset scroll to top after filtering
        this.setScrollAmount(0);
    }

    @Unique
    private boolean moresearchbars$isCategoryEntry(AbstractSelectionList.Entry<?> entry) {
        for (java.lang.reflect.Field field : entry.getClass().getDeclaredFields()) {
            if (field.getType().equals(GameRules.class) ||
                    field.getType().getName().contains("GameRules$Category")) {
                return true;
            }
        }
        return entry.getClass().getSimpleName().toLowerCase(Locale.ROOT).contains("category");
    }

    @Unique
    private void moresearchbars$addEntry(AbstractSelectionList.Entry<?> entry) {
        this.addEntry((AbstractSelectionList.Entry) entry);
    }
}