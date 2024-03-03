package pansangg.froggyvoteapi.site;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import pansangg.froggyvoteapi.Main;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import static org.bukkit.Bukkit.*;

public class SitePart extends FormDataHandler {
    public HttpServer server;

    public SitePart(String host, int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(host, port), (Integer) Main.conf.get("site_max_connections"));
            server.createContext("/", this);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sha256(String s) {
        return Hashing.sha256().hashString(s, Charsets.UTF_8).toString();
    }

    @Override
    public void handle(HttpExchange e, Map<String, Object> params, String data) {
        String response = "error";
        int status_code = 500;

        String method = e.getRequestMethod();
        String path = e.getRequestURI().getPath();

        getLogger().info("Path pinged: " + path);

        if (method.equals("GET")) {
            response = "ok";
            status_code = 200;
        }

        if (path.equals(Main.conf.get("bind_url_vote"))) {
            Map<String, Object> vote_config = (Map<String, Object>) Main.conf.get("vote");
            Map<String, Object> action_config = (Map<String, Object>) vote_config.get("action");

            String nickname = (String) params.get("nickname");
            getPlayer(nickname).sendMessage((String) vote_config.get("message"));

            if (action_config.get("type").equals("command")) {
                dispatchCommand(getServer().getConsoleSender(), (String) action_config.get("command"));
            } else if (action_config.get("type").equals("vault")) {
                if (Main.economy != null) {
                    Main.economy.depositPlayer(nickname, (Double) params.get("amount"));
                }
            } else {
                getLogger().warning("Вы указали несуществующий тип действия! / You're using unexceptable type of action!");
            }
        }

        try {
            e.sendResponseHeaders(status_code, response.length());

            OutputStream os = e.getResponseBody();
            os.write(response.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
