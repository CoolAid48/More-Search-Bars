package coolaid.moresearchbars.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import coolaid.moresearchbars.MoreSearchBars;
import coolaid.moresearchbars.platform.Services;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_NAME = "moresearchbars.json";

    private ConfigManager() {
    }

    public static SearchBarConfig load() {
        Path configPath = getConfigPath();
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                SearchBarConfig loaded = GSON.fromJson(reader, SearchBarConfig.class);
                if (loaded != null) {
                    return loaded;
                }
            } catch (IOException e) {
                MoreSearchBars.LOGGER.warn("Failed to read config file, using defaults.", e);
            }
        }
        return new SearchBarConfig();
    }

    public static void save(SearchBarConfig config) {
        Path configPath = getConfigPath();
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException e) {
            MoreSearchBars.LOGGER.warn("Failed to save config file.", e);
        }
    }

    private static Path getConfigPath() {
        return Services.PLATFORM.getConfigDir().resolve(CONFIG_NAME);
    }
}