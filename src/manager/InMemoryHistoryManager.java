package manager;

import logic.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node<Task>> tempNodeMap = new HashMap<>(); /* Вспомогательная таблица
    для извлечения из двусвязного списка */
    private LinkedList<Node<Task>> test = new LinkedList<>(); // Двусвязный список
    private Node<Task> head; // Голова двусвязного списка
    private Node<Task> tail; // Хвост двусвязного списка

    // Метод возвращает вспомогательную таблицу для учета просмотренных задач
    public HashMap<Integer, Node<Task>> getTempNodeMap() {
        return tempNodeMap;
    }

    /* Метод добавляет новый элемент и удаляет старый двойник элемента из всех коллекций хранящих
        историю просмотра задач */
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
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryHistoryManager that = (InMemoryHistoryManager) o;
        return tempNodeMap.equals(that.tempNodeMap) && test.equals(that.test)
                && head.equals(that.head) && tail.equals(that.tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tempNodeMap, test, head, tail);
    }

    @Override
    public String toString() {
        return "InMemoryHistoryManager{" +
                "tempNodeMap=" + tempNodeMap +
                ", test=" + test +
                ", head=" + head +
                ", tail=" + tail +
                '}';
    }
}
