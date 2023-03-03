package tests.controllersTest;

import controllers.InMemoryTaskManager;
import controllers.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected static TaskManager manager;

    @Test
    public void shouldAddNewTask() {
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));

        manager.addTask(task);

        Map<Integer, Task> tasks = manager.getTasks();
        assertNotNull(tasks.size());
        assertEquals(1, task.getId());
    }

    @Test
    public void shouldAddNewEpic() {
        Epic  epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);

        Map<Integer, Epic> epics = manager.getEpics();
        assertNotNull(epics.size());
        assertEquals(1, epic.getId());

        Epic epic2 = new Epic(manager.generateId(), "Эпик2", "Описание эпика");;
        manager.addEpic(epic2);

        assertEquals(2, epics.size());

    }

    @Test
    public void shouldAddNewSubtask() {
        Epic  epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);

        Subtask  subtask = new Subtask(manager.generateId(),
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13));
        manager.addSubtask(subtask);

        Map<Integer, Subtask> subtasks = manager.getSubtasks();
        assertNotNull(subtasks.size());
        assertEquals(2, subtask.getId());
        assertEquals(subtask, subtasks.get(2), "Задачи не совпадают.");

    }

    @Test
    void getTasksMapTest() {
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));

        manager.addTask(task);

        Task task2 = new Task(manager.generateId(), "Задача 2",
                "Задача 2", Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 23, 12, 0), Duration.ofMinutes(15));
        manager.addTask(task2);

        Map<Integer, Task> expected = manager.getTasks();

        Map<Integer, Task> actual = new HashMap<>();
        actual.put(task.getId(), task);
        actual.put(task2.getId(), task2);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.keySet(), actual.keySet());
        assertArrayEquals(expected.values().toArray(), actual.values().toArray());

    }

    @Test
    void getSubtasksMapTest() {
        Epic  epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);

        Epic epic2 = new Epic(manager.generateId(), "Эпик 2",
                "Эпик 2");
        manager.addEpic(epic2);

        Map<Integer, Epic> expected = manager.getEpics();

        Map<Integer, Epic> actual = new HashMap<>();
        actual.put(epic.getId(), epic);
        actual.put(epic2.getId(), epic2);

        assertEquals(expected.size(), actual.size());
        assertEquals(expected.keySet(), actual.keySet());
        assertArrayEquals(expected.values().toArray(), actual.values().toArray());

    }

    @Test
    void shouldClearTasksMap() {
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));
        manager.addTask(task);

        Task task2 = new Task(manager.generateId(), "Задача 1",
                "Задача 1", Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 13, 0), Duration.ofMinutes(15));

        manager.addTask(task2);
        Map<Integer, Task> expected = manager.getTasks();

        assertEquals(2, expected.size());

        manager.clearTasksMap();

        assertEquals(0, expected.size());
        assertTrue(expected.isEmpty());

    }

    @Test
    void shouldClearEpicsMap() {
        Epic  epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);

        Epic epic2 = new Epic(manager.generateId(), "Эпик 2",
                "Эпик 2", Status.NEW);
        manager.addEpic(epic2);

        Map<Integer, Epic> expected = manager.getEpics();

        assertEquals(2, expected.size());

        manager.clearEpicsMap();

        assertEquals(0, expected.size());
        assertTrue(expected.isEmpty());

    }

    @Test
    void shouldRemoveTaskById() {
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));
        manager.addTask(task);

        Task task2 = new Task(manager.generateId(), "Задача 2",
                "Задача 2", Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 3, 12, 0), Duration.ofMinutes(15));
        manager.addTask(task2);

        Map<Integer, Task> expected = manager.getTasks();

        assertEquals(2, expected.size());

        manager.removeTaskById(task.getId());

        assertEquals(1, expected.size());
        assertEquals(task2, expected.get(task2.getId()));
    }

    @Test
    void shouldRemoveEpicById() {
        Epic  epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);

        Epic epic2 = new Epic(manager.generateId(), "Эпик 2",
                "Эпик 2", Status.NEW);
        manager.addEpic(epic2);

        Map<Integer, Epic> expected = manager.getEpics();

        assertEquals(2, expected.size());

        manager.removeEpicById(epic.getId());

        assertEquals(1, expected.size());
        assertEquals(epic2, expected.get(epic2.getId()));
    }

    @Test
    void shouldRemoveSubtaskById() {
        Epic  epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);

        Subtask subtask = new Subtask(manager.generateId(),
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13));
        manager.addSubtask(subtask);

        Subtask subtask2 = new Subtask(manager.generateId(),
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 15, 15, 0),
                Duration.ofMinutes(13));
        manager.addSubtask(subtask2);

        Map<Integer, Subtask> expected = manager.getSubtasks();

        assertEquals(2, expected.size());

        manager.removeSubtaskById(subtask.getId());

        assertEquals(1, expected.size());
        assertEquals(subtask2, expected.get(subtask2.getId()));
    }

    @Test
    void shouldNotRemoveSubtaskByIdWhenBadId() {
        Epic  epic = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic);


        Subtask subtask = new Subtask(manager.generateId(),
                "Подзадача 1",
                "Описание подзадачи 1",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 10, 15, 0),
                Duration.ofMinutes(13));
        manager.addSubtask(subtask);

        Subtask subtask2 = new Subtask(manager.generateId(),
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13));

        manager.addSubtask(subtask2);

        Map<Integer, Subtask> expected = manager.getSubtasks();

        assertEquals(2, expected.size());

        manager.removeSubtaskById(0);

        assertEquals(2, expected.size());
        assertEquals(subtask2, expected.get(subtask2.getId()));
    }

    @Test
    void shouldReturnTrueWhenMapIsEmpty(){
        Map<Integer,Task> map = manager.getTasks();
        assertTrue(map.isEmpty());
    }

    @Test
    void shouldReturnFalseWhenMapIsEmpty(){
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));
        manager.addTask(task);
        Map<Integer,Task> map = manager.getTasks();
        assertFalse(map.isEmpty());
    }

    @Test
    void getTaskByIdTest(){
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));

        manager.addTask(task);

        Task task2 = new Task(1,
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));

        assertNotNull(manager.getTaskById(1));
        assertEquals(manager.getTaskById(1),task2);
    }

    @Test
    void getTaskByIdWhenIdDoesNotExist(){
        assertNull(manager.getTaskById(100));

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> {
                    manager.getTaskById(100).getId();
                });
        assertEquals(null, exception.getMessage());
    }

    @Test
    void getSubtaskByIdWhenIdDoesNotExist(){
        assertNull(manager.getSubtaskById(100));

        final NullPointerException exception = assertThrows(
                NullPointerException.class,

                () -> manager.getSubtaskById(100).getId());

        assertEquals(null, exception.getMessage());
    }

    @Test
    void getEpicByIdWhenIdDoesNotExist(){
        assertNull(manager.getEpicById(100));

        final NullPointerException exception = assertThrows(
                NullPointerException.class,

                () -> manager.getEpicById(100).getId());

        assertEquals(null, exception.getMessage());
    }

    @Test
    void getSubtaskByIdTest(){
        Epic epic = new Epic(manager.generateId(),
                "Epic 1",
                "Epic 1",
                Status.NEW);

        manager.addEpic(epic);
        Subtask subtask1 = new Subtask(manager.generateId(),
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13));

        manager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(2,
                "Подзадача 2",
                "Описание подзадачи 2",
                Status.DONE,
                epic.getId(),
                LocalDateTime.of(2022, Month.AUGUST, 13, 15, 0),
                Duration.ofMinutes(13));

        assertNotNull(manager.getSubtaskById(2));
        assertEquals(manager.getSubtaskById(2),subtask2);
    }

    @Test
    void getEpicByIdTest(){
        Epic epic = new Epic(manager.generateId(),
                "Epic 1",
                "Epic 1",
                Status.NEW);

        manager.addEpic(epic);

        Epic epic2 = new Epic(1,
                "Epic 1",
                "Epic 1",
                Status.NEW);

        assertNotNull(manager.getEpicById(1));
        assertEquals(manager.getEpicById(1),epic2);
    }

    @Test
    void updateTaskTest(){
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));


        manager.addTask(task);

        Task task2  = new Task(task.getId(),
                "Задача 1",
                "Задача 1",
                Status.DONE,
                LocalDateTime.of(2022, Month.APRIL, 1, 12, 0),
                Duration.ofMinutes(15));

        manager.updateTask(task2);

        assertNotNull(manager.getTasks());
        assertEquals(task,manager.getTaskById(1));
        assertEquals(1,manager.getTasks().size());
    }

    @Test
    void updateTaskTestWhenDateTimeNull(){
        Task task = new Task(manager.generateId(),
                "Задача 1",
                "Задача 1",
                Status.NEW);

        manager.addTask(task);

        Task task2  = new Task(task.getId(),
                "Задача 1",
                "Задача 1",
                Status.DONE);

        manager.updateTask(task2);

        assertNotNull(manager.getTasks());
        assertEquals(task,manager.getTaskById(1));
        assertEquals(1,manager.getTasks().size());
    }

}
