package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager, HistoryManager {

    private LinkedList<Node<Task>> test = new LinkedList<>(); // Двусвязный список
    private HashMap<Integer, Node<Task>> tempNodeMap = new HashMap<>(); /* Вспомогательная таблица
    для извлечения из двусвязного списка */
    private HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    private HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    private HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    private int taskId = 0; // Cчетчик задач
    private Node<Task> head; // Голова двусвязного списка
    private Node<Task> tail; // Хвост двусвязного списка
    private int size = 0; // Размер двусвязного списка

    /* Метод добавляет новый и удаляет старый двойник элемента из всех коллекций хранящих историю
    просмотра задач */
    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (tempNodeMap.containsKey(taskId)) {
            Node<Task> tempNode = tempNodeMap.get(taskId);
            remove(taskId);
            removeNode(tempNode);
            linkLast(task);
            Node<Task> tempNode1 = this.tail;
            tempNodeMap.put(taskId, tempNode1);
        } else {
            linkLast(task);
            Node<Task> tempNode = tail;
            tempNodeMap.put(taskId, tempNode);
        }
    }

    // Метод удаления объекта из вспомогательного списка истории просмотра
    @Override
    public void remove(int id) {
        tempNodeMap.remove(id);
    }

    // Метод возвращает перечень просмотренных задач
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
        int id = getTaskId();
        Epic epic = new Epic(name, description, id);
        descriptionEpic.put(id, epic);
    }

    // Метод ввода новой подзадачи
    @Override
    public void inputNewSubTask(String name, String description, Status status, int epicId) {
        int id = getTaskId();
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
        if (tempNodeMap.containsKey(numberTask)) {
            if (descriptionTasks.containsKey(numberTask)) {
                Node<Task> tempNode = tempNodeMap.get(numberTask);
                remove(numberTask);
                removeNode(tempNode);
                descriptionTasks.remove(numberTask);
            } else if (descriptionEpic.containsKey(numberTask)) {
                Node<Task> tempNode = tempNodeMap.get(numberTask);
                remove(numberTask);
                removeNode(tempNode);
                descriptionEpic.remove(numberTask);
            }
        }
    }

    // Метод удаления всех задач
    @Override
    public void deletAllTasks() {
        for (Integer id : descriptionTasks.keySet()) {
            deletTaskById(id);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return taskId == that.taskId && size == that.size && test.equals(that.test)
                && tempNodeMap.equals(that.tempNodeMap)
                && descriptionTasks.equals(that.descriptionTasks)
                && descriptionSubTasks.equals(that.descriptionSubTasks)
                && descriptionEpic.equals(that.descriptionEpic)
                && head.equals(that.head) && tail.equals(that.tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(test, tempNodeMap, descriptionTasks, descriptionSubTasks
                , descriptionEpic, taskId, head, tail, size);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "test=" + test +
                ", tempNodeMap=" + tempNodeMap +
                ", descriptionTasks=" + descriptionTasks +
                ", descriptionSubTasks=" + descriptionSubTasks +
                ", descriptionEpic=" + descriptionEpic +
                ", taskId=" + taskId +
                ", head=" + head +
                ", tail=" + tail +
                ", size=" + size +
                '}';
    }

    // Класс реализации объектов для двусвязного списка
    public class Node<T> {

        public T data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> newNext, T element, Node<T> newPrev) {
            this.data = element;
            this.prev = newPrev;
            this.next = newNext;
        }
    }

    // Метод добавления узла в конец двусвязного списока
    public void linkLast(Task element) {
        final Node<Task> oldTail = this.tail;
        final Node<Task> newNode = new Node<>(null, element, oldTail);
        this.tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
    }

    // Метод получения всех узлов из двусвязного списка
    public List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node<Task> element = this.head;
        while (element != null) {
            taskList.add(element.data);
            element = element.next;
        }
        return taskList;
    }

    // Метод удаления узла из двусвязного списка
    public void removeNode(Node<Task> element) {
        final Node<Task> next = element.next;
        final Node<Task> prev = element.prev;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            element.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            element.next = null;
        }

        element.data = null;
        size--;
    }
}





