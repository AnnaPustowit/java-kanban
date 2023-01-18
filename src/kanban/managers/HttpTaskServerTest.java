package kanban.managers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HttpTaskServerTest {
    ;
    GsonBuilder builder;
    Gson gson ;
    private HttpTaskServer server;
    private KVServer kvServer;
    private TaskManager tm;
    private Task taskTest;
    private Epic epicTest;
    private Subtask subtaskTest;
    LocalDateTime localDateTimeTask;
    LocalDateTime localDateTimeSubtask;

    @BeforeEach
    void init() throws IOException, InterruptedException {
        kvServer = new KVServer();
        kvServer.start();
        tm = Managers.getDefaultHtpManager();//тут новый серверный менеджер
        builder = new GsonBuilder();
        builder.serializeNulls();
        gson = builder.create();
        server = new HttpTaskServer(tm);
        localDateTimeTask = LocalDateTime.of(2022,12, 23,11, 0);
        taskTest = new Task(1, Task.Type.TASK, "Задача 1.", "Описание задачи 1.", Task.Status.NEW, localDateTimeTask,30);
        tm.createNewTask(taskTest);
        epicTest = new Epic(2, Task.Type.EPIC, "Эпик 2.", "Описание эпика 2.", Task.Status.NEW, null,0);
        tm.createNewTask(epicTest);
        localDateTimeSubtask = LocalDateTime.of(2022,12, 23,13, 0);
        subtaskTest = new Subtask(3, Task.Type.SUBTASK, "Сабтаск 3.", "Описание сабтаска 3.", Task.Status.NEW, localDateTimeSubtask,30,2);
        tm.createNewTask(subtaskTest);
        server.start();
    }
    @AfterEach
    public void stop() {
        server.stop();
        kvServer.stop();
    }

    @Test
    void postTasks() throws IOException, InterruptedException { //ведутся работы
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/task/"));
        Task newTask = new Task(4, Task.Type.TASK, "Задача 4.", "Описание задачи 4.", Task.Status.NEW, LocalDateTime.of(2022,12, 23,14, 0),30);
        String json = gson.toJson(newTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        System.out.println(json);

        Task t = tm.getTaskById(4);
        String s1 = newTask.toString();
        String s2 = t.toString();
        assertEquals(s1,s2);
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/task/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Task>>(){}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);
        assertNotNull(actual);

        assertEquals(1, actual.size());
        String s1 = taskTest.toString();
        String s2 = actual.get(0).toString();
        assertEquals(s1,s2);
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/epic/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);
        assertNotNull(actual);

        assertEquals(1, actual.size());
        String s1 = epicTest.toString();
        String s2 = actual.get(0).toString();
        assertEquals(s1,s2);
    }

    @Test
    void getAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/subtask/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<List<Subtask>>(){}.getType();
        List<Subtask> actual = gson.fromJson(response.body(), subtaskType);
        assertNotNull(actual);

        assertEquals(1, actual.size());
        String s1 = subtaskTest.toString();
        String s2 = actual.get(0).toString();
        assertEquals(s1,s2);
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/task/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Task>>(){}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertEquals(null,actual);
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/epic/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);

        assertEquals(null, actual);
    }

    @Test
    void deleteAllSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/subtask/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<List<Subtask>>(){}.getType();
        List<Epic> actual = gson.fromJson(response.body(), subtaskType);

        assertEquals(null, actual);
    }

    @Test
    void getTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/task/?id=1"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksSrc = new ArrayList<>();
        tasksSrc.add(taskTest);
        String json = gson.toJson(tasksSrc);

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertNotNull(jsonArray);
        String s1 = json;
        String s2 = jsonArray.toString();

        assertEquals(s1,s2);
    }

    @Test
    void deleteTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/task/?id=1"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<List<Task>>(){}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);
        assertEquals(null, actual);
    }

    @Test
    void postTaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/task/update/"));
        Task updateTask = new Task(1, Task.Type.TASK, "Теперь это другая задача 1.", "Теперь это новое описание задачи 1.", Task.Status.NEW, LocalDateTime.of(2022,12, 23,20, 0),30);
        String json = gson.toJson(updateTask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Task t = tm.getTaskById(1);
        String s1 = updateTask.toString();
        System.out.println(s1);
        String s2 = t.toString();
        System.out.println(s2);
        assertEquals(s1,s2);
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        tm.getTaskById(1);
        tm.getTaskById(2);
        tm.getTaskById(3);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/history/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksSrc = new ArrayList<>();
        tasksSrc.add(taskTest);
        tasksSrc.add(epicTest);
        tasksSrc.add(subtaskTest);
        String json = gson.toJson(tasksSrc);

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        assertNotNull(jsonArray);
        String s1 = json;
        String s2 = jsonArray.toString();

        assertEquals(s1,s2);
    }

    @Test
    void getPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = (URI.create("http://localhost:8081/tasks/"));
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Map<LocalDateTime, Task> treeMap = new TreeMap<>();
        treeMap.put(localDateTimeTask,taskTest);
        treeMap.put(localDateTimeSubtask,subtaskTest);
        String json = gson.toJson(treeMap);

        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonObject jsonMap = jsonElement.getAsJsonObject();

        assertNotNull(jsonMap);
        String s1 = json;
        String s2 = jsonMap.toString();

        assertEquals(s1,s2);
    }
}
