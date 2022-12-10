package kanban.task;

public class Subtask extends Task {
    int epicId;

    public Subtask(int taskId, Type type, String nameTask, String description, Task.Status status, int epicId) {
        super(taskId, type, nameTask, description, status);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return  (taskId +
                "," + type +
                "," + nameTask  +
                "," + description +
                "," + status +
                "," + epicId) ;
    }

    public Subtask() {
        epicId = 0;
    }

    public int getEpicId() {
        return epicId;
    }
}
