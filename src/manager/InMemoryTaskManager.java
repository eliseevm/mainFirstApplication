package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager, HistoryManager {

    private LinkedList<Task> test = new LinkedList<>();
    private List<Task> history = new ArrayList<>(); // История просмотра задач
    private HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    private HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    private HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    private int taskId = 0; // Cчетчик задач
    private int subTaskId = 0; // Счетчик подзадач
    private int epicId = 0; // Счетчик эпиков
    private Node<Task> head; // Голова двусвязного списка
    private Node<Task> tail; // Хвост двусвязного списка
    private int size = 0; // Размер двусвязного списка

    @Override
    public void add(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(int id) {
    }

    // Метод возвращает последние 10 задач
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    // Метод возвращает перечень задач
    @Override
    public HashMap<Integer, Task> getDescriptionTasks() {
        return descriptionTasks;
    }

    // Метод возвращает перечень подзадач
    @Override
    public HashMap<Integer, SubTask> getDescriptionSubTasks() {
        return descriptionSubTasks;
    }

    // Перечень возвращает перечень эпиков
    @Override
    public HashMap<Integer, Epic> getDescriptionEpic() {
        return descriptionEpic;
    }

    // Метод получения номера для задачи
    @Override
    public int getTaskId() {
        return taskId++;
    }

    // Метод получения номера для подзадачи
    @Override
    public int getSubTaskId() {
        return subTaskId++;
    }

    // Метод получения номера для эпика
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
        add(descriptionTasks.get(numberTask));
        return descriptionTasks.get(numberTask);
    }

    // Метод возвращает подзадачи по ID
    @Override
    public SubTask outputSubTaskById(int numberTask) {
        add(descriptionSubTasks.get(numberTask));
        return descriptionSubTasks.get(numberTask);
    }

    // Метод возвращает эпик по ID
    @Override
    public Epic outputEpicById(int numberTask) {
        add(descriptionEpic.get(numberTask));
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

    // Метод добавляет задачу в список "история просмотренных задач"
    private void addTaskInHistory(Task forAdd) {
        history.add(forAdd);
        if (history.size() > 10) {
            history.remove(0);
        }
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
        history.clear();
    }

    // Метод удаления задачи из истории просмотра задач
    private void deletTask(Task delete) {
        if (history.contains(delete)) {
            history.remove(delete);
        }
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


    public class Node<T> {

        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> oldHead, T element, Node<T> oldTail) {
            this.data = element;
            this.prev = oldTail;
            this.next = oldHead;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }
    }

    public void linkLast(Task element) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(null, element, oldTail);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else oldTail.next = newNode;
        size++;
    }

    // Метод получения размера двусвязного списка
    public int size() {
        return test.size();
    }

    // Метод получения задач из двусвязного списка
    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node<Task> element = head;
        while (element != null) {
            taskList.add(element.data);
            element = element.getNext();
        }
        return taskList;
    }
    public void removeNode(Task element) {

    }
}


