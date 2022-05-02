package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    private HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    private HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    private HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    private InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager(); // Создаем
    // новый объект класса
    private HashMap<Integer, Object> generalMap = new HashMap<>(); // Таблица для объединения задач
    // вскх типов в одгу коллекцию
    private List<Object> generalList = new ArrayList<>(); // Список в который перекладываем объекты
    // из generalMap для сортировки в Collection
    private int taskId = 0; // Cчетчик задач

    // Метод возвращает объединенный список задач
    public List<Object> getGeneralList() {
        return generalList;
    }

    // Метод возвращает объединенную таблицу задач
    public HashMap<Integer, Object> getGeneralMap() {
        return generalMap;
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
    public void getPrioritizedTasks() {
        generalMap.clear();
        generalList.clear();
        generalMap.putAll(descriptionTasks);
        generalMap.putAll(descriptionSubTasks);
        generalMap.putAll(descriptionEpic);
        for (Integer temp : generalMap.keySet()) {
            Object r = generalMap.get(temp);
            generalList.add(r);
        }
        Collections.sort(generalList, comparator);
        printPrioritizedTasks();
    }


    // Создаем анонимный класс с имплементацией Comparator и переопределяем метод compare
    Comparator comparator = new Comparator() {
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

    // Метод печати списка задач сортированного по времени начала
    public void printPrioritizedTasks() {
        for (Object t : generalList) {
            System.out.println(t.toString());
        }
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
        getPrioritizedTasks();
        if (timeСontrol(task)) {
            return;
        } else {
            System.out.println("Задание добавлено");
            int id = task.getId();
            descriptionTasks.put(id, task);
        }
    }

    // Метод ввода нового эпика
    @Override
    public void inputNewEpic(Epic epic) throws IOException, ManagerSaveException {
        getPrioritizedTasks();
        if (timeСontrol(epic)) {
            return;
        } else {
            System.out.println("Задание добавлено");
            int id = epic.getId();
            descriptionEpic.put(id, epic);
        }
    }

    // Метод ввода новой подзадачи
    @Override
    public void inputNewSubTask(SubTask subTask, int epicId) throws
            IOException, ManagerSaveException {
        getPrioritizedTasks();
        if (timeСontrol(subTask)) {
            return;
        } else {
            System.out.println("Задание добавлено");
            int id = subTask.getId();
            Epic epic = descriptionEpic.get(epicId);
            epic.getListSubTask().add(subTask);
            descriptionSubTasks.put(id, subTask);
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
        Long min = task.getDuration().getSeconds();
        LocalDateTime end = start.plusSeconds(min);
        if (!generalList.isEmpty()) {
            for (Object t : generalList) {
                Task temp = (Task) t;
                LocalDateTime endFromList = temp.getStartTime().plusSeconds(temp.getDuration()
                        .getSeconds());
                if (start.isAfter(endFromList) || end.isBefore(temp.getStartTime())) {
                } else {
                    System.out.println("Время уже занято!");
                    r = r + 1;
                }
            }
        } else {
        }
        if (r > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InMemoryTaskManager that = (InMemoryTaskManager) o;
        return taskId == that.taskId
                && descriptionTasks.equals(that.descriptionTasks)
                && descriptionSubTasks.equals(that.descriptionSubTasks)
                && descriptionEpic.equals(that.descriptionEpic)
                && inMemoryHistoryManager.equals(that.inMemoryHistoryManager)
                && generalList.equals(that.generalList)
                && generalMap.equals(that.generalMap) && comparator.equals(that.comparator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptionTasks, descriptionSubTasks, descriptionEpic
                , inMemoryHistoryManager, generalList, generalMap, taskId, comparator);
    }

    @Override
    public String toString() {
        return "InMemoryTaskManager{" +
                "descriptionTasks=" + descriptionTasks +
                ", descriptionSubTasks=" + descriptionSubTasks +
                ", descriptionEpic=" + descriptionEpic +
                ", inMemoryHistoryManager=" + inMemoryHistoryManager +
                ", generalList=" + generalList +
                ", generalMap=" + generalMap +
                ", taskId=" + taskId +
                ", comparator=" + comparator +
                '}';
    }
}
