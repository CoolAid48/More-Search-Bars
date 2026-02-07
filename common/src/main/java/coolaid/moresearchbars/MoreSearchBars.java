package coolaid.moresearchbars;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class MoreSearchBars {

    public static final String MOD_ID = "moresearchbars";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static String statsSearchString = "";
    private static String serverSearchString = "";

    public static void init() {
        // Write common init code here.

        LOGGER.info("Initializing More Search Bars!");
    }

    public static int selectedTab;
    public static boolean isSearchingServers = false;

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

    public static void clearServerSearch() {
        serverSearchString = "";
    }
}