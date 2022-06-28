package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import logic.Epic;
import logic.SubTask;
import logic.Task;
import service.ManagerSaveException;

import java.io.*;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    private KVTaskClient taskClient; // Клиент для взаимодействия с серваром хранения.
    private GsonBuilder gsonBuilder;
    private Gson gson;
    private List<Task> historyList = getHistoryList();

    public HttpTaskManager(String url) {
        super(url);
        taskClient = new KVTaskClient(url);
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter1());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }

    // Метод восстановления менеджера из хранилиша-сервера.
    public static TaskManager loadFromServer() throws IOException, ManagerSaveException {
        HttpTaskManager manager = new HttpTaskManager("http://localhost:8078");
        manager.getRecover();
        return manager;
    }

    // Переопределяем метод записи состояния менеджера.
    @Override
    public void save() {
        historyList = getHistoryManager().getHistory();
        String[] historyId = new String[historyList.size()]; // Объявляем массив записи истории.
        for (int i = 0; i < historyList.size(); i++) {
            String id = String.valueOf(historyList.get(i).getId()); /* Собираем номера задач для
             записи в файл. */
            historyId[i] = id;
        }
        String jsonTask = gson.toJson(getDescriptionTasks()); // Передаем на хранение задачи.
        taskClient.put("task", jsonTask);
        String jsonSubTask = gson.toJson(getDescriptionSubTasks()); // Передаем на хра-ие подзадачи.
        taskClient.put("subtask", jsonSubTask);
        String jsonEpic = gson.toJson(getDescriptionEpic()); // Передаем на хранение эпики.
        taskClient.put("epic", jsonEpic);
        String json = gson.toJson(String.join(",", historyId)); // Передаем историю.
        taskClient.put("history", json);
    }

    // Метод восстановления состояеия менеджера из хранилиша-сервера.
    public void getRecover() {
        HttpResponse<String> response1 = taskClient.load("task");
        HashMap<Integer, Task> descriptionTask = gson.fromJson(response1.body()
                , new TypeToken<HashMap<Integer, Task>>() {
                }.getType());
        HashMap<Integer, Task> mapT = getDescriptionTasks();
        mapT.putAll(descriptionTask);
        HttpResponse<String> response3 = taskClient.load("epic");
        HashMap<Integer, Epic> descriptionEpic = gson.fromJson(response3.body()
                , new TypeToken<HashMap<Integer, Epic>>() {
                }.getType());
        HashMap<Integer, Epic> mapE = getDescriptionEpic();
        mapE.putAll(descriptionEpic);
        HttpResponse<String> response2 = taskClient.load("subtask");
        HashMap<Integer, SubTask> descriptionSubTask = gson.fromJson(response2.body()
                , new TypeToken<HashMap<Integer, SubTask>>() {
                }.getType());
        HashMap<Integer, SubTask> mapST = getDescriptionSubTasks();
        mapST.putAll(descriptionSubTask);
        HttpResponse<String> response = taskClient.load("history");
        String r = gson.fromJson(response.body(), String.class);
        String[] his = r.split(",");
        historyFromString(his);
        sorted();
    }
}

// Создаем адаптер для сериализации полей с типом LocalDateTime.
class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    // задаём формат выходных данных: "dd--MM--yyyy"
    private final DateTimeFormatter formatterWriter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
    private final DateTimeFormatter formatterReader
            = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");

    @Override
    public void write(final JsonWriter jsonWriter
            , final LocalDateTime startTime) throws IOException {
        // приводим localDate к необходимому формату
        jsonWriter.value(startTime.format(formatterWriter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}

// Создаем адаптер для сериализации полей с типом LocalDateTime.
class LocalDateTimeAdapter1 extends TypeAdapter<LocalDateTime> {
    // задаём формат выходных данных: "dd--MM--yyyy"
    private final DateTimeFormatter formatterWriter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");
    private final DateTimeFormatter formatterReader
            = DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss");

    @Override
    public void write(final JsonWriter jsonWriter
            , final LocalDateTime endTime) throws IOException {
        // приводим localDate к необходимому формату
        jsonWriter.value(endTime.format(formatterWriter));
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), formatterReader);
    }
}







