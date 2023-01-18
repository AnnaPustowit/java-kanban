package kanban.managers;

import java.io.IOException;
import java.net.URI;


public class Managers {
    public static TaskManager getDefault() {
        return FileBackedTasksManager.isHistoryFileExist()
                ? FileBackedTasksManager.loadFromFile()
               : new FileBackedTasksManager();
    }

    public static TaskManager getDefaultHtpManager() throws IOException, InterruptedException {
        return new HttpTaskManager(URI.create("http://localhost:8078"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
