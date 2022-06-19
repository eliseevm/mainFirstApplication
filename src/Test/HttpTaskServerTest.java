package Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import logic.Epic;
import logic.SubTask;
import logic.Task;
import manager.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    TaskManager httpTaskManager;
    HttpTaskServer httpTaskServer;
    HttpTaskClient client;
    GsonBuilder gsonBuilder;
    Gson gson;

    /*public HttpTaskServerTest() throws IOException {
        super(new HttpTaskManager("http://localhost:8078"));
        httpTaskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(httpTaskManager);
        client = new HttpTaskClient("http://localhost:8080/tasks");
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter1());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }*/

    @BeforeEach
    void begin() throws IOException {
      httpTaskManager = new HttpTaskManager("http://localhost:8078");
        httpTaskServer = new HttpTaskServer(httpTaskManager);
        client = new HttpTaskClient("http://localhost:8080/tasks");
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter1());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        httpTaskServer.start();

    }

   /* @AfterAll
    void finish() {
        httpTaskServer.stop();
    }*/
    @Test
    void testInputNewTask() throws IOException, ManagerSaveException {
       Task task = new Task("HTTPTest", "HTTPDescription", Status.DONE
                , 7, 10, LocalDateTime.parse("2022.06.25 15.22.25"));
        String json = gson.toJson(task);
        client.put("/task?id=7", json);
        System.out.println(json);
        String respJson = client.load("/task?id=7");
        System.out.println(respJson);
        assertEquals(json, respJson);
    }

   /* @Test
    void testInputNewEpic() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionEpic().size());
        taskManager.inputNewEpic(new Epic("name", "description", 8));
        assertEquals(3, taskManager.getDescriptionEpic().size());
    }

    @Test
    void testInputNewSubTask() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionSubTasks().size());
        taskManager.inputNewSubTask(new SubTask("name", "description", Status.IN_PROGRESS
                , 9, 1, durationSubTask, startTimeSubTask2));
        assertEquals(3, taskManager.getDescriptionSubTasks().size());
    }

    @Test
    void testGetDescriptionTasks() throws IOException, ManagerSaveException {

        assertEquals(3, taskManager.getDescriptionTasks().size());
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getDescriptionTasks().size());
    }

    @Test
    void testGetDescriptionSubTasks() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionSubTasks().size());
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getDescriptionSubTasks().size());
    }

    @Test
    void testGetDescriptionEpic() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionEpic().size());
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getDescriptionEpic().size());
    }

    @Test
    void testGetTaskId() {
        assertEquals(0, taskManager.getTaskId());
    }

    @Test
    void testGetHistory() throws IOException, ManagerSaveException {
        assertEquals(1, taskManager.getHistory().get(2).getId());
        taskManager.deleteAllTasks();
        assertEquals(5, taskManager.getHistory().get(1).getId());
        assertEquals(7, taskManager.getHistory().size());
    }

    @Test
    void testOutputAllTask() throws IOException, ManagerSaveException {
        assertEquals(3, taskManager.outputAllTask().size());
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.outputAllTask().size());
        taskManager.inputNewTask(new Task("name", "description", Status.DONE
                , 0, durationTask, startTimeTask1));
        assertEquals(1, taskManager.outputAllTask().size());
    }

    @Test
    void testOutputAllEpics() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.outputAllEpics().size());
        taskManager.deleteTaskById(2);
        assertEquals(1, taskManager.getDescriptionEpic().size());
    }

    @Test
    void testOutputSubtaskByEpik() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.outputSubtaskByEpik(1).size());
        assertEquals(Status.NEW, taskManager.outputSubtaskByEpik(1).get(0).getStatus());
    }

    @Test
    void testOutputTaskById() throws IOException, ManagerSaveException {
        assertEquals(5, taskManager.outputTaskById(5).getId());
        assertEquals(Status.DONE, taskManager.outputTaskById(5).getStatus());
    }

    @Test
    void testOutputSubTaskById() throws IOException, ManagerSaveException {
        assertEquals(3, taskManager.outputSubTaskById(3).getId());
        assertEquals(Status.NEW, taskManager.outputSubTaskById(3).getStatus());
    }

    @Test
    void testOutputEpicById() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.outputEpicById(2).getId());
        assertEquals(Status.IN_PROGRESS, taskManager.outputEpicById(1).getStatus());
    }

    @Test
    void testUpdateTask() {
        assertEquals(Status.NEW, taskManager.getDescriptionTasks().get(6).getStatus());
        assertEquals(3, taskManager.getDescriptionTasks().size());
        taskManager.updateTask(new Task("name", "description", Status.IN_PROGRESS
                , 6, durationTask, startTimeTask3));
        assertEquals(3, taskManager.getDescriptionTasks().size());
        assertEquals(Status.IN_PROGRESS, taskManager.getDescriptionTasks().get(6).getStatus());
    }

    @Test
    void testUpdateSubTask() {
        taskManager.updateSubTask(new SubTask("Обновленная", "description", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask));
        ;
        assertEquals(2, taskManager.getDescriptionSubTasks().size());
        assertEquals("Обновленная", taskManager.getDescriptionSubTasks().get(3).getName());
    }

    @Test
    void testUpdateEpic() {
        taskManager.updateEpic(new Epic("Обновленный", "description", 1));
        assertEquals(2, taskManager.getDescriptionEpic().size());
        assertEquals("Обновленный", taskManager.getDescriptionEpic().get(1).getName());
    }

    @Test
    void testDeletTaskById() throws IOException, ManagerSaveException {
        taskManager.deleteTaskById(0);
        assertEquals(2, taskManager.getDescriptionTasks().size());
        assertNull(taskManager.getDescriptionTasks().get(0));
    }

    @Test
    void testDeletAllTasks() throws IOException, ManagerSaveException {
        taskManager.deleteAllTasks();
        assertNull(taskManager.getDescriptionEpic().get(1));
        assertNull(taskManager.getDescriptionTasks().get(0));
        assertNull(taskManager.getDescriptionSubTasks().get(3));
    }*/

    class HttpTaskClient {
        HttpClient client;
        String url;
        URI uri;
        private HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();

        public HttpTaskClient(String url) {
            client = HttpClient.newHttpClient();
            this.url = url; // Устанавливаем значение адреса ресурса через конструктор при создании клиента
        }

        public void put(String key, String json) {
            try {
                uri = URI.create("http://localhost:8080/tasks/" + key);
                HttpRequest.Builder rb = HttpRequest.newBuilder(); // Получаем экземпляр класса строителя
                HttpRequest request = rb // создаем объект описывающий запрос
                        .uri(uri)
                        .version(HttpClient.Version.HTTP_1_1)
                        .header("Accept", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
                HttpResponse<String> response = client.send(request, handler);
                System.out.println("Код добавления/обновления по ключу " + key + " " + response.statusCode());
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        String load(String key) {
            uri = URI.create(url + key);
            String body = null;
            try {
                HttpRequest.Builder rb = HttpRequest.newBuilder();
                HttpRequest request = rb
                        .GET()
                        .uri(uri)
                        .version(HttpClient.Version.HTTP_1_1)
                        .header("Accept", "application/json")
                        .build();
                HttpResponse<String> response = client.send(request, handler);
                body = response.body();
                System.out.println(body);
            } catch (IOException | InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
            return body;
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
            return Duration.ofMinutes(58);
        }
    }
}
