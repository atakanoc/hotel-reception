package hotelproject.controllers.utils;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigManagerTest {

    private ConfigManager configManager;
    private String configPath;
    private String key1;

    @Before
    public void setUp() {
        configPath = "app.properties";
        configManager = new ConfigManager(configPath);
        key1 = "db.url";
    }

    @Test
    public void testGetConfigPath() {
        assertEquals(configManager.getConfigPath(), configPath);
    }

    @Test
    public void testGetPValue() {
        assertNotNull(configManager.getPValue(key1));
    }
}