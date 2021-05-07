package chess.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SettingsHelperTest {

    @Test
    void TestSettings() {
        SettingsHelper settings = new SettingsHelper();
        settings.put(new Setting("last-pgn-location", "there"));
        Assertions.assertEquals("there", settings.getSetting("last-pgn-location").getValue());
        settings.remove("last-pgn-location");
        Assertions.assertNull(settings.getSetting("last-pgn-location"));
    }
}