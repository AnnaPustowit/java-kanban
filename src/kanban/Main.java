package kanban;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Scanner;

import kanban.managers.FileBackedTasksManager;
import kanban.managers.Managers;
import kanban.managers.TaskManager;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

public class Main {
public static void main(String[] args) {

    TaskManager testManager = Managers.getDefault();

    Scanner scanner = new Scanner(System.in);

    while(true) {
        printMenu();
        int userInput = scanner.nextInt();
        if(userInput == 1){             // добавление задачи
            //manager.createNewTask( some kanban.task-object );
            while (true) {
                System.out.println("Выберете номер задачи от 1 до 7, чтобы создать задачу, или число 100, чтобы не создавать задачу.");
                int userChoice = scanner.nextInt();
                if (userChoice == 1) {
                    Task task1 = new Task(1, Task.Type.TASK, "Съесть пирожок", "Пирожой с вишней",
                            Task.Status.NEW, LocalDateTime.of(2022, 12, 1, 10, 0), 30);
                    testManager.createNewTask(task1);
                } else if (userChoice == 2) {
                    Epic epic1 = new Epic(2, Task.Type.EPIC, "Съесть пиццы кусок", "Кусочек пиццы",
                            Task.Status.DONE, null, 0);
                    testManager.createNewTask(epic1);
                } else if (userChoice == 3) {
                    Subtask subtask1 = new Subtask(3, Task.Type.SUBTASK, "Выбрать пиццу в меню ресторана",
                            "Выбор пиццы в меню", Task.Status.NEW, LocalDateTime.of(2022, 12, 1, 12, 0), 30, 2);
                    testManager.createNewTask(subtask1);
                } else if (userChoice == 4) {
                    Subtask subtask2 = new Subtask(4, Task.Type.SUBTASK, "Заказать пиццу",
                            "Заказ пиццы", Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 13, 0), 30, 2);
                    testManager.createNewTask(subtask2);
                } else if (userChoice == 5) {
                    Epic epic2 = new Epic(5, Task.Type.EPIC, "Сходить в магазин", "Магазин у дома", Task.Status.DONE,
                            null, 0);
                    testManager.createNewTask(epic2);
                } else if (userChoice == 6) {
                    Subtask subtask3 = new Subtask(6, Task.Type.SUBTASK, "Купить булочку", "Простую булочку",
                            Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 15, 0), 30, 5);
                    testManager.createNewTask(subtask3);
                } else if (userChoice == 7) {
                    Task task2 = new Task(7, Task.Type.TASK, "Забрать заказ", "Заказ из магазина техники",
                            Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 10, 25), 30);//16:00
                    testManager.createNewTask(task2);
                } else if (userChoice == 100) {
                    break;
                } else {
                    System.out.println("Такой ввод для создания задачи не принимается!");
                }
            }
        } else if (userInput == 2) {    // вывести список задач в консоль
            testManager.getTasksList();
            testManager.getEpicsList();
            testManager.getSubtasksList();
        } else if (userInput == 3) {    // удаление всех задач
            testManager.deleteAllTasks();
            testManager.deleteAllEpics();
            testManager.deleteAllSubtasks();
        } else if(userInput == 4){      // получение задачи по id
            System.out.println("Введите индентификационный номер задачи: ");
            int numberOfTask = scanner.nextInt();
            testManager.getTaskById(numberOfTask);
        } else if(userInput == 5){      // удаление задачи по id
            System.out.println("Введите индентификационный номер задачи: ");
            int numberOfTask = scanner.nextInt();
            testManager.deleteTaskById(numberOfTask);
        } else if (userInput == 6) {    // обновление (замещение) задачи
            //manager.updateTasks( some kanban.task-object );
            Subtask subtask4 = new Subtask(3, Task.Type.SUBTASK,"Пойти погулять",
                    "Пешая прогулка",Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 19, 0), 30, 2);
            testManager.updateTasks(subtask4);
        } else if (userInput == 7) {
            for(Map.Entry<LocalDateTime, Task> pair : testManager.getPrioritizedTasks().entrySet()) {
                System.out.println("KEY : " + pair.getKey() + "  -  VALUE : " + pair.getValue());
            }
        } else if (userInput == 9) {    // тестирование
            kanban.TestClass test = new kanban.TestClass();
            test.runTest();
        } else if (userInput == 8) {
            testManager.getHistory();
        } else if (userInput == 0) {
            System.out.println("Программа завершена.");
            break;
        } else{
            System.out.println("Такой команды нет!");
        }
        System.out.println();
    }
}

private static void printMenu() {
    System.out.println("1. Создание.");
    System.out.println("2. Получение списка всех задач.");
    System.out.println("3. Удаление всех задач.");
    System.out.println("4. Получение по идентификатору.");
    System.out.println("5. Удаление по идентификатору.");
    System.out.println("6. Обновление.");
    System.out.println("7. Вывести список задач по дате и времени.");
    System.out.println("8. Получить историю просмотра задач.");
    System.out.println("0. Завершить программу.");
}
}
