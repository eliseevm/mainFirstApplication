package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.util.ArrayList;
import java.util.HashMap;

public interface TaskManager {

    // Метод возвращает описание задачи
    HashMap<Integer, Task> getDescriptionTasks();

    // Метод возвращает описание подзадачи
    HashMap<Integer, SubTask> getDescriptionSubTasks();

    // Метод возвращает описание эпика
    HashMap<Integer, Epic> getDescriptionEpic();

    // Метод получает номер ID для задачи
    int getTaskId();

    // Метод возвращает все задачи
    HashMap<Integer, Task> outputAllTask();

    // Метод возвращает все эпики
    HashMap<Integer, Epic> outputAllEpics();

    // Метод возвращает подзадачи по эпику
    ArrayList<SubTask> outputSubtaskByEpik(int epicId);

    // Метод возвращает задачи по ID
    Task outputTaskById(int numberTask);

    // Метод возвращает подзадачи по ID
    SubTask outputSubTaskById(int numberTask);

    // Метод возвращает эпик по ID
    Epic outputEpicById(int numberTask);

    // Метод ввода новой задачи
    void inputNewTask(String name, String description, Status status);

    // Метод ввода нового эпика
    void inputNewEpic(String name, String description);

    // Метод ввода новой подзадачи
    void inputNewSubTask(String name, String description, Status status, int epicId);

    // Метод для обновления задач по номеру.
    void updateTask(Task task);

    // Метод для обновления подзадач по номеру.
    void updateSubTask(SubTask subTask);

    // Метод для обновления эпика по номеру.
    void updateEpic(Epic epic);

    void deletTaskById(int numberTask);

    // Метод удаления всех зад
    void deletAllTasks();
}