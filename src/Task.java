public class Task {
    protected int taskId;
    protected String nameTask;
    protected String description;
    protected Status status;

    public enum Status {
        NEW,
        IN_PROGRESS,
        DONE
    }

    public Task(int taskId, String nameTask, String description, Status status) {
        this.taskId = taskId;
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
    }

    public Task() {
        this.taskId = 0;
        this.nameTask = "";
        this.description = "";
        this.status = null;
    }

    public int getId() {
        return taskId;
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

    //public int getEpicId() {
        //return 0;
    //}
}