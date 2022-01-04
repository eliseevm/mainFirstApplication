import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Manager {

    private static int idi = 0; // Инициализируем переменную для нумерации задач
    ArrayList<Task> tasks = new ArrayList<>(); // Список параметров задачи
    ArrayList<SubTask> subtasks = new ArrayList<>(); // Список параметров подзадачи
    ArrayList<Epic> epictasks = new ArrayList<>(); // Список параметров эпика
    HashMap<Integer, Task> descriptionTasks = new HashMap<>(); // Перечень задач
    HashMap<Integer, SubTask> descriptionSubTasks = new HashMap<>(); // Перечень подзадач
    HashMap<Integer, Epic> descriptionEpic = new HashMap<>(); // Перечень эпиков
    HashMap<String, ArrayList<SubTask>> subtaskInEpic = new HashMap<>(); /* Перечень подзадач по од-
    ному эпику. В качестве ключа Имя эпика */


    int getId() {  // Метод для извлечения текущего порядкового номера с увеличением на 1
        idi = idi + 1;
        return idi;
    }

    void setId(int id) { // Метод для записи нового текущего значения id
        idi = id;
    }

    void updateTask(int inputId) { // Метод для обновления задач по номеру.
        Scanner scanner = new Scanner(System.in);
        if (descriptionTasks.containsKey(inputId)) { // Проверка на наличие в таблице указанного №
            Task oldTask = descriptionTasks.get(inputId);
            System.out.println("Введите новое название задачи");
            oldTask.name = scanner.nextLine();
            System.out.println("Введите новое описание задачи");
            oldTask.descriptionTask = scanner.nextLine();
            System.out.println("Введите новый статус задачи");
            oldTask.status = scanner.nextLine();
        } else if (descriptionSubTasks.containsKey(inputId)) {
            SubTask oldTask = descriptionSubTasks.get(inputId);
            System.out.println("Введите новое название задачи");
            oldTask.name = scanner.nextLine();
            System.out.println("Введите новое описание задачи");
            oldTask.descriptionSubTask = scanner.nextLine();
            System.out.println("Введите новый статус задачи");
            oldTask.status = scanner.nextLine();
        } else if (descriptionEpic.containsKey(inputId)) {
            Epic oldEpic = descriptionEpic.get(inputId);
            System.out.println("Введите новое название задачи");
            oldEpic.epicName = scanner.nextLine();
            System.out.println("Введите новое описание задачи");
            oldEpic.descriptionEpic = scanner.nextLine();
            System.out.println("Введите новый статус задачи");
            if (installStatus(inputId)) { // Вызов метода-установщика статуса эпика
                oldEpic.status = "done";
            } else {
                oldEpic.status = "in progress";
            }
        } else System.out.println("Такой задачи нет");
    }

    // Метод ввода новой задачи
    void inputNewTask(String name, String descriptionTask, String status) {
        int id = getId();
        tasks.add(new Task(name, descriptionTask, status));
        descriptionTasks.put(id, new Task(name, descriptionTask, status));
        setId(id);
    }

    // Метод ввода нового эпика
    void inputNewEpic(String epicName, String descriptionTaskEpic, String status) {
        Scanner scanner = new Scanner(System.in);
        int id = getId();
        epictasks.add(new Epic(epicName, descriptionTaskEpic, status));
        descriptionEpic.put(id, new Epic(epicName, descriptionTaskEpic, status));
        setId(id);
        System.out.println("Ввод подзадач");
        while (true) {
            System.out.println("Нажмите 0 для возврата в основное меню или ");
            System.out.println("Введите название подзадачи");
            String name = scanner.nextLine();
            id = getId();
            if (name.equals("0")) {
                break;
            }
            System.out.println("Введите описание подзадачи");
            String descriptionTask = scanner.nextLine();
            subtasks.add(new SubTask(name, descriptionTask, status));
            descriptionSubTasks.put(id, new SubTask(name, descriptionTask, status));
            setId(id); // Создаём перечень подзадач
        }
        subtaskInEpic.put(epicName, subtasks); // Привязываем перечень подзадач к эпику
    }

    void outputTaskById(int numberTask) { // Метод вывода задач по номеру
        if (descriptionTasks.containsKey(numberTask)) {
            System.out.println(descriptionTasks.get(numberTask));
        } else if (descriptionSubTasks.containsKey(numberTask)) {
            System.out.println(descriptionSubTasks.get(numberTask));
        } else if (descriptionEpic.containsKey(numberTask)) {
            System.out.println(descriptionEpic.get(numberTask));
        } else {
            System.out.println("Такого номера задачи нет");
        }
    }

    void outputAllTask() { // Метод вывода всех задач
        for (Integer keyt : descriptionTasks.keySet()) {
            Task listr = descriptionTasks.get(keyt);
            System.out.println("Result " + listr);
        }
        for (Integer keyt : descriptionEpic.keySet()) {
            Epic listr = descriptionEpic.get(keyt);
            System.out.println("Result " + listr);
        }
        for (Integer keyt : descriptionSubTasks.keySet()) {
            SubTask listr = descriptionSubTasks.get(keyt);
            System.out.println("Result " + listr);
        }
    }

    void outputAllEpics() { // Метод вывода всех эпиков
        for (Integer keyt : descriptionEpic.keySet()) {
            Epic listr = descriptionEpic.get(keyt);
            System.out.println("Result " + listr);
        }
    }

    void outputSubtaskByEpik(String keyt) { // Метод вывода подзадач по эпику
        if (subtaskInEpic.containsKey(keyt)) {
            ArrayList<SubTask> listr = subtaskInEpic.get(keyt);
            System.out.println("В эпике " + keyt);
            System.out.println("Задача " + listr);
        }
    }

    void delitTaskById(int numberTask) { // Метод удаления задачи по номеру
        Scanner scanner = new Scanner(System.in);
        if (descriptionTasks.containsKey(numberTask)) {
            System.out.println("УДАЛИТЬ ЗАДАЧУ? 1 - ДА ");
            int delit = scanner.nextInt();
            if (delit == 1) {
                descriptionTasks.remove(numberTask);
            }
        } else if (descriptionSubTasks.containsKey(numberTask)) {
            System.out.println("УДАЛИТЬ ЗАДАЧУ? 1 - ДА ");
            int delit = scanner.nextInt();
            if (delit == 1) {
                descriptionSubTasks.remove(numberTask);
            }
        } else if (descriptionSubTasks.containsKey(numberTask)) {
            System.out.println("УДАЛИТЬ ЗАДАЧУ? 1 - ДА ");
            int delit = scanner.nextInt();
            if (delit == 1) {
                descriptionSubTasks.remove(numberTask);
            }
        } else System.out.println("Такой задачи нет");
    }

    void delitAllTasks() { // Метод удаления всех задач
        Scanner scanner = new Scanner(System.in);
        System.out.println("Очистить список задач полностью ? ; 1 - ДА");
        int clear = scanner.nextInt();
        if (clear == 1) {
            descriptionSubTasks.clear();
            descriptionTasks.clear();
            descriptionEpic.clear();
        } else {
            System.out.println("Такой команды нет");
        }
    }

    boolean installStatus(int id) { // Метод-установщик статуса эпика
        boolean indicator = true;
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
                indicator = true;
            } else {
                indicator = false;
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


