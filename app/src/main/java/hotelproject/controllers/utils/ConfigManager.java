package hotelproject.controllers.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This class is used for managing configuration for database connection.
 */
public class ConfigManager {
    private String configPath;

    public ConfigManager(String configPath) {
        this.configPath = configPath;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String configPath) {
        this.configPath = configPath;
    }

    /**
     * According a file path to get configuration property.
     *
     * @param key for getting the focal property's value
     * @return the property return as a string.
     */
    public String getPValue(String key) {
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(new File(configPath).getAbsolutePath());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            prop.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return prop.getProperty(key);
    }

    /**
     * You should not use this method, use this.
     *
     * @param keys as an an array list of strings.
     * @return the property return as an array list of strings.
     * @see ConfigManager#getPValue(java.lang.String) instead
     */
    @Deprecated
    public ArrayList<String> getPValue(ArrayList<String> keys) {
        Properties prop = new Properties();
        InputStream is = null;
        ArrayList<String> values = new ArrayList<>();
        try {
            is = new FileInputStream(configPath);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        try {
            prop.load(is);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        for (String key : keys) {
            values.add(prop.getProperty(key));
        }
        return values;
    }
}
