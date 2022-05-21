package Test;

import logic.Epic;
import logic.SubTask;
import logic.Task;
import manager.ManagerSaveException;
import manager.Status;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    Duration durationTask = Duration.ofMinutes(10);
    LocalDateTime startTimeTask = LocalDateTime.of(2022, 7, 7
            , 8, 22, 25);
    LocalDateTime startTimeTask1 = LocalDateTime.of(2022, 7, 8
            , 8, 22, 25);
    LocalDateTime startTimeTask2 = LocalDateTime.of(2022, 7, 9
            , 8, 22, 25);
    LocalDateTime startTimeTask3 = LocalDateTime.of(2022, 8, 9
            , 11, 22, 25);
    Duration durationSubTask = Duration.ofMinutes(20);
    LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 9, 10
            , 8, 22, 25);
    LocalDateTime startTimeSubTask1 = LocalDateTime.of(2022, 10, 11
            , 8, 22, 25);
    LocalDateTime startTimeSubTask2 = LocalDateTime.of(2022, 11, 12
            , 8, 22, 25);

    @BeforeEach
    void volumeForTests() throws IOException, ManagerSaveException {

        taskManager.inputNewTask(new Task("name", "description", Status.NEW
                , 0, durationTask, startTimeTask));
        taskManager.inputNewEpic(new Epic("name", "description", 1));
        taskManager.inputNewEpic(new Epic("name", "description", 2));
        taskManager.inputNewSubTask(new SubTask("name", "description", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask), 1);
        taskManager.inputNewSubTask(new SubTask("name", "description", Status.DONE
                , 4, 1, durationSubTask, startTimeSubTask1), 1);
        taskManager.inputNewTask(new Task("name", "description", Status.DONE
                , 5, durationTask, startTimeTask1));
        taskManager.inputNewTask(new Task("name", "description", Status.NEW
                , 6, durationTask, startTimeTask2));
        taskManager.outputTaskById(0);
        taskManager.outputTaskById(5);
        taskManager.outputEpicById(1);
        taskManager.outputEpicById(2);
        taskManager.outputSubTaskById(3);
        taskManager.outputSubTaskById(4);
        taskManager.outputTaskById(6);
    }

    @Test
    void testGetDescriptionTasks() throws IOException, ManagerSaveException {
        assertEquals(3, taskManager.getDescriptionTasks().size());
        taskManager.deletAllTasks();
        assertEquals(0, taskManager.getDescriptionTasks().size());
    }

    @Test
    void testGetDescriptionSubTasks() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionSubTasks().size());
        taskManager.deletAllTasks();
        assertEquals(0, taskManager.getDescriptionSubTasks().size());
    }

    @Test
    void testGetDescriptionEpic() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionEpic().size());
        taskManager.deletAllTasks();
        assertEquals(0, taskManager.getDescriptionEpic().size());
    }

    @Test
    void testGetTaskId() {
        assertEquals(0, taskManager.getTaskId());
    }

    @Test
    void testGetHistory() throws IOException, ManagerSaveException {
        assertEquals(1, taskManager.getHistory().get(2).getId());
        taskManager.deletAllTasks();
        assertEquals(5, taskManager.getHistory().get(1).getId());
        assertEquals(7, taskManager.getHistory().size());
    }

    @Test
    void testOutputAllTask() throws IOException, ManagerSaveException {
        assertEquals(3, taskManager.outputAllTask().size());
        taskManager.deletAllTasks();
        assertEquals(0, taskManager.outputAllTask().size());
        taskManager.inputNewTask(new Task("name", "description", Status.DONE
                , 0, durationTask, startTimeTask1));
        assertEquals(1, taskManager.outputAllTask().size());
    }

    @Test
    void testOutputAllEpics() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.outputAllEpics().size());
        taskManager.deletTaskById(2);
        assertEquals(1, taskManager.getDescriptionEpic().size());
    }

    @Test
    void testOutputSubtaskByEpik() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.outputSubtaskByEpik(1).size());
        assertEquals(Status.NEW, taskManager.outputSubtaskByEpik(1).get(0).getStatus());
    }

    @Test
    void testOutputTaskById() throws IOException, ManagerSaveException {
        assertEquals(5, taskManager.outputTaskById(5).getId());
        assertEquals(Status.DONE, taskManager.outputTaskById(5).getStatus());
    }

    @Test
    void testOutputSubTaskById() throws IOException, ManagerSaveException {
        assertEquals(3, taskManager.outputSubTaskById(3).getId());
        assertEquals(Status.NEW, taskManager.outputSubTaskById(3).getStatus());
    }

    @Test
    void testOutputEpicById() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.outputEpicById(2).getId());
        assertEquals(Status.IN_PROGRESS, taskManager.outputEpicById(1).getStatus());
    }

    @Test
    void testInputNewTask() throws IOException, ManagerSaveException {
        assertEquals(3, taskManager.getDescriptionTasks().size());
        taskManager.inputNewTask(new Task("name", "description", Status.DONE
                , 7, durationTask, startTimeTask3));
        assertEquals(4, taskManager.getDescriptionTasks().size());
    }

    @Test
    void testInputNewEpic() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionEpic().size());
        taskManager.inputNewEpic(new Epic("name", "description", 8));
        assertEquals(3, taskManager.getDescriptionEpic().size());
    }

    @Test
    void testInputNewSubTask() throws IOException, ManagerSaveException {
        assertEquals(2, taskManager.getDescriptionSubTasks().size());
        taskManager.inputNewSubTask(new SubTask("name", "description", Status.IN_PROGRESS
                , 9, 1, durationSubTask, startTimeSubTask2), 1);
        assertEquals(3, taskManager.getDescriptionSubTasks().size());
    }

    @Test
    void testUpdateTask() {
        assertEquals(Status.NEW, taskManager.getDescriptionTasks().get(6).getStatus());
        assertEquals(3, taskManager.getDescriptionTasks().size());
        taskManager.updateTask(new Task("name", "description", Status.IN_PROGRESS
                , 6, durationTask, startTimeTask3));
        assertEquals(3, taskManager.getDescriptionTasks().size());
        assertEquals(Status.IN_PROGRESS, taskManager.getDescriptionTasks().get(6).getStatus());
    }

    @Test
    void testUpdateSubTask() {
        taskManager.updateSubTask(new SubTask("Обновленная", "description", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask));
        ;
        assertEquals(2, taskManager.getDescriptionSubTasks().size());
        assertEquals("Обновленная", taskManager.getDescriptionSubTasks().get(3).getName());
    }

    @Test
    void testUpdateEpic() {
        taskManager.updateEpic(new Epic("Обновленный", "description", 1));
        assertEquals(2, taskManager.getDescriptionEpic().size());
        assertEquals("Обновленный", taskManager.getDescriptionEpic().get(1).getName());
    }

    @Test
    void testDeletTaskById() throws IOException, ManagerSaveException {
        taskManager.deletTaskById(0);
        assertEquals(2, taskManager.getDescriptionTasks().size());
        assertNull(taskManager.getDescriptionTasks().get(0));
    }

    @Test
    void testDeletAllTasks() throws IOException, ManagerSaveException {
        taskManager.deletAllTasks();
        assertNull(taskManager.getDescriptionEpic().get(1));
        assertNull(taskManager.getDescriptionTasks().get(0));
        assertNull(taskManager.getDescriptionSubTasks().get(3));
    }
}