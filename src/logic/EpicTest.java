package logic;

import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import manager.Status;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    LocalDateTime startTimeEpic = LocalDateTime.of(2022, 4, 4
            , 8, 22, 25);
    Duration durationSubTask = Duration.ofMinutes(20);
    LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 4, 5
            , 8, 22, 25);
    InMemoryTaskManager ex = new InMemoryTaskManager();


    @Test
    void getStatusTest() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0
                , startTimeEpic));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 1, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 2, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 3, 0, durationSubTask, startTimeSubTask), 0);
        assertEquals(Status.NEW, ex.outputEpicById(0).getStatus());
    }
    @Test
    void getStatusTest1() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0
                , startTimeEpic));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 1, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 2, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 3, 0, durationSubTask, startTimeSubTask), 0);
        assertEquals(Status.DONE, ex.outputEpicById(0).getStatus());
    }
    @Test
    void getStatusTest2() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0
                , startTimeEpic));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 1, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 2, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 3, 0, durationSubTask, startTimeSubTask), 0);
        assertEquals(Status.IN_PROGRESS, ex.outputEpicById(0).getStatus());
    }
    @Test
    void getStatusTest3() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0
                , startTimeEpic));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.IN_PROGRESS
                , 1, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.IN_PROGRESS
                , 2, 0, durationSubTask, startTimeSubTask), 0);
        ex.inputNewSubTask(new SubTask("name", "описание", Status.IN_PROGRESS
                , 3, 0, durationSubTask, startTimeSubTask), 0);
        assertEquals(Status.IN_PROGRESS, ex.outputEpicById(0).getStatus());
    }
    @Test
    void getStatusTest4() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0
                , startTimeEpic));
        assertEquals(Status.NEW, ex.outputEpicById(0).getStatus());
    }
}