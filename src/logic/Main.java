package logic;

import manager.InMemoryTaskManager;
import manager.Status;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        int command;
        int vybor;
        int idO = 0;
        int epicIdiO = 0;
        String name = "Это задача";
        String name2 = "Это эпик";
        String name3 = "Это подзадача";
        String nameO = "Обновилась Sub";
        String description = "Описание задачи";
        String description4 = "Описание Эпика";
        String description5 = "Описание подзадачи";
        String descriptionO = "Обновился Sub";
        Status status = Status.NEW;
        Status status0 = Status.DONE;
        Status status4 = Status.DONE;
        InMemoryTaskManager manager = new InMemoryTaskManager();
        System.out.println("Привет");
        System.out.println("Сделай свой выбор");
        while (true) {
            printMenu();
            Scanner scanner = new Scanner(System.in);
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
                List<Task> histor = InMemoryTaskManager.history();
                for (Task output : histor) {
                    System.out.println(output);
                }
            } else if (command == 0) {
                break;
            }
        }
    }

    static void printMenu() {
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
        System.out.println("13 - Удалить задачу по номеру");
        System.out.println("14 - Удалить все задачи");
        System.out.println("15 - Посмотреть историю задач");
        System.out.println("0 - Завершить работу приложения.");
    }
}



