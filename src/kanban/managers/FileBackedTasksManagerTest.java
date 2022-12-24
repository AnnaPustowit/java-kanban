package kanban.managers;

import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {
    @BeforeEach
    public void makeTestFileBackedTasksManager() {
        taskManager = new FileBackedTasksManager();
    }

   @Test
    void loadFromFile() {
       String s1 = "id,type,name,status,description,epic,startTime,duration";
       String s2 = "1,TASK,Задача 1.,Описание задачи 1.,NEW,23.10.2022 11:00,30";
       String s3 = "2,EPIC,Эпик 1.,Описание эпика 1.,NEW,23.10.2022 12:00,30";
       String s4 = "3,SUBTASK,Сабтаск 1.,Описание сабтаска 1.,NEW,23.10.2022 12:00,30,2";
       String s5 = "";
       String s6 = "1,2,3";
       String[] arrayString = new String[6];
       arrayString[0] = s1;
       arrayString[1] = s2;
       arrayString[2] = s3;
       arrayString[3] = s4;
       arrayString[4] = s5;
       arrayString[5] = s6;

       String[] arrayFile = new String[6];
        try (BufferedReader br = new BufferedReader(new FileReader("tasksFile.txt"))) {
            int count = 0;
            while (count < 6) {
                String line = br.readLine();
                arrayFile[count] = line;
                count++;
           }
        } catch (EOFException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
       assertArrayEquals(arrayString, arrayFile, "Массивы не равны!");

       FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile();

       LocalDateTime localTimeTask = LocalDateTime.of(2022,10, 23,11, 0);
       Task testTaskFromFile = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localTimeTask,30);
       LocalDateTime localTimeEpicAndSubtask = LocalDateTime.of(2022,10, 23,12, 0);
       Epic testEpicFromFile = new Epic(2, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, localTimeEpicAndSubtask,30);
       Subtask testSubtaskFromFile = new Subtask(3, Task.Type.SUBTASK, "Сабтаск 1.", "Описание сабтаска 1.", Task.Status.NEW, localTimeEpicAndSubtask,30,2);

       String testTaskFromFileString = testTaskFromFile.toString();
       String testEpicFromFileString = testEpicFromFile.toString();
       String testSubtaskFromFileString = testSubtaskFromFile.toString();
       final Task testTask = fileBackedTasksManager.getTaskById(1);
       final Task testEpic = fileBackedTasksManager.getTaskById(2);
       final Task testSubtask = fileBackedTasksManager.getTaskById(3);
       final List<Task> test1 = fileBackedTasksManager.getTasksList();
       final List<Task> test2 = fileBackedTasksManager.getEpicsList();
       final List<Task> test3 = fileBackedTasksManager.getSubtasksList();
       final String taskString = testTask.toString();
       final String epicString = testEpic.toString();
       final String subtaskString = testSubtask.toString();

       assertEquals(1,test1.size(),"Список задач пуст.");
       assertEquals(1,test2.size(),"Список задач пуст.");
       assertEquals(1,test3.size(),"Список задач пуст.");
       assertEquals(testTaskFromFileString,taskString,"Вернуть задачу из файла не удалось.");
       assertEquals(testEpicFromFileString,epicString,"Вернуть эпик из файла не удалось.");
       assertEquals(testSubtaskFromFileString,subtaskString,"Вернуть сабтаск из файла не удалось.");

    }

    @Test
    void createNewTask() {
    }

    @Test
    void deleteAllTasks() {
    }

    @Test
    void deleteAllEpics() {
    }

    @Test
    void deleteAllSubtasks() {
    }

    @Test
    void getTaskById() {
    }

    @Test
    void deleteTaskById() {
    }

    @Test
    void updateTasks() {
    }
}