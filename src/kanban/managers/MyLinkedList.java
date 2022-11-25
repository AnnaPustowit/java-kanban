package kanban.managers;

import kanban.task.Task;

import java.util.ArrayList;
import java.util.Iterator;

class MyLinkedList {

    private TaskNode head;
    private TaskNode tail;
    private  int size;


    public MyLinkedList() {
        head = null;
        size = 0;
    }

    public int size(){
        return size;
    }

    public void linkLast(TaskNode node) {
        if (size == 0) {
            head = node;
        }
        else if (size == 1) {
            head.connectRight(node);
            node.connectLeft(head);
            tail = node;
        } else {
            tail.connectRight(node);
            node.connectLeft(tail);
            tail = node;
        }
        size++;
    }

    public void removeNode(TaskNode node) {
        if (size > 2) {
            if (head.equals(node)) {
                head = head.getNextElement();
                head.cutLeft();
            } else if (tail.equals(node)){
                tail = tail.getPrevElement();
                tail.cutRight();
            } else {

                TaskNode left = node.getPrevElement();
                TaskNode right = node.getNextElement();

                left.connectRight(right);
                right.connectLeft(left);

            }


        } else if (size == 2) {
            if (head.equals(node)) {
                head = tail;
                tail = null;
                head.cutLeft();
            } else if (tail.equals(node)){
                head.cutRight();
                tail = null;
            }
        } else {
            head = null;
        }
        size--;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> array = new ArrayList<>();
        TaskNode node = head;
        while (node != null) {
            array.add(node.getTask());
            node = node.getNextElement();
        }
        return array;
    }

}