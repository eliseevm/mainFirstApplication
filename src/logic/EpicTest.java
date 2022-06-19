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
    private int durationSubTask = 20;
    private LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 4, 24
            , 8, 22, 25);
    private LocalDateTime startTimeSubTask1 = LocalDateTime.of(2022, 4, 15
            , 9, 22, 25);
    private LocalDateTime startTimeSubTask2 = LocalDateTime.of(2022, 4, 7
            , 10, 22, 25);
    private InMemoryTaskManager ex = new InMemoryTaskManager();

    @Test
    void getStatusTestByStatusNew() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 1, 0, durationSubTask, startTimeSubTask));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 2, 0, durationSubTask, startTimeSubTask1));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 3, 0, durationSubTask, startTimeSubTask2));
        assertEquals(Status.NEW, ex.outputEpicById(0).getStatus());
    }

    @Test
    void getStatusTestByStatusDone() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 1, 0, durationSubTask, startTimeSubTask));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 2, 0, durationSubTask, startTimeSubTask1));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 3, 0, durationSubTask, startTimeSubTask2));
        assertEquals(Status.DONE, ex.outputEpicById(0).getStatus());
    }

    @Test
    void getStatusTestByStatusIN_Progress() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 1, 0, durationSubTask, startTimeSubTask));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.DONE
                , 2, 0, durationSubTask, startTimeSubTask1));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.NEW
                , 3, 0, durationSubTask, startTimeSubTask2));
        assertEquals(Status.IN_PROGRESS, ex.outputEpicById(0).getStatus());
    }

    @Test
    void getStatusTestByStatusAllIN_Progress() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.IN_PROGRESS
                , 1, 0, durationSubTask, startTimeSubTask));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.IN_PROGRESS
                , 2, 0, durationSubTask, startTimeSubTask1));
        ex.inputNewSubTask(new SubTask("name", "описание", Status.IN_PROGRESS
                , 3, 0, durationSubTask, startTimeSubTask2));
        assertEquals(Status.IN_PROGRESS, ex.outputEpicById(0).getStatus());
    }

    @Test
    void getStatusTestByStatusForEpicFree() throws IOException, ManagerSaveException {
        ex.inputNewEpic(new Epic("name", "description", 0));
        assertEquals(Status.NEW, ex.outputEpicById(0).getStatus());
    }
}