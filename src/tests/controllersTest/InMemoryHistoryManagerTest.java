package tests.controllersTest;

import controllers.HistoryManager;
import controllers.InMemoryHistoryManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager manager;
    private int id = 0;

    @BeforeEach
    void beforeEach(){
        manager = new InMemoryHistoryManager();
    }


    @Test
    void addTaskInHistoryTest() {
        Task task = new Task(id++,
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));

        Task task1 = new Task(id++,
                "Задача 2",
                "Задача 2",
                Status.NEW);

        Task task2 = new Task(id++,
                "Задача 3",
                "Задача 3",
                Status.DONE);

        manager.add(task);
        manager.add(task1);
        manager.add(task2);

        assertEquals(List.of(task,task1,task2),manager.getHistory());
    }
    @Test
    void addTaskInHistoryAfterCallsTest() {
        Task task = new Task(id++,
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));

        Task task1 = new Task(id++,
                "Задача 2",
                "Задача 2",
                Status.NEW);

        Task task2 = new Task(id++,
                "Задача 3",
                "Задача 3",
                Status.DONE);

        manager.add(task);
        manager.add(task1);
        manager.add(task2);
        manager.add(task);

        assertEquals(List.of(task1,task2,task),manager.getHistory());
    }

    @Test
    void shouldRemoveOneTaskFromHistory() {
        Task task1 = new Task(id++,
                "Задача 2",
                "Задача 2",
                Status.NEW);

        manager.add(task1);

        assertEquals(1,manager.getHistory().size());

        manager.remove(task1.getId());

        assertEquals(0,manager.getHistory().size());
    }

    @Test
    void shouldNotRemoveTaskFromHistory() {
        Task task1 = new Task(id++,
                "Задача 2",
                "Задача 2",
                Status.NEW);

        manager.add(task1);

        assertEquals(1,manager.getHistory().size());

        manager.remove(100);

        assertEquals(1,manager.getHistory().size());
    }
}