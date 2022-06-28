package Test;

import logic.Epic;
import logic.SubTask;
import logic.Task;
import manager.FileBackedTasksManager;
import service.ManagerSaveException;
import manager.Status;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    FileBackedTasksManager manager3 =
            new FileBackedTasksManager(new File("src/history.csv"));

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File("src/history.csv")));
    }

    @Test
    void testStartManager() throws IOException, ManagerSaveException {
        int durationTask = 10;
        int durationSubTask = 10;
        LocalDateTime startTimeTask = LocalDateTime.of(2022, 6, 3
                , 8, 22, 25);
        LocalDateTime startTimeTask2 = LocalDateTime.of(2022, 5, 28
                , 8, 22, 25);
        LocalDateTime startTimeTask1 = LocalDateTime.of(2022, 6, 4
                , 8, 22, 25);
        LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 9, 5
                , 8, 22, 25);
        LocalDateTime startTimeSubTask1 = LocalDateTime.of(2022, 10, 5
                , 8, 22, 25);
        LocalDateTime startTimeSubTask2 = LocalDateTime.of(2022, 10, 5
                , 18, 22, 25);
        LocalDateTime startTimeSubTask3 = LocalDateTime.of(2022, 10, 5
                , 23, 22, 25);

        manager3.inputNewTask(new Task("name", "description"
                , Status.NEW, manager3.getTaskId(), durationTask, startTimeTask));
        manager3.inputNewEpic(new Epic("name", "description"
                , manager3.getTaskId()));
        manager3.inputNewSubTask(new SubTask("name", "description"
                , Status.DONE, manager3.getTaskId(), 1, durationSubTask
                , startTimeSubTask));
        manager3.inputNewSubTask(new SubTask("name", "description"
                , Status.DONE, manager3.getTaskId(), 1, durationSubTask
                , startTimeSubTask1));
        manager3.inputNewTask(new Task("name", "description"
                , Status.NEW, manager3.getTaskId(), durationTask, startTimeTask1));
        manager3.inputNewTask(new Task("name", "description"
                , Status.NEW, manager3.getTaskId(), durationTask, startTimeTask2));
        manager3.inputNewEpic(new Epic("name", "description"
                , manager3.getTaskId()));
        manager3.inputNewSubTask(new SubTask("name", "description"
                , Status.DONE, manager3.getTaskId(), 6, durationSubTask
                , startTimeSubTask2));
        manager3.inputNewSubTask(new SubTask("name", "description"
                , Status.DONE, manager3.getTaskId(), 6, durationSubTask
                , startTimeSubTask3));
        manager3.outputTaskById(0);
        manager3.outputTaskById(5);
        manager3.outputEpicById(1);
        manager3.outputEpicById(6);
        manager3.outputSubTaskById(3);
        manager3.outputSubTaskById(2);
        manager3.outputTaskById(4);

        FileBackedTasksManager manager4 = FileBackedTasksManager
                .loadFromFile(new File("src/history.csv"));
        manager4.startFromString();
        assertEquals(manager3.getHistory().size()
                , manager4.getHistory().size());
        assertEquals(manager3.getDescriptionEpic().size()
                , manager4.getDescriptionEpic().size());
        assertEquals(manager3.getDescriptionTasks().size(), manager4.getDescriptionTasks().size());
        assertEquals(manager3.getDescriptionSubTasks().size()
                , manager4.getDescriptionSubTasks().size());
        manager4.sorted();
    }
}