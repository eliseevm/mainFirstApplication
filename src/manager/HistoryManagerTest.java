package manager;

import logic.Epic;
import logic.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    LocalDateTime startTimeTask = LocalDateTime.of(2022, 4, 4
            , 8, 22, 25);
    LocalDateTime startTimeTask1 = LocalDateTime.of(2022, 4, 4
            , 7, 22, 25);
    Duration durationTask = Duration.ofMinutes(20);
    LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 4, 5
            , 8, 22, 25);
    Duration durationSubTask = Duration.ofMinutes(18);

    InMemoryTaskManager ex = new InMemoryTaskManager();

    @BeforeEach
    void historyTest() throws IOException, ManagerSaveException {
        ex.inputNewTask(new Task("name", "описание", Status.NEW
                , 0, durationTask, startTimeTask));
        ex.inputNewEpic(new Epic("name", "описание", 1));
        ex.inputNewEpic(new Epic("name", "описание", 2));
        ex.inputNewTask(new Task("name", "описание", Status.DONE
                , 3, durationTask, startTimeTask1));
        ex.outputTaskById(0);
        ex.outputTaskById(3);
        ex.outputEpicById(1);
        ex.outputEpicById(2);

    }

    @Test
    void add() throws IOException, ManagerSaveException {
        ex.getHistoryManager().add(new Task("name", "описание", Status.DONE
                , 8, durationTask, startTimeTask));
        assertEquals(8, ex.getHistoryManager().getHistory().get(4).getId());
    }

    @Test
    void remove() {
        InMemoryHistoryManager.Node<Task> e = ex.getHistoryManager().getTempNodeMap().get(1);
        ex.getHistoryManager().getTempNodeMap().remove(1);
        InMemoryHistoryManager.Node<Task> r = ex.getHistoryManager().getTempNodeMap().get(1);
        assertNotEquals(e, r);
        assertNull(r);
    }

    @Test
    void getHistory() throws IOException {
        assertEquals(3, ex.getHistory().get(1).getId());
    }
}