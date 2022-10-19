import java.util.HashMap;
import java.util.HashSet;

public class Manager {

    int idGenerator;
    HashMap<Integer, Task> tasksMap;
    HashMap<Integer, Epic> epicsMap;
    HashMap<Integer, Subtask> subtasksMap;

    public Manager() {
        idGenerator = 1;
        tasksMap = new HashMap<>();
        epicsMap = new HashMap<>();
        subtasksMap = new HashMap<>();
    }

    public void createNewTask(Object object) {
        if (object.getClass() == new Task().getClass()) {
            tasksMap.put(((Task) object).getId(), (Task) object);
        } else if (object.getClass() == new Epic().getClass()) {
            epicsMap.put(((Epic) object).getId(), (Epic) object);
        } else if (object.getClass() == new Subtask().getClass()) {
            int tempEpicId = ((Subtask) object).getEpicId();
            int tempSubtaskId = ((Subtask) object).getId();
            subtasksMap.put(tempSubtaskId, (Subtask) object);
            epicsMap.get(tempEpicId).addSubtasks(tempSubtaskId, subtasksMap);
        }
    }

    public void getTaskList(){
        if (tasksMap.isEmpty() && epicsMap.isEmpty() && subtasksMap.isEmpty()) {
            System.out.println("Список задач пуст.");
            return;
        }

        for (Integer key : tasksMap.keySet()) {
            System.out.println("Задача номер: " + key + " . Название: " + tasksMap.get(key).getName());
            System.out.println("Описание: " + tasksMap.get(key).getDescription());
            System.out.println("Статус задачи: " + tasksMap.get(key).getStatus() );
        }
        System.out.println();

        for (Integer key : epicsMap.keySet()) {
            System.out.println("Эпик номер: " + key + " . Название: " + epicsMap.get(key).getName());
            System.out.println("Описание: " + epicsMap.get(key).getDescription());
            System.out.println("Статус эпика: " + epicsMap.get(key).getStatus());

            HashSet<Integer> subtaskTempSet = epicsMap.get(key).getSetOfSubtasks();
            for (Integer subKey : subtaskTempSet) {
                System.out.println("Сабтаск номер: " + subKey + " . Название: " + subtasksMap.get(subKey).getName());
                System.out.println("Описание: " + subtasksMap.get(subKey).getDescription());
                System.out.println("Статус сабтаска: " + subtasksMap.get(subKey).getStatus());
            }
            System.out.println();
        }
    }

    public void deleteAllTasks() {
            tasksMap.clear();
            epicsMap.clear();
            subtasksMap.clear();
            System.out.println("Список задач очищен.");
    }

    public Object getTaskById(int number) {
        Object tempTask;
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
        return tempTask;
    }

    public void deleteTaskById(int number) {
        if (tasksMap.containsKey(number)) {
            tasksMap.remove(number);
        } else if (epicsMap.containsKey(number)) {
            HashSet<Integer>  subtasksTempSet  = epicsMap.get(number).getSetOfSubtasks();
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

    public void updateTasks(Object object) {
        if (object.getClass() == new Task().getClass()) {
            tasksMap.put(((Task) object).getId(), (Task) object);
        } else if (object.getClass() == new Epic().getClass()) {
            int tempEpicId = ((Epic) object). getId();

            // удаляем все subtasks у старого epic
            HashSet<Integer>  subtasksTempSet  = epicsMap.get(tempEpicId).getSetOfSubtasks();
            for (Integer key : subtasksTempSet){
                subtasksMap.remove(key);
            }
            epicsMap.put(tempEpicId, (Epic) object);
        } else if (object.getClass() == new Subtask().getClass()) {
            int tempEpicId = ((Subtask) object).getEpicId();
            int tempSubtaskId = ((Subtask) object).getId();
            subtasksMap.put(tempSubtaskId, (Subtask) object);
            epicsMap.get(tempEpicId).addSubtasks(tempSubtaskId, subtasksMap);
        }
    }

    public int generateId(){
        return idGenerator++;
    }

    public void addTestObjects() {
        Task task1 = new Task(generateId(), "Съесть пирожок", "Пирожой с вишней", "NEW");

        int testEpic1 = generateId();
        Epic epic1 = new Epic(testEpic1,"Съесть пиццы кусок", "Кусочек пиццы",
                "DONE");
        Subtask subtask1 = new Subtask(generateId(),"Выбрать пиццу в меню ресторана",
                "Выбор пиццы в меню", "NEW", testEpic1);
        Subtask subtask2 = new Subtask(generateId(), "Заказать пиццу",
                "Заказ пиццы", "DONE", testEpic1);

        int testEpic2 = generateId();
        Epic epic2 = new Epic(testEpic2,"Сходить в магазин", "Магазин у дома", "DONE");
        Subtask subtask3 = new Subtask(generateId(), "Купить булочку", "Простую булочку",
                "DONE", testEpic2);

        Task task2 = new Task(generateId(), "Забрать заказ", "Заказ из магазина техники",
                "DONE");

        createNewTask(task1);
        createNewTask(epic1);
        createNewTask(subtask1);
        createNewTask(subtask2);
        createNewTask(epic2);
        createNewTask(subtask3);
        createNewTask(task2);
    }
    public void updatedTestObjects() {
        int testSubtask3 = 3;
        int testEpic2 = 2;
        Subtask subtask4 = new Subtask(testSubtask3, "Пойти погулять",
                "Пешая прогулка","DONE",testEpic2);
        updateTasks(subtask4);
    }
}