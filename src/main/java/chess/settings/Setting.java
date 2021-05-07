package chess.settings;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "settings", schemaVersion = "1.0")
public class Setting {
    @Id
    private String id;

    private String value;

    public Setting() {
    }

    public Setting(String id, String value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
