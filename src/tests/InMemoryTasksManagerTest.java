package tests;


import controllers.HistoryManager;
import controllers.InMemoryTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class InMemoryTasksManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach


    public void beforeEach() {
        manager = new InMemoryTaskManager();

    }

}
