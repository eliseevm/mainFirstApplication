package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import logic.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 8080;
    GsonBuilder gsonBuilder;
    Gson gson;
    private TaskManager manager;
    private HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new AllTasksHandler());
        this.manager = manager;
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter1());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }


    class AllTasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response;
            String method = httpExchange.getRequestMethod();
            String patch = httpExchange.getRequestURI().getPath();
            String[] patches = patch.split("/");
            switch (method) {
                case "GET":
                    System.out.println("Обрабатываю GET запрос ");
                    if (httpExchange.getRequestURI().getQuery() != null) {
                        Integer id = Integer.parseInt(httpExchange.getRequestURI()
                                .getQuery().split("\\=")[1]);
                        if (patches[2].equals("task") && (!id.equals(null))) {
                            try {
                                Task task = manager.outputTaskById(id);
                                response = gson.toJson(task);
                                httpExchange.sendResponseHeaders(200, 0);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        } else if (patches[2].equals("epic") && (!id.equals(null))) {
                           try {
                               Epic epic = manager.getDescriptionEpic().get(id);
                               response = gson.toJson(epic);
                               httpExchange.sendResponseHeaders(200, 0);
                               try (OutputStream os = httpExchange.getResponseBody()) {
                                   os.write(response.getBytes());
                               }
                           } catch (Throwable ex) {
                               ex.printStackTrace();
                           }
                        } else if (patches[2].equals("subtask") && (!id.equals(null))) {
                            try {
                                Epic epic = manager.getDescriptionEpic().get(id);
                                List listST = epic.getListSubTask();
                                response = gson.toJson(listST);
                                httpExchange.sendResponseHeaders(200, 0);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } catch (Throwable ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else if (httpExchange.getRequestURI().getQuery() == null) {
                        if (patches[2].equals("task")) {
                            try {
                                Set<Task> tasks = manager.getPrioritizedTasks();
                                response = gson.toJson(tasks);
                                httpExchange.sendResponseHeaders(200, 0);
                                try (OutputStream os = httpExchange.getResponseBody()) {
                                    os.write(response.getBytes());
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else if (patches[2].equals("epic")) {
                        try {
                            HashMap<Integer, Epic> epics = manager.getDescriptionEpic();
                            response = gson.toJson(epics.toString());
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                    } else if (patches[2].equals("history")) {
                        try {
                            List history = manager.getHistory();
                            String responseGson = gson.toJson(history.toString());
                            response = responseGson;
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        } catch (Throwable ex) {
                            ex.printStackTrace();
                        }
                    } else {
                            response = gson.toJson("Не правильный запрос, перефразируйте");
                            httpExchange.sendResponseHeaders(404, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }

                    return;
                case "POST":
                    System.out.println("Обрабатываю POST запрос");
                   try {
                       InputStream inputStream = httpExchange.getRequestBody();
                       String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                       inputStream.close();
                       Task task = gson.fromJson(body, Task.class);
                       if (patches[2].equals("task")) {
                           manager.inputNewTask(task);
                           response = gson.toJson("Задача добавлена");
                           httpExchange.sendResponseHeaders(200, 0);
                           try (OutputStream os = httpExchange.getResponseBody()) {
                               os.write(response.getBytes());
                           }
                       }
                   } catch (Exception ex) {
                       ex.printStackTrace();
                   }
                    return;
                case "DELETE":
                    System.out.println("Обрабатываю DELETE запрос");
                    if (httpExchange.getRequestURI().getQuery() != null) {
                        Integer id = Integer.parseInt(httpExchange.getRequestURI().getQuery().split("\\=")[1]);
                        if ((patches[2].equals("task") || patches[2].equals("epic")) && (!id.equals(null))) {
                            try {
                                manager.deleteTaskById(id);
                            } catch (ManagerSaveException ex) {
                                System.out.println("Ошибка сервера!");
                            }
                            response = gson.toJson("Задача удалена!");
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }
                    } else if (httpExchange.getRequestURI().getQuery() == null) {
                        if (patches[2].equals("task")) {
                            try {
                                manager.deleteAllTasks();
                            } catch (ManagerSaveException ex) {
                                System.out.println("Ошибка сервера!");
                            }
                            response = gson.toJson("Задачи удалены!");
                            httpExchange.sendResponseHeaders(200, 0);
                            try (OutputStream os = httpExchange.getResponseBody()) {
                                os.write(response.getBytes());
                            }
                        }
                    }
                    return;
                default:
                    response = gson.toJson("Запрос не выполнен, повторите команду!");
                    httpExchange.sendResponseHeaders(200, 0);
                    try (OutputStream os = httpExchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
            }
        }
    }
    class  LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
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
            return Duration.ofMinutes(58);
        }
    }
    class ListAdapter extends TypeAdapter<ArrayList> {
        // задаём формат выходных данных: "dd--MM--yyyy"

        @Override
        public void write(final JsonWriter jsonWriter, final ArrayList listSubtask) throws IOException {
            // приводим localDate к необходимому формату
            jsonWriter.value(listSubtask.toString());
        }

        @Override
        public ArrayList read(final JsonReader jsonReader) throws IOException {
            return null;
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        server.start();
    }

    public void stop () {
        System.out.println("Завершаем работу сервера на порту " + PORT);
        server.stop(2);
    }
}


