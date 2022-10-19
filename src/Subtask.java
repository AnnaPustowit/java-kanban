public class Subtask extends Task {
    int epicId;

    public Subtask(int taskId, String nameTask, String description, String status, int epicId) {
        super(taskId, nameTask, description, status);
        this.epicId = epicId;
    }

    public Subtask() {
        epicId = 0;
    }

    public int getEpicId() {
        return epicId;
    }
}
