package coolaid.moresearchbars.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "moresearchbars")
public class SearchBarConfig implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean enableMultiplayerSearch = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableGameruleSearch = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableStatsSearch = true;

    @ConfigEntry.Gui.Tooltip
    public boolean enableKeybindsSearch = true;
}
