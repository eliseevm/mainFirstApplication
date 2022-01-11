package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    private HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    private HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    private HashMap<Integer, ArrayList<SubTask>> subtaskInEpic = new HashMap<>(); /* Перечень
    подзадач по одному эпику. В качестве ключа Имя эпика */


    public HashMap<Integer, SubTask> getDescriptionSubTasks() {
        return descriptionSubTasks;
    }

    // Метод возвращает все задачи
    public HashMap<Integer, Task> outputAllTask() {
        return descriptionTasks;
    }

    // Метод возвращает все эпики
    public HashMap<Integer, Epic> outputAllEpics() {
        return descriptionEpic;
    }

    // Метод возвращает подзадачи по эпику
    public ArrayList<SubTask> outputSubtaskByEpik(int epicId) {
        ArrayList<SubTask> temp = null;
        if (subtaskInEpic.containsKey(epicId)) {
            temp = subtaskInEpic.get(epicId);
        }
        return temp;
    }

    // Метод возвращает задачи по ID
    public Task outputTaskById(int numberTask) {
        Task request = null;
        if (descriptionTasks.containsKey(numberTask)) {
            request = descriptionTasks.get(numberTask);
        }
        return request;
    }

    // Метод возвращает подзадачи по ID
    public SubTask outputSubTaskById(int numberTask) {
        SubTask request = null;
        if (descriptionSubTasks.containsKey(numberTask)) {
            request = descriptionSubTasks.get(numberTask);
        }
        return request;
    }

    // Метод возвращает эпик по ID
    public Epic outputEpicById(int numberTask) {
        Epic request = null;
        if (descriptionEpic.containsKey(numberTask)) {
            descriptionEpic.get(numberTask);
            request = descriptionEpic.get(numberTask);
        }
        return request;
    }

    // Метод ввода новой задачи
    public void inputNewTask(String name, String description, String status) {
        int id = Task.getId();
        descriptionTasks.put(id, new Task(name, description, status));
        Task.setId(id + 1);
    }

    // Метод ввода нового эпика
    public void inputNewEpic(String name, String description) {
        int id = Epic.getId();
        descriptionEpic.put(id, new Epic(name, description));
        Epic.setId(id + 1);
    }
    public void createNewSubTask(String name, String description, String status, int epicId) {
        Epic.inputNewSubTask (name, description, status,epicId);
    }

    // Метод для обновления задач по номеру.
    public void updateTask(int id, String name, String description, String status) {
        if (descriptionTasks.containsKey(id)) { // Проверка на наличие в таблице указанного №
            descriptionTasks.put(id, new Task(name, description, status));
        }
    }

    // Метод для обновления подзадач по номеру.
    public void updateSubTask(int id, String name, String description, String status, int epicId) {
        if (descriptionSubTasks.containsKey(id)) {
            descriptionSubTasks.put(id, new SubTask(name, description, status, epicId));
            // Здесь как то надо инициировать проверку эпика на предмет изменения статуса
        }
    }

    // Метод для обновления эпика по номеру.
    public void updateEpic(String name, String description, int epicId) {
        if (descriptionEpic.containsKey(epicId)) {
            descriptionEpic.put(epicId, new Epic(name, description));
        }
    }


    // Метод удаления задачи по номеру
    public void deletTaskById(int numberTask) {
        if (descriptionTasks.containsKey(numberTask)) {
            descriptionTasks.remove(numberTask);
        }
    }

    public void deletAllTasks() { // Метод удаления всех задач
        descriptionTasks.clear();
    }
}


