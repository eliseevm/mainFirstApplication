package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    FileBackedTasksManager manager = new FileBackedTasksManager(new File("src/history.csv"));
    FileBackedTasksManager manager6 = new FileBackedTasksManager(new File("src/test2.csv"));

    Duration durationTask = Duration.ofMinutes(10);
    LocalDateTime startTimeTask = LocalDateTime.of(2022, 4, 3
            , 8, 22, 25);
    LocalDateTime startTimeEpic = LocalDateTime.of(2022, 4, 4
            , 8, 22, 25);
    Duration durationSubTask = Duration.ofMinutes(20);
    LocalDateTime startTimeSubTask = LocalDateTime.of(2022, 4, 5
            , 8, 22, 25);



    @Test
    void recoveryManagerStandart() throws IOException, ManagerSaveException {
        FileBackedTasksManager manager4 = FileBackedTasksManager
                .loadFromFile(new File("src/history.csv"));
        assertEquals(manager.getHistory().get(2).getDuration()
                , manager4.getHistory().get(2).getDuration());
        assertEquals(manager.getDescriptionEpic().get(1).getDuration()
                , manager4.getDescriptionEpic().get(1).getDuration());
        assertEquals(manager.getDescriptionTasks(), manager4.getDescriptionTasks());
        assertEquals(manager.getDescriptionSubTasks(), manager4.getDescriptionSubTasks());
    }

    @Test
    void recoveryManagerNoSubTask() throws IOException, ManagerSaveException {
        manager6.inputNewTask(new Task("name", "описание", Status.NEW
                , 0, durationTask, startTimeTask));
        manager6.inputNewEpic(new Epic("name", "описание", 1
                , startTimeEpic));
        manager6.inputNewEpic(new Epic("name", "описание", 2
                , startTimeEpic));
        manager6.inputNewTask(new Task("name", "описание", Status.DONE
                , 3, durationTask, startTimeTask));
        manager6.outputTaskById(0);
        manager6.outputTaskById(3);
        manager6.outputEpicById(1);
        manager6.outputEpicById(2);
        FileBackedTasksManager manager5 = FileBackedTasksManager
                .loadFromFile(new File("src/test2.csv"));
        manager5.fromString();
        System.out.println(manager6.getHistoryManager().getHistory().get(0));
        System.out.println(manager5.getHistoryManager().getHistory().get(0));
        assertEquals(manager6.getHistory().get(2).getDuration()
                , manager5.getHistory().get(2).getDuration());
        assertEquals(manager6.getDescriptionEpic().get(1).getDuration()
                , manager5.getDescriptionEpic().get(1).getDuration());
        assertEquals(manager6.getDescriptionTasks().get(0), manager5.getDescriptionTasks().get(0));
    }

    @BeforeEach
    void volumeForTests() throws IOException, ManagerSaveException {
        manager.inputNewTask(new Task("name", "description", Status.NEW
                , 0, durationTask, startTimeTask));
        manager.inputNewEpic(new Epic("name", "description", 1
                , startTimeEpic));
        manager.inputNewEpic(new Epic("name", "description", 2
                , startTimeEpic));
        manager.inputNewSubTask(new SubTask("name", "description", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask), 1);
        manager.inputNewSubTask(new SubTask("name", "description", Status.DONE
                , 4, 1, durationSubTask, startTimeSubTask), 1);
        manager.inputNewTask(new Task("name", "description", Status.DONE
                , 5, durationTask, startTimeTask));
        manager.inputNewTask(new Task("name", "description", Status.NEW
                , 6, durationTask, startTimeTask));
        manager.outputTaskById(0);
        manager.outputTaskById(5);
        manager.outputEpicById(1);
        manager.outputEpicById(2);
        manager.outputSubTaskById(3);
        manager.outputSubTaskById(4);
        manager.outputTaskById(6);

    }

    @Test
    void getDescriptionTasksStandart() {
        assertEquals(new Task("name", "description", Status.NEW
                        , 0, durationTask, startTimeTask)
                , manager.getDescriptionTasks().get(0));
    }

    @Test
    void getDescriptionTasksError() {
        assertNull(manager.getDescriptionTasks().get(manager.getDescriptionTasks().size() + 5),"Списка задачь не существует");
    }

    @Test
    void getDescriptionTasksEmpty() {
        assertNull(manager.getDescriptionTasks().get(3));
    }

    @Test
    void getDescriptionSubTasksStandart() {
        assertEquals(new SubTask("name", "description", Status.NEW
                        , 3, 1, durationSubTask, startTimeSubTask)
                , manager.getDescriptionSubTasks().get(3));
        assertEquals(1, manager.getDescriptionSubTasks().get(3).getEpicId());
    }

    @Test
    void getDescriptionSubTasksEmpty() {
        assertNull(manager.getDescriptionSubTasks().get(1));
    }

    @Test
    void getDescriptionSubTasksError() {
        assertNull(manager.getDescriptionSubTasks().get(0));
    }

    @Test
    void getDescriptionEpicStandart() {
        assertEquals(Status.IN_PROGRESS, manager.getDescriptionEpic().get(1).getStatus());
    }

    @Test
    public void shouldEpicStatusBiDoneEndNew() {
        assertEquals(Status.IN_PROGRESS, manager.getDescriptionEpic().get(1).getStatus());
    }

    @Test
    public void shouldEpicStatusBiDone() throws ManagerSaveException, IOException {
        manager.deletAllTasks();
        manager.inputNewEpic(new Epic("name", "description", 1
                , startTimeEpic));
        manager.getDescriptionEpic().get(1).getListSubTask().add(0, new SubTask("Проверяем статус", "пишем тест"
                , Status.DONE, 3, 1, durationSubTask, startTimeSubTask));
        manager.getDescriptionEpic().get(1).getListSubTask().add(1, new SubTask("Проверяем статус", "пишем тест"
                , Status.DONE, 3, 1, durationSubTask, startTimeSubTask));
        System.out.println(manager.getDescriptionEpic().get(1).getListSubTask().get(0).getStatus());
        assertEquals(Status.DONE, manager.getDescriptionEpic().get(1).getStatus());
    }

    @Test
    public void shouldEpicStatusBiNew() throws ManagerSaveException, IOException {
        manager.deletAllTasks();
        manager.inputNewEpic(new Epic("name", "description", 1
                , startTimeEpic));
        manager.inputNewSubTask(new SubTask("Проверяем статус", "пишем тест"
                , Status.NEW, 0, 1, durationSubTask, startTimeSubTask), 1);
        manager.inputNewSubTask(new SubTask("Проверяем статус", "пишем тест"
                , Status.NEW, 2, 1, durationSubTask, startTimeSubTask), 1);
        manager.inputNewSubTask(new SubTask("Проверяем статус", "пишем тест"
                , Status.NEW, 3, 1, durationSubTask, startTimeSubTask), 1);
        assertEquals(Status.NEW, manager.getDescriptionEpic().get(1).getStatus());
    }

    @Test
    public void shouldEpicStatusBiInprogress() throws ManagerSaveException, IOException {
        manager.deletAllTasks();
        manager.inputNewEpic(new Epic("name", "description", 1
                , startTimeEpic));
        manager.inputNewSubTask(new SubTask("Проверяем статус", "пишем тест"
                , Status.NEW, 0, 1, durationSubTask, startTimeSubTask), 1);
        manager.inputNewSubTask(new SubTask("Проверяем статус", "пишем тест"
                , Status.DONE, 2, 1, durationSubTask, startTimeSubTask), 1);
        manager.inputNewSubTask(new SubTask("Проверяем статус", "пишем тест"
                , Status.IN_PROGRESS, 3, 1, durationSubTask, startTimeSubTask), 1);
        assertEquals(Status.IN_PROGRESS, manager.getDescriptionEpic().get(1).getStatus());
    }

    @Test
    void getDescriptionEpicEmpty() throws IOException, ManagerSaveException {
        manager.deletAllTasks();
        assertNull(manager.getDescriptionEpic().get(0));
    }

    @Test
    void getDescriptionEpicError() throws IOException, ManagerSaveException {
        assertNull(manager.getDescriptionEpic().get(4));
    }

    @Test
    void getHistoryManagerStandart() {
        assertEquals(7, manager.getHistoryManager().getTasks().size());
    }

    @Test
    void getHistoryManagerStandart1() {
        assertEquals(new Task("name", "description", Status.DONE
                , 5, durationTask, startTimeTask), manager.getHistoryManager().getHistory().get(1));
    }

    @Test
    void getTaskIdStsndsrt() {
        manager.setTaskId(8);
        assertEquals(8, manager.getTaskId());
        assertEquals(9, manager.getTaskId());
    }

    @Test
    void getHistory() throws IOException {
        assertEquals(new Task("name", "description", Status.DONE
                , 5, durationTask, startTimeTask), manager.getHistory().get(1));
    }

    @Test
    void setTaskId() {
        manager.setTaskId(6);
        assertEquals(6, manager.getTaskId());
    }

    @Test
    void outputAllTaskStandart() {
        assertEquals(manager.getDescriptionTasks(), manager.outputAllTask());
    }

    @Test
    void outputAllTaskEmpty() throws IOException, ManagerSaveException {
        manager.deletAllTasks();
        assertEquals(0, manager.outputAllTask().size());
    }

    @Test
    void outputAllEpicsStandart() {
        assertEquals(manager.getDescriptionEpic(), manager.outputAllEpics());
    }

    @Test
    void outputAllEpicsEmpty() {
        manager.getDescriptionEpic().clear();
        assertEquals(null, manager.outputAllEpics().get(1));
    }

    @Test
    void outputSubtaskByEpikStandart() {
        List<SubTask> subTask = manager.getDescriptionEpic().get(1).getListSubTask();
        assertEquals(subTask, manager.outputSubtaskByEpik(1));
    }

    @Test
    void outputSubtaskByEpikEmpty() {
        manager.getDescriptionEpic().clear();
        manager.getDescriptionSubTasks().clear();
        final NullPointerException exception = assertThrows(
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        List<SubTask> listEpics = manager.getDescriptionEpic().get(1)
                                .getListSubTask();
                    }
                });
        assertEquals("Cannot invoke \"logic.Epic.getListSubTask()" +
                "\" because the return value of \"java.util.HashMap.get(Object)" +
                "\" is null", exception.getMessage());
    }

    @Test
    void outputSubtaskByEpikError() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() {
                        List<SubTask> listEpics = manager.getDescriptionEpic().get(3)
                                .getListSubTask();
                    }
                });
        assertEquals("Cannot invoke \"logic.Epic.getListSubTask()" +
                "\" because the return value of \"java.util.HashMap.get(Object)" +
                "\" is null", exception.getMessage());
    }

    @Test
    void outputTaskByIdStandart() throws IOException, ManagerSaveException {
        Task task = new Task("name", "description", Status.DONE
                , 5, durationTask, startTimeTask);
        assertEquals(task, manager.outputTaskById(5));
    }

    @Test
    void outputTaskByIdEmpty() throws IOException, ManagerSaveException {
        manager.getDescriptionTasks().clear();
        assertNull(manager.getDescriptionTasks().get(5));
        final NullPointerException exception = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.getHistoryManager().add(manager.getDescriptionTasks().get(5));
            }
        });
        assertEquals("Cannot invoke \"logic.Task.getId()\" because \"task\" is null", exception.getMessage());
    }

    @Test
    void outputTaskByIdStandartError() throws IOException, ManagerSaveException {
        final NullPointerException exception = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.getHistoryManager().add(manager.getDescriptionTasks().get(2));
            }
        });
        assertEquals("Cannot invoke \"logic.Task.getId()\" because \"task\" is null", exception.getMessage());
    }

    @Test
    void outputSubTaskByIdStandart() throws IOException, ManagerSaveException {
        SubTask subTask = new SubTask("name", "description", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask);
        assertEquals(subTask, manager.outputSubTaskById(3));
    }

    @Test
    void outputSubTaskByIdEmpty() {
        manager.getDescriptionSubTasks().clear();
        final NullPointerException exception = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.getHistoryManager().add(manager.getDescriptionSubTasks().get(3));
            }
        });
        assertEquals("Cannot invoke \"logic.Task.getId()\" because \"task\" is null", exception.getMessage());
    }

    @Test
    void outputSubTaskByIdError() throws IOException, ManagerSaveException {
        final NullPointerException exception = assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                manager.getHistoryManager().add(manager.getDescriptionSubTasks().get(2));
            }
        });
        assertEquals("Cannot invoke \"logic.Task.getId()\" because \"task\" is null", exception.getMessage());
    }


    @Test
    void outputEpicByIdStandart() throws IOException, ManagerSaveException {
        SubTask subTask = new SubTask("name", "description", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask);
        assertEquals(subTask, manager.outputEpicById(1).getListSubTask().get(0));
    }

    @Test
    void outputEpicByIdEmpty() throws IOException, ManagerSaveException {
        manager.getDescriptionEpic().clear();
        final NullPointerException exception = assertThrows(
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getHistoryManager().add(manager.getDescriptionEpic().get(3));
                    }
                });
        assertEquals("Cannot invoke \"logic.Task.getId()\" because \"task\" is null", exception.getMessage());
    }

    @Test
    void outputEpicByIdError() throws IOException, ManagerSaveException {
        final NullPointerException exception = assertThrows(
                NullPointerException.class, new Executable() {
                    @Override
                    public void execute() throws Throwable {
                        manager.getHistoryManager().add(manager.getDescriptionEpic().get(3));
                    }
                });
        assertEquals("Cannot invoke \"logic.Task.getId()\" because \"task\" is null", exception.getMessage());
    }

    @Test
    void inputNewTaskStandart() throws IOException, ManagerSaveException {
        Task task = new Task("name", "description", Status.NEW
                , 0, durationTask, startTimeTask);
        manager.inputNewTask(task);
        manager.getDescriptionTasks().put(1, task);
        assertEquals(manager.outputTaskById(0), manager.outputTaskById(1));
    }

    @Test
    void inputNewEpic() throws IOException, ManagerSaveException {
        Epic epic = new Epic("name", "description", 0
                , startTimeEpic);
        manager.inputNewEpic(epic);
        manager.getDescriptionEpic().put(1, epic);
        assertEquals(manager.getDescriptionEpic().get(0), manager.getDescriptionEpic().get(1));
    }

    @Test
    void inputNewSubTask() throws IOException, ManagerSaveException {
        SubTask subTask = new SubTask("name", "description", Status.DONE
                , 3, 1, durationSubTask, startTimeSubTask);
        manager.inputNewSubTask(subTask, 1);
        assertEquals(new SubTask("name", "description", Status.DONE
                        , 3, 1, durationSubTask, startTimeSubTask)
                , manager.getDescriptionSubTasks().get(3));
    }

    @Test
    void updateTask() throws IOException, ManagerSaveException {
        Task task = new Task("name", "updateTask", Status.NEW
                , 6, durationTask, startTimeTask);
        manager.updateTask(task);
        assertEquals(task, manager.getDescriptionTasks().get(6));
    }

    @Test
    void updateSubTask() {
        SubTask subTask = new SubTask("name", "updateSubTask", Status.NEW
                , 3, 1, durationSubTask, startTimeSubTask);
        manager.updateSubTask(subTask);
        assertEquals(subTask, manager.getDescriptionSubTasks().get(3));
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("name", "updateEpic", 0
                , startTimeEpic);
        manager.updateEpic(epic);
        assertEquals(epic, manager.getDescriptionEpic().get(0));
    }

    @Test
    void deletTaskById() throws IOException, ManagerSaveException {
        manager.deletTaskById(5);
        assertNull(manager.getDescriptionTasks().get(5));
    }

    @Test
    void deletAllTasks() throws IOException, ManagerSaveException {
        manager.deletAllTasks();
        assertEquals(null, manager.getDescriptionEpic().get(1));
        assertNull(manager.getDescriptionTasks().get(0));
    }
}
