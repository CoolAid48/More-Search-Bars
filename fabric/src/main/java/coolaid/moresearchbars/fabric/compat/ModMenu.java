package coolaid.moresearchbars.fabric.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import coolaid.moresearchbars.config.SearchBarConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class ModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(SearchBarConfig.class, parent).get();
    }
}