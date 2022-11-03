package kanban.managers;

import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class InMemoryTaskManager implements TaskManager {
    int idGenerator;
    Map<Integer, Task> tasksMap;
    Map<Integer, Epic> epicsMap;
    Map<Integer, Subtask> subtasksMap;
    HistoryManager historyManager;

    public InMemoryTaskManager() {
        idGenerator = 1;
        tasksMap = new HashMap<>();
        epicsMap = new HashMap<>();
        subtasksMap = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createNewTask(Task task) {
        if (task instanceof Epic) {
            epicsMap.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            int tempEpicId = ((Subtask)task).getEpicId();
            int tempSubtaskId = task.getId();
            if (epicsMap.containsKey(tempEpicId)) {
                subtasksMap.put(tempSubtaskId, (Subtask) task);
                epicsMap.get(tempEpicId).addSubtasks(tempSubtaskId, subtasksMap);
            } else {
                System.out.println("Для этой подзадачи не существует эпика. Добавление не выполнено.");
            }
        } else if (task instanceof Task) {
            tasksMap.put(task.getId(), task);
        }
    }

    @Override
    public List getTasksList() {
        List<Task> tasksList = new ArrayList<>();

        for (Integer key : tasksMap.keySet()) {
            System.out.println("Задача номер: " + key + " . Название: " + tasksMap.get(key).getName());
            System.out.println("Описание: " + tasksMap.get(key).getDescription());
            System.out.println("Статус задачи: " + tasksMap.get(key).getStatus());
            tasksList.add(tasksMap.get(key));
        }
        System.out.println();
        return tasksList;
    }

    @Override
    public List getEpicsList() {
        List<Epic> epicsList = new ArrayList<>();

        for (Integer key : epicsMap.keySet()) {
            System.out.println("Эпик номер: " + key + " . Название: " + epicsMap.get(key).getName());
            System.out.println("Описание: " + epicsMap.get(key).getDescription());
            System.out.println("Статус эпика: " + epicsMap.get(key).getStatus());
            epicsList.add(epicsMap.get(key));
        }
        System.out.println();
        return epicsList;
    }

    @Override
    public List getSubtasksList() {
        List<Subtask> subtasksList = new ArrayList<>();

        for (Integer key : subtasksMap.keySet()) {
            System.out.println("Сабтаск номер: " + key + " . Название: " + subtasksMap.get(key).getName());
            System.out.println("Описание: " + subtasksMap.get(key).getDescription());
            System.out.println("Статус сабтаска: " + subtasksMap.get(key).getStatus());
            subtasksList.add(subtasksMap.get(key));
        }
        System.out.println();
        return subtasksList;
    }

    @Override
    public void deleteAllTasks() {
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        epicsMap.clear();
        subtasksMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasksMap.clear();
        for (int i = 0; i < epicsMap.size(); i++) {
            epicsMap.get(i).clearSubtasksList();
        }
    }

    @Override
    public Task getTaskById(int number) {
        Task tempTask;
        if (tasksMap.containsKey(number)) {
            tempTask = tasksMap.get(number);
        } else if (epicsMap.containsKey(number)) {
            tempTask = epicsMap.get(number);
        } else if (subtasksMap.containsKey(number)) {
            tempTask = subtasksMap.get(number);
        } else {
            System.out.println("Задача под таким номером еще не создана.");
            tempTask = new Task();
        }
        historyManager.add(tempTask);
        return tempTask;
    }

    @Override
    public void deleteTaskById(int number) {
        if (tasksMap.containsKey(number)) {
            tasksMap.remove(number);
        } else if (epicsMap.containsKey(number)) {
            HashSet<Integer> subtasksTempSet = epicsMap.get(number).getSetOfSubtasks();
            for (Integer subKey : subtasksTempSet) {
                subtasksMap.remove(subKey);
            }
            epicsMap.remove(number);
        } else if (subtasksMap.containsKey(number)) {
            int tempEpicId = subtasksMap.get(number).getEpicId();
            subtasksMap.remove(number);
            epicsMap.get(tempEpicId).deleteSubtasks(number, subtasksMap);
        } else {
            System.out.println("Задача под таким номером еще не создана.");
        }
    }

    @Override
    public void updateTasks(Task task) {
        if (task instanceof Epic) {
            int tempEpicId = task.getId();
            if (epicsMap.containsKey(tempEpicId)) {
                // удаляем все subtasks у старого epic
                HashSet<Integer> subtasksTempSet = epicsMap.get(tempEpicId).getSetOfSubtasks();
                for (Integer key : subtasksTempSet) {
                    subtasksMap.remove(key);
                }
                epicsMap.put(tempEpicId, (Epic) task);
            } else {
                System.out.println("Эпика с таким номером не существует. Обновление не выполнено.");
            }
        } else if (task instanceof  Subtask) {
            int tempEpicId = ((Subtask)task).getEpicId();
            int tempSubtaskId = task.getId();
            if (epicsMap.containsKey(tempEpicId) && subtasksMap.containsKey(tempSubtaskId)) {
                subtasksMap.put(tempSubtaskId, (Subtask) task);
                epicsMap.get(tempEpicId).addSubtasks(tempSubtaskId, subtasksMap);
            } else {
                System.out.println("Подзадачи или эпика с таким номером не существует. Обновление не выполнено.");
            }
        } else if (task instanceof Task) {
            if (tasksMap.containsKey(task.getId())) {
                tasksMap.put(task.getId(), task);
            } else {
                System.out.println("Задачи с таким номером не существует. Обновление не выполнено.");
            }
        }
        historyManager.update(task);

    }

    @Override
    public int generateId(){
        return idGenerator++;
    }

    @Override
    public List<Task> getHistory(){
        return historyManager.getHistory();
    }


}