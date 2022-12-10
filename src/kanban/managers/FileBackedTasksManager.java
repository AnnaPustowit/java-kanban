package kanban.managers;

import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

import java.io.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static kanban.task.Task.Type;
import static kanban.task.Task.Status;


public class FileBackedTasksManager extends InMemoryTaskManager {

    final String  FILE_LINE = "id,type,name,status,description,epic";
    static File tasksFile = new File("tasksFile.txt");

    public FileBackedTasksManager() {
        super();
    }

    public void save() throws ManagerSaveException {
        clearFile();
        try (FileWriter fw = new FileWriter(tasksFile, true)) {
            ArrayList<Task> taskArray = (ArrayList<Task>) historyManager.getHistory();
            fw.write(FILE_LINE + '\n');
            for (Task task : taskArray) {
                fw.write(task.toString() + '\n');
            }
            fw.write('\n');
            fw.write(historyToString(taskArray));
        } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new ManagerSaveException("Файл не существует.");
        } catch (IOException e) {
                e.printStackTrace();
                throw new ManagerSaveException("Исключение ввода-вывода.");
        }
    }

    static FileBackedTasksManager loadFromFile() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(tasksFile))) {
            if(br.ready()) {
                br.readLine();
            } else{
                throw new EOFException("Файл истории задач пуст.");
            }
            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                Task rebuiltTask = fromString(line);
                if(rebuiltTask instanceof Epic) {
                    fileBacked.epicsMap.put(rebuiltTask.getId(), (Epic) rebuiltTask);//  test
                } else if (rebuiltTask instanceof Subtask) {
                    fileBacked.subtasksMap.put(rebuiltTask.getId(), (Subtask) rebuiltTask);
                } else {
                    fileBacked.tasksMap.put(rebuiltTask.getId(), rebuiltTask);
                }
            }
            List<Integer> rebuiltHistory = historyFromString(br.readLine());
            fileBacked.restoreHistory(rebuiltHistory);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBacked;
    }

    public static Task fromString(String value) {
        String[] array = value.split(",");
        int id = Integer.parseInt(array[0]);
        String types = array[1];
        Type type = null;
        String name = array[2];
        String description = array[3];
        Status status = Status.valueOf(array[4]);
        int epicId = 0;
        switch (types) {
            case "TASK":
                type = Type.valueOf(types);
                return new Task(id, type, name, description, status);
            case "EPIC":
                type = Type.valueOf(types);
                return new Epic(id, type, name, description, status);
            case "SUBTASK":
                type = Type.valueOf(types);
                epicId = Integer.parseInt(array[5]);
                return new Subtask(id, type, name, description, status, epicId);
            default:
                return null;
        }
    }

    static String historyToString(ArrayList<Task> taskArray) {
        String history = "";
        for (int i = 0; i < taskArray.size() - 1; i++) {
            history += taskArray.get(i).getId() + ",";
        }
        if (taskArray.size() > 0)
            history += taskArray.get(taskArray.size() - 1).getId();
        return history;
    }

    static List<Integer> historyFromString(String value) {
        String[] historyString = value.split(",");
        List<Integer> historyInt = new LinkedList<>();
        for (int i = 0; i < historyString.length; i++) {
            historyInt.add(Integer.parseInt(historyString[i]));
        }
        return historyInt;
    }

    public void clearFile() {
        try (FileWriter fw = new FileWriter(tasksFile)) {
            fw.write("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreHistory(List<Integer> idList) {
        for (Integer id : idList) {
            historyManager.add(getTaskById(id));
        }
    }

    static boolean isHistoryFileExist() {
        return tasksFile.isFile();
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        save();
    }

    @Override
    public List getTasksList() {
        return super.getTasksList();
    }

    @Override
    public List getEpicsList() {
        return super.getEpicsList();
    }

    @Override
    public List getSubtasksList() {
        return super.getSubtasksList();

    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int number) {
        return super.getTaskById(number);
    }

    @Override
    public void deleteTaskById(int number) {
        super.deleteTaskById(number);
        save();
    }

    @Override
    public void updateTasks(Task task) {
        super.updateTasks(task);
        save();
    }

    @Override
    public int generateId() {
        return super.generateId();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
