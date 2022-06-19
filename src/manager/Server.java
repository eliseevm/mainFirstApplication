package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import logic.Task;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 8082;
    GsonBuilder gsonBuilder;
    Gson gson;
    private TaskManager manager;
    private HttpServer server;

    public Server (TaskManager manager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        this.manager = manager;
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter1());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gson = gsonBuilder.create();

    }

    public class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response;
            String metod = exchange.getRequestMethod();
            String patch = exchange.getRequestURI().getPath();
            String[] patches = patch.split("/");
            switch (metod) {
                case "GET":
                    System.out.println("Обрабатываю GET запрос ");
                    if (exchange.getRequestURI().getQuery() != null) {
                        Integer id = Integer.parseInt(exchange.getRequestURI()
                                .getQuery().split("\\=")[1]);
                        if (patches[2].equals("") && (!id.equals(null))) {
                            try {
                                Task task = manager.outputTaskById(id);
                                String str = gson.toJson(task);
                                response = str;
                                exchange.sendResponseHeaders(200, 0);
                                try (OutputStream os = exchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            }catch (ManagerSaveException ex) {
                                ex.printStackTrace();
                            }

                        }
                    }
            }
        }
    }
    class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        // задаём формат выходных данных: "dd--MM--yyyy"
        private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");
        private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime startTime) throws IOException {
            // приводим localDate к необходимому формату
            jsonWriter.value(startTime.format(formatterWriter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        }
    }
    class LocalDateTimeAdapter1 extends TypeAdapter<LocalDateTime> {
        // задаём формат выходных данных: "dd--MM--yyyy"
        private final DateTimeFormatter formatterWriter = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");
        private final DateTimeFormatter formatterReader = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime endTime) throws IOException {
            // приводим localDate к необходимому формату
            jsonWriter.value(endTime.format(formatterWriter));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
        }
    }

    class DurationAdapter extends TypeAdapter<Duration> {

        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toString());
        }

        @Override
        public Duration read(final JsonReader jsonReader) throws IOException {
            return null;
        }
    }
    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

}
