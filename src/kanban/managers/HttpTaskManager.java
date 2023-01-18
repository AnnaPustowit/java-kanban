package kanban.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {
        private static final Gson gson = new Gson();
        public KVTaskClient taskClient;

        public HttpTaskManager(URI url) throws IOException, InterruptedException{
            super();
            this.taskClient = new KVTaskClient(url);
            loadFromServer();
        }

        public void loadFromServer() throws IOException, InterruptedException {
            // создать менеджер из сервера
            try {
                if (!taskClient.load("tasks").isEmpty()) {
                    Type taskType = new TypeToken<HashMap<Integer, Task>>() {
                    }.getType();
                    tasksMap = gson.fromJson(taskClient.load("tasks"), taskType);
                }

                if (!taskClient.load("epics").isEmpty()) {
                    Type epicType = new TypeToken<HashMap<Integer, Epic>>() {
                    }.getType();
                    epicsMap = gson.fromJson(taskClient.load("epics"), epicType);
                }

                if (!taskClient.load("subtasks").isEmpty()) {
                    Type subtaskType = new TypeToken<HashMap<Integer, Subtask>>() {
                    }.getType();
                    subtasksMap = gson.fromJson(taskClient.load("subtasks"), subtaskType);
                }

                if (!taskClient.load("history").isEmpty()) {
                    Type historyType = new TypeToken<List<Task>>() {}.getType();
                    List<Task> historyList = gson.fromJson(taskClient.load("history"), historyType);
                    historyList.forEach(historyManager::add);
                }
            } catch (IOException | InterruptedException exception) {
                System.out.println("Ошибка при восстановлении данных.");
            }
        }

        @Override
        protected void save() {
            try {
                taskClient.put("tasks", gson.toJson(tasksMap));
                taskClient.put("epics", gson.toJson(epicsMap));
                taskClient.put("subtasks", gson.toJson(subtasksMap));
                taskClient.put("history", gson.toJson(historyManager.getHistory()));
            } catch (IOException | InterruptedException err) {
                throw new ManagerSaveException("Ошибка при сохранении данных.");
            }
        }
}
