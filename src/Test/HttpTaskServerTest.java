package Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import logic.Epic;
import logic.SubTask;
import logic.Task;
import manager.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import service.HttpTaskServer;
import service.KVServer;
import service.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    Task task;
    Epic epic;
    SubTask subTask;
    TaskManager manager = Managers.getDefault();
    HttpTaskServer server;
    KVServer kvServer;
    HttpResponse<String> response;
    HttpRequest.Builder rB;
    GsonBuilder gsonBuilder;
    Gson gson;
    HttpClient client;
    HttpResponse.BodyHandler<String> handler;

    public HttpTaskServerTest() throws IOException, ManagerSaveException {
        created();
        history();
        server = new HttpTaskServer(manager);
        kvServer = new KVServer();
        rB = HttpRequest.newBuilder();
        client = HttpClient.newHttpClient();
        handler = HttpResponse.BodyHandlers.ofString();
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter1());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        server.start();
        kvServer.start();
    }

    public void created() throws IOException, ManagerSaveException {
        int durationTask = 15;
        LocalDateTime startTimeTask = LocalDateTime.of(2022, 6
                , 23, 10, 22, 25);
        LocalDateTime startTimeTask1 = LocalDateTime.of(2022, 7
                , 3, 10, 22, 23);
        LocalDateTime startTimeTask2 = LocalDateTime.of(2022, 8
                , 3, 10, 22, 23);
        LocalDateTime startTimeTask3 = LocalDateTime.of(2022, 8
                , 7, 10, 20, 20);
        LocalDateTime startTimeTask4 = LocalDateTime.of(2022, 8
                , 3, 11, 22, 23);
        LocalDateTime startTimeTask5 = LocalDateTime.of(2022, 8
                , 7, 15, 20, 20);

        task = new Task("name", "description"
                , Status.NEW, 0, durationTask, startTimeTask4);
        epic = new Epic("Epicname", "Epicdescription", 7);
        subTask = new SubTask("STname", "STdescription", Status.DONE
                , 8, 3, durationTask, startTimeTask5);

        manager.inputNewEpic(new Epic("Epicname", "Epicdescription", 0));
        manager.inputNewTask(new Task("name", "description"
                , Status.NEW, 1, durationTask, startTimeTask));
        manager.inputNewTask(new Task("name", "description"
                , Status.IN_PROGRESS, 2, durationTask, startTimeTask1));
        manager.inputNewEpic(new Epic("Epicname", "Epicdescription", 3));
        manager.inputNewSubTask(new SubTask("STname", "STdescription", Status.DONE
                , 4, 3, durationTask, startTimeTask2));
        manager.inputNewSubTask(new SubTask("STname", "STdescription", Status.NEW
                , 5, 3, durationTask, startTimeTask3));
    }

    void history() {
        try {
            manager.outputSubTaskById(4);
            manager.outputTaskById(1);
            manager.outputEpicById(3);
        } catch (IOException | ManagerSaveException ex) {
            ex.getMessage();
            System.out.println("Непредвиденная ошибка в мэйне");
        }
    }

    @AfterEach
    void serverStop() {
        server.stop();
        kvServer.stop();
    }

    @Test
    void testInputTask() {
        String json = gson.toJson(task);
        try {
            URI uri = URI.create("http://localhost:8080/tasks/task");
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .version((HttpClient.Version.HTTP_1_1))
                    .header("Accept", "application/json")
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            assertEquals(200, response.statusCode(), "Задача не добавлена");
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testInputTask ");
        }
    }

    @Test
    void testInputEpic() {
        String json = gson.toJson(epic);
        try {
            URI uri = URI.create("http://localhost:8080/tasks/epic");
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .version((HttpClient.Version.HTTP_1_1))
                    .header("Accept", "application/json")
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            assertEquals(200, response.statusCode(), "Задача не добавлена");
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testInputEpic ");
        }
    }

    @Test
    void testInputSubTask() {
        String json = gson.toJson(subTask);
        try {
            URI uri = URI.create("http://localhost:8080/tasks/subtask");
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .version((HttpClient.Version.HTTP_1_1))
                    .header("Accept", "application/json")
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            assertEquals(200, response.statusCode(), "Задача не добавлена");
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testInputSubTask ");
        }
    }

    @Test
    void testGetTask() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/task?id=1");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            Task task = gson.fromJson(response.body(), Task.class);
            int id = task.getId();
            assertEquals(1, id);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testGetTask ");
        }
    }

    @Test
    void testGetEpic() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/epic?id=3");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            Epic epic = gson.fromJson(response.body(), Epic.class);
            int id = epic.getId();
            assertEquals(3, id);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testGetEpic ");
        }
    }

    @Test
    void testGetSubTask() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/subtask?id=4");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            SubTask subTask = gson.fromJson(response.body(), SubTask.class);
            int id = subTask.getId();
            assertEquals(4, id);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testGetSubTask ");
        }
    }

    @Test
    void testGetDescriptionTasks() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            HashMap<Integer, Task> map = gson.fromJson(response.body()
                    , new TypeToken<HashMap<Integer, Task>>() {
                    }.getType());
            int sise = map.size();
            assertEquals(2, sise);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testGetDescriptionTasks ");
        }
    }

    @Test
    void testGetDescriptionEpics() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/epics");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            HashMap<Integer, Epic> map = gson.fromJson(response.body()
                    , new TypeToken<HashMap<Integer, Epic>>() {
                    }.getType());
            int sise = map.size();
            assertEquals(2, sise);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testGetDescriptionEpics ");
        }
    }

    @Test
    void testGetDescriptionSubTasksByEpic() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/subtasks?id=3");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            ArrayList<SubTask> map = gson.fromJson(response.body()
                    , new TypeToken<ArrayList<SubTask>>() {
                    }.getType());
            int sise = map.size();
            assertEquals(2, sise);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в testGetDescriptionSubTasksByEpic ");
        }
    }

    @Test
    void testGetHistory() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/history");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            ArrayList<Task> list = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
            }.getType());
            int sise = list.size();
            assertEquals(3, sise);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testGetHistory ");
        }
    }

    @Test
    void testGetPrioritizedTasks() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/tasksort");
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            Set<Task> map = gson.fromJson(response.body()
                    , new TypeToken<Set<Task>>() {
                    }.getType());
            int sise = map.size();
            assertEquals(4, sise);
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testGetPrioritizedTasks ");
        }
    }

    @Test
    void testDeleteTaskById() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/task?id=1");
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .version((HttpClient.Version.HTTP_1_1))
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            assertEquals(200, response.statusCode(), "Задача не удалена");
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testDeleteTaskById ");
        }
    }

    @Test
    void testDeleteAllTasks() {
        try {
            URI uri = URI.create("http://localhost:8080/tasks/tasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .DELETE()
                    .version((HttpClient.Version.HTTP_1_1))
                    .uri(uri)
                    .build();
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            response = client.send(request, handler);
            assertEquals(200, response.statusCode(), "Задача не удалена");
        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage() + "Исключение в методе testDeleteAllTasks ");
        }
    }

    static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        // задаём формат выходных данных: "dd--MM--yyyy"
        private final DateTimeFormatter formatterWriter
                = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss");
        private final DateTimeFormatter formatterReader
                = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss");

        @Override
        public void write(final JsonWriter jsonWriter
                , final LocalDateTime startTime) throws IOException {
            // приводим localDate к необходимому формату
            try {
                jsonWriter.value(startTime.format(formatterWriter));
            } catch (NullPointerException ex) {
                System.out.println("Вылетел нулл в адаптере записи");
            } catch (DateTimeParseException ex) {
                System.out.println("Вылетел нулл 00 в адаптере записи");
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            LocalDateTime r = null;
            try {
                r = LocalDateTime.parse(jsonReader.nextString(), formatterReader);
            } catch (NullPointerException ex) {
                System.out.println("Вылетел нулл 1 в адаптере");
            } catch (DateTimeParseException ex) {
                System.out.println("Вылетел нулл 11 в адаптере записи");
            }
            return r;
        }
    }

    static class LocalDateTimeAdapter1 extends TypeAdapter<LocalDateTime> {
        // задаём формат выходных данных: "dd--MM--yyyy"
        private final DateTimeFormatter formatterWriter
                = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss");
        private final DateTimeFormatter formatterReader
                = DateTimeFormatter.ofPattern("yyyy.MM.dd HH.mm.ss");

        @Override
        public void write(final JsonWriter jsonWriter
                , final LocalDateTime endTime) throws IOException {
            // приводим localDate к необходимому формату
            try {
                jsonWriter.value(endTime.format(formatterWriter));
            } catch (NullPointerException ex) {
                System.out.println("Вылетел нулл 3 в адаптере записи");
            } catch (DateTimeParseException ex) {
                System.out.println("Вылетел нулл 13 в адаптере записи");
            }
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            LocalDateTime r = null;
            try {
                r = LocalDateTime.parse(jsonReader.nextString(), formatterReader);
            } catch (NullPointerException ex) {
                System.out.println("Вылетел нулл 2 в адаптере");
            } catch (DateTimeParseException ex) {
                System.out.println("Вылетел нулл 12 в адаптере записи");
            }
            return r;
        }
    }
}