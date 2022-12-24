package kanban.task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    @Test
    void getEpicIdTest() {
        LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,10, 0);
        Subtask subtask1 = new Subtask(2, Task.Type.SUBTASK, "Сабтаск 1.", "Описание сабтаска 1.", Task.Status.DONE, localDateTime1,30,100);
        int epicId = subtask1.getEpicId();
        Assertions.assertEquals(100, epicId);
    }
}