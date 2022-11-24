package kanban.managers;

import kanban.task.Task;

import java.util.ArrayList;
import java.util.Iterator;

class MyLinkedList {

    private TaskNode head;
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
            head.connectLeft(node);
            head.connectRight(node);
            node.connectLeft(head);
            node.connectRight(head);
        } else {
            TaskNode temp = head.getPrevElement();
            temp.connectRight(node);
            node.connectLeft(temp);
            node.connectRight(head);
        }
        size++;
    }

    public void removeNode(TaskNode node) {
        if (node.getPrevElement() != null && node.getNextElement() != null && size > 2) {
            if (head.equals(node)) {
                head = head.getNextElement();
            }

            TaskNode left = node.getPrevElement();
            TaskNode right = node.getNextElement();

            left.connectRight(right);
            right.connectLeft(left);
        } else if (size == 2) {
            if (head.equals(node)) {
                head = head.getNextElement();
            }
            TaskNode right = node.getNextElement();
            right.cutLeft();
            right.cutRight();
        } else {
            head = null;
        }
        size--;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> array = new ArrayList<>();
        if (head != null) {
            array.add(head.getTask());
            if (size > 1) {
                TaskNode node = head.getNextElement();
                while (node != head) {
                    array.add(node.getTask());
                    node = node.getNextElement();
                }
            }
        }
        return array;
    }


}
