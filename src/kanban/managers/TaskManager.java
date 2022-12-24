package kanban.managers;

import kanban.task.Task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;

public interface TaskManager {

    public void createNewTask(Task task);

    public List getTasksList();

    public List getEpicsList();

    public List getSubtasksList();

    public void deleteAllTasks();

    public void deleteAllEpics();

    public void deleteAllSubtasks();

    public Task getTaskById(int number);

    public void deleteTaskById(int number);

    public void updateTasks(Task task);

    public int generateId();

    public List<Task> getHistory();

    public TreeMap<LocalDateTime, Task> getPrioritizedTasks();
}
