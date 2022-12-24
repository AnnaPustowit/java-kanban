package kanban.managers;

import kanban.task.Task;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {



    @BeforeEach
    public void makeTest() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        LocalDateTime localDateTime3 = LocalDateTime.of(2022,12, 23,13, 0);

        Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
        Task task2 = new Task(2, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime2,30);
        Task task3 = new Task(3, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime3,30);


    }

    @Test
    void getHistory() { // проверка, когда история пустая
        HistoryManager historyManager = new InMemoryHistoryManager();

        final List<Task> history = historyManager.getHistory();

        assertEquals(0, history.size(), "История пустая.");
    }

    @Test
    void add() { // проверка добавления задачи в историю задач
        HistoryManager historyManager = new InMemoryHistoryManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);

        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void removeAverage() { // проверка удаления истории задач - середина
        HistoryManager historyManager = new InMemoryHistoryManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        Task task2 = new Task(2, Task.Type.TASK, "Задача 2.", "Описание задачи 2.", Task.Status.NEW, localDateTime2,30);
        LocalDateTime localDateTime3 = LocalDateTime.of(2022,12, 23,13, 0);
        Task task3 = new Task(3, Task.Type.TASK, "Задача 3.", "Описание задачи 3.", Task.Status.NEW, localDateTime3,30);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);


        historyManager.remove(2);
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.get(0).getId());
        assertEquals(3, history.get(1).getId());
    }

    @Test
    void removeFirst() { // проверка удаления истории задач - начало
        HistoryManager historyManager = new InMemoryHistoryManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        Task task2 = new Task(2, Task.Type.TASK, "Задача 2.", "Описание задачи 2.", Task.Status.NEW, localDateTime2,30);
        LocalDateTime localDateTime3 = LocalDateTime.of(2022,12, 23,13, 0);
        Task task3 = new Task(3, Task.Type.TASK, "Задача 3.", "Описание задачи 3.", Task.Status.NEW, localDateTime3,30);
        LocalDateTime localDateTime4 = LocalDateTime.of(2022,12, 23,14, 0);
        Task task4 = new Task(4, Task.Type.TASK, "Задача 4.", "Описание задачи 4.", Task.Status.NEW, localDateTime4,30);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);


        historyManager.remove(1);
        final List<Task> history = historyManager.getHistory();
        assertEquals(2, history.get(0).getId());
        assertEquals(3, history.get(1).getId());

    }

    @Test
    void removeLast() { // проверка удаления истории задач - конец
        HistoryManager historyManager = new InMemoryHistoryManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        Task task2 = new Task(2, Task.Type.TASK, "Задача 2.", "Описание задачи 2.", Task.Status.NEW, localDateTime2,30);
        LocalDateTime localDateTime3 = LocalDateTime.of(2022,12, 23,13, 0);
        Task task3 = new Task(3, Task.Type.TASK, "Задача 3.", "Описание задачи 3.", Task.Status.NEW, localDateTime3,30);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(3);
        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.get(0).getId());
        assertEquals(2, history.get(1).getId());
    }

    @Test
    void update() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);

        historyManager.add(task1);

        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        Task task2 = new Task(1, Task.Type.TASK, "Обновленная задача 1.", "Описание обновленной задачи 1.", Task.Status.NEW, localDateTime2,30);

        historyManager.add(task1); // проверка на дубли
        historyManager.update(task2);

        final List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "История содержит один элемент.");
        assertEquals("Обновленная задача 1.", history.get(0).getName());
    }
}