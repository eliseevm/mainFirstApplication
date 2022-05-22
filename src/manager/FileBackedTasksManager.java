package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private File fileName;

    public FileBackedTasksManager(File fileName) {

        this.fileName = fileName;
    }

    private final HashMap<Integer, Task> tempDescriptionTask = getDescriptionTasks();
    private final HashMap<Integer, SubTask> tempDescriptionSubTasks = getDescriptionSubTasks();
    private final HashMap<Integer, Epic> tempDescriptionEpic = getDescriptionEpic();
    private List<Task> historyList;
    

    // Метод возвращает менеджер с восстановленными мз файла параметрами
    public static FileBackedTasksManager loadFromFile(File file) throws IOException
            , ManagerSaveException {
        FileBackedTasksManager manager =
                new FileBackedTasksManager(new File(String.valueOf(file)));
        manager.fromString();
        return manager;
    }

    // Метод сохранения параметров менеджера перед завершением работы программы
    public void save() throws ManagerSaveException {
        historyList = getHistoryManager().getHistory();
        try {
            FileWriter writer = new FileWriter(fileName); // Открываем поток для записи в файл
            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                    "type", "name", "description", "status", "id"
                    , "duration", "startTime", "biEpic" + "\n"));
            for (Integer keyTask : tempDescriptionTask.keySet()) {
                String strTask = toString(tempDescriptionTask.get(keyTask)); // Таску в строку
                String typ = TaskEnum.TASK.toString(); // Преобразуем Енум типа Таски в строку
                String str = String.join(",", typ, strTask + "\n"); // Сборка
                // строки и добавление в нее поля "тип". Сборка строки через символ ;
                writer.write(str); // Записываем в файл
            }
            for (Integer keyEpic : tempDescriptionEpic.keySet()) {
                String strEpic = toString(tempDescriptionEpic.get(keyEpic)); //  Таску в строку
                String typ = TaskEnum.EPIC.toString(); // Преобразуем Енум типа Таски в строку
                String str = String.join(",", typ, strEpic + "\n"); // Собор строки
                // и добавляем в нее поле "тип". Сборка строки через символ ;
                writer.write(str); // Записываем в файл
            }
            for (Integer keySubTask : tempDescriptionSubTasks.keySet()) {
                String strSubTask = toString(tempDescriptionSubTasks.get(keySubTask));
                String typ = TaskEnum.SUBTASK.toString();
                String epicId = String.valueOf(tempDescriptionSubTasks.get(keySubTask).getEpicId());
                String str = String.join(",", typ, strSubTask, epicId + "\n");
                writer.write(str);
            }
            writer.write("" + "\n"); // Добавляем в файл пустую строку

            String[] historyId = new String[historyList.size()]; // Объявляем массив записи истории
            for (int i = 0; i < historyList.size(); i++) {
                String id = String.valueOf(historyList.get(i).getId()); // Собираем номера задач для
                // записи в файл
                historyId[i] = id;
            }
            String str = String.join(",", historyId);
            writer.write(str);
            writer.close(); // Закрываем поток
        } catch (IOException exception) { // Ловим исключение
            throw new ManagerSaveException("Сообщение об исключении");
        }
    }

    // Метод преобразования задач в строку
    private String toString(Task task) {
        String name = task.getName();
        String description = task.getDescription();
        String status = task.getStatus().toString();
        String id = String.valueOf(task.getId());
        String duration = task.getDuration().toString();
        String startTime = task.getStartTime().toString();
        String str = String.join(",", name, description, status, id, duration, startTime);
        return str;
    }
    public void startFromString() throws IOException, ManagerSaveException {
        fromString();
    }

    // Метод восстановления менеджера в состояние, которое было перед завершением работы программы
    private void fromString() throws IOException, ManagerSaveException {
        Reader r = new FileReader(fileName); // Создаем поток для чтения файла
        BufferedReader br = new BufferedReader(r); // Создаем буфер для строки
        while (br.ready()) { // Запускаем цикл для построчного извлечения из буфера
            String line = br.readLine(); // Извлекаем построчно
            String[] split2 = line.split(";"); // Делим строку и раскладываем части строки
            try {
                if (split2.length > 30) { // по ячейкам массива с разделителем ;
                    throw new ManagerSaveException("Превышено количество элементов массива");
                }
            } catch (ManagerSaveException exception) {
                System.out.println(exception.getMessage());
            }
            for (String str : split2) { // Запускаем цикл для получения отдельных подстрок
                String[] split1 = str.split(","); // Создаем массив для разбивки строки на
                // части для распределения по переменным
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
        br.close(); // Закрываем буфер обмена
        getMaxId(); // Вызываем метод восстановления счетчика задач
    }

    // Метод восстановления счетчика задач
    private void getMaxId() {
        int maxId = 0;
        for (Integer key : tempDescriptionEpic.keySet()) {
            int epicId = tempDescriptionEpic.get(key).getId();
            if (epicId > maxId) {
                maxId = epicId;
            }
        }
        for (Integer key : tempDescriptionTask.keySet()) {
            int taskId = tempDescriptionTask.get(key).getId();
            if (taskId > maxId) {
                maxId = taskId;
            }
        }
        for (Integer key : tempDescriptionSubTasks.keySet()) {
            int subTaskId = tempDescriptionSubTasks.get(key).getId();
            if (subTaskId > maxId) {
                maxId = subTaskId;
            }
        }
        setTaskId(maxId);
    }

    // Метод восстановления задач
    private void taskFromString(String[] split) throws ManagerSaveException {

        String name = split[1];
        String deskription = split[2];
        Status status = Status.valueOf(split[3]);
        Integer idTask = Integer.parseInt(split[4]);
        Duration duration = Duration.parse(split[5]);
        LocalDateTime startTime = LocalDateTime.parse(split[6]);
        Task restoredTask = new Task(name, deskription, status, idTask, duration, startTime);
        tempDescriptionTask.put(idTask, restoredTask);
    }

    // Метод восстановления подзадач
    private void subTaskFromString(String[] split) throws ManagerSaveException {
        String name = split[1];
        String deskription = split[2];
        Status status = Status.valueOf(split[3]);
        Integer id = Integer.parseInt(split[4]);
        Integer epicId = Integer.parseInt(split[7]);
        Duration duration = Duration.parse(split[5]);
        LocalDateTime startTime = LocalDateTime.parse(split[6]);
        SubTask restoredSubTask = new SubTask(name, deskription, status, id, epicId
                , duration, startTime);
        Epic epic = getDescriptionEpic().get(epicId);
        epic.getListSubTask().add(restoredSubTask);
        getDescriptionSubTasks().put(id, restoredSubTask);
    }

    // Метод восстановления эпика
    private void epicFromString(String[] split) throws ManagerSaveException {
        String name = split[1];
        String deskription = split[2];
        Integer id = Integer.parseInt(split[4]);
        Epic restoredEpic = new Epic(name, deskription, id);
        tempDescriptionEpic.put(id, restoredEpic);
    }

    // Метод восстановления истории задач
    private void historyFromString(String[] split) {
        for (String tempId : split) {
            int id = Integer.parseInt(tempId);
            for (Integer tempKey : tempDescriptionTask.keySet()) {
                Task tempTask = tempDescriptionTask.get(tempKey);
                int finalId = tempTask.getId();
                if (finalId == id) {
                    getHistoryManager().add(tempDescriptionTask.get(tempKey));
                }
            }
            for (Integer tempTaskId : tempDescriptionSubTasks.keySet()) {
                SubTask tempTask = tempDescriptionSubTasks.get(tempTaskId);
                int finalId = tempTask.getId();
                if (finalId == id) {
                    getHistoryManager().add(tempDescriptionSubTasks.get(tempTaskId));
                }
            }
            for (Integer tempTaskId : tempDescriptionEpic.keySet()) {
                Epic tempTask = tempDescriptionEpic.get(tempTaskId);
                int finalId = tempTask.getId();
                if (finalId == id) {
                    getHistoryManager().add(tempDescriptionEpic.get(tempTaskId));
                }
            }
        }
    }

    // Метод возвращает задачи по ID
    @Override
    public Task outputTaskById(int numberTask) throws ManagerSaveException {
        getHistoryManager().add(getDescriptionTasks().get(numberTask));
        save();
        return getDescriptionTasks().get(numberTask);
    }

    // Метод возвращает подзадачи по ID
    @Override
    public SubTask outputSubTaskById(int numberTask) throws ManagerSaveException {
        getHistoryManager().add(getDescriptionSubTasks().get(numberTask));
        save();
        return getDescriptionSubTasks().get(numberTask);
    }

    // Метод возвращает эпик по ID
    @Override
    public Epic outputEpicById(int numberTask) throws ManagerSaveException {
        getHistoryManager().add(getDescriptionEpic().get(numberTask));
        save();
        return getDescriptionEpic().get(numberTask);
    }

    // Метод ввода новой задачи
    @Override
    public void inputNewTask(Task task) throws ManagerSaveException {
        sorted();
        if (!timeСontrol(task)) {
            return;
        } else {
            int id = task.getId();
            getDescriptionTasks().put(id, task);
            save();
        }
    }

    // Метод ввода нового эпика
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

    // Метод ввода новой подзадачи
    @Override
    public void inputNewSubTask(SubTask subTask, int epicId) throws ManagerSaveException {
        sorted();
        if (!timeСontrol(subTask)) {
            return;
        } else {
            int id = subTask.getId();
            Epic epic = getDescriptionEpic().get(epicId);
            epic.getListSubTask().add(subTask);
            getDescriptionSubTasks().put(id, subTask);
            save();
            sorted();
        }
    }

    // Метод удаления задачи по номеру
    @Override
    public void deletTaskById(int numberTask) throws ManagerSaveException {
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
        save();
    }

    // Метод удаления задач всех типов
    @Override
    public void deletAllTasks() throws ManagerSaveException {
        getDescriptionEpic().clear();
        getDescriptionTasks().clear();
        getDescriptionSubTasks().clear();
        save();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FileBackedTasksManager manager = (FileBackedTasksManager) o;
        return fileName.equals(manager.fileName)
                && tempDescriptionTask.equals(manager.tempDescriptionTask)
                && tempDescriptionSubTasks.equals(manager.tempDescriptionSubTasks)
                && tempDescriptionEpic.equals(manager.tempDescriptionEpic)
                && historyList.equals(manager.historyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fileName, tempDescriptionTask
                , tempDescriptionSubTasks, tempDescriptionEpic, historyList);
    }

    @Override
    public String toString() {
        return "FileBackedTasksManager{" +
                "fileName=" + fileName +
                ", tempDescriptionTask=" + tempDescriptionTask +
                ", tempDescriptionSubTasks=" + tempDescriptionSubTasks +
                ", tempDescriptionEpic=" + tempDescriptionEpic +
                ", historyList=" + historyList +
                '}';
    }
}






