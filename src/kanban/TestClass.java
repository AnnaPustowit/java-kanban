package kanban;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

import kanban.managers.Managers;
import kanban.managers.TaskManager;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

public class TestClass {

    /*TaskManager testManager = Managers.getDefault();

    public void runTest(){
        addTestObjects();
        //testManager.getHistory();
        //testManager.getTasksList();
        //testManager.getEpicsList();
        //testManager.getSubtasksList();
        //testManager.getHistory();
        System.out.println();
        testManager.getHistory();
        //testManager.getTasksList();
        //testManager.getEpicsList();
        //testManager.getSubtasksList();
        System.out.println();
        testManager.getTaskById(1);
        testManager.getTaskById(2);
        testManager.getTaskById(3);
        testManager.getTaskById(4);
        testManager.getTaskById(5);
        testManager.getTaskById(6);
       // testManager.getTaskById(7);
        System.out.println("ДО");
        testManager.getHistory();
        updatedTestObjects();
        System.out.println();
        //testManager.deleteTaskById(2);
        //testManager.deleteTaskById(3);
        //testManager.deleteTaskById(4);
        //testManager.deleteTaskById(6);
        System.out.println("ПОСЛЕ");
        //testManager.getHistory();
        System.out.println();
        updatedTestObjects();
        //testManager.getTasksList();
        //testManager.getEpicsList();
        // testManager.getSubtasksList();
        //testManager.getHistory();
        System.out.println();
        //testManager.deleteAllTasks();
        //testManager.deleteAllEpics();
        //testManager.deleteAllSubtasks();
        testManager.getHistory();
        System.out.println();
        System.out.println("Выводим TreeMap тасков и сабтасков по времени ");
        for(Map.Entry<LocalDateTime, Task> pair : testManager.getPrioritizedTasks().entrySet()) {
            System.out.println("KEY : " + pair.getKey() + "  -  VALUE : " + pair.getValue());
        }
        System.out.println();
    }


    public void addTestObjects() {

        // LocalDateTime time = LocalDateTime.of(2022, 12, 1, 10, 0);

        Task task1 = new Task(testManager.generateId(), Task.Type.TASK,"Съесть пирожок", "Пирожой с вишней",
                Task.Status.NEW,LocalDateTime.of(2022, 12, 1, 10, 0), 30 );

        int testEpic1 = testManager.generateId();
        Epic epic1 = new Epic(testEpic1, Task.Type.EPIC,"Съесть пиццы кусок", "Кусочек пиццы",
                Task.Status.DONE, null, 0);
        Subtask subtask1 = new Subtask(testManager.generateId(), Task.Type.SUBTASK,"Выбрать пиццу в меню ресторана",
                "Выбор пиццы в меню", Task.Status.NEW, LocalDateTime.of(2022, 12, 1, 12, 0), 30, testEpic1);
        Subtask subtask2 = new Subtask(testManager.generateId(), Task.Type.SUBTASK, "Заказать пиццу",
                "Заказ пиццы", Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 13, 0), 30, testEpic1);

        int testEpic2 = testManager.generateId();
        Epic epic2 = new Epic(testEpic2, Task.Type.EPIC,"Сходить в магазин", "Магазин у дома", Task.Status.DONE,
                null, 0);
        Subtask subtask3 = new Subtask(testManager.generateId(), Task.Type.SUBTASK,"Купить булочку", "Простую булочку",
                Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 15, 0), 30, testEpic2);

        Task task2 = new Task(testManager.generateId(), Task.Type.TASK,"Забрать заказ", "Заказ из магазина техники",
                Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 10, 25), 30);//16:00

        testManager.createNewTask(task1);
        testManager.createNewTask(epic1);
        testManager.createNewTask(subtask1);
        testManager.createNewTask(subtask2);
        testManager.createNewTask(epic2);
        testManager.createNewTask(subtask3);
        testManager.createNewTask(task2);
    }

    public void updatedTestObjects() {

        int testSubtask3 = 3;
        int testEpic2 = 2;
        Subtask subtask4 = new Subtask(testSubtask3, Task.Type.SUBTASK,"Пойти погулять",
                "Пешая прогулка",Task.Status.DONE, LocalDateTime.of(2022, 12, 1, 22, 0), 30, testEpic2);
        testManager.updateTasks(subtask4);
    }

*/
}
