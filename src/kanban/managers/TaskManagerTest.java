package kanban.managers;
import java.util.List;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest <T extends TaskManager>{

        public static TaskManager taskManager;

        @Test
        void createNewTask() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            taskManager.createNewTask(task1);
            final Task savedTask = taskManager.getTaskById(1);

            assertNotNull(savedTask, "Задача не найдена.");
            assertEquals(task1, savedTask, "Задачи не совпадают.");

            final List<Task> tasks = taskManager.getTasksList();

            assertNotNull(tasks, "Задачи не возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        }

        @Test
        void getTasksList() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            taskManager.createNewTask(task1);

            final List<Task> tasks = taskManager.getTasksList();

            assertNotNull(tasks, "Задачи на возвращаются.");
            assertEquals(1, tasks.size(), "Неверное количество задач.");
            assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
        }

        @Test
        void getEpicsList() {
            Epic epic1 = new Epic(1, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
            taskManager.createNewTask(epic1);

            final List<Task> epics = taskManager.getEpicsList();

            assertNotNull(epics, "Задачи на возвращаются.");
            assertEquals(1, epics.size(), "Неверное количество задач.");
            assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
        }

        @Test
        void getSubtasksList() {
            Epic epic1 = new Epic(1, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
            taskManager.createNewTask(epic1);
            LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
            Subtask subtask2 = new Subtask(2, Task.Type.SUBTASK, "Сабтаск 2.", "Описание сабтаска 2.", Task.Status.NEW, localDateTime2,30,1);
            taskManager.createNewTask(subtask2);
            final List<Task> subtasks = taskManager.getSubtasksList();

            assertNotNull(subtasks, "Задачи на возвращаются.");
            assertEquals(1, subtasks.size(), "Неверное количество задач.");
            assertEquals(subtask2, subtasks.get(0), "Задачи не совпадают.");
        }

        @Test
        void deleteAllTasks() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            taskManager.createNewTask(task1);
            taskManager.deleteAllTasks();
            final List<Task> tasks = taskManager.getTasksList();

            assertEquals(0, tasks.size(), "Удаление не прошло успешно.");
        }

        @Test
        void deleteAllEpics() {
            Epic epic1 = new Epic(1, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
            taskManager.createNewTask(epic1);
            taskManager.deleteAllEpics();
            final List<Task> epics = taskManager.getEpicsList();

            assertEquals(0, epics.size(), "Удаление не прошло успешно.");
        }

        @Test
        void deleteAllSubtasks() {
            LocalDateTime localDateTime2 = LocalDateTime.of(2022,10, 23,12, 0);
            Subtask subtask1 = new Subtask(1, Task.Type.SUBTASK, "Сабтаск 1.", "Описание сабтаска 1.", Task.Status.NEW, localDateTime2,30,2);
            taskManager.createNewTask(subtask1);
            taskManager.deleteAllEpics();
            final List<Task> subtasks = taskManager.getEpicsList();

            assertEquals(0, subtasks.size(), "Удаление не прошло успешно.");
        }

        @Test
        void getTaskById() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,10, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            Epic epic1 = new Epic(2, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
            LocalDateTime localDateTime2 = LocalDateTime.of(2022,10, 23,12, 0);
            Subtask subtask1 = new Subtask(3, Task.Type.SUBTASK, "Сабтаск 1.", "Описание сабтаска 1.", Task.Status.NEW, localDateTime2,30,2);

            taskManager.createNewTask(task1);
            taskManager.createNewTask(epic1);
            taskManager.createNewTask(subtask1);
            final Task savedTask = taskManager.getTaskById(1);
            final Task savedEpic = taskManager.getTaskById(2);
            final Task savedSubtask = taskManager.getTaskById(3);

            assertEquals(task1, savedTask, "Задачи не совпадают.");
            assertEquals(epic1, savedEpic, "Задачи не совпадают.");
            assertEquals(subtask1, savedSubtask, "Задачи не совпадают.");
        }

        @Test
        void deleteTaskById() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,10, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            Epic epic1 = new Epic(2, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
            LocalDateTime localDateTime2 = LocalDateTime.of(2022,10, 23,12, 0);
            Subtask subtask1 = new Subtask(3, Task.Type.SUBTASK, "Сабтаск 1.", "Описание сабтаска 1.", Task.Status.NEW, localDateTime2,30,2);

            taskManager.createNewTask(task1);
            taskManager.createNewTask(epic1);
            taskManager.createNewTask(subtask1);
            taskManager.deleteTaskById(1);
            taskManager.deleteTaskById(2);
            taskManager.deleteTaskById(3);

            final List<Task> tasks = taskManager.getTasksList();
            final List<Task> epics = taskManager.getTasksList();
            final List<Task> subtasks = taskManager.getTasksList();
            assertEquals(0, tasks.size(), "Удаление не прошло успешно.");
            assertEquals(0, epics.size(), "Удаление не прошло успешно.");
            assertEquals(0, subtasks.size(), "Удаление не прошло успешно.");
        }

        @Test
        void updateTasks() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,10, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            LocalDateTime localDateTime2 = LocalDateTime.of(2022,10, 23,12, 0);
            Task updateTask = new Task(1, Task.Type.TASK, "бновленная задача 1.", "Описание обновленной задачи 1.", Task.Status.NEW, localDateTime2,30);
            taskManager.createNewTask(task1);
            taskManager.updateTasks(updateTask);
            final Task savedTask = taskManager.getTaskById(1);
            assertEquals(updateTask, savedTask, "Задачи не совпадают.");


        }

        @Test
        void generateId() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,12, 23,11, 0);
            Task task1 = new Task(taskManager.generateId(), Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);

            Epic epic2 = new Epic(taskManager.generateId(), Task.Type.EPIC, "Эпик 2.", "Описание эпика 2.", Task.Status.NEW, null,0);

            LocalDateTime localDateTime2 = LocalDateTime.of(2022,12, 23,12, 0);
            Subtask subtask3 = new Subtask(taskManager.generateId(), Task.Type.TASK, "Сабтаск 3.", "Описание сабтаска 3.", Task.Status.NEW, localDateTime2,30,2);
            taskManager.createNewTask(task1);
            taskManager.createNewTask(epic2);
            taskManager.createNewTask(subtask3);

            final int idTask = task1.getId();
            final int idEpic = epic2.getId();
            final int idSubtask = subtask3.getId();

            assertEquals(1, idTask, "Неверный id.");
            assertEquals(2, idEpic, "Неверный id.");
            assertEquals(3, idSubtask, "Неверный id.");
        }

        @Test
        void getHistory() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,10, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            Epic epic1 = new Epic(2, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
            LocalDateTime localDateTime2 = LocalDateTime.of(2022,10, 23,12, 0);
            Subtask subtask1 = new Subtask(3, Task.Type.SUBTASK, "Сабтаск 1.", "Описание сабтаска 1.", Task.Status.NEW, localDateTime2,30,2);

            taskManager.createNewTask(task1);
            taskManager.createNewTask(epic1);
            taskManager.createNewTask(subtask1);
            final Task savedTask = taskManager.getTaskById(1);
            final Task savedEpic = taskManager.getTaskById(2);
            final Task savedSubtask = taskManager.getTaskById(3);

            final List<Task> history = taskManager.getHistory();
            assertEquals(3, history.size(), "История пустая.");
        }

        @Test
        void getPrioritizedTasks() {
            LocalDateTime localDateTime1 = LocalDateTime.of(2022,10, 23,11, 0);
            Task task1 = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTime1,30);
            Epic epic1 = new Epic(2, Task.Type.EPIC, "Эпик 1.", "Описание эпика 1.", Task.Status.NEW, null,0);
            LocalDateTime localDateTime2 = LocalDateTime.of(2022,10, 23,12, 0);
            Subtask subtask1 = new Subtask(3, Task.Type.SUBTASK, "Сабтаск 1.", "Описание сабтаска 1.", Task.Status.NEW, localDateTime2,30,2);

            taskManager.createNewTask(task1);
            taskManager.createNewTask(epic1);
            taskManager.createNewTask(subtask1);

            TreeMap<LocalDateTime,Task> time = taskManager.getPrioritizedTasks();
            assertEquals(2, time.size(), "Список задач пуст.");
        }
    }