package kanban.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest{

    @BeforeEach
    public void makeTestInMemoryTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void getPrioritizedTasks() {

    }

    @Test
    void createNewTask() {

    }

    @Test
    void getTasksList() {
    }

    @Test
    void getEpicsList() {
    }

    @Test
    void getSubtasksList() {
    }

    @Test
    void deleteAllTasks() {
    }

    @Test
    void deleteAllEpics() {
    }

    @Test
    void deleteAllSubtasks() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void deleteTaskById() {
    }

    @Test
    void updateTasks() {
    }

    @Test
    void generateId() {
    }

    @Test
    void getHistory() {
    }
}