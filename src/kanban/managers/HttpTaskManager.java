package kanban.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTaskManager extends FileBackedTasksManager {
        private static final Gson gson = new Gson();
        public KVTaskClient taskClient;

        public HttpTaskManager(URI url) throws IOException, InterruptedException{
            super();
            this.taskClient = new KVTaskClient(url);
        }


        public static HttpTaskManager loadFromServer() throws IOException, InterruptedException {
            // создать менеджер из сервера
            HttpTaskManager m = new HttpTaskManager(URI.create("http://localhost:8080/registr"));
            try {
                Map<Integer, Task> tasks = gson.fromJson(
                        m.taskClient.load("tasks"),
                        new TypeToken<HashMap<Integer, Task>>() {
                        }.getType()
                );
                Map<Integer, Epic> epics = gson.fromJson(
                        m.taskClient.load("epics"),
                        new TypeToken<HashMap<Integer, Epic>>() {
                        }.getType()
                );
                Map<Integer, Subtask> subtasks = gson.fromJson(
                        m.taskClient.load("subtasks"),
                        new TypeToken<HashMap<Integer, Subtask>>() {
                        }.getType()
                );
                List<Task> historyList = gson.fromJson(
                        m.taskClient.load("history"),
                        new TypeToken<List<Task>>() {
                        }.getType()
                );
                HistoryManager history = new InMemoryHistoryManager();
                historyList.forEach(history::add);



                m.tasksMap = tasks;
                m.epicsMap = epics;
                m.subtasksMap = subtasks;
                m.historyManager = history;

            } catch (IOException | InterruptedException exception) {
                System.out.println("Ошибка при восстановлении данных");
            }
            return m;
        }

        @Override
        protected void save() {
            try {
                taskClient.put("tasks", gson.toJson(tasksMap));
                taskClient.put("epics", gson.toJson(epicsMap));
                taskClient.put("subtasks", gson.toJson(subtasksMap));
                taskClient.put("history", gson.toJson(historyManager.getHistory()));
            } catch (IOException | InterruptedException err) {
                throw new ManagerSaveException("Ошибка при сохранении данных");
            }
        }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        //save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int number) {
        Task taskForReturn = super.getTaskById(number);
        save();
        return taskForReturn;

    }


    @Override
    public void deleteTaskById(int number) {
        super.deleteTaskById(number);
        save();
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);
        save();
    }
}

