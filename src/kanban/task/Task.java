package kanban.task;

import java.util.Comparator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    protected int taskId;
    protected Type type;
    protected String nameTask;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected int duration;


    public String toString() {
        return  (taskId +
                "," + type +
                "," + nameTask  +
                "," + description +
                "," + status +
                "," + toFormatTime(startTime) +
                "," + duration);
    }

    public static String toFormatTime(LocalDateTime time) {
        if (time != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            String formatDateTime = time.format(formatter);
            return formatDateTime;
        }
        return null;
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

    public Task(int taskId, Type type, String nameTask, String description, Status status, LocalDateTime startTime, int duration) {
        this.taskId = taskId;
        this.type = type;
        this.nameTask = nameTask;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task() {
        this.taskId = 0;
        this.type = null;
        this.nameTask = "";
        this.description = "";
        this.status = null;
        this.startTime = null;
        this.duration = 0;
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
    public LocalDateTime getStartTime() {
        return startTime;
    }
    public int getDuration() {
        return duration;
    }


}
