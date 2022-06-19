package logic;

import manager.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;

public class Mains {

    public static void main(String[] args) throws IOException, ManagerSaveException {
        TaskManager httpTaskManager = Managers.getDefault();
        HttpTaskServer server = new HttpTaskServer(httpTaskManager);
        KVServer kvServer = new KVServer();
        server.start();
        kvServer.start();
        createPattern(httpTaskManager);
        history(httpTaskManager);

    }

    public static void createPattern(TaskManager manager) throws IOException {
        int durationTask = 15;
        LocalDateTime startTimeTask = LocalDateTime.of(2022, 6, 3, 15, 22, 23);
        LocalDateTime startTimeTask1 = LocalDateTime.of(2022, 7, 3, 15, 22, 23);
        LocalDateTime startTimeTask2 = LocalDateTime.of(2022, 8, 3, 15, 22, 23);
        LocalDateTime startTimeTask3 = LocalDateTime.of(2022, 9, 3, 15, 22, 23);
        LocalDateTime startTimeTask4 = LocalDateTime.of(2022, 10, 3, 15, 22, 23);

        manager.inputNewTask(new Task("name", "description"
                , Status.NEW, 0, durationTask, startTimeTask));
        manager.inputNewTask(new Task("name", "description"
                , Status.IN_PROGRESS, 1, durationTask, startTimeTask1));
        try {
            manager.inputNewEpic(new Epic("Epicname", "Epicdescription", 2));
            manager.inputNewSubTask(new SubTask("STname", "STdescription", Status.NEW
                    , 3, 2, durationTask, startTimeTask2));
        } catch (ManagerSaveException ex) {
            System.out.println("Исключение при создании подзадачи в майнс");
        }
    }

    public static void history(TaskManager manager) {
        try {
            manager.outputSubTaskById(3);
            manager.outputTaskById(0);
            manager.outputEpicById(2);
        } catch (IOException | ManagerSaveException ex) {
            ex.getMessage();
            System.out.println("Непредвиденная ошибка в мэйне");
        }
    }
}
