package kanban.task;

import java.util.Map;
import java.util.HashSet;

public class Epic extends Task {

    private HashSet<Integer> subtasksId;

    public Epic(int taskId, Type type, String nameTask, String description, Task.Status status) {
        super(taskId, type, nameTask, description, status);
        subtasksId = new HashSet<>();
    }

    public Epic() {
        subtasksId = new HashSet<>();
    }

    public void addSubtasks(Integer number, Map<Integer, Subtask> subtask) {
        subtasksId.add(number);
        checkCurrentStatus(subtask);
    }

    public void deleteSubtasks(Integer number, Map<Integer, Subtask> subtask) {
        subtasksId.remove(number);
        checkCurrentStatus(subtask);
    }

    public void clearSubtasksList() {
        subtasksId.clear();
        status = Task.Status.DONE;
    }

    public HashSet getSetOfSubtasks() {
        return subtasksId;
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
}
