public class Task {
    protected int taskId;
    protected String nameTask;
    protected String description;
    protected String status;

    public Task(int taskId, String nameTask, String description, String status) {
        this.taskId = taskId;
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
    }

    public Task() {
        this.taskId = 0;
        this.nameTask = "";
        this.description = "";
        this.status = "";
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

    public String getStatus() {
        return status;
    }
}