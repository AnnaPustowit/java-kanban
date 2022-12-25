package kanban.managers;

import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static kanban.task.Task.Type;
import static kanban.task.Task.Status;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private final String  FILE_LINE = "id,type,name,status,description,epic,startTime,duration";
    private static File tasksFile = new File("tasksFile.txt");

    public FileBackedTasksManager() {
        super();
    }

    private void save() throws ManagerSaveException {
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

    public static FileBackedTasksManager loadFromFile() {
        FileBackedTasksManager fileBacked = new FileBackedTasksManager();
        try (BufferedReader br = new BufferedReader(new FileReader(tasksFile))) {
            if(!br.ready()) {
                throw new EOFException("Файл истории задач пуст.");
            }
            String line = br.readLine();
            if (!line.equals(fileBacked.FILE_LINE)) {
                throw new IOException("Файл поврежден.");
            }
            boolean isTasksExist = false;
            while (br.ready()) {
                line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                Task rebuiltTask = fromString(line);
                if(rebuiltTask instanceof Epic) {
                    fileBacked.epicsMap.put(rebuiltTask.getId(), (Epic) rebuiltTask);
                } else if (rebuiltTask instanceof Subtask) {
                    fileBacked.subtasksMap.put(rebuiltTask.getId(), (Subtask) rebuiltTask);
                } else {
                    fileBacked.tasksMap.put(rebuiltTask.getId(), rebuiltTask);
                }
                isTasksExist = true;
            }
            if (isTasksExist) {
                List<Integer> rebuiltHistory = historyFromString(br.readLine());
                fileBacked.restoreHistory(rebuiltHistory);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileBacked;
    }

    private static Task fromString(String value) {
        String[] array = value.split(",");
        int id = Integer.parseInt(array[0]);
        String types = array[1];
        Type type = null;
        String name = array[2];
        String description = array[3];
        Status status = Status.valueOf(array[4]);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime localTime = null;
        if(array[5] != null) {
            localTime = LocalDateTime.parse(array[5], formatter);
        } else {
            localTime = null;
        }

        int tasksDuration = Integer.parseInt(array[6]);
        int epicId = 0;
        switch (types) {
            case "TASK":
                type = Type.valueOf(types);
                return new Task(id, type, name, description, status, localTime, tasksDuration);
            case "EPIC":
                type = Type.valueOf(types);
                return new Epic(id, type, name, description, status,localTime, tasksDuration);
            case "SUBTASK":
                type = Type.valueOf(types);
                epicId = Integer.parseInt(array[7]);
                return new Subtask(id, type, name, description, status,localTime, tasksDuration, epicId);
            default:
                return null;
        }
    }

    private static String historyToString(ArrayList<Task> taskArray) {
        String history = "";
        for (int i = 0; i < taskArray.size() - 1; i++) {
            history += taskArray.get(i).getId() + ",";
        }
        if (!taskArray.isEmpty())
            history += taskArray.get(taskArray.size() - 1).getId();
        return history;
    }

    private static List<Integer> historyFromString(String value) {
        String[] historyString = value.split(",");
        List<Integer> historyInt = new LinkedList<>();
        for (int i = 0; i < historyString.length; i++) {
            historyInt.add(Integer.parseInt(historyString[i]));
        }
        return historyInt;
    }

    private void clearFile() {
        try (FileWriter fw = new FileWriter(tasksFile)) {
            fw.write("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void restoreHistory(List<Integer> idList) {
        for (Integer id : idList) {
            historyManager.add(getTaskById(id));
        }
    }

    public static boolean isHistoryFileExist() {
        return tasksFile.isFile();
    }

    @Override
    public void createNewTask(Task task) {
        super.createNewTask(task);
        //save();
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
        Task taskForReturn = super.getTaskById(number);
        save();
        return taskForReturn;

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
}
