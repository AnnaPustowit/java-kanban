package kanban.task;

public class Task {
    protected int taskId;
    protected Type type;
    protected String nameTask;
    protected String description;
    protected Status status;

    public String toString() {
        return  (taskId +
                "," + type +
                "," + nameTask  +
                "," + description +
                "," + status) ;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    public enum Type {
        TASK,
        EPIC,
        SUBTASK
    }

    public Task(int taskId, Type type, String nameTask, String description, Status status) {
        this.taskId = taskId;
        this.type = type;
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
    }

    public Task() {
        this.taskId = 0;
        this.type = null;
        this.nameTask = "";
        this.description = "";
        this.status = null;
    }

    public int getId() {
        return taskId;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return nameTask;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }
}
