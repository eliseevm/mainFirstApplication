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
    private int taskId = 0;
    private int subTaskId = 0;
    private int epicId = 0;

    public HashMap<Integer, Task> getDescriptionTasks() {
        return descriptionTasks;
    }

    public HashMap<Integer, Epic> getDescriptionEpic() {
        return descriptionEpic;
    }

    public int getTaskId() {
        return taskId++;
    }
    public int getSubTaskId() {
        return subTaskId++;
    }

    public int getEpicId() {
        return epicId++;
    }


    // Метод возвращает все подзадачи
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
        Epic listEpics = descriptionEpic.get(epicId);
        return listEpics.getListSubTask();
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
        int id = getTaskId();
        Task tasc = new Task(name, description, status, id);
        descriptionTasks.put(id, tasc);
    }

    // Метод ввода нового эпика
    public void inputNewEpic(String name, String description) {
        int id = getEpicId();
        Epic epic = new Epic(name, description, id);
        descriptionEpic.put(id, epic);
    }

    // Метод ввода новой подзадачи
    public void inputNewSubTask(String name, String description, String status, int epicId) {
        int id = getSubTaskId();
        Epic epic = descriptionEpic.get(epicId);
        SubTask subTask = new SubTask(name, description, status, id, epicId);
        epic.getListSubTask().add(subTask);
        descriptionSubTasks.put(id, subTask);

        epic.getStatus();
    }

    // Метод для обновления задач по номеру.
    public void updateTask(int id, String name, String description, String status) {
        if (descriptionTasks.containsKey(id)) { // Проверка на наличие в таблице указанного №
            descriptionTasks.put(id, new Task(name, description, status, id));
        }
    }

    // Метод для обновления подзадач по номеру.
    public void updateSubTask(SubTask subTask) {
        SubTask oldSubTask;
        for (Integer id : descriptionEpic.keySet()) {
            if (subTask.epicId == id) {
                Epic epic = descriptionEpic.get(id);
                for (int i = 0; i < epic.getListSubTask().size(); i++) {
                    oldSubTask = epic.getListSubTask().get(i);
                    if (subTask.id == oldSubTask.id) {
                        oldSubTask.name = subTask.name;
                        oldSubTask.description = subTask.description;
                    }
                }
            }
        }
    }

    // Метод для обновления эпика по номеру.
    public void updateEpic(String name, String description, int epicId) {
        if (descriptionEpic.containsKey(epicId)) {
            Epic epic = descriptionEpic.get(epicId);
            epic.name = name;
            epic.description = description;
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


