package kanban;

import java.util.Scanner;
import kanban.managers.Managers;
import kanban.managers.TaskManager;

public class Main {
public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    TaskManager manager = Managers.getDefault();
    while(true) {
        printMenu();
        int userInput = scanner.nextInt();
        if(userInput == 1){             // добавление задачи
            //manager.createNewTask( some kanban.task-object );
        } else if (userInput == 2) {    // вывести список задач в консоль
            manager.getTasksList();
            manager.getEpicsList();
            manager.getSubtasksList();
        } else if (userInput == 3) {    // удаление всех задач
            manager.deleteAllTasks();
            manager.deleteAllEpics();
            manager.deleteAllSubtasks();
        } else if(userInput == 4){      // получение задачи по id
            System.out.println("Введите индентификационный номер задачи: ");
            int numberOfTask = scanner.nextInt();
            manager.getTaskById(numberOfTask);
        } else if(userInput == 5){      // удаление задачи по id
            System.out.println("Введите индентификационный номер задачи: ");
            int numberOfTask = scanner.nextInt();
            manager.deleteTaskById(numberOfTask);
        } else if (userInput == 6) {    // обновление (замещение) задачи
            //manager.updateTasks( some kanban.task-object );
        } else if (userInput == 9) {    // тестирование
            kanban.TestClass test = new kanban.TestClass();
            test.runTest();
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
    System.out.println("0. Завершить программу");
}
}
