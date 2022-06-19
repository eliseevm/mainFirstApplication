package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface TaskManager {
    // Метод возвращает описание задачи
    HashMap<Integer, Task> getDescriptionTasks();

    // Метод возвращает описание подзадачи
    HashMap<Integer, SubTask> getDescriptionSubTasks();

    // Метод возвращает описание эпика
    HashMap<Integer, Epic> getDescriptionEpic();

    // Метод получает номер ID для задачи
    int getTaskId();

    // Метод возвращает историю просмотров
    List<Task> getHistory() throws IOException;

    Set<Task> getPrioritizedTasks();

    // Метод возвращает все задачи
    HashMap<Integer, Task> outputAllTask();

    // Метод возвращает все эпики
    HashMap<Integer, Epic> outputAllEpics();

    // Метод возвращает подзадачи по эпику
    ArrayList<SubTask> outputSubtaskByEpik(int epicId);

    // Метод возвращает задачи по ID
    Task outputTaskById(int numberTask) throws IOException, ManagerSaveException;

    // Метод возвращает подзадачи по ID
    SubTask outputSubTaskById(int numberTask) throws IOException, ManagerSaveException;

    // Метод возвращает эпик по ID
    Epic outputEpicById(int numberTask) throws IOException, ManagerSaveException;

    // Метод ввода новой задачи
    void inputNewTask(Task task) throws IOException;

    // Метод ввода нового эпика
    void inputNewEpic(Epic epic) throws IOException, ManagerSaveException;

    // Метод ввода новой подзадачи
    void inputNewSubTask(SubTask subTask) throws IOException, ManagerSaveException;

    // Метод для обновления задач по номеру.
    void updateTask(Task task);

    // Метод для обновления подзадач по номеру.
    void updateSubTask(SubTask subTask);

    // Метод для обновления эпика по номеру.
    void updateEpic(Epic epic);

    // Метод удаляет задачу по номеру
    void deleteTaskById(int numberTask) throws IOException, ManagerSaveException;

    // Метод удаления всех зад
    void deleteAllTasks() throws IOException, ManagerSaveException;

}