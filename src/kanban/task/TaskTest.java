package kanban.task;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeAll;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    public static Task taskTest;

    @BeforeAll
    public static void makeTest() {
        LocalDateTime localDateTime = LocalDateTime.of(2022,12, 23,16, 0);
        taskTest = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime,30);
    }

    @Test
    public void getIdTest(){
        int testId = taskTest.getId();
        Assertions.assertEquals(1, testId);
    }

    @Test
    public void getTypeTest() {
        Task.Type testType = taskTest.getType();
        Assertions.assertEquals(Task.Type.TASK, testType);
    }

    @Test
    public void getNameTest() {
        String testName = taskTest.getName();
        Assertions.assertEquals("Задача 1.", testName);
    }

    @Test
    public void getDescriptionTest() {
        String testDescription = taskTest.getDescription();
        Assertions.assertEquals("Описание задачи 1.", testDescription);
    }

    @Test
    public void getStatusTest() {
        Task.Status testStatus = taskTest.getStatus();
        Assertions.assertEquals(Task.Status.NEW, testStatus);
    }

    @Test
    public void getStartTimeTest() {
        LocalDateTime testTime = LocalDateTime.of(2022,12, 23,16, 0);
        LocalDateTime testTaskTime = taskTest.getStartTime();
        Assertions.assertEquals(testTime, testTaskTime);
    }

    @Test
    public void getDurationTest() {
        int testDuration = taskTest.getDuration();
        Assertions.assertEquals(30, testDuration);
    }
}