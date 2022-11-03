package kanban;

import kanban.managers.Managers;
import kanban.managers.TaskManager;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

public class TestClass {

    TaskManager testManager = Managers.getDefault();

    public void runTest(){
        addTestObjects();
        testManager.getHistory();
        testManager.getTasksList();
        testManager.getEpicsList();
        testManager.getSubtasksList();
        testManager.getHistory();
        System.out.println();
        testManager.getHistory();
        testManager.getTasksList();
        testManager.getEpicsList();
        testManager.getSubtasksList();
        System.out.println();
        testManager.getTaskById(1);
        testManager.getTaskById(2);
        testManager.getTaskById(3);
        testManager.getTaskById(4);
        testManager.getTaskById(5);
        System.out.println("ДО");
        testManager.getHistory();
        updatedTestObjects();
        System.out.println();
        //testManager.deleteTaskById(2);
        testManager.deleteTaskById(3);
        System.out.println("ПОСЛЕ");
        testManager.getHistory();
        System.out.println();
        updatedTestObjects();
        testManager.getTasksList();
        testManager.getEpicsList();
        testManager.getSubtasksList();
        testManager.getHistory();
        System.out.println();
        testManager.deleteAllTasks();
        testManager.deleteAllEpics();
        testManager.deleteAllSubtasks();
        testManager.getHistory();
    }


    public void addTestObjects() {

        Task task1 = new Task(testManager.generateId(), "Съесть пирожок", "Пирожой с вишней", Task.Status.NEW);

        int testEpic1 = testManager.generateId();
        Epic epic1 = new Epic(testEpic1,"Съесть пиццы кусок", "Кусочек пиццы",
                Task.Status.DONE);
        Subtask subtask1 = new Subtask(testManager.generateId(),"Выбрать пиццу в меню ресторана",
                "Выбор пиццы в меню", Task.Status.NEW, testEpic1);
        Subtask subtask2 = new Subtask(testManager.generateId(), "Заказать пиццу",
                "Заказ пиццы", Task.Status.DONE, testEpic1);

        int testEpic2 = testManager.generateId();
        Epic epic2 = new Epic(testEpic2,"Сходить в магазин", "Магазин у дома", Task.Status.DONE);
        Subtask subtask3 = new Subtask(testManager.generateId(), "Купить булочку", "Простую булочку",
                Task.Status.DONE, testEpic2);

        Task task2 = new Task(testManager.generateId(), "Забрать заказ", "Заказ из магазина техники",
                Task.Status.DONE);

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
        Subtask subtask4 = new Subtask(testSubtask3, "Пойти погулять",
                "Пешая прогулка",Task.Status.DONE, testEpic2);
        testManager.updateTasks(subtask4);
    }


}
