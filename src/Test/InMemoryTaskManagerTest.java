package Test;

import logic.Epic;
import logic.SubTask;
import logic.Task;
import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import manager.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    InMemoryTaskManager manager = new InMemoryTaskManager();

    Duration durationTask = Duration.ofMinutes(10);
    LocalDateTime startTimeTask = LocalDateTime.of(2022, 7, 7
            , 8, 22, 25);
    LocalDateTime startTimeTask1 = LocalDateTime.of(2022, 8, 8
            , 8, 22, 25);
    LocalDateTime startTimeTask2 = LocalDateTime.of(2022, 9, 9
            , 8, 22, 25);
    LocalDateTime startTimeTask3 = LocalDateTime.of(2022, 10, 9
            , 11, 22, 25);
    Duration durationSubTask = Duration.ofMinutes(20);
    LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 11, 10
            , 8, 22, 25);
    LocalDateTime startTimeSubTask1 = LocalDateTime.of(2022, 12, 11
            , 8, 22, 25);
    LocalDateTime startTimeSubTask2 = LocalDateTime.of(2022, 7, 12
            , 8, 22, 25);

    @BeforeEach
    void volumeForTests1() throws IOException, ManagerSaveException {
        manager.inputNewTask(new Task("name", "description", Status.NEW
                , 0, durationTask, startTimeTask));
        manager.inputNewEpic(new Epic("name", "description", 1));
        manager.inputNewEpic(new Epic("name", "description", 2));
        manager.inputNewSubTask(new SubTask("name", "description", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask), 1);
        manager.inputNewSubTask(new SubTask("name", "description", Status.DONE
                , 4, 1, durationSubTask, startTimeSubTask1), 1);
        manager.inputNewTask(new Task("name", "description", Status.DONE
                , 5, durationTask, startTimeTask1));
        manager.inputNewTask(new Task("name", "description", Status.NEW
                , 6, durationTask, startTimeTask2));
        manager.outputTaskById(0);
        manager.outputTaskById(5);
        manager.outputEpicById(1);
        manager.outputEpicById(2);
        manager.outputSubTaskById(3);
        manager.outputSubTaskById(4);
        manager.outputTaskById(6);
        manager.sorted();
    }

    @Test
    void sorted() throws IOException, ManagerSaveException {
        manager.inputNewEpic(new Epic("Сортировочный", "Для проверки", 7));
        manager.sorted();
        assertEquals(8, manager.getPrioritizedTasks().size());
    }

    @Test
    void getPrioritizedTasks() {
        assertEquals(7, manager.getPrioritizedTasks().size());
    }

    @Test
    void setTaskId() {
        InMemoryTaskManager manager = new InMemoryTaskManager();
        manager.setTaskId(2);
        assertEquals(2, manager.getTaskId());
    }

    @Test
    void timeСontrol() throws IOException, ManagerSaveException {
        assertFalse(manager.timeСontrol(new Task("name"
                , "description", Status.NEW, 0, durationTask, startTimeTask)));
    }
}
