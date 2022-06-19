package Test;

import logic.Epic;
import logic.SubTask;
import logic.Task;
import manager.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    HttpTaskManager manager = new HttpTaskManager("http://localhost:8078");
    KVServer kvServer = new KVServer();

    public HttpTaskManagerTest() throws IOException {
        super(new HttpTaskManager("http://localhost:8078"));
        kvServer.start();
    }

    @Test
    void testStartManager() throws ManagerSaveException, IOException {
        int durationTask = 10;
        int durationSubTask = 10;
        LocalDateTime startTimeTask = LocalDateTime.of(2022, 6, 3
                , 8, 22, 25);
        LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 9, 5
                , 8, 22, 25);
        manager.inputNewTask(new Task("name", "description"
                , Status.NEW, 0, durationTask, startTimeTask));
        manager.inputNewEpic(new Epic("name", "description", 1));
        manager.inputNewSubTask(new SubTask("name", "description"
                , Status.DONE, 2, 1, durationSubTask, startTimeSubTask));
        manager.outputTaskById(0);
        manager.outputSubTaskById(2);
        manager.outputEpicById(1);
        HttpTaskManager manager1 = new HttpTaskManager("http://localhost:8078/");
        manager1.loadFromServer();
        assertEquals(manager.getHistory().size()
                , manager1.getHistory().size());
        assertEquals(manager.getDescriptionEpic().size()
                , manager1.getDescriptionEpic().size());
        assertEquals(manager.getDescriptionTasks().size(), manager1.getDescriptionTasks().size());
        assertEquals(manager.getDescriptionSubTasks().size()
                , manager1.getDescriptionSubTasks().size());
        manager1.sorted();
    }
}
