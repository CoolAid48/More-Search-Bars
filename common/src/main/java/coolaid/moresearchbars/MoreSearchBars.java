/*
 * DISCLAIMER & ATTRIBUTION
 *
 * This mod heavily adapts and modifies code and behavior from jaredlll08's "Controlling" mod,
 * which is under the MIT License.
 *
 * Original copyright and license notices are preserved where applicable.
 * This project is not affiliated with or endorsed by the original authors
 * All modifications are the responsibility of this project.
 */

package coolaid.moresearchbars;

import coolaid.moresearchbars.config.ConfigManager;
import coolaid.moresearchbars.config.SearchBarConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class MoreSearchBars {

    public static final String MOD_ID = "moresearchbars";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static SearchBarConfig CONFIG;

    private static String statsSearchString = "";
    private static String serverSearchString = "";
    private static String gameRuleSearchString = "";

    public static int selectedTab;
    public static boolean isSearchingServers = false;

    public static void init() {
        // Write common init code here.

        CONFIG = ConfigManager.load();

        LOGGER.info("Initializing More Search Bars!");
    }

    // Stats search methods
    public static void setStatsSearchString(String s) {
        statsSearchString = s.toLowerCase(Locale.ROOT);
    }

    public static String getStatsSearchString() {
        return statsSearchString;
    }

    // Server search methods
    public static String getServerSearchString() {
        return serverSearchString;
    }

    public static void setServerSearchString(String search) {
        serverSearchString = search;
    }

    // GameRule search methods
    public static String getGameRuleSearchString() {
        return gameRuleSearchString;
    }

    public static void setGameRuleSearchString(String text) {
        gameRuleSearchString = text;
    }

    // Keybind search methods are handled elsewhere
}
