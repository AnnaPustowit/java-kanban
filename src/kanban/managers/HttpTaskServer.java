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
import java.util.regex.Pattern;
import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer  {
    private static final int PORT = 8081;
    private static final Gson gson = new Gson();
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
        server.createContext("/tasks/", new TasksHandler());                // вернуть список задач по приоритету
        server.createContext("/tasks/task/", new TasksHandler());           //вернуть/удалить лист тасков
        server.createContext("/tasks/epic/", new TasksHandler());           //вернуть/удалить  лист эпиков
        server.createContext("/tasks/subtask/", new TasksHandler());        //вернуть/удалить  лист сабтасков
        server.createContext("/tasks/history/", new TasksHandler());        //вернуть историю
        server.createContext("/tasks/task/?id=", new TasksHandler());       //вернуть/удалить по id
        server.createContext("/tasks/task/update/", new TasksHandler());    //обновление тасков
        server.createContext("/tasks/epic/update/", new TasksHandler());    //обновление эпиков
        server.createContext("/tasks/subtask/update/", new TasksHandler()); //обновление сабтасков
        server.start();
    }

    public void stop() {
        server.stop(5);
        System.out.println("HTTP-сервер остановлен!");
    }

    class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) {
            try {
                String path = exchange.getRequestURI().getPath();
                String method = exchange.getRequestMethod();
                Endpoint endpoint = getEndpoint(path, method);

                switch (endpoint) {
                    case POST: {
                        if (Pattern.matches("^/tasks/task/\\w*+$", path)) {
                            Task task = gson.fromJson(read(exchange), Task.class);
                            taskManager.createNewTask(task);
                        } else if (Pattern.matches("^/tasks/epic/\\w*+$", path)) {
                            Epic epic = gson.fromJson(read(exchange), Epic.class);
                            taskManager.createNewTask(epic);
                        } else if (Pattern.matches("^/tasks/subtask/\\w*+$", path)) {
                            Subtask subtask =  gson.fromJson(read(exchange), Subtask.class);
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
                        if (Pattern.matches("^/tasks/task/update/\\w*+$", path)) {
                            Task task = gson.fromJson(read(exchange), Task.class);
                            taskManager.updateTasks(task);
                        } else if (Pattern.matches("^/tasks/epic/update/\\w*+$", path)) {
                            Epic epic = (Epic) gson.fromJson(read(exchange), Task.class);
                            taskManager.updateTasks(epic);
                        } else if (Pattern.matches("^/tasks/subtask/update/\\w*+$", path)) {
                            Subtask subtask = (Subtask) gson.fromJson(read(exchange), Task.class);
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

        public String read(HttpExchange exchange) throws IOException {
            return new String(exchange.getRequestBody().readAllBytes(), UTF_8);
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
                } else if (Pattern.matches("^/tasks/task/update/\\w*+$", path) || Pattern.matches("^/tasks/epic/update/\\w*+$", path)
                        || Pattern.matches("^/tasks/subtask/update/\\w*+$", path)) {
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
