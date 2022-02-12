package logic;

import manager.InMemoryTaskManager;
import manager.Status;

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
    static Status status = Status.NEW;
    static Status status0 = Status.DONE;
    static Status status4 = Status.DONE;

    public static void main(String[] args) {

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
                        System.out.println("Это все подзадачи по эпику : " + manager
                                .outputSubtaskByEpik(vybor));
                    } else if (command == 4) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager.outputTaskById(vybor);
                        System.out.println(temp);
                    } else if (command == 5) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager.outputSubTaskById(vybor);
                        System.out.println(temp);
                    } else if (command == 6) {
                        System.out.println("Введите №");
                        vybor = scanner.nextInt();
                        Task temp = manager.outputEpicById(vybor);
                        System.out.println(temp);
                    } else if (command == 7) {
                        manager.inputNewTask(name, description, status);
                        System.out.println(manager.getDescriptionTasks());
                    } else if (command == 8) {
                        manager.inputNewEpic(name2, description4);
                        System.out.println(manager.getDescriptionEpic());
                    } else if (command == 9) {
                        System.out.println("Введите № Эпика ");
                        vybor = scanner.nextInt();
                        manager.inputNewSubTask(name3, description5, status4, vybor);
                        System.out.println(manager.getDescriptionEpic());
                        Epic epic = manager.getDescriptionEpic().get(vybor);
                        System.out.println(epic.getListSubTask());
                    } else if (command == 10) {
                        Task task = new Task(nameO, descriptionO, status0, idO);
                        manager.updateTask(task);
                        System.out.println(manager.getDescriptionTasks());
                    } else if (command == 11) {
                        Epic epic = new Epic(nameO, descriptionO, idO);
                        manager.updateEpic(epic);
                        System.out.println(manager.getDescriptionEpic());
                    } else if (command == 12) {
                        System.out.println("начало 12");
                        SubTask subTask = new SubTask(nameO, descriptionO, status0, idO, epicIdiO);
                        manager.updateSubTask(subTask);
                        Epic epic = manager.getDescriptionEpic().get(epicIdiO);
                        System.out.println(manager.getDescriptionSubTasks());
                        System.out.println("конец 12");
                    } else if (command == 13) {
                        System.out.println("Введите № задачи ");
                        vybor = scanner.nextInt();
                        manager.deletTaskById(vybor);
                        System.out.println(manager.getDescriptionTasks());
                    } else if (command == 14) {
                        manager.deletAllTasks();
                    } else if (command == 15) {
                        List<Task> histor = manager.getHistory();
                        for (Task output : histor) {
                            System.out.println(output);
                        }
                    } else if (command == 16) {


                    } else if (command == 0) {
                        break;
                    }
                }
            }
        }
    }
    public static void startTest() {
        System.out.println("Сейчас создаю задачи");
        test7();
        test7();
        System.out.println("Сейчас создаю эпики");
        test8();
        test8();
        System.out.println("Сейчас создпю подзадачи");
        test9(3);
        test9(3);
        test9(3);
        System.out.println("Смотрю задачи и эпики");
        test4(0);
        test4(1);
        test6(2);
        test6(3);
        System.out.println("Смотрю историю");
        test15();
        System.out.println("Смотрю задачи и эпики");
        test6(2);
        test6(3);
        test4(1);
        test4(0);
        System.out.println("Смотрю историю");
        test15();
        System.out.println("Смотрю задачи и эпики");
        test4(1);
        test6(3);
        test6(2);
        test4(0);
        System.out.println("Смотрю историю");
        test15();
        System.out.println("Удаляю задачи");
        test13(1);
        test13(3);
        System.out.println("Смотрю историю");
        test15();
    }

    static void test4(int vybor) {
        Task temp = manager.outputTaskById(vybor);
        System.out.println("Просмотрена задача № " + vybor);
    }

    static void test6(int vybor) {
        Task temp = manager.outputEpicById(vybor);
        System.out.println("Просмотрен эпик № " + vybor);
    }

    static void test7() {
        manager.inputNewTask(name, description, status);
        System.out.println("Создана задача");
    }

    static void test8() {
        manager.inputNewEpic(name2, description4);
        System.out.println("Создан эпик");
    }

    static void test9(int vybor) {
        manager.inputNewSubTask(name3, description5, status4, vybor);
        System.out.println(manager.getDescriptionEpic());
        Epic epic = manager.getDescriptionEpic().get(vybor);
        System.out.println("Создана подзадача для эпика № " + vybor);
    }

    static void test13(int vybor) {
        manager.deletTaskById(vybor);
        System.out.println("Удалена задача № " + vybor);
    }

    static void test15() {
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
        System.out.println("16 - Запустить тест");
        System.out.println("0 - Завершить работу приложения.");
    }
}




