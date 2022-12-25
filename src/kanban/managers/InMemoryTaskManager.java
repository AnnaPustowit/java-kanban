package kanban.managers;

import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    int idGenerator;
    Map<Integer, Task> tasksMap;
    Map<Integer, Epic> epicsMap;
    Map<Integer, Subtask> subtasksMap;
    static HistoryManager historyManager;
    Map<LocalDateTime, Task> taskTreeMap;

    public InMemoryTaskManager() {
        idGenerator = 1;
        tasksMap = new HashMap<>();
        epicsMap = new HashMap<>();
        subtasksMap = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        taskTreeMap = new TreeMap<>();
    }

    @Override
    public TreeMap<LocalDateTime, Task> getPrioritizedTasks() {
        return (TreeMap<LocalDateTime, Task>) taskTreeMap;
    }

    @Override
    public void createNewTask(Task task) {
        if ( !(task instanceof  Epic) && (task.getStartTime() != null)) {//проверка на пересечения по времени
            for (Map.Entry<LocalDateTime, Task> pair : taskTreeMap.entrySet()) {
                if ( ((pair.getKey().isBefore(task.getStartTime()) || pair.getKey().isEqual(task.getStartTime())) &&
                        (task.getStartTime().isBefore(pair.getKey().plusMinutes(pair.getValue().getDuration())) ||
                                task.getStartTime().isEqual(pair.getKey().plusMinutes(pair.getValue().getDuration())))) ||

                        ((task.getStartTime().isBefore(pair.getKey()) || task.getStartTime().isEqual(pair.getKey())) &&
                                (task.getStartTime().plusMinutes(task.getDuration()).isAfter(pair.getKey()) ||
                                        task.getStartTime().plusMinutes(task.getDuration()).isEqual(pair.getKey()))) ||

                        ((pair.getKey().isBefore(task.getStartTime()) || pair.getKey().isEqual(task.getStartTime())) &&
                                (task.getStartTime().plusMinutes(task.getDuration()).isBefore(pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration())) ||
                                        task.getStartTime().plusMinutes(task.getDuration()).isEqual(pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration())))) ||

                        ((task.getStartTime().isBefore(pair.getKey()) || task.getStartTime().isBefore(pair.getKey())) &&
                                (pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration()).isBefore(task.getStartTime().plusMinutes(task.getDuration())) ||
                                        pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration()).isEqual(task.getStartTime().plusMinutes(task.getDuration()))))) {
                    System.out.println("Ошибка - такое время уже занято");
                    return;
                }

            }
            taskTreeMap.put(task.getStartTime(), task);
        }
        System.out.println("Id: " + task.getId());

        if (task instanceof Epic) {
            epicsMap.put(task.getId(), (Epic) task);
        } else if (task instanceof Subtask) {
            int tempEpicId = ((Subtask) task).getEpicId();
            int tempSubtaskId = task.getId();
            if (epicsMap.containsKey(tempEpicId)) {
                 subtasksMap.put(tempSubtaskId, (Subtask) task);
                 epicsMap.get(tempEpicId).addSubtasks(tempSubtaskId, subtasksMap);
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
        for (Map.Entry<Integer, Task> pair : tasksMap.entrySet()) {

            taskTreeMap.remove(pair.getValue().getStartTime());//удалить все ключи-даты по таскам

            historyManager.remove(pair.getKey());
        }
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasks();
        for (Map.Entry<Integer, Epic> pair : epicsMap.entrySet()) {
            historyManager.remove(pair.getKey());
        }
        epicsMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        for (Map.Entry<Integer, Subtask> pair : subtasksMap.entrySet()) {

            taskTreeMap.remove(pair.getValue().getStartTime());//удалить все ключи-даты по сабтаскам

            historyManager.remove(pair.getKey());
        }
        subtasksMap.clear();
        for (Map.Entry<Integer, Epic> pair : epicsMap.entrySet()) {
            pair.getValue().clearSubtasksList();
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
            return new Task();
        }
        historyManager.add(tempTask);
        return tempTask;
    }

    @Override
    public void deleteTaskById(int number) {
        if (tasksMap.containsKey(number)) {

            taskTreeMap.remove(tasksMap.get(number).getStartTime());//NEW

            tasksMap.remove(number);

        } else if (epicsMap.containsKey(number)) {
            HashSet<Integer> subtasksTempSet = epicsMap.get(number).getSetOfSubtasks();

            for (Integer subKey : subtasksTempSet) {
                LocalDateTime tempTime = subtasksMap.get(subKey).getStartTime();
                if (tempTime != null) {
                    taskTreeMap.remove(tempTime);
                }
                subtasksMap.remove(subKey);

            }
            epicsMap.remove(number);
        } else if (subtasksMap.containsKey(number)) {

            taskTreeMap.remove(subtasksMap.get(number).getStartTime());//NEW

            int tempEpicId = subtasksMap.get(number).getEpicId();
            subtasksMap.remove(number);
            epicsMap.get(tempEpicId).deleteSubtasks(number, subtasksMap);
        } else {
            System.out.println("Задача под таким номером еще не создана.");
        }
        historyManager.remove(number);
    }

    @Override
    public void updateTasks(Task task) {
        if (!(task instanceof  Epic)) { //проверка на пересечения по времени
            for (Map.Entry<LocalDateTime, Task> pair : taskTreeMap.entrySet()) {
                if (((pair.getKey().isBefore(task.getStartTime()) || pair.getKey().isEqual(task.getStartTime())) &&
                        (task.getStartTime().isBefore(pair.getKey().plusMinutes(pair.getValue().getDuration())) ||
                                task.getStartTime().isEqual(pair.getKey().plusMinutes(pair.getValue().getDuration())))) ||

                        ((task.getStartTime().isBefore(pair.getKey()) || task.getStartTime().isEqual(pair.getKey())) &&
                                (task.getStartTime().plusMinutes(task.getDuration()).isAfter(pair.getKey()) ||
                                        task.getStartTime().plusMinutes(task.getDuration()).isEqual(pair.getKey()))) ||

                        ((pair.getKey().isBefore(task.getStartTime()) || pair.getKey().isEqual(task.getStartTime())) &&
                                (task.getStartTime().plusMinutes(task.getDuration()).isBefore(pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration())) ||
                                        task.getStartTime().plusMinutes(task.getDuration()).isEqual(pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration())))) ||

                        ((task.getStartTime().isBefore(pair.getKey()) || task.getStartTime().isBefore(pair.getKey())) &&
                                (pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration()).isBefore(task.getStartTime().plusMinutes(task.getDuration())) ||
                                        pair.getValue().getStartTime().plusMinutes(pair.getValue().getDuration()).isEqual(task.getStartTime().plusMinutes(task.getDuration()))))) {
                    System.out.println("Ошибка обновления - такое время уже занято");
                    return;
                }
            }
        }
        LocalDateTime timeForRemove = null;
        for (Map.Entry<LocalDateTime, Task> pair : taskTreeMap.entrySet()) {
            if (pair.getValue().getId() == task.getId()) {//если у таска внутри дат есть ид такой,то удалить эту пару
                timeForRemove = pair.getKey();
            }
        }
        taskTreeMap.remove(timeForRemove);
        taskTreeMap.put(task.getStartTime(), task);//вызвать метод добавления таска с новым временем

        if (task instanceof Epic) {
            int tempEpicId = task.getId();
            if (epicsMap.containsKey(tempEpicId)) {
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
        return (List<Task>) historyManager.getHistory();
    }
}

