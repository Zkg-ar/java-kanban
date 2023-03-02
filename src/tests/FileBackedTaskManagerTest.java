package tests;

import controllers.FileBackedTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{

    private FileBackedTaskManager fileBackedTaskManager;
    @BeforeEach
    public void beforeEach() throws IOException {
        manager = new FileBackedTaskManager(new File("src/resources/ManagerFile.csv"));
        fileBackedTaskManager = new FileBackedTaskManager(new File("src/resources/ManagerFile.csv"));
    }

    @Test
    public void shouldCorrectlyLoadDataFromFile() throws IOException {

        Task task = new Task(fileBackedTaskManager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));
        fileBackedTaskManager.addTask(task);

        Epic  epic = new Epic(fileBackedTaskManager.generateId(), "Эпик1", "Описание эпика");
        fileBackedTaskManager.addEpic(epic);

        Subtask subtask = new Subtask(fileBackedTaskManager.generateId(),
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 10, 15, 0),
                Duration.ofMinutes(13));
        fileBackedTaskManager.addSubtask(subtask);

        fileBackedTaskManager.getTaskById(task.getId());
        fileBackedTaskManager.getSubtaskById(subtask.getId());
        fileBackedTaskManager.getEpicById(epic.getId());

        fileBackedTaskManager.loadFromFile(new File("src/resources/ManagerFile.csv"));

        assertNotNull(fileBackedTaskManager.getHistory());
        assertEquals(Map.of(task.getId(),task), fileBackedTaskManager.getTasks());
        assertEquals(Map.of(epic.getId(),epic), fileBackedTaskManager.getEpics());
        assertEquals(Map.of(subtask.getId(),subtask), fileBackedTaskManager.getSubtasks());
        assertEquals(List.of(task, subtask,epic), fileBackedTaskManager.getHistory());

    }

    @Test
    public void shouldCorrectlyLoadDataFromFileWithEmptyHistory() throws IOException {

        Task task = new Task(fileBackedTaskManager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));
        fileBackedTaskManager.addTask(task);

        Epic  epic = new Epic(fileBackedTaskManager.generateId(), "Эпик1", "Описание эпика");
        fileBackedTaskManager.addEpic(epic);

        Subtask subtask = new Subtask(fileBackedTaskManager.generateId(),
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 10, 15, 0),
                Duration.ofMinutes(13));
        fileBackedTaskManager.addSubtask(subtask);

        fileBackedTaskManager.loadFromFile(new File("src/resources/ManagerFile.csv"));

        assertEquals(Collections.EMPTY_LIST, fileBackedTaskManager.getHistory());
    }
}
