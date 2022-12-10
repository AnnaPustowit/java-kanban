package kanban.managers;

import kanban.task.Task;

import java.util.List;

public interface HistoryManager {

    public void update(Task task);

    public void add(Task task);

    public void remove(int id);

    public List<Task> getHistory();
}