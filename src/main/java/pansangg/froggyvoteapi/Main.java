package pansangg.froggyvoteapi;

import org.bukkit.plugin.java.JavaPlugin;
import pansangg.froggyvoteapi.utils.MapBuilder;
import pansangg.froggyvoteapi.utils.Config;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public final class Main extends JavaPlugin {

    public static Config conf;
    public static Main me;

    @Override
    public void onEnable() {
        conf = new Config(
                Paths.get(getDataFolder().getPath(), "config.yml").toFile(),
                new MapBuilder<String, Object>()
                        .put("site_host", "localhost")
                        .put("site_port", 8080)
                        .put("bind_url_comment", "/api/comment")
                        .put("bind_url_vote", "/api/vote")
                        .put(
                                "add_comment",
                                new MapBuilder<String, Object>()
                                        .put("message", "Вы оставили комментарий, и получили за это награду!")
                                        .put(
                                            "action", new MapBuilder<String, Object>()
                                                .put("type", "command/vault")
                                                .put("command", "/say NICKNAME прокомментировал сервер, нам (не) важно ваше мнение!")
                                                .put("amount", "8")
                                        )
                        )
                        .put(
                                "del_comment",
                                new MapBuilder<String, Object>()
                                        .put("message", "Вы убрали комментарий, и ваша награда была отозвана!")
                                        .put(
                                                "action", new MapBuilder<String, Object>()
                                                        .put("type", "command/vault")
                                                        .put("command", "/say NICKNAME убрал комментарий :(")
                                                        .put("amount", "-8")
                                        )
                        )
                        .put(
                                "vote",
                                new MapBuilder<String, Object>()
                                        .put("message", "Вы проголосовали за сервер, и получили за это БОЛЬШУЮ награду!")
                                        .put(
                                                "action", new MapBuilder<String, Object>()
                                                        .put("type", "command/vault")
                                                        .put("command", "/say Спасибо за голос, NICKNAME!")
                                                        .put("amount", "16")
                                        )
                        )
                        .toMap());
        me = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
