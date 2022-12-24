package kanban.task;
import java.time.LocalDateTime;
import java.time.Duration;

public class Subtask extends Task {
    int epicId;

    public Subtask(int taskId, Type type, String nameTask, String description, Task.Status status, LocalDateTime startTime, int duration, int epicId) {
        super(taskId, type, nameTask, description, status,startTime, duration);
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return  (taskId +
                "," + type +
                "," + nameTask  +
                "," + description +
                "," + status +
                "," + toFormatTime(startTime) +
                "," + duration +
                "," + epicId) ;
    }

    public Subtask() {
        epicId = 0;
    }

    public int getEpicId() {
        return epicId;
    }
}
