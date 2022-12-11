package kanban.managers;

public class Managers {
    public static TaskManager getDefault() {
        return FileBackedTasksManager.isHistoryFileExist()
                ? FileBackedTasksManager.loadFromFile()
                : new FileBackedTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
