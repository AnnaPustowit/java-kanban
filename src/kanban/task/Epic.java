package kanban.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashSet;

public class Epic extends Task {

    private HashSet<Integer> subtasksId;
    LocalDateTime endTime;

    public Epic(int taskId, Type type, String nameTask, String description, Task.Status status, LocalDateTime startTime, int duration) {
        super(taskId, type, nameTask, description, status, startTime, duration);
        subtasksId = new HashSet<>();
        endTime = null;
    }

    public Epic() {
        subtasksId = new HashSet<>();
    }

    public void addSubtasks(Integer number, Map<Integer, Subtask> subtask) {
        subtasksId.add(number);
        checkCurrentStatus(subtask);
        checkEndTimeAndStartTime(subtask);
    }

    public void deleteSubtasks(Integer number, Map<Integer, Subtask> subtask) {
        subtasksId.remove(number);
        checkCurrentStatus(subtask);
        checkEndTimeAndStartTime(subtask);
    }

    public void clearSubtasksList() {
        subtasksId.clear();
        status = Task.Status.DONE;
    }

    public HashSet getSetOfSubtasks() {
        return subtasksId;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void checkCurrentStatus(Map<Integer, Subtask> subtask) {
        int newCount = 0;
        int inProgressCount = 0;
        int doneCount = 0;

        for (Integer key : subtasksId) {
            Task.Status status = subtask.get(key).getStatus();
            if (status.equals(Task.Status.NEW)) {
                newCount++;
            } else if (status.equals(Task.Status.IN_PROGRESS)) {
                inProgressCount++;
            } else if (status.equals(Task.Status.DONE)) {
                doneCount++;
            }
        }

        if (newCount != 0 && inProgressCount == 0 && doneCount == 0) {
            this.status = Task.Status.NEW;
        } else if(doneCount != 0 && newCount == 0 && inProgressCount == 0) {
            this.status = Task.Status.DONE;
        } else {
            this.status = Task.Status.IN_PROGRESS;
        }
    }

    private void checkEndTimeAndStartTime(Map<Integer, Subtask> subtask) { // Доработать, чтобы у эпика время было временем его раннего саба(сейчас рандом)
        if(subtasksId.size() == 0) {
            this.startTime = null;
            this.endTime = null;
            this.duration = 0;
            return;
        }

        LocalDateTime maxTime = null;
        LocalDateTime minTime = null;

        int sum = 0;

        for (Integer key : subtasksId) {

            if (minTime == null)
                minTime = subtask.get(key).getStartTime();

            if (maxTime == null)
                maxTime = subtask.get(key).getStartTime();

            if (subtask.get(key).getStartTime().isBefore(minTime) || subtask.get(key).getStartTime().isEqual(minTime)) {
                minTime = subtask.get(key).getStartTime();
            }
            if (subtask.get(key).getStartTime().isAfter(maxTime) || subtask.get(key).getStartTime().isEqual(maxTime)) {
                maxTime = subtask.get(key).getStartTime();
            }
            sum += subtask.get(key).getDuration();
        }

        this.startTime = minTime;
        this.endTime = maxTime;
        this.duration = sum;
    }


}
