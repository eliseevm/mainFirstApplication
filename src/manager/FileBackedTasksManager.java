package manager;

import logic.Epic;
import logic.SubTask;
import logic.Task;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    private File fileName;

    HashMap<Integer, Task> tempDescriptionTask = getDescriptionTasks();
    HashMap<Integer, SubTask> tempDescriptionSubTasks = getDescriptionSubTasks();
    HashMap<Integer, Epic> tempDescriptionEpic = getDescriptionEpic();
    List<Task> historyList;

    public FileBackedTasksManager(File fileNames) {

        fileName = fileNames;
    }

    // Метод сохранения параметров менеджера перед заверщением работы программы
    public void save() throws IOException {
        historyList = getHistoryManager().getHistory(); // Получаем историю просмотров
        FileWriter writer = new FileWriter(fileName); // Открылаем поток для записи в файл

        for (Integer keyTask : tempDescriptionTask.keySet()) {
            String strTask = toString(tempDescriptionTask.get(keyTask)); // Преобр. Таску в строку
            String typ = TaskEnum.TASK.toString(); // Преобразуем Енум типа Таски в строку
            String str = String.join(",", typ, strTask, ";"); // Собираем строку
            // и обавляем в нее поле "тип". Сборка строки через символ ;
            writer.write(str); // Записываем в файл
        }
        for (Integer keyEpic : tempDescriptionEpic.keySet()) {
            String strEpic = toString(tempDescriptionEpic.get(keyEpic)); // Преобр. Таску в строку
            String typ = TaskEnum.EPIC.toString(); // Преобразуем Енум типа Таски в строку
            String str = String.join(",", typ, strEpic, ";"); // Собираем строку
            // и обавляем в нее поле "тип". Сборка строки через символ ;
            writer.write(str); // Записываем в файл
        }
        for (Integer keySubTask : tempDescriptionSubTasks.keySet()) {
            String strSubTask = toString(tempDescriptionSubTasks.get(keySubTask));
            String typ = TaskEnum.SUBTASK.toString();
            String epicId = String.valueOf(tempDescriptionSubTasks.get(keySubTask).getEpicId());
            String str = String.join(",", typ, strSubTask, epicId, ";");
            writer.write(str);
        }
        writer.write("" + ";"); // Добавляем в файл пустую строку

        String[] historyId = new String[historyList.size()]; // Объявляем массив для записи истории
        for (int i = 0; i < historyList.size(); i++) {
            String id = String.valueOf(historyList.get(i).getId()); // Собираем номера задач для
            // записи в файл
            historyId[i] = id;
        }
        String historyToFile = String.join(",", historyId); // Формируем строку для записи
        writer.write(historyToFile);
        writer.close(); // Закрываем поток
    }

    // Метод преобразования задач в строку
    public String toString(Task task) {
        String id = String.valueOf(task.getId());
        String name = task.getName();
        String description = task.getDescription();
        String status = task.getStatus().toString();
        String str = String.join(",", id, name, status, description);
        return str;
    }

    // Метод восстоновления менеджера в состояние, которое было перед заверщением работы программы
    public void fromString() throws IOException {
        Reader r = new FileReader("src/history.csv"); // Создаем поток для чтения файла
        BufferedReader br = new BufferedReader(r); // Создаем буфер для строки
        while (br.ready()) { // Запускаем цикл для построчного извлечения из буфера
            String line = br.readLine(); // Извлекаем построчно
            String[] split2 = line.split(";"); // Делим строку и раскладываем части строки
            // по ячейкам массива с разделителем ;
            for (String str : split2) { // Запускаем цикл для получения отдельных подстрок
                String[] split1 = str.split(","); // Создаем массив для разбивки строки на
                // части для рпспределения по переменным
                if (split1[0].equals("TASK")) {
                    taskFromString(split1);
                } else if (split1[0].equals("EPIC")) {
                    epicFromString(split1);
                } else if (split1[0].equals("SUBTASK")) {
                    subTaskFromString(split1);
                } else if (split1[0].equals("")) {
                    System.out.println("Пробел");
                } else {
                    historyFromString(split1);
                }
            }
        }
        br.close(); // Закрываем буфер обмена
        getMaxId(); // Вызываем метод восстанавления счетчика задач
    }

    // Метод восстанавления счетчика задач
    public void getMaxId() {
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
    public void taskFromString(String[] split) {
        Integer idTask = Integer.parseInt(split[1]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String deskription = split[4];
        Task restoredTask = new Task(name, deskription, status, idTask);
        tempDescriptionTask.put(idTask, restoredTask);
    }

    // Метод восстановления подзадач
    public void subTaskFromString(String[] split) {
        Integer id = Integer.parseInt(split[1]);
        String name = split[2];
        Status status = Status.valueOf(split[3]);
        String deskription = split[4];
        Integer epicId = Integer.parseInt(split[5]);
        Epic tempEpic = tempDescriptionEpic.get(epicId);
        SubTask restoredSubTask = new SubTask(name, deskription, status, id, epicId);
        tempDescriptionSubTasks.put(id, restoredSubTask);
        List<SubTask> listByEpic = tempEpic.getListSubTask();
        listByEpic.add(restoredSubTask);
    }

    // Метод восстановления епика
    public void epicFromString(String[] split) {
        Integer idEpic = Integer.parseInt(split[1]);
        String name = split[2];
        String deskription = split[4];
        Epic restoredEpic = new Epic(name, deskription, idEpic);
        tempDescriptionEpic.put(idEpic, restoredEpic);
    }

    // Метод восстановления истории задач
    public void historyFromString(String[] split) {
        for (String tempId : split) {
            int id = Integer.parseInt(tempId);
            for (Integer tempTaskId : tempDescriptionTask.keySet()) {
                Task tempTask = tempDescriptionTask.get(tempTaskId);
                int finalId = tempTask.getId();
                if (finalId == id) {
                    getHistoryManager().add(tempDescriptionTask.get(tempTaskId));
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
    public Task outputTaskById(int numberTask) throws IOException {
        getHistoryManager().add(getDescriptionTasks().get(numberTask));
        save();
        return getDescriptionTasks().get(numberTask);
    }

    // Метод возвращает подзадачи по ID
    @Override
    public SubTask outputSubTaskById(int numberTask) throws IOException {
        getHistoryManager().add(getDescriptionSubTasks().get(numberTask));
        save();
        return getDescriptionSubTasks().get(numberTask);
    }

    // Метод возвращает эпик по ID
    @Override
    public Epic outputEpicById(int numberTask) throws IOException {
        getHistoryManager().add(getDescriptionEpic().get(numberTask));
        save();
        return getDescriptionEpic().get(numberTask);
    }

    // Метод ввода новой задачи
    @Override
    public void inputNewTask(String name, String description, Status status) throws IOException {
        int id = getTaskId();
        Task tasc = new Task(name, description, status, id);
        getDescriptionTasks().put(id, tasc);
        save();
    }

    // Метод ввода нового эпика
    @Override
    public void inputNewEpic(String name, String description) throws IOException {
        int id = getTaskId();
        Epic epic = new Epic(name, description, id);
        getDescriptionEpic().put(id, epic);
        save();
    }

    // Метод ввода новой подзадачи
    @Override
    public void inputNewSubTask(String name, String description, Status status, int epicId) throws IOException {
        int id = getTaskId();
        Epic epic = getDescriptionEpic().get(epicId);
        SubTask subTask = new SubTask(name, description, status, id, epicId);
        epic.getListSubTask().add(subTask);
        getDescriptionSubTasks().put(id, subTask);
        save();
    }
    // Метод удаления задачи по номеру
    @Override
    public void deletTaskById(int numberTask) throws IOException {
        HashMap<Integer, InMemoryHistoryManager.Node<Task>> tMap = getHistoryManager().getTempNodeMap();
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
    public void deletAllTasks() throws IOException {
        getDescriptionEpic().clear();
        getDescriptionTasks().clear();
        getDescriptionSubTasks().clear();
        save();
    }+++++++++++++++++++++++++++++++++++++++++++++++++++++
}






