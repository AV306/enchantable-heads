// See https://github.com/AV306/KeybindsGalore-Plus/blob/master/src/main/java/me/av306/EnchantableHeads/ConfigManager.java
// for updates to this monolithic lasagna class
// @author Kyra the fluffy blue dragon

package net.potato.enchantableheads;

import net.potato.enchantableheads.EnchantableHeads;;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.lang.reflect.Field;

public class ConfigManager {
    private final String configFilePath;

    private File configFile;

    public ConfigManager(String configFilePath) {
        this.configFilePath = configFilePath;
        this.checkConfigFile();
        this.readConfigFile();
    }

    private void checkConfigFile() {
        this.configFile = new File(this.configFilePath);

        if (!this.configFile.exists()) {
            try (FileOutputStream fos = new FileOutputStream(this.configFile);) {
                this.configFile.createNewFile();

                EnchantableHeads.LOGGER.warn("EnchantableHeads config file not found, copying embedded one");
                fos.write(this.getClass().getResourceAsStream("/enchantableheads_config.properties").readAllBytes());
            } catch (IOException ioe) {
                EnchantableHeads.LOGGER.error("IOException while copying default configs!");
                ioe.printStackTrace();
            }
        }
    }

    public void readConfigFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(this.configFilePath))) {
            // Iterate over each line in the file
            for (String line : reader.lines().toArray(String[]::new)) {
                // Skip comments and blank lines
                if (line.startsWith("#") || line.isBlank()) continue;

                // Split it by the equals sign (.properties format)
                String[] entry = line.split("=");

                try {
                    // Saw someone get locale problems when parsing decimal places (comma vs period) once, not taking the chance
                    // Get the field this config line wants to set
                    Field f = EnchantableHeads.class.getDeclaredField(entry[0].toUpperCase(Locale.ENGLISH));

                    // Assign the value to the field, based on type
                    // Add more else-ifs for more types
                    if (f.getType().isAssignableFrom(short.class)) {
                        // Short value
                        f.setShort(null, Short.parseShort(entry[1].replace("0x", ""), 16));
                    } else if (f.getType().isAssignableFrom(int.class)) {
                        // Integer value
                        f.setInt(null, Integer.parseInt(entry[1]));
                    } else if (f.getType().isAssignableFrom(float.class)) {
                        f.setFloat(null, Float.parseFloat(entry[1]));
                    } else if (f.getType().isAssignableFrom(boolean.class)) {
                        f.setBoolean(null, Boolean.parseBoolean(entry[1]));
                    } else {
                        EnchantableHeads.LOGGER.error("Unrecognised data type for config entry {}", line);
                    }
                } catch (ArrayIndexOutOfBoundsException oobe) { // Most likely cause: mising config value
                    EnchantableHeads.LOGGER.warn("Malformed config entry: {}", line);
                } catch (NoSuchFieldException nsfe) { // Most likely cause: invalid field name
                    EnchantableHeads.LOGGER.warn("No matching field found for config entry: {}", line);
                } catch (IllegalAccessException illegal) { // Shouldn't happen unless I messed up
                    EnchantableHeads.LOGGER.error("Could not set field for: {}", line);
                    illegal.printStackTrace();
                }
            }
        } catch (IOException ioe) { // WHo knows? Anything is possible
            EnchantableHeads.LOGGER.error("IOException while reading config file: {}", ioe.getMessage());
        }

        EnchantableHeads.LOGGER.info("Done reading config file!");
    }
}