import java.util.HashMap;
import java.util.HashSet;

public class Epic extends Task {

    private HashSet<Integer> subtasksId;

    public Epic(int taskId, String nameTask, String description, String status) {
        super(taskId, nameTask, description, status);
        subtasksId = new HashSet<>();
    }

    public Epic() {
        subtasksId = new HashSet<>();
    }

    public void addSubtasks(Integer number, HashMap<Integer, Subtask> subtask) {
        subtasksId.add(number);
        checkCurrentStatus(subtask);
    }

    public void deleteSubtasks(Integer number, HashMap<Integer, Subtask> subtask) {
        subtasksId.remove(number);
        checkCurrentStatus(subtask);
    }

    public HashSet getSetOfSubtasks() {
        return subtasksId;
    }

    private void checkCurrentStatus(HashMap<Integer, Subtask> subtask) {
        int newCount = 0;
        int inProgressCount = 0;
        int doneCount = 0;

        for (Integer key : subtasksId) {
            String tempStr = subtask.get(key).getStatus();
            if (tempStr.equals("NEW")) {
                newCount++;
            } else if (tempStr.equals("IN_PROGRESS")) {
                inProgressCount++;
            } else if (tempStr.equals("DONE")) {
                doneCount++;
            }
        }

        if (newCount !=0 && inProgressCount == 0 && doneCount == 0) {
            this.status = "NEW";
        } else if(doneCount != 0 && newCount == 0 && inProgressCount == 0) {
            this.status = "DONE";
        } else {
            this.status = "IN_PROGRESS";
        }
    }
}
