import logic.Epic;
import logic.SubTask;
import logic.Task;
import manager.FileBackedTasksManager;
import manager.Status;

import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Mains {


    static FileBackedTasksManager manager2
            = new FileBackedTasksManager(new File("src/history.csv"));


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
    static Status status = Status.NEW;
    static Status status0 = Status.DONE;
    static Status status4 = Status.DONE;

    public static void main(String[] args) throws IOException {

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
                        System.out.println("Это все задачи : " + manager2.outputAllTask());
                    } else if (command == 2) {
                        System.out.println("Это все эпики : " + manager2.outputAllEpics());
                    } else if (command == 3) {
                        System.out.println("Введите № эпика");
                        vybor = scanner.nextInt();
                        System.out.println("Это все подзадачи по эпику : " + manager2
                                .outputSubtaskByEpik(vybor));
                    } else if (command == 4) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager2.outputTaskById(vybor);
                        System.out.println(temp);
                    } else if (command == 5) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager2.outputSubTaskById(vybor);
                        System.out.println(temp);
                    } else if (command == 6) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager2.outputEpicById(vybor);
                        System.out.println(temp);
                    } else if (command == 7) {
                        manager2.inputNewTask(name, description, status);
                        System.out.println(manager2.getDescriptionTasks());
                    } else if (command == 8) {
                        manager2.inputNewEpic(name2, description4);
                        System.out.println(manager2.getDescriptionEpic());
                    } else if (command == 9) {
                        System.out.println("Введите № Эпика ");
                        vybor = scanner.nextInt();
                        manager2.inputNewSubTask(name3, description5, status4, vybor);
                        System.out.println(manager2.getDescriptionEpic());
                        Epic epic = manager2.getDescriptionEpic().get(vybor);
                        System.out.println(epic.getListSubTask());
                    } else if (command == 10) {
                        Task task = new Task(nameO, descriptionO, status0, idO);
                        manager2.updateTask(task);
                        System.out.println(manager2.getDescriptionTasks());
                    } else if (command == 11) {
                        Epic epic = new Epic(nameO, descriptionO, idO);
                        manager2.updateEpic(epic);
                        System.out.println(manager2.getDescriptionEpic());
                    } else if (command == 12) {
                        System.out.println("начало 12");
                        SubTask subTask = new SubTask(nameO, descriptionO, status0, idO, epicIdiO);
                        manager2.updateSubTask(subTask);
                        Epic epic = manager2.getDescriptionEpic().get(epicIdiO);
                        System.out.println(manager2.getDescriptionSubTasks());
                        System.out.println("конец 12");
                    } else if (command == 13) {
                        System.out.println("Введите № задачи ");
                        vybor = scanner.nextInt();
                        manager2.deletTaskById(vybor);
                        System.out.println(manager2.getDescriptionTasks());
                    } else if (command == 14) {
                        manager2.deletAllTasks();
                    } else if (command == 15) {
                        List<Task> histor2 = manager2.getHistory();
                        for (Task output : histor2) {
                            System.out.println(output);
                        }
                    } else if (command == 0) {

                        break;
                    }
                }
            }
        }
    }

    public static void startTest() throws IOException {
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

    static void watchTask(int vybor) throws IOException {
        Task temp = manager2.outputTaskById(vybor);
        System.out.println("Просмотрена задача № " + vybor);
    }

    static void watchEpic(int vybor) throws IOException {
        Task temp = manager2.outputEpicById(vybor);
        System.out.println("Просмотрен эпик № " + vybor);
    }

    static void createNewTask() throws IOException {
        manager2.inputNewTask(name, description, status);
        System.out.println("Создана задача");
    }

    static void createNewEpic() throws IOException {
        manager2.inputNewEpic(name2, description4);
        System.out.println("Создан эпик");
    }

    static void createNewSubtaskByEpic(int vybor) throws IOException {
        manager2.inputNewSubTask(name3, description5, status4, vybor);
        System.out.println(manager2.getDescriptionEpic());
        Epic epic = manager2.getDescriptionEpic().get(vybor);
        System.out.println("Создана подзадача для эпика № " + vybor);
    }

    static void deleteTaskById(int vybor) throws IOException {
        manager2.deletTaskById(vybor);
        System.out.println("Удалена задача № " + vybor);
    }

    static void watchHistory() throws IOException {
        List<Task> histor = manager2.getHistory();
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
        System.out.println("16 - Запустить тест");
        System.out.println("0 - Завершить работу приложения.");
    }
}



