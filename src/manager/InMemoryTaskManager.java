package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    Comparator<Object> comparator = new Comparator<Object>() {
        @Override
        public int compare(Object o1, Object o2) {
            int w = 1;
            Task r = (Task) o1;
            Task e = (Task) o2;
            if (r.getStartTime().isBefore(e.getStartTime())) {
                w = -1;
            }
            return w;
        }
    };

    private HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    private HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    private HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    private InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager(); // Создаем
    private Set<Task> generalList = new TreeSet<Task>(comparator); // Список в который перекладываем объекты
    private HashMap<Integer, Object> generalMap = new HashMap<>(); /* Таблица для объединения задач
    вскх типов в одгу коллекцию из generalMap для сортировки в Collection */

    private int taskId = 0; // Счетчик задач

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

    // Метод возвращает перечень эпиков
    @Override
    public HashMap<Integer, Epic> getDescriptionEpic() {
        return descriptionEpic;
    }

    //  Метод возвращает объект с историей просмотра задач
    public InMemoryHistoryManager getHistoryManager() {
        return inMemoryHistoryManager;
    }

    // Метод получения номера для задачи
    @Override
    public int getTaskId() {
        return taskId++;
    }

    // Метод возвращает List - список истории просмотра, прлученный из двусвязного списка
    @Override
    public List<Task> getHistory() throws IOException {
        return inMemoryHistoryManager.getHistory();
    }

    // Метод сортирует задачи всех типов и вызывает метод печати отсортированного списка
    public Set<Task> sorted() {
        generalMap.clear();
        generalList.clear();
        generalMap.putAll(descriptionTasks);
        generalMap.putAll(descriptionSubTasks);
        generalMap.putAll(descriptionEpic);
        for (Integer temp : generalMap.keySet()) {
            Object r = generalMap.get(temp);
            generalList.add((Task) r);
        }
        return generalList;
    }

    public Set<Task> getPrioritizedTasks() {
        return generalList;
    }

    // Метод сохраняет новый id (восстанавливает максимальный id из файла)
    public void setTaskId(int taskId) {
        this.taskId = taskId;
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
    public Task outputTaskById(int numberTask) throws IOException, ManagerSaveException {
        inMemoryHistoryManager.add(descriptionTasks.get(numberTask));
        return descriptionTasks.get(numberTask);
    }

    // Метод возвращает подзадачи по ID
    @Override
    public SubTask outputSubTaskById(int numberTask) throws IOException, ManagerSaveException {
        inMemoryHistoryManager.add(descriptionSubTasks.get(numberTask));
        return descriptionSubTasks.get(numberTask);
    }

    // Метод возвращает эпик по ID
    @Override
    public Epic outputEpicById(int numberTask) throws IOException, ManagerSaveException {
        inMemoryHistoryManager.add(descriptionEpic.get(numberTask));
        return descriptionEpic.get(numberTask);
    }

    // Метод ввода новой задачи
    @Override
    public void inputNewTask(Task task) throws IOException, ManagerSaveException {
        sorted();
        boolean r = timeСontrol(task);
        if (r = false) {
            return;
        } else {
            int id = task.getId();
            getDescriptionTasks().put(id, task);
        }
    }

    // Метод ввода нового эпика
    @Override
    public void inputNewEpic(Epic epic) throws IOException, ManagerSaveException {
        sorted();
        boolean r = timeСontrol(epic);
        if (r = false) {
            return;
        } else {
            int id = epic.getId();
            descriptionEpic.put(id, epic);
        }
    }

    // Метод ввода новой подзадачи
    @Override
    public void inputNewSubTask(SubTask subTask, int epicId) throws
            IOException, ManagerSaveException {
        sorted();
        boolean r = timeСontrol(subTask);
        if (r = false) {
            return;
        } else {
            int id = subTask.getId();
            Epic epic = descriptionEpic.get(epicId);
            epic.getListSubTask().add(subTask);
            descriptionSubTasks.put(id, subTask);
        }
        sorted();
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
    public void deletTaskById(int numberTask) throws IOException, ManagerSaveException {
        HashMap<Integer, InMemoryHistoryManager.Node<Task>> tMap
                = inMemoryHistoryManager.getTempNodeMap();
        if (tMap.containsKey(numberTask)) {
            if (descriptionTasks.containsKey(numberTask)) {
                InMemoryHistoryManager.Node<Task> tempNode = tMap.get(numberTask);
                tMap.remove(numberTask);
                inMemoryHistoryManager.removeNode(tempNode);
                descriptionTasks.remove(numberTask);
            } else if (descriptionEpic.containsKey(numberTask)) {
                InMemoryHistoryManager.Node<Task> tempNode = tMap.get(numberTask);
                tMap.remove(numberTask);
                inMemoryHistoryManager.removeNode(tempNode);
                descriptionEpic.remove(numberTask);
            }
        }
    }

    // Метод удаления задач всех типов
    @Override
    public void deletAllTasks() throws IOException, ManagerSaveException {
        descriptionEpic.clear();
        descriptionTasks.clear();
        descriptionSubTasks.clear();
        generalList.clear();
        generalMap.clear();
    }

    // Метод проверки всех задач на пересечение по duration
    public boolean timeСontrol(Task task) {
        int r = 0; // Счетчик пересечений
        LocalDateTime start = task.getStartTime();
        LocalDateTime end = task.getEndTime();
        if (generalList.size() != 0) {
            for (Task t : generalList) {
                LocalDateTime endTimeFromList = t.getEndTime();
                LocalDateTime startTimeFromList = t.getStartTime();
                if (start.isAfter(endTimeFromList) || end.isBefore(startTimeFromList)) {
                } else {
                    System.out.println("Время для " + "уже занято!");
                    r = r + 1;
                }
            }
        }
        if (r > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return taskId == that.taskId && comparator.equals(that.comparator)
                && descriptionTasks.equals(that.descriptionTasks)
                && descriptionSubTasks.equals(that.descriptionSubTasks)
                && descriptionEpic.equals(that.descriptionEpic)
                && inMemoryHistoryManager.equals(that.inMemoryHistoryManager)
                && generalList.equals(that.generalList) && generalMap.equals(that.generalMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comparator, descriptionTasks, descriptionSubTasks, descriptionEpic
                , inMemoryHistoryManager, generalList, generalMap, taskId);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "comparator=" + comparator +
                ", descriptionTasks=" + descriptionTasks +
                ", descriptionSubTasks=" + descriptionSubTasks +
                ", descriptionEpic=" + descriptionEpic +
                ", inMemoryHistoryManager=" + inMemoryHistoryManager +
                ", generalList=" + generalList +
                ", generalMap=" + generalMap +
                ", taskId=" + taskId +
                '}';
    }
}