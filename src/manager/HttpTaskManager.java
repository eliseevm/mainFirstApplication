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

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
    KVTaskClient taskClient;
    GsonBuilder gsonBuilder;
    Gson gson;

    private List<Task> historyList = getHistoryList();

    public HttpTaskManager(String url) {
        super(url);
        this.taskClient = new KVTaskClient(url);
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter1());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
    }/*
    @Override
    public void save() throws ManagerSaveException {
        historyList = getHistoryManager().getHistory();
            for (Integer keyTask : getDescriptionTasks().keySet()) {
                String strTask = toStrings(getDescriptionTasks().get(keyTask)); // Таску в строку
                String typ = TaskEnum.TASK.toString(); // Преобразуем Енум типа Таски в строку
                String str = String.join(",", typ, strTask + "\n"); // Сборка
                // строки и добавление в нее поля "тип". Сборка строки через символ ;
                String json = gson.toJson(str);
                taskClient.put(typ, json); // Записываем в файл
            }
            for (Integer keyEpic : getDescriptionEpic().keySet()) {
                String strEpic = toStrings(getDescriptionEpic().get(keyEpic)); //  Таску в строку
                String typ = TaskEnum.EPIC.toString(); // Преобразуем Енум типа Таски в строку
                String str = String.join(",", typ, strEpic + "\n"); // Собор строки
                // и добавляем в нее поле "тип". Сборка строки через символ ;
                String json = gson.toJson(str);
                taskClient.put(typ, json); // Записываем в файл
            }
            for (Integer keySubTask : getDescriptionSubTasks().keySet()) {
                String strSubTask = toStrings(getDescriptionSubTasks().get(keySubTask));
                String typ = TaskEnum.SUBTASK.toString();
                String epicId = String.valueOf(getDescriptionSubTasks().get(keySubTask).getEpicId());
                String str = String.join(",", typ, strSubTask, epicId + "\n");
                String json = gson.toJson(str);
                taskClient.put(typ, json);
            }

            String[] historyId = new String[historyList.size()]; // Объявляем массив записи истории
            for (int i = 0; i < historyList.size(); i++) {
                String id = String.valueOf(historyList.get(i).getId()); // Собираем номера задач для
                // записи в файл
                historyId[i] = id;
            }
            String str = String.join(",", historyId);
            String json = gson.toJson(str);
            taskClient.put("history", json);
        }*/

public static HttpTaskManager loadFromServer() throws IOException, ManagerSaveException {
    HttpTaskManager manager = new HttpTaskManager("http://localhost:8078/");
    manager.getRecover();
    return  manager;
}

    @Override
    public void save() {
        historyList = getHistoryManager().getHistory();
        String[] historyId = new String[historyList.size()]; // Объявляем массив записи истории
        for (int i = 0; i < historyList.size(); i++) {
            String id = String.valueOf(historyList.get(i).getId()); // Собираем номера задач для
            // записи в файл
            historyId[i] = id;
        }
        String jsonTask = gson.toJson(getDescriptionTasks());
        taskClient.put("task", jsonTask);
        String jsonSubTask = gson.toJson(getDescriptionSubTasks());
        taskClient.put("subtask", jsonSubTask);
        String jsonEpic = gson.toJson("getDescriptionEpic()");
        taskClient.put("epic", jsonEpic);
        String json = gson.toJson(String.join(",", historyId));
        taskClient.put("history", json);
    }

    public void getRecover() throws IOException, ManagerSaveException {
        String gsonH = taskClient.load("history");
        String[] str = gson.fromJson(gsonH, String[].class);
        historyFromString(str);
        String gsonT = taskClient.load("task");
        HashMap<Integer, Task> descriptionTask = gson.fromJson(gsonT, new TypeToken<HashMap<Integer
                , Task>>() {
        }.getType());
        HashMap<Integer, Task> mapT = getDescriptionTasks();
        mapT = descriptionTask;
        String gsonST = taskClient.load("subtask");
        HashMap<Integer, SubTask> descriptionSubTask = gson.fromJson(gsonST
                , new TypeToken<HashMap<Integer
                , SubTask>>() {
        }.getType());
        HashMap<Integer, SubTask> mapST = getDescriptionSubTasks();
        mapST = descriptionSubTask;
        String gsonE = taskClient.load("epic");
        HashMap<Integer, Epic> descriptionEpic = gson.fromJson(gsonE, new TypeToken<HashMap<Integer
                , Epic>>() {
        }.getType());
        HashMap<Integer, Epic> mapE = getDescriptionEpic();
        mapE = descriptionEpic;
        sorted();
    }
}

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    // задаём формат выходных данных: "dd--MM--yyyy"
    private final DateTimeFormatter formatterWriter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");
    private final DateTimeFormatter formatterReader
            = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");

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

class LocalDateTimeAdapter1 extends TypeAdapter<LocalDateTime> {
    // задаём формат выходных данных: "dd--MM--yyyy"
    private final DateTimeFormatter formatterWriter
            = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");
    private final DateTimeFormatter formatterReader
            = DateTimeFormatter.ofPattern("dd.MM.yyyy hh.mm.ss");

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







