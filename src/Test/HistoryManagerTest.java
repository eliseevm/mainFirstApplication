package Test;

import logic.Epic;
import logic.Task;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import service.ManagerSaveException;
import manager.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    LocalDateTime startTimeTask = LocalDateTime.of(2022, 4, 4
            , 8, 22, 25);
    LocalDateTime startTimeTask1 = LocalDateTime.of(2022, 4, 4
            , 7, 22, 25);
    int durationTask = 20;
    LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 4, 5
            , 8, 22, 25);

    InMemoryTaskManager manager = new InMemoryTaskManager();

    @BeforeEach
    void historyTest() throws IOException, ManagerSaveException {
        manager.inputNewTask(new Task("name", "описание", Status.NEW
                , 0, durationTask, startTimeTask));
        manager.inputNewEpic(new Epic("name", "описание", 1));
        manager.inputNewEpic(new Epic("name", "описание", 2));
        manager.inputNewTask(new Task("name", "описание", Status.DONE
                , 3, durationTask, startTimeTask1));
        manager.outputTaskById(0);
        manager.outputTaskById(3);
        manager.outputEpicById(1);
        manager.outputEpicById(2);
    }

    @Test
    void add() {
        manager.getHistoryManager().add(new Task("name", "описание", Status.DONE
                , 8, durationTask, startTimeTask));
        assertEquals(8, manager.getHistoryManager().getHistory().get(4).getId());
    }

    @Test
    void remove() {
        InMemoryHistoryManager.Node<Task> e = manager.getHistoryManager().getTempNodeMap().get(1);
        manager.getHistoryManager().getTempNodeMap().remove(1);
        InMemoryHistoryManager.Node<Task> r = manager.getHistoryManager().getTempNodeMap().get(1);
        assertNotEquals(e, r);
        assertNull(r);
    }

    @Test
    void getHistory() throws IOException {
        assertEquals(3, manager.getHistory().get(1).getId());
    }
}