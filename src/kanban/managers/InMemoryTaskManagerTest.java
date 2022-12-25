package kanban.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest{

    @BeforeEach
    public void makeTestInMemoryTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

}