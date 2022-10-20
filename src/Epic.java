import java.util.Map;
import java.util.HashSet;

public class Epic extends Task {

    private HashSet<Integer> subtasksId;

    public Epic(int taskId, String nameTask, String description, Status status) {
        super(taskId, nameTask, description, status);
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

    public HashSet getSetOfSubtasks() {
        return subtasksId;
    }

    private void checkCurrentStatus(Map<Integer, Subtask> subtask) {
        int newCount = 0;
        int inProgressCount = 0;
        int doneCount = 0;

        for (Integer key : subtasksId) {
            Status status = subtask.get(key).getStatus();
            if (status.equals(Status.NEW)) {
                newCount++;
            } else if (status.equals(Status.IN_PROGRESS)) {
                inProgressCount++;
            } else if (status.equals(Status.DONE)) {
                doneCount++;
            }
        }

        if (newCount !=0 && inProgressCount == 0 && doneCount == 0) {
            this.status = Status.NEW;
        } else if(doneCount != 0 && newCount == 0 && inProgressCount == 0) {
            this.status = Status.DONE;
        } else {
            this.status = Status.IN_PROGRESS;
        }
    }
}
