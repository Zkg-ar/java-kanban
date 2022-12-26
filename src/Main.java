import controllers.Manager;
import model.*;


public class Main {

    public static void main(String[] args) {

        Manager manager = new Manager();
        Task task1 = new Task(manager.generateId(), "Сделать задачу 3-ого спринта",
                "Какой-то текст описания", Status.NEW);
        manager.addTask(task1);
        Task task2 = new Task(manager.generateId(), "Купить подарки на НГ", "Какой-то текст описания",
                Status.NEW);
        manager.addTask(task2);

        System.out.println("Список задач до обновления: " + manager.getTasks().toString());
        task1.setStatus(Status.IN_PROGRESS);

        System.out.println();

        Task task3 = new Task(task2.getId(), "Купить подарчки на 8 Марта", "Какой-то текст описания",
                Status.IN_PROGRESS);
        manager.updateTask(task3);

        System.out.println("Список задач после обновления: " + manager.getTasks().toString());
        System.out.println();

        Epic epic1 = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
        manager.addEpic(epic1);
        Subtask subtask = new Subtask(manager.generateId(), "Подзадача 1", "Описание подзадачи 1",
                Status.NEW,epic1.getId());
        manager.addSubtask(subtask);

        Subtask subtask2 = new Subtask(manager.generateId(), "Подзадача 2", "Описание подзадачи 2",
                Status.NEW,
                epic1.getId());
        manager.addSubtask(subtask2);

        Epic epic2 = new Epic(manager.getId(), "Эпик2", "Описание эпика");
        manager.addEpic(epic2);


        Subtask subtask3 = new Subtask(manager.generateId(), "Подзадача 1", "Описание подзадачи",
                Status.DONE, epic2.getId());
        manager.addSubtask(subtask3);

        //manager.setEpicStatus(epic1);

        System.out.println("ТестСтатуса epic1 " + epic1.getStatus());
        System.out.println("ТестСтатуса epic2 " + epic1.getStatus());

        System.out.println("Список всех эпиков: " + manager.getEpics().toString());
        System.out.println("Список всех подзадач: " + manager.getSubtasks().toString());
        System.out.println("Подзадачи epi1:" + manager.getEpicsSubtasks(epic1).toString());
        System.out.println("Подзадачи epic2:" + manager.getEpicsSubtasks(epic2).toString());
        System.out.println();

        manager.removeTaskById(task3.getId());
        System.out.println("Список задач после удаления: " + manager.getTasks().toString());
        System.out.println();
        manager.removeEpicById(epic1.getId());
        System.out.println("Список эпиков после удаления: " + manager.getEpics().toString());
        System.out.println();
        System.out.println("Список подзадач после удаления эпика: " + manager.getSubtasks().toString());
        System.out.println();

    }
}

