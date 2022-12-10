package kanban.managers;

import kanban.task.Task;

public class TaskNode {
    private Task task;
    private TaskNode prevElement;
    private TaskNode nextElement;

    public TaskNode(Task task) {
        this.task = task;
        prevElement = null;
        nextElement = null;
    }

    public void connectLeft(TaskNode node) {
        prevElement = node;
    }

    public void connectRight(TaskNode node) {
        nextElement = node;
    }

    public void cutLeft() {
        prevElement = null;
    }

    public void cutRight() {
        nextElement = null;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskNode getPrevElement() {
        return prevElement;
    }

    public void setPrevElement(TaskNode prevElement) {
        this.prevElement = prevElement;
    }

    public TaskNode getNextElement() {
        return nextElement;
    }

    public void setNextElement(TaskNode nextElement) {
        this.nextElement = nextElement;
    }
}
