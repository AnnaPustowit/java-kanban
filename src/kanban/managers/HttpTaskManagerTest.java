package kanban.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest extends TaskManagerTest {
    GsonBuilder builder;
    Gson gson ;
    private KVServer server;

    @BeforeEach
    public void makeTestHttpTasksManager() throws IOException, InterruptedException {
       server = new KVServer();
       server.start();
       builder = new GsonBuilder();
       builder.serializeNulls();
       gson = builder.create();
       taskManager = Managers.getDefaultHtpManager();
    }

    @AfterEach
    public void stop() {
        server.stop();
    }
}
