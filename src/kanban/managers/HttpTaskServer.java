package kanban.managers;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kanban.task.Epic;
import kanban.task.Subtask;
import kanban.task.Task;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;


public class HttpTaskServer  {
    private static final int PORT = 8081;
    private static final Gson gson = new Gson();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
     TaskManager taskManager;
     HttpServer server;

    public HttpTaskServer() throws IOException, InterruptedException {
        this(new FileBackedTasksManager());
    }
    public HttpTaskServer (TaskManager taskManager) throws IOException, InterruptedException {
        this.taskManager = taskManager;

    }

    public void start() throws IOException  {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks/", new TasksHandler());            // вернуть список задач по приоритету
        server.createContext("/tasks/task/", new TasksHandler());       //вернуть лист тасков
        server.createContext("/tasks/epic/", new TasksHandler());       //вернуть лист эпиков
        server.createContext("/tasks/subtask/", new TasksHandler());    //вернуть лист сабтасков
        server.createContext("/tasks/history/", new TasksHandler());    //
        server.createContext("/tasks/task/?id=", new TasksHandler());
        server.start();
    }

    public void stop() throws IOException  {
        server.stop(5);
        System.out.println("HTTP-сервер остановлен!");
    }

    //public static void main(String[] args) throws IOException {
       // HttpTaskServer sv = new HttpTaskServer();
       // sv.start();
       // sv.stop();
    //}
    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                Endpoint endpoint = getEndpoint(path, method);

                switch (endpoint) {
                    case POST: {
                        if (Pattern.matches("^/tasks/task/task:/\\w*+$", path)) {
                            String s = path.replaceFirst("/tasks/task/task:/", "");//
                            Task task = gson.fromJson(s, Task.class);
                            taskManager.createNewTask(task);
                        } else if (Pattern.matches("^/tasks/epic/epic:/\\w*+$", path)) {
                            String s = path.replaceFirst("/tasks/epic/epic:/", "");//
                            Epic epic = gson.fromJson(s, Epic.class);
                            taskManager.createNewTask(epic);
                        } else if (Pattern.matches("^/tasks/subtask/subtask:/\\w*+$", path)) {
                            String s = path.replaceFirst("/tasks/subtask/subtask:/", "");//
                            Subtask subtask =  gson.fromJson(s, Subtask.class);
                            taskManager.createNewTask(subtask);
                        }
                        exchange.sendResponseHeaders(200, 0);//
                        break;
                    }
                    case GET_TASKS: {
                        String response = gson.toJson(taskManager.getTasksList());//
                        sendText(exchange, response);
                        break;
                    }
                    case GET_EPICS: {
                        String response = gson.toJson(taskManager.getEpicsList());//
                        sendText(exchange, response);
                        break;
                    }
                    case GET_SUBTASKS: {
                        String response = gson.toJson(taskManager.getSubtasksList());//
                        sendText(exchange, response);
                        break;
                    }
                    case DELETE_TASKS: {
                        taskManager.deleteAllTasks();
                        exchange.sendResponseHeaders(200, 0);//
                        break;
                    }
                    case DELETE_EPICS: {
                        taskManager.deleteAllEpics();
                        exchange.sendResponseHeaders(200, 0);//
                        break;
                    }
                    case DELETE_SUBTASKS: {
                        taskManager.deleteAllSubtasks();
                        exchange.sendResponseHeaders(200, 0);//
                        break;
                    }
                    case GET_ID: {
                        String idStr = path.replaceFirst("/tasks/task/\\?id=", "");//
                        int idInt = parsePathInt(idStr);
                        if (idInt != 0) {
                            String response = gson.toJson(taskManager.getTaskById(idInt));
                            sendText(exchange, response);
                            break;
                        }
                    }
                    case DELETE_ID: {
                        String idStr = path.replaceFirst("/tasks/task/\\?id=", "");//
                        int idInt = parsePathInt(idStr);
                        if (idInt != 0) {
                            taskManager.deleteTaskById(idInt);
                            exchange.sendResponseHeaders(200, 0);
                            break;
                        }
                    }
                    case POST_ID: {
                        if (Pattern.matches("^/tasks/task/task:/\\w*+$", path)) {
                            String s = path.replaceFirst("/tasks/task/task:/", "");//
                            Task task = gson.fromJson(s, Task.class);
                            taskManager.updateTasks(task);
                        } else if (Pattern.matches("^/tasks/epic/epic:/\\w*+$", path)) {
                            String s = path.replaceFirst("/tasks/epic/epic:/", "");//
                            Epic epic = (Epic) gson.fromJson(s, Task.class);
                            taskManager.updateTasks(epic);
                        } else if (Pattern.matches("^/tasks/subtask/subtask:/\\w*+$", path)) {
                            String s = path.replaceFirst("/tasks/subtask/subtask:/", "");//
                            Subtask subtask = (Subtask) gson.fromJson(s, Task.class);
                            taskManager.updateTasks(subtask);
                        }
                        exchange.sendResponseHeaders(200, 0);//
                        break;
                    }
                    case GET_HISTORY: {
                        String response = gson.toJson(taskManager.getHistory());//
                        sendText(exchange, response);
                        break;
                    }
                    case GET_PRIORITIZED: {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());//
                        sendText(exchange, response);
                        break;
                    }
                    default: {
                        exchange.sendResponseHeaders(405, 0);
                        break;
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                exchange.close();
            }
        }

        private int parsePathInt(String path) {
            String[] pathId = path.split("/");
            int id;
            try {
                id = Integer.parseInt(pathId[pathId.length - 1]);
            } catch (NumberFormatException exception) {
                id = 0;
            }
            return id;
        }

        private void sendText(HttpExchange exchange, String text) throws IOException {
            byte[] resp = text.getBytes(UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, resp.length);
            exchange.getResponseBody().write(resp);
        }

        private Endpoint getEndpoint(String path, String method) {
            if (method.equals("POST")) {    //POST
                if (Pattern.matches("^/tasks/task/\\w*+$", path) || Pattern.matches("^/tasks/epic/\\w*+$", path)
                        || Pattern.matches("^/tasks/subtask/\\w*+$", path)) {
                    return Endpoint.POST;
                } else if (Pattern.matches("^/tasks/task/\\d+$", path) || Pattern.matches("^/tasks/epic/\\d+$", path)
                        || Pattern.matches("^/tasks/subtask/\\d+$", path)) {
                    return Endpoint.POST_ID;
                }
            } else if (method.equals("GET")) {    // GET
                if (Pattern.matches("^/tasks/task/$", path)) {
                    return Endpoint.GET_TASKS;
                } else if (Pattern.matches("^/tasks/epic/$", path)) {
                    return Endpoint.GET_EPICS;
                } else if (Pattern.matches("^/tasks/subtask/$", path)) {
                    return Endpoint.GET_SUBTASKS;
                } else if (Pattern.matches("^/tasks/task/\\?id=\\d+$", path)) {
                    return Endpoint.GET_ID;
                } else if (Pattern.matches("^/tasks/history/$", path)) {
                    return Endpoint.GET_HISTORY;
                } else if (Pattern.matches("^/tasks/$", path)) {
                    return Endpoint.GET_PRIORITIZED;
                }
            } else if (method.equals("DELETE")) {    // DELETE
                if (Pattern.matches("^/tasks/task/$", path)) {
                    return Endpoint.DELETE_TASKS;
                } else if (Pattern.matches("^/tasks/epic/$", path)) {
                    return Endpoint.DELETE_EPICS;
                } else if (Pattern.matches("^/tasks/subtask/$", path)) {
                    return Endpoint.DELETE_SUBTASKS;
                } else if (Pattern.matches("^/tasks/task/\\?id=\\d+$", path) || Pattern.matches("^/task/subtask/\\?id=\\d+$", path) ||
                        Pattern.matches("^/tasks/epic/\\?id=\\d+$", path)) {
                    return Endpoint.DELETE_ID;
                }
            }
            return Endpoint.UNKNOWN;
        }
    }
        public enum Endpoint {
            POST,
            GET_TASKS,
            GET_EPICS,
            GET_SUBTASKS,
            DELETE_TASKS,
            DELETE_EPICS,
            DELETE_SUBTASKS,
            GET_ID,
            DELETE_ID,
            POST_ID,
            GET_HISTORY,
            GET_PRIORITIZED,
            UNKNOWN
        }

}

