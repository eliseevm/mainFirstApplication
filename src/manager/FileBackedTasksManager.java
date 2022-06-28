package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;
import service.ManagerSaveException;
import service.TaskEnum;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private File fileName; // Имя файла для сохранения состояния менеджера.
    private String url; // Адрес ресурса для сохранения состояния менеджера.
    private List<Task> historyList; // Список просмотренных задач.

    // Конструктор для саздания экземпляров FileBackedTasksManager.
    public FileBackedTasksManager(File fileName) {
        this.fileName = fileName;
    }

    // Конструктор для саздания экземпляров HttpTasksManager.
    public FileBackedTasksManager(String url) {
        this.url = url;
    }

    public List<Task> getHistoryList() {
        return historyList;
    }

    // Метод возвращает менеджер с восстановленными мз файла параметрами.
    public static FileBackedTasksManager loadFromFile(File file) throws IOException
            , ManagerSaveException {
        FileBackedTasksManager manager =
                new FileBackedTasksManager(new File(String.valueOf(file)));
        manager.fromString();
        return manager;
    }

    // Метод сохранения параметров менеджера в файл, перед завершением работы программы.
    public void save() throws ManagerSaveException {
        historyList = getHistoryManager().getHistory();
        try {
            FileWriter writer = new FileWriter(fileName); // Открываем поток для записи в файл
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                    "type", "name", "description", "status", "id"
                    , "duration", "startTime", "biEpic" + "\n"));
            for (Integer keyTask : getDescriptionTasks().keySet()) {
                String strTask = toStrings(getDescriptionTasks().get(keyTask)); // Таску в строку.
                String typ = TaskEnum.TASK.toString(); // Преобразуем Енум типа Таски в строку.
                String str = String.join(",", typ, strTask + "\n"); // Сборка
                // строки и добавление в нее поля "тип", сборка строки через символ ";".
                writer.write(str); // Записываем строку в файл.
            }
            for (Integer keyEpic : getDescriptionEpic().keySet()) {
                String strEpic = toStrings(getDescriptionEpic().get(keyEpic)); //  Таску в строку.
                String typ = TaskEnum.EPIC.toString(); // Преобразуем Енум типа Таски в строку.
                String str = String.join(",", typ, strEpic + "\n"); // Собираем
                // строку и добавляем в нее поле "тип", сборка строки через символ ";".
                writer.write(str); // Записываем строку в файл.
            }
            for (Integer keySubTask : getDescriptionSubTasks().keySet()) {
                String strSubTask = toStrings(getDescriptionSubTasks().get(keySubTask));
                String typ = TaskEnum.SUBTASK.toString();
                String epicId = String.valueOf(getDescriptionSubTasks().get(keySubTask).getEpicId());
                String str = String.join(",", typ, strSubTask, epicId + "\n");
                writer.write(str);
            }
            writer.write("" + "\n"); // Добавляем в файл пустую строку.

            String[] historyId = new String[historyList.size()]; // Объявляем массив записи истории.
            for (int i = 0; i < historyList.size(); i++) {
                String id = String.valueOf(historyList.get(i).getId()); // Приводим к типу String.
                historyId[i] = id;
            }
            String str = String.join(",", historyId); // Собираем историю просм-ов в строку.
            writer.write(str); // Записываем строку в файл.
            writer.close(); // Закрываем поток.
        } catch (IOException exception) { // Ловим исключение.
            throw new ManagerSaveException("Исключение при записи в файл");
        }
    }

    // Метод преобразования объектов задач в строку.
    String toStrings(Task task) {
        String str = "";
        try {
            String name = task.getName();
            String description = task.getDescription();
            String status = task.getStatus().toString();
            String id = String.valueOf(task.getId());
            String duration = String.valueOf(task.getDuration());
            String startTime = task.getStartTime().toString();
            str = String.join(",", name, description, status, id, duration, startTime);
        } catch (NullPointerException ex) {
            System.out.println("При записи состояния менеджера пока не " +
                    "установлены параметры продолжительности и окончания эпика!");
        }
        return str;
    }

    // Метод запускает восстановление менеджера из файла.
    public void startFromString() throws IOException, ManagerSaveException {
        fromString();
    }

    // Метод восстановления менеджера в состояние, которое было перед завершением работы программы.
    private void fromString() throws IOException, ManagerSaveException {
        Reader r = new FileReader(fileName); // Создаем поток для чтения файла.
        BufferedReader br = new BufferedReader(r); // Создаем буфер для строки.
        while (br.ready()) { // Запускаем цикл для построчного извлечения из буфера.
            String line = br.readLine(); // Извлекаем построчно.
            String[] split2 = line.split(";"); // Делим строку и раскладываем части строки.
            try {
                if (split2.length > 30) { // по ячейкам массива с разделителем ";".
                    throw new ManagerSaveException("Превышено количество элементов массива");
                }
            } catch (ManagerSaveException exception) {
                System.out.println(exception.getMessage());
            }
            for (String str : split2) { // Запускаем цикл для получения отдельных подстрок.
                String[] split1 = str.split(","); // Создаем массив для разбивки строки на
                // части для распределения по переменным.
                if (split1[0].equals("TASK")) {
                    taskFromString(split1);
                } else if (split1[0].equals("EPIC")) {
                    epicFromString(split1);
                } else if (split1[0].equals("SUBTASK")) {
                    subTaskFromString(split1);
                } else if (split1[0].equals("")) {
                } else if (split1[0].equals("type")) {
                } else {
                    historyFromString(split1);
                }
            }
        }
        br.close(); // Закрываем буфер обмена.
        getMaxId(); // Вызываем метод восстановления счетчика задач.
    }

    // Метод восстановления счетчика задач.
    private void getMaxId() {
        int maxId = 0;
        for (Integer key : getDescriptionEpic().keySet()) {
            int epicId = getDescriptionEpic().get(key).getId();
            if (epicId > maxId) {
                maxId = epicId;
            }
        }
        for (Integer key : getDescriptionTasks().keySet()) {
            int taskId = getDescriptionTasks().get(key).getId();
            if (taskId > maxId) {
                maxId = taskId;
            }
        }
        for (Integer key : getDescriptionSubTasks().keySet()) {
            int subTaskId = getDescriptionSubTasks().get(key).getId();
            if (subTaskId > maxId) {
                maxId = subTaskId;
            }
        }
        setTaskId(maxId);
    }

    // Метод восстановления задач.
    protected void taskFromString(String[] split) {
        String name = split[1];
        String deskription = split[2];
        Status status = Status.valueOf(split[3]);
        Integer idTask = Integer.parseInt(split[4]);
        int duration = Integer.parseInt(split[5]);
        LocalDateTime startTime = LocalDateTime.parse(split[6]);
        Task restoredTask = new Task(name, deskription, status, idTask, duration, startTime);
        getDescriptionTasks().put(idTask, restoredTask);
    }

    // Метод восстановления подзадач.
    protected void subTaskFromString(String[] split) {
        String name = split[1];
        String deskription = split[2];
        Status status = Status.valueOf(split[3]);
        Integer id = Integer.parseInt(split[4]);
        Integer epicId = Integer.parseInt(split[7]);
        int duration = Integer.parseInt(split[5]);
        LocalDateTime startTime = LocalDateTime.parse(split[6]);
        SubTask restoredSubTask = new SubTask(name, deskription, status, id, epicId
                , duration, startTime);
        Epic epic = getDescriptionEpic().get(epicId);
        epic.getListSubTask().add(restoredSubTask);
        getDescriptionSubTasks().put(id, restoredSubTask);
    }

    // Метод восстановления эпиков.
    protected void epicFromString(String[] split) {
        String name = split[1];
        String deskription = split[2];
        Integer id = Integer.parseInt(split[4]);
        Epic restoredEpic = new Epic(name, deskription, id);
        getDescriptionEpic().put(id, restoredEpic);
    }

    // Метод восстановления истории просмотра задач.
    protected void historyFromString(String[] split) {
        for (String tempId : split) {
            int id = Integer.parseInt(tempId);
            for (Integer tempKey : getDescriptionTasks().keySet()) {
                Task tempTask = getDescriptionTasks().get(tempKey);
                int finalId = tempTask.getId();

                if (finalId == id) {
                    getHistoryManager().add(getDescriptionTasks().get(tempKey));
                }
            }
            for (Integer tempTaskId : getDescriptionSubTasks().keySet()) {
                SubTask tempTask = getDescriptionSubTasks().get(tempTaskId);
                int finalId = tempTask.getId();
                if (finalId == id) {
                    getHistoryManager().add(getDescriptionSubTasks().get(tempTaskId));
                }
            }
            for (Integer tempTaskId : getDescriptionEpic().keySet()) {
                Epic tempTask = getDescriptionEpic().get(tempTaskId);
                int finalId = tempTask.getId();
                if (finalId == id) {
                    getHistoryManager().add(getDescriptionEpic().get(tempTaskId));
                }
            }
        }
    }

    // Метод возвращает задачи по ID.
    @Override
    public Task outputTaskById(int numberTask) throws ManagerSaveException {
        InMemoryHistoryManager r = getHistoryManager();
        HashMap<Integer, Task> ter = getDescriptionTasks();
        Task task = ter.get(numberTask);
        r.add(task);
        System.out.println(task);
        save();
        return getDescriptionTasks().get(numberTask);
    }

    // Метод возвращает подзадачи по ID.
    @Override
    public SubTask outputSubTaskById(int numberTask) throws ManagerSaveException {
        getHistoryManager().add(getDescriptionSubTasks().get(numberTask));
        save();
        return getDescriptionSubTasks().get(numberTask);
    }

    // Метод возвращает эпик по ID.
    @Override
    public Epic outputEpicById(int numberTask) throws ManagerSaveException {
        getHistoryManager().add(getDescriptionEpic().get(numberTask));
        save();
        return getDescriptionEpic().get(numberTask);
    }

    // Метод ввода новой задачи.
    @Override
    public void inputNewTask(Task task) {
        sorted();
        if (!timeСontrol(task)) {
            return;
        } else {
            int id = task.getId();
            getDescriptionTasks().put(id, task);
            try {
                save();
                sorted();
            } catch (ManagerSaveException ex) {
                ex.getMessage();
            }
        }
    }

    // Метод ввода нового эпика.
    @Override
    public void inputNewEpic(Epic epic) throws ManagerSaveException {
        sorted();
        if (!timeСontrol(epic)) {
            return;
        } else {
            int id = epic.getId();
            getDescriptionEpic().put(id, epic);
            save();
        }
    }

    // Метод ввода новой подзадачи.
    @Override
    public void inputNewSubTask(SubTask subTask) throws ManagerSaveException {
        sorted();
        if (!timeСontrol(subTask)) {
            return;
        } else {
            int id = subTask.getId();
            int epicId = subTask.getEpicId();
            Epic epic = getDescriptionEpic().get(epicId);
            epic.getListSubTask().add(subTask);
            getDescriptionSubTasks().put(id, subTask);
            epic.setStatus(epic.createStatus());
            epic.setDuration(epic.createDuration());
            epic.setStartTime(epic.createStartTime());
            epic.setEndTime(epic.createEndTime());
            try {
                save();
                sorted();
            } catch (ManagerSaveException ex) {
                ex.getMessage();
            }
        }
    }

    // Метод удаления задачи по номеру.
    @Override
    public void deleteTaskById(int numberTask) {
        HashMap<Integer, InMemoryHistoryManager.Node<Task>> tMap
                = getHistoryManager().getTempNodeMap();
        if (tMap.containsKey(numberTask)) {
            if (getDescriptionTasks().containsKey(numberTask)) {
                InMemoryHistoryManager.Node<Task> tempNode = tMap.get(numberTask);
                tMap.remove(numberTask);
                getHistoryManager().removeNode(tempNode);
                getDescriptionTasks().remove(numberTask);
            } else if (getDescriptionEpic().containsKey(numberTask)) {
                InMemoryHistoryManager.Node<Task> tempNode = tMap.get(numberTask);
                tMap.remove(numberTask);
                getHistoryManager().removeNode(tempNode);
                getDescriptionEpic().remove(numberTask);
            }
        }
        try {
            save();
        } catch (Exception ex) {
            System.out.println("Исключение в методе удаления задачи пономеру");
        }
        sorted();
    }

    // Метод удаления задач всех типов.
    @Override
    public void deleteAllTasks() throws ManagerSaveException {
        getDescriptionEpic().clear();
        getDescriptionTasks().clear();
        getDescriptionSubTasks().clear();
        getPrioritizedTasks().clear();
        save();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FileBackedTasksManager manager = (FileBackedTasksManager) o;
        return fileName.equals(manager.fileName)
                && getDescriptionTasks().equals(manager.getDescriptionTasks())
                && getDescriptionSubTasks().equals(manager.getDescriptionSubTasks())
                && getDescriptionEpic().equals(manager.getDescriptionEpic())
                && historyList.equals(manager.historyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileName, getDescriptionTasks()
                , getDescriptionSubTasks(), getDescriptionEpic(), historyList);
    }

}






