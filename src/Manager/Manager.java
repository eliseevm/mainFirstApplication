package Manager;

import Logic.Epic;
import Logic.SubTask;
import Logic.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private static int idi = 0; // Инициализируем переменную для нумерации задач
    ArrayList<Task> tasks = new ArrayList<>(); // Список параметров задачи
    ArrayList<SubTask> subtasks = new ArrayList<>(); // Список параметров подзадачи
    ArrayList<Epic> epictasks = new ArrayList<>(); // Список параметров эпика
    HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    HashMap<Integer, ArrayList<SubTask>> subtaskInEpic = new HashMap<>(); /* Перечень подзадач по од
    ному эпику. В качестве ключа Имя эпика */

    int getIdi() {  // Метод для извлечения текущего порядкового номера с увеличением на 1
        idi = idi + 1;
        return idi;
    }

    void setIdi(int id) { // Метод для записи нового текущего значения id
        idi = id;
    }

    String updateTask(int inputId, int epicId, String name, String descriptionTask
            , String status) { // Метод для обновления задач по номеру.
        String update = " ";
        if (descriptionTasks.containsKey(inputId)) { // Проверка на наличие в таблице указанного №
            descriptionTasks.put(inputId, new Task(name, descriptionTask, status));
            update = "1"; // Возврпщает 1 для печати сообщения "Задача обновлена" в консоль
        } else if (descriptionSubTasks.containsKey(inputId)) {
            descriptionSubTasks.put(inputId, new SubTask(epicId, name, descriptionTask, status));
            update = "1"; // Возврпщает 1 для печати сообщения "Задача обновлена" в консоль
        } else if (descriptionEpic.containsKey(inputId)) {
            descriptionEpic.put(inputId, new Epic(name, descriptionTask
                    , getStatus(inputId)));
            update = "1"; // Возврпщает 1 для печати сообщения "Задача обновлена" в консоль
        } else {
            update = "0"; // Возврпщает 0 для печати сообщения "Такой задачи нет" в консоль
        }
        return update;
    }


    // Метод ввода новой задачи
    void inputNewTask(String name, String description, String status) {
        int id = getIdi();
        tasks.add(new Task(name, description, status));
        descriptionTasks.put(id, new Task(name, description, status));
        setIdi(id);
    }

    // Метод ввода нового эпика
    void inputNewEpic(String name, String description, String status) {
        int id = getIdi();
        epictasks.add(new Epic(name, description, status));
        descriptionEpic.put(id, new Epic(name, description, status));
        setIdi(id);
    }

    void inputNewSubTask(int epicId, String name, String description, String status) {
        int id = getIdi();
        subtasks.add(new SubTask(epicId, name, description, status));
        descriptionSubTasks.put(id, new SubTask(epicId, name, description, status));
        setIdi(id);
        subtaskInEpic.put(epicId, subtasks); // Привязываем перечень подзадач к эпику
        
    }

    Object outputTaskById(int numberTask) { // Метод вывода задач по номеру
        Object request;
        if (descriptionTasks.containsKey(numberTask)) {
            request = descriptionTasks.get(numberTask);
        } else if (descriptionSubTasks.containsKey(numberTask)) {
            request = descriptionSubTasks.get(numberTask);
        } else if (descriptionEpic.containsKey(numberTask)) {
            request = descriptionEpic.get(numberTask);
        } else {
            request = null;
        }
        return request;
    }

    Object outputAllTask() { // Метод вывода всех задач
        ArrayList<Object> temporaryList = new ArrayList<>();
        temporaryList.clear();
        Object listr;
        for (Integer keyt : descriptionTasks.keySet()) {
            listr = descriptionTasks.get(keyt);
            temporaryList.add(listr);
        }
        for (Integer keyt : descriptionEpic.keySet()) {
            listr = descriptionEpic.get(keyt);
            temporaryList.add(listr);
        }
        for (Integer keyt : descriptionSubTasks.keySet()) {
            listr = descriptionSubTasks.get(keyt);
            temporaryList.add(listr);
        }
        return temporaryList;
    }

    HashMap<Integer, Epic> outputAllEpics() { // Метод вывода всех эпиков
        return descriptionEpic;
    }

    ArrayList<SubTask> outputSubtaskByEpik(int epicId) { // Метод вывода подзадач по эпику
        ArrayList<SubTask> listr = null;
        if (subtaskInEpic.containsKey(epicId)) {
            listr = subtaskInEpic.get(epicId);
        }
        return listr;
    }

    String delitTaskById(int numberTask) { // Метод удаления задачи по номеру
        String remove = " ";
        if (descriptionTasks.containsKey(numberTask)) {
            descriptionTasks.remove(numberTask);
            remove = " 1 ";
        } else if (descriptionSubTasks.containsKey(numberTask)) {
            descriptionSubTasks.remove(numberTask);
            remove = " 1 ";
        } else if (descriptionEpic.containsKey(numberTask)) {
            descriptionEpic.remove(numberTask);
            subtaskInEpic.remove(numberTask);
            remove = " 1 ";
        } else {
            remove = " 0 ";
        }
        return remove;
    }

    void delitAllTasks() { // Метод удаления всех задач
        descriptionSubTasks.clear();
        descriptionTasks.clear();
        descriptionEpic.clear();
        subtaskInEpic.clear();
    }

    String getStatus(int id) { // Метод-установщик статуса эпика
        String indicator = " ";
        if (subtaskInEpic.containsKey(id)) {
            ArrayList<SubTask> listr = subtaskInEpic.get(id);
            int j = 0;
            for (int i = 0; i < listr.size(); i++) {
                SubTask subTask = listr.get(i);
                if ((subTask.status).equals("done")) {
                    j = j++;
                }
            }
            if (j == listr.size()) {
                indicator = "done";
            } else if (j == 0) {
                indicator = "new";
            } else {
                indicator = "in progress";
            }
        }
        return indicator;

    }

    static void printMenu() {
        System.out.println("1 - Внесите новую задачу.");
        System.out.println("2 - Обновить задачу");
        System.out.println("3 - Внесите эпик");
        System.out.println("4 - Вывести задачу по ID");
        System.out.println("5 - Вывести все задачи");
        System.out.println("6 - Вывести список эпиков");
        System.out.println("7 - Вывести подзадачи по эпику");
        System.out.println("8 - Удалить задачу по номеру");
        System.out.println("9 - Удалить все задачи");
        System.out.println("0 - Завершить работу приложения.");
    }
}


