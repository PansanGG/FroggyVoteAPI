package pansangg.froggyvoteapi.site;

import com.sun.net.httpserver.HttpServer;

public class SitePart extends FormDataHandler {
    public HttpServer server;

    public SitePart(String host, int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(host,port),0);
            server.createContext("/",this);
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
    public void handle(HttpExchange e, Map<String,Object> params, String data) {
        String response = "error";
        int status_code = 500;

        String method = e.getRequestMethod();
        String path = e.getRequestURI().getPath();

        if (method.equals("GET")) {
            response = "ok";
            status_code = 200;
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
