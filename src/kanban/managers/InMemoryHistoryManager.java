package kanban.managers;

import kanban.task.Task;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, TaskNode> historyMap;
    MyLinkedList historyList;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
        historyList = new MyLinkedList();
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> t = historyList.getTasks();
        for (Task task : t) {
            System.out.println(task.getName());
        }
        return t;
    }

    @Override
    public void add(Task task) {
        if (historyMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        TaskNode taskNode = new TaskNode(task);
        historyList.linkLast(taskNode);
        historyMap.put(task.getId(), taskNode);
    }

    @Override
    public void remove(int id) {
        historyList.removeNode(historyMap.get(id));
        historyMap.remove(historyMap.get(id));
    }

    @Override
    public void update(Task newTask){
        for (Map.Entry<Integer, TaskNode> entry : historyMap.entrySet()) {
            if (entry.getKey() == newTask.getId()) {
                TaskNode taskNode = new TaskNode(newTask);
                historyMap.put(entry.getKey(), taskNode);
            }
        }
    }



}