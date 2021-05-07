package chess.settings;

import io.jsondb.JsonDBTemplate;
import javassist.NotFoundException;

public class SettingsHelper {
    private JsonDBTemplate jsonDBTemplate;

    public SettingsHelper() {
        String dbFilesLocation = System.getProperty("user.dir");
        String baseScanPackage = "chess.settings";
        jsonDBTemplate = new JsonDBTemplate(dbFilesLocation, baseScanPackage);
        if (!jsonDBTemplate.collectionExists(Setting.class)) {
            jsonDBTemplate.createCollection(Setting.class);
        }
    }

    public Setting getSetting(String id) {
        try {
            String query = String.format("/.[id='%s']", id);
            return jsonDBTemplate.findOne(query, Setting.class);
        } catch (Exception e) {
            return null;
        }
    }

    public void put(Setting setting) {
        jsonDBTemplate.upsert(setting);
    }

    public void remove(String id) {
        Setting toRemove = getSetting(id);
        jsonDBTemplate.remove(toRemove, Setting.class);
    }

}
