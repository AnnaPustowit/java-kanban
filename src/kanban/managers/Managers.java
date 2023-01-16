package kanban.managers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefault() {
        return FileBackedTasksManager.isHistoryFileExist()
                ? FileBackedTasksManager.loadFromFile()
               : new FileBackedTasksManager();
    }

    public static TaskManager getDefault(URI url) throws IOException, InterruptedException {
        return new HttpTaskManager(url);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
