/*package logic;

import manager.InMemoryTaskManager;
import manager.ManagerSaveException;
import manager.Status;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {

    static InMemoryTaskManager manager = new InMemoryTaskManager();

    static int command;
    static int vybor;
    static int idO = 0;
    static int epicIdiO = 0;
    static String name = "Это задача";
    static String name2 = "Это эпик";
    static String name3 = "Это подзадача";
    static String nameO = "Обновилась Sub";
    static String description = "Описание задачи";
    static String description4 = "Описание Эпика";
    static String description5 = "Описание подзадачи";
    static String descriptionO = "Обновился Sub";
    static Status status = Status.IN_PROGRESS;
    static Status status0 = Status.DONE;
    static Status status4 = Status.DONE;

    public static void main(String[] args) throws IOException, ManagerSaveException {

        while (true) {
            System.out.println("Привет! ручное тестирование - 1; автоматическое - 2; Выход - 0");
            Scanner scanner = new Scanner(System.in);
            command = scanner.nextInt();
            if (command == 2) {
                startTest();
            } else if (command == 0) {
                break;
            } else if (command == 1) {
                while (true) {
                    printMenu();
                    command = scanner.nextInt();
                    if (command == 1) {
                        System.out.println("Это все задачи : " + manager.outputAllTask());
                    } else if (command == 2) {
                        System.out.println("Это все эпики : " + manager.outputAllEpics());
                    } else if (command == 3) {
                        System.out.println("Введите № эпика");
                        vybor = scanner.nextInt();
                        try {
                            System.out.println("Это все подзадачи по эпику : " + manager
                                    .outputSubtaskByEpik(vybor));
                        } catch (NullPointerException ex) {
                            System.out.println("Печатать пока нечего");
                        }
                    } else if (command == 4) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager.outputTaskById(vybor);
                        System.out.println(temp);
                    } else if (command == 5) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager.outputSubTaskById(vybor);
                       try {
                           System.out.println(temp);
                       } catch (NullPointerException ex) {
                           System.out.println("Такой задачи пока нет!");
                       }
                    } else if (command == 6) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager.outputEpicById(vybor);
                        System.out.println(temp);
                    } else if (command == 7) {
                        int duration = 10;
                        LocalDateTime startTime = LocalDateTime.of(2022, 6, 4
                                , 8, 22, 25);
                        Task task = new Task(name, description, status, manager.getTaskId()
                                , duration, startTime);
                        manager.inputNewTask(task);
                        System.out.println(manager.getDescriptionTasks());
                    } else if (command == 8) {
                        LocalDateTime startTime = LocalDateTime.of(2022, 6, 5
                                , 9, 21, 26);
                        Epic epic = new Epic(name, description, manager.getTaskId());
                        manager.inputNewEpic(epic);
                        System.out.println(manager.getDescriptionEpic());
                    } else if (command == 9) {
                        int duration = 20;
                        LocalDateTime startTime = LocalDateTime.of(2022, 6, 6
                                , 10, 22, 25);
                        System.out.println("Введите № Эпика ");
                        vybor = scanner.nextInt();
                        SubTask subTask = new SubTask(name, description, status, manager.getTaskId()
                                , vybor, duration, startTime);
                        manager.inputNewSubTask(subTask);
                        System.out.println(manager.getDescriptionEpic());
                        Epic epic = manager.getDescriptionEpic().get(vybor);
                        try {
                            System.out.println(epic.getListSubTask());
                        } catch (NullPointerException ex) {
                            System.out.println("Печатать пока нечего");
                        }
                    } else if (command == 10) {
                        int duration = 15;
                        LocalDateTime startTime = LocalDateTime.of(2022, 6, 7
                                , 8, 22, 25);
                        Task task = new Task(nameO, descriptionO, status0, idO, duration
                                , startTime);
                        manager.updateTask(task);
                        System.out.println(manager.getDescriptionTasks());
                    } else if (command == 11) {
                        int duration = 15;
                        LocalDateTime startTime = LocalDateTime.of(2022, 6, 8
                                , 9, 22, 25);
                        Epic epic = new Epic(nameO, descriptionO, idO);
                        manager.updateEpic(epic);
                        System.out.println(manager.getDescriptionEpic());
                    } else if (command == 12) {
                        int duration = 15;
                        LocalDateTime startTime = LocalDateTime.of(2022, 6, 9
                                , 10, 22, 25);
                        System.out.println("начало 12");
                        SubTask subTask = new SubTask(nameO, descriptionO, status0, idO, epicIdiO
                                , duration, startTime);
                        manager.updateSubTask(subTask);
                        Epic epic = manager.getDescriptionEpic().get(epicIdiO);
                        System.out.println(manager.getDescriptionSubTasks());
                        System.out.println("конец 12");
                    } else if (command == 13) {
                        System.out.println("Введите № задачи ");
                        vybor = scanner.nextInt();
                        manager.deleteTaskById(vybor);
                        System.out.println(manager.getDescriptionTasks());
                    } else if (command == 14) {
                        manager.deleteAllTasks();
                    } else if (command == 15) {
                        List<Task> histor = manager.getHistory();
                        for (Task output : histor) {
                            System.out.println(output);
                        }
                    } else if (command == 16) {
                        System.out.println("Печатаю СЕТ");
                        for (Task task : manager.getPrioritizedTasks()) {
                            System.out.println(task);
                        }
                    } else if (command == 0) {
                        break;
                    }
                }
            }
        }
    }

    public static void startTest() throws IOException, ManagerSaveException {
        System.out.println("Сейчас создаю задачи");
        createNewTask();
        createNewTask();
        System.out.println("Сейчас создаю эпики");
        createNewEpic();
        createNewEpic();
        System.out.println("Сейчас создпю подзадачи");
        createNewSubtaskByEpic(3);
        createNewSubtaskByEpic(3);
        createNewSubtaskByEpic(3);
        System.out.println("Смотрю задачи и эпики");
        watchTask(0);
        watchTask(1);
        watchEpic(2);
        watchEpic(3);
        System.out.println("Смотрю историю");
        watchHistory();
        System.out.println("Смотрю задачи и эпики");
        watchEpic(2);
        watchEpic(3);
        watchTask(1);
        watchTask(0);
        System.out.println("Смотрю историю");
        watchHistory();
        System.out.println("Смотрю задачи и эпики");
        watchTask(1);
        watchEpic(3);
        watchEpic(2);
        watchTask(0);
        System.out.println("Смотрю историю");
        watchHistory();
        System.out.println("Удаляю задачи");
        deleteTaskById(1);
        deleteTaskById(3);
        System.out.println("Смотрю историю");
        watchHistory();
    }

    static void watchTask(int vybor) throws IOException, ManagerSaveException {
        Task temp = manager.outputTaskById(vybor);
        System.out.println("Просмотрена задача № " + vybor);
    }

    static void watchEpic(int vybor) throws IOException, ManagerSaveException {
        Task temp = manager.outputEpicById(vybor);
        System.out.println("Просмотрен эпик № " + vybor);
    }

    static void createNewTask() throws IOException, ManagerSaveException {
        int duration = 10;
        LocalDateTime startTime = LocalDateTime.of(2022, 3, 4
                , 8, 22, 25);
        Task task = new Task(name, description, status, manager.getTaskId(), duration, startTime);
        manager.inputNewTask(task);
        System.out.println("Создана задача");
    }

    static void createNewEpic() throws IOException, ManagerSaveException {
        int duration = 15;
        LocalDateTime startTime = LocalDateTime.of(2022, 3, 4
                , 9, 22, 25);
        Epic epic = new Epic(name, description, manager.getTaskId());
        manager.inputNewEpic(epic);
        System.out.println("Создан эпик");
    }

    static void createNewSubtaskByEpic(int vybor) throws IOException, ManagerSaveException {
        int duration = 20;
        LocalDateTime startTime = LocalDateTime.of(2022, 3, 4
                , 10, 22, 25);
        System.out.println("Введите № Эпика ");
        SubTask subTask = new SubTask(name, description, status, manager.getTaskId()
                , vybor, duration, startTime);
        manager.inputNewSubTask(subTask);
        System.out.println(manager.getDescriptionEpic());
        System.out.println("Создана подзадача для эпика № " + vybor);
    }

    static void deleteTaskById(int vybor) throws IOException, ManagerSaveException {
        manager.deleteTaskById(vybor);
        System.out.println("Удалена задача № " + vybor);
    }

    static void watchHistory() throws IOException {
        List<Task> histor = manager.getHistory();
        for (Task output : histor) {
            System.out.println(output);
        }
    }

    public static void printMenu() {
        System.out.println("1 - Получить все задачи");
        System.out.println("2 - Получить все эпики");
        System.out.println("3 - Получить подзадачи по эпику");
        System.out.println("4 - Получить задачу по ID");
        System.out.println("5 - Получить подзадачу по ID");
        System.out.println("6 - Получить эпик по ID");
        System.out.println("7 - Добавить новую задачу.");
        System.out.println("8 - Добавить новый эпик");
        System.out.println("9 - Добавить новую подзадачу");
        System.out.println("10 - Обновить задачу по ID");
        System.out.println("11 - Обновить эпик по ID");
        System.out.println("12 - Обновить подзадачу по ID");
        System.out.println("13 - Удалить задачу или эпик по ID номеру");
        System.out.println("14 - Удалить все задачи");
        System.out.println("15 - Посмотреть историю просмотра задач");
        System.out.println("16 - Вывести список заддач отсортированных по дате начала");
        System.out.println("0 - Завершить работу приложения.");
    }
}
*/
