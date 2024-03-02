package pansangg.froggyvoteapi.utils;

import com.google.common.base.Charsets;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class Config extends HashMap<String, Object> {
    private final Yaml yaml;
    private final File file;

    public Config(File file, Map<String, Object> def) {
        this.file = file;
        this.yaml = new Yaml();
        if (file.exists()) {
            loadConfig();
        } else {
            try {
                Files.createDirectories(file.getParentFile().toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (def == null) {
            } else putAll(def);
            saveConfig();
        }
    }

    public void loadConfig() {
        try {
            clear();
            putAll(yaml.load(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            yaml.dump(this, new FileWriter(file, Charsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> clone() {
        return new HashMap<>(this);
    }
}