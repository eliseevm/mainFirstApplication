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

    public HashMap<Integer, SubTask> getDescriptionSubTasks() {
        return descriptionSubTasks;
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
       return descriptionTasks.get(numberTask);
    }

    // Метод возвращает подзадачи по ID
    public SubTask outputSubTaskById(int numberTask) {
        return descriptionSubTasks.get(numberTask);
    }

    // Метод возвращает эпик по ID
    public Epic outputEpicById(int numberTask) {
        return descriptionEpic.get(numberTask);
    }

    // Метод ввода новой задачи
    public void inputNewTask(String name, String description, String status) {
        int id = getTaskId();
        Task tasc = new Task(name, description, status, id);
        descriptionTasks.put(id, tasc);
    }

    // Метод ввода нового эпика
    public void inputNewEpic(String name, String description) {
        System.out.println(name + description);
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
    }

    // Метод для обновления задач по номеру.
    public void updateTask(Task task) {
        int id = task.getId();
        descriptionTasks.put(id, task);
    }


    // Метод для обновления подзадач по номеру.
    public void updateSubTask(SubTask subTask) {
        SubTask oldSubTask;
        for (Integer id : descriptionEpic.keySet()) {
            if (subTask.getEpicId() == id) {
                Epic epic = descriptionEpic.get(id);
                for (int i = 0; i < epic.getListSubTask().size(); i++) {
                    oldSubTask = epic.getListSubTask().get(i);
                    if (subTask.getId() == oldSubTask.getId()) {
                        oldSubTask.setName(subTask.getName());
                        oldSubTask.setDescription(subTask.getDescription());
                    }
                }
            }
        }
    }

    // Метод для обновления эпика по номеру.
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        descriptionEpic.put(id, epic);
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


