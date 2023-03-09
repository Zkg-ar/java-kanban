package tests.modelsTest;

import model.Epic;
import model.Status;
import model.Subtask;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    private static Epic epic1;
    private static Map<Integer, Subtask> subtasks;

    @BeforeEach
     void BeforeEach() {
        subtasks = new HashMap<>();
        epic1 = new Epic(1, "epic1", "description");
    }

    @Test
    void getStatusWhenAllSubtasksStatusDone() {
        subtasks.put(2, new Subtask(2,
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.DONE,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 2, 15, 0),
                Duration.ofMinutes(24)));
        subtasks.put(3, new Subtask(3,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 12, 15, 0),
                Duration.ofMinutes(9)));
        subtasks.put(4,new Subtask(4,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic1.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13)));


        epic1.setSubtasks(subtasks);

        assertEquals(Status.DONE, epic1.getStatus());
    }

    @Test
    void getStatusWhenAllSubtasksStatusNew() {
        subtasks.put(2, new Subtask(2,
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.NEW,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 2, 15, 0),
                Duration.ofMinutes(24)));
        subtasks.put(3, new Subtask(3,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.NEW,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 12, 15, 0),
                Duration.ofMinutes(9)));
        subtasks.put(4,new Subtask(4,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.NEW,
                epic1.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13)));

        epic1.setSubtasks(subtasks);

        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void getStatusWhenMapOfSubtasksIsEmpty() {
        assertEquals(Status.NEW, epic1.getStatus());
    }

    @Test
    void getStatusWhenSubtasksStatusDoneAndNew() {

        subtasks.put(2, new Subtask(2,
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.DONE,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 2, 15, 0),
                Duration.ofMinutes(24)));
        subtasks.put(3, new Subtask(3,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.NEW,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 12, 15, 0),
                Duration.ofMinutes(9)));
        subtasks.put(4,new Subtask(4,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic1.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13)));

        epic1.setSubtasks(subtasks);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }

    @Test
    void getStatusWhenAllSubtasksStatusInProgress() {

        subtasks.put(2, new Subtask(2,
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.IN_PROGRESS,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 2, 15, 0),
                Duration.ofMinutes(24)));
        subtasks.put(3, new Subtask(3,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.IN_PROGRESS,
                epic1.getId(),
                LocalDateTime.of(2022, Month.APRIL, 12, 15, 0),
                Duration.ofMinutes(9)));
        subtasks.put(4,new Subtask(4,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.IN_PROGRESS,
                epic1.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13)));

        epic1.setSubtasks(subtasks);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus());
    }
}