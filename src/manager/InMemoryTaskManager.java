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
    private int k = 0;

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

    // Метод возвращает саисок отсортированный по дате начала
    public Set<Task> getPrioritizedTasks() {
        return generalList;
    }


    // Метод сортирует задачи всех типов и вызывает метод печати отсортированного списка
    public Set<Task> sorted() {
        generalMap.clear();
        generalList.clear();
        generalMap.putAll(descriptionTasks);
        generalMap.putAll(descriptionSubTasks);
        for (Integer temp : generalMap.keySet()) {
            Object r = generalMap.get(temp);
            generalList.add((Task) r);
        }
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
        Epic epic = null;
        if (descriptionEpic.get(epicId) == null) {
            System.out.println("Не могу показать подзадачу, такого эпика не существует!");
        } else if (descriptionEpic.get(epicId) != null) {
            epic = descriptionEpic.get(epicId);
    } return epic.getListSubTask();
    }

    // Метод возвращает задачи по ID
    @Override
    public Task outputTaskById(int numberTask) throws IOException, ManagerSaveException {
        Task task = null;
        if (descriptionTasks.get(numberTask) == null) {
            System.out.println("Задачи с таким номером не найдено!");
        } else if (descriptionTasks.get(numberTask) != null) {
            try {
                inMemoryHistoryManager.add(descriptionTasks.get(numberTask));
            } catch (NullPointerException ex) {
                System.out.println("Не могу логировать просмотр, такой задачи не существует!");
            }
            task = descriptionTasks.get(numberTask);
        }
        return task;
    }

    // Метод возвращает подзадачи по ID
    @Override
    public SubTask outputSubTaskById(int numberTask) throws IOException, ManagerSaveException {
        try {
            inMemoryHistoryManager.add(descriptionSubTasks.get(numberTask));
        } catch (NullPointerException ex) {
            System.out.println("Не могу логировать просмотр, такой задачи не существует!");
        }
        return descriptionSubTasks.get(numberTask);
    }

    // Метод возвращает эпик по ID
    @Override
    public Epic outputEpicById(int numberTask) throws IOException, ManagerSaveException {
       try {
           inMemoryHistoryManager.add(descriptionEpic.get(numberTask));
       } catch (NullPointerException ex) {
        System.out.println("Не могу логировать просмотр, такого эпика не существует!");
    }
        return descriptionEpic.get(numberTask);
    }

    // Метод ввода новой задачи
    @Override
    public void inputNewTask(Task task) {
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
    public void inputNewSubTask(SubTask subTask) {
        sorted();
        boolean r = timeСontrol(subTask);
        if (r = false) {
           return;
       } else {
            try {
                int id = subTask.getId();
                int epicId = subTask.getEpicId();
                Epic epic = descriptionEpic.get(epicId);
                epic.getListSubTask().add(subTask);
                descriptionSubTasks.put(id, subTask);
                Epic control = epic;
                descriptionEpic.put(epicId, control);
            } catch (NullPointerException ex) {
                System.out.println("Для выбранного id задачи не создано, введите другой id!");
            }
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
    public void deleteTaskById(int numberTask) {
            if (descriptionTasks.containsKey(numberTask)) {
                descriptionTasks.remove(numberTask);
            } else if (descriptionEpic.containsKey(numberTask)) {
                descriptionEpic.remove(numberTask);
            }
            sorted();
        }



    // Метод удаления задач всех типов
    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        descriptionEpic.clear();
        descriptionTasks.clear();
        descriptionSubTasks.clear();
        generalList.clear();
        sorted();
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
                try {
                    if (start.isAfter(endTimeFromList) || end.isBefore(startTimeFromList)) {
                    } else {
                        System.out.println("Время для " + task + "уже занято!");
                        r = r + 1;
                    }
                } catch (NullPointerException ex) {
                    System.out.println("Подзадача не создана, время определится позже!");
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