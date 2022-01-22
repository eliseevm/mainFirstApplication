package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {

    private static List<Task> historis = new ArrayList<>(); // История просмотра задач
    private HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    private HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    private HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    private int taskId = 0;
    private int subTaskId = 0;
    private int epicId = 0;

    // Метод возвращает последние 10 задач
    public static List<Task> history() {
        return historis;
    }

    // Метод добавляет задачу в список "история"
    public static void addTaskInHistory(Task forAdd) {
        if (historis.size() < 10) {
            historis.add(forAdd);
        } else {
            historis.remove(0);
            historis.add(forAdd);
        }
    }
// Метод удаления задачи из истории просмотра задач
    public static  void deletTask(Task delete) {
        if (historis.contains(delete)) {
            historis.remove(delete);
        }
    }

    @Override
    public HashMap<Integer, Task> getDescriptionTasks() {
        return descriptionTasks;
    }

    @Override
    public HashMap<Integer, SubTask> getDescriptionSubTasks() {
        return descriptionSubTasks;
    }

    @Override
    public HashMap<Integer, Epic> getDescriptionEpic() {
        return descriptionEpic;
    }

    @Override
    public int getTaskId() {
        return taskId++;
    }

    @Override
    public int getSubTaskId() {
        return subTaskId++;
    }

    @Override
    public int getEpicId() {
        return epicId++;
    }

    // Метод возвращает все задачи
    @Override
    public HashMap<Integer, Task> outputAllTask() {
        return descriptionTasks;
    }

    // Метод возвращает все эпики
    @Override
    public HashMap<Integer, Epic> outputAllEpics() {
        return descriptionEpic;
    }

    // Метод возвращает подзадачи по эпику
    @Override
    public ArrayList<SubTask> outputSubtaskByEpik(int epicId) {
        Epic listEpics = descriptionEpic.get(epicId);
        return listEpics.getListSubTask();
    }


    // Метод возвращает задачи по ID
    @Override
    public Task outputTaskById(int numberTask) {
        Task temp = (descriptionTasks.get(numberTask));
        addTaskInHistory(temp);
        return descriptionTasks.get(numberTask);
    }

    // Метод возвращает подзадачи по ID
    @Override
    public SubTask outputSubTaskById(int numberTask) {
        addTaskInHistory(descriptionSubTasks.get(numberTask));
        return descriptionSubTasks.get(numberTask);
    }

    // Метод возвращает эпик по ID
    @Override
    public Epic outputEpicById(int numberTask) {
        addTaskInHistory(descriptionEpic.get(numberTask));
        return descriptionEpic.get(numberTask);
    }

    // Метод ввода новой задачи
    @Override
    public void inputNewTask(String name, String description, Status status) {
        int id = getTaskId();
        Task tasc = new Task(name, description, status, id);
        descriptionTasks.put(id, tasc);
    }

    // Метод ввода нового эпика
    @Override
    public void inputNewEpic(String name, String description) {
        int id = getEpicId();
        Epic epic = new Epic(name, description, id);
        descriptionEpic.put(id, epic);
    }

    // Метод ввода новой подзадачи
    @Override
    public void inputNewSubTask(String name, String description, Status status, int epicId) {
        int id = getSubTaskId();
        Epic epic = descriptionEpic.get(epicId);
        SubTask subTask = new SubTask(name, description, status, id, epicId);
        epic.getListSubTask().add(subTask);
        descriptionSubTasks.put(id, subTask);
    }

    // Метод для обновления задач по номеру.
    @Override
    public void updateTask(Task task) {
        int id = task.getId();
        descriptionTasks.put(id, task);
    }


    // Метод для обновления подзадач по номеру.
    @Override
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
    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        descriptionEpic.put(id, epic);
    }

    // Метод удаления задачи по номеру
    @Override
    public void deletTaskById(int numberTask) {
        if (descriptionTasks.containsKey(numberTask)) {
            descriptionTasks.remove(numberTask);
            deletTask(descriptionTasks.get(numberTask));
        }
    }

    // Метод удаления всех задач
    @Override
    public void deletAllTasks() {
        descriptionTasks.clear();
        historis.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return taskId == that.taskId && subTaskId == that.subTaskId && epicId == that.epicId
                && descriptionTasks.equals(that.descriptionTasks)
                && descriptionSubTasks.equals(that.descriptionSubTasks)
                && descriptionEpic.equals(that.descriptionEpic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptionTasks, descriptionSubTasks, descriptionEpic
                , taskId, subTaskId, epicId);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "descriptionTasks=" + descriptionTasks +
                ", descriptionSubTasks=" + descriptionSubTasks +
                ", descriptionEpic=" + descriptionEpic +
                ", taskId=" + taskId +
                ", subTaskId=" + subTaskId +
                ", epicId=" + epicId +
                '}';
    }
}


