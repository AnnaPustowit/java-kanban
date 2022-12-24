package kanban.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

class EpicTest {

    private static Map<Integer, Epic> epicTest;
    private static Map<Integer, Subtask> subtaskTest;
    private static HashSet<Integer> subtasksIdTest;
    private static Epic epic1;
    private static Subtask subtask2;
    private static Subtask subtask3;

    @BeforeEach
    public void beforeEachTest() {
        epicTest = new HashMap<>();
        subtaskTest = new HashMap<>();
        subtasksIdTest = new HashSet<>();

        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,10, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,13, 0);
        epic1 = new Epic(1, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
        subtask2 = new Subtask(2, Task.Type.SUBTASK, "Сабтаск 2.", "Описание сабтаска 2.", Task.Status.DONE, localDateTime1,30,1);
        subtask3 = new Subtask(3, Task.Type.SUBTASK, "Сабтаск 3.", "Описание сабтаска 3.", Task.Status.DONE, localDateTime2,30,1);

        epicTest.put(epic1.getId(), epic1);


        subtaskTest.put(subtask2.getId(), subtask2);
        subtaskTest.put(subtask3.getId(), subtask3);

        epic1.getSetOfSubtasks().add(2);
        epic1.getSetOfSubtasks().add(3);
    }

    @Test
    void checkEpicStatusNew() { // проверка статуса эпика, когда все его сабтаски - NEW
        Map<Integer, Epic> epic = new HashMap<>();
        Map<Integer, Subtask> subtask = new HashMap<>();

        Epic epic10 = new Epic(10, Task.Type.EPIC, "Эпик 10.", "Описание эпика 10.", Task.Status.NEW, null,0);
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        Subtask subtask11 = new Subtask(11, Task.Type.SUBTASK, "Сабтаск 11.", "Описание сабтаска 11.", Task.Status.NEW, localDateTime1,30,10);
        Subtask subtask12 = new Subtask(12, Task.Type.SUBTASK, "Сабтаск 12.", "Описание сабтаска 12.", Task.Status.NEW, localDateTime2,30,10);

        epic.put(epic10.getId(), epic10);
        subtask.put(subtask11.getId(), subtask11);
        subtask.put(subtask12.getId(), subtask12);

        epic10.addSubtasks(11, subtask);
        epic10.addSubtasks(12, subtask);

        Task.Status testStatus = epic.get(10).getStatus();
        Assertions.assertEquals(Task.Status.NEW, testStatus);
    }

    @Test
    void checkEpicStatusInProgress() { // проверка статуса эпика, когда все его сабтаски - IN_PROGRESS
        Map<Integer, Epic> epic = new HashMap<>();
        Map<Integer, Subtask> subtask = new HashMap<>();

        Epic epic10 = new Epic(10, Task.Type.EPIC, "Эпик 10.", "Описание эпика 10.", Task.Status.NEW, null,0);
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        Subtask subtask11 = new Subtask(11, Task.Type.SUBTASK, "Сабтаск 11.", "Описание сабтаска 11.", Task.Status.IN_PROGRESS, localDateTime1,30,10);
        Subtask subtask12 = new Subtask(12, Task.Type.SUBTASK, "Сабтаск 12.", "Описание сабтаска 12.", Task.Status.IN_PROGRESS, localDateTime2,30,10);

        epic.put(epic10.getId(), epic10);
        subtask.put(subtask11.getId(), subtask11);
        subtask.put(subtask12.getId(), subtask12);

        epic10.addSubtasks(11, subtask);
        epic10.addSubtasks(12, subtask);

        Task.Status testStatus = epic.get(10).getStatus();
        Assertions.assertEquals(Task.Status.IN_PROGRESS, testStatus);
    }

    @Test
    void checkEpicStatusNewAndDone() { // проверка статуса эпика, когда его сабтаски - NEW и DONE
        Map<Integer, Epic> epic = new HashMap<>();
        Map<Integer, Subtask> subtask = new HashMap<>();

        Epic epic10 = new Epic(10, Task.Type.EPIC, "Эпик 10.", "Описание эпика 10.", Task.Status.NEW, null,0);
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
        Subtask subtask11 = new Subtask(11, Task.Type.SUBTASK, "Сабтаск 11.", "Описание сабтаска 11.", Task.Status.NEW, localDateTime1,30,10);
        Subtask subtask12 = new Subtask(12, Task.Type.SUBTASK, "Сабтаск 12.", "Описание сабтаска 12.", Task.Status.DONE, localDateTime2,30,10);

        epic.put(epic10.getId(), epic10);
        subtask.put(subtask11.getId(), subtask11);
        subtask.put(subtask12.getId(), subtask12);

        epic10.addSubtasks(11, subtask);
        epic10.addSubtasks(12, subtask);

        Task.Status testStatus = epic.get(10).getStatus();
        Assertions.assertEquals(Task.Status.IN_PROGRESS, testStatus);
    }

    @Test
    void addSubtasksTest() {
        epic1.addSubtasks(2, subtaskTest);
        epic1.addSubtasks(3, subtaskTest);
        Task.Status testStatus = epicTest.get(1).getStatus();
        Assertions.assertEquals(Task.Status.DONE, testStatus); // проверка статуса эпика, когда все его сабтаски - DONE

        LocalDateTime testMinTime1 = epicTest.get(1).getStartTime();
        LocalDateTime testMaxTime1 = epicTest.get(1).getEndTime();

        LocalDateTime testMinTime2 = LocalDateTime.of(2022,12, 23,10, 0);
        LocalDateTime testMaxTime2 = LocalDateTime.of(2022,12, 23,13, 0);

        Assertions.assertEquals(testMinTime2, testMinTime1);
        Assertions.assertEquals(testMaxTime2, testMaxTime1);

        int sizeOfEpicSet = epic1.getSetOfSubtasks().size();
        Assertions.assertEquals(2, sizeOfEpicSet);
    }

    @Test
    void deleteSubtasksTest() {
        epic1.deleteSubtasks(2, subtaskTest);
        epic1.deleteSubtasks(3, subtaskTest);

        LocalDateTime testMinTime1 = epicTest.get(1).getStartTime();
        LocalDateTime testMaxTime1 = epicTest.get(1).getEndTime();

        Assertions.assertEquals(null, testMinTime1);
        Assertions.assertEquals(null, testMaxTime1);

        int sizeOfEpicSet = epic1.getSetOfSubtasks().size();
        Assertions.assertEquals(0, sizeOfEpicSet);
    }

    @Test
    void clearSubtasksListTest() {
        epic1.clearSubtasksList();
        int sizeOfEpicSet = epic1.getSetOfSubtasks().size();
        Assertions.assertEquals(0, sizeOfEpicSet);

        Task.Status testStatus = epicTest.get(1).getStatus();
        Assertions.assertEquals(Task.Status.DONE, testStatus); // проверка статуса эпика(DONE), когда у него нет сабтасков
    }

}