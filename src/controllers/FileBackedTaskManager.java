package controllers;

import model.*;
import exception.ManagerSaveException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    Map<Integer, Task> tasksMap = new HashMap<>();
    Map<Integer, Task> epicsMap = new HashMap<>();
    Map<Integer, Task> subtasksMap = new HashMap<>();


    private File file;
    private static final String TITLE = "id,type,name,description,status,epic\n";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public void clearTasksMap() {
        super.clearTasksMap();
        save();
    }

    @Override
    public void clearSubtasksMap() {
        super.clearSubtasksMap();
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtasksMapForEpic(Subtask subtask) {
        super.updateSubtasksMapForEpic(subtask);
        save();
    }


    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void clearEpicsMap() {
        super.clearEpicsMap();
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();

    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        try {
            Task task = super.getTaskById(id);
            save();
            return task;
        } catch (NullPointerException exp) {
            throw new NullPointerException("Задача с таким id отсутвует");
        }
    }

    @Override
    public Epic getEpicById(int id) {
        try {
            Epic epic = super.getEpicById(id);
            save();
            return epic;
        } catch (NullPointerException exp) {
            throw new NullPointerException("Эпик с таким id отсутвует");
        }
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        try {
            Subtask sb = super.getSubtaskById(id);
            save();
            return sb;
        } catch (NullPointerException exp) {
            throw new NullPointerException("Эпик с таким id отсутвует");
        }
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    private Map<Integer, Task> getTasksOfAllTypes() {
        Map<Integer, Task> all = new TreeMap<>();
        for (Integer key : getTasks().keySet()) {
            all.put(key, getTasks().get(key));
        }

        for (Integer key : getEpics().keySet()) {
            all.put(key, getEpics().get(key));
        }

        for (Integer key : getSubtasks().keySet()) {
            all.put(key, getSubtasks().get(key));
        }


        return all;
    }


    private void save() {
        try (Writer fileWriter = new FileWriter(file)) {
            Map<Integer, Task> map = getTasksOfAllTypes();
            fileWriter.write(TITLE);
            for (Integer key : map.keySet()) {
                fileWriter.append(map.get(key) + "\n");
            }
            fileWriter.append("\n");
            fileWriter.append(historyToString(historyManager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
        String stringsFromFile = "";
        try {
            stringsFromFile = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла.");
        }

        String[]values = stringsFromFile.split("\n");
        for(int i = 1; i < values.length-2;i++){

                try {
                    Task task = fromString(values[i]);
                    if (task instanceof Subtask) {
                        fileBackedTaskManager.subtasksMap.put(task.getId(), task);
                    } else if (task instanceof Epic) {
                        fileBackedTaskManager.epicsMap.put(task.getId(), task);
                    } else if (task instanceof Task) {
                        fileBackedTaskManager.tasksMap.put(task.getId(), task);
                    }
                } catch (NullPointerException exp) {
                    throw  new NullPointerException();
                }
        }
        List<Integer> historyById = historyFromString(values[values.length-1]);
        for (Integer id : historyById) {
            fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getTasksOfAllTypes().get(id));
        }


        return fileBackedTaskManager;

    }


//    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
//        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);
//        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
//            fileReader.readLine();  //Избавляемся от заголовка
//            while (fileReader.ready()) {
//                String input = fileReader.readLine();
//                if (input.trim() != TITLE && !input.isEmpty() && !input.equals("\r")) {
//                    try {
//                        Task task = fromString(input);
//                        if (task instanceof Subtask) {
//                            fileBackedTaskManager.subtasksMap.put(task.getId(), task);
//                        } else if (task instanceof Epic) {
//                            fileBackedTaskManager.epicsMap.put(task.getId(), task);
//                        } else if (task instanceof Task){
//                            fileBackedTaskManager.tasksMap.put(task.getId(), task);
//                        } else {
//                            List<Integer> historyById = historyFromString(input);
//                            for(Integer id : historyById){
//                                fileBackedTaskManager.historyManager.add(fileBackedTaskManager.getTasksOfAllTypes().get(id));
//                            }
//                        }
//
//                    } catch (NullPointerException exp) {
//                        System.out.println("Вернулся не Task/Subtask/Epic,а null");
//                    }
//
//                }
//            }
//        } catch (IOException e) {
//            throw new ManagerSaveException("Ошибка при чтении из файла");
//        }
//        return  fileBackedTaskManager;
//    }


    public static Task fromString(String value) {

        String[] values = value.split(",", 6);
        int id = Integer.parseInt(values[0].trim());
        Types type = Types.valueOf(values[1]);
        String name = values[2];
        String description = values[3];
        Status status = Status.valueOf(values[4]);

        switch (type) {
            case EPIC:
                return new Epic(id, name, description, status);
            case TASK:
                return new Task(id, name, description, status);
            case SUBTASK:
                int epicId = Integer.parseInt(values[5]);
                return new Subtask(id, name, description, status, epicId);
        }
        return null;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    private static String historyToString(HistoryManager manager) {
        List<String> historyList = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            historyList.add(String.valueOf(task.getId()));
        }
        String history = String.join(",", historyList);
        return history;
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        for (String id : value.split(",")) {
            history.add(Integer.parseInt(id));
        }
        return history;
    }

//    public static void main(String[] args) throws ManagerSaveException {
//        FileBackedTaskManager manager = loadFromFile(new File("src/files/ManagerFile.csv"));
//
//
//        Task task1 = new Task(manager.generateId(), "Задача 1",
//                "Задача 1", Status.NEW);
//        manager.addTask(task1);
//
//        Task task2 = new Task(manager.generateId(), "Задача 2",
//                "Задача 2", Status.NEW);
//        manager.addTask(task2);
//
//        Epic epic1 = new Epic(manager.generateId(), "Эпик1", "Описание эпика");
//        manager.addEpic(epic1);
//        Subtask subtask1 = new Subtask(manager.generateId(), "Подзадача 1", "Описание подзадачи 1",
//                Status.NEW, epic1.getId());
//        manager.addSubtask(subtask1);
//        Subtask subtask2 = new Subtask(manager.generateId(), "Подзадача 2", "Описание подзадачи 2",
//                Status.DONE, epic1.getId());
//        manager.addSubtask(subtask2);
//        Subtask subtask3 = new Subtask(manager.generateId(), "Подзадача 3", "Описание подзадачи 3",
//                Status.IN_PROGRESS, epic1.getId());
//        manager.addSubtask(subtask3);
//
//        Epic epic2 = new Epic(manager.generateId(), "Эпик2", "Описание эпика");
//        manager.addEpic(epic2);
//
//
//        manager.getTaskById(1);
//        manager.getTaskById(2);
//        manager.getEpicById(3);
//        manager.getSubtaskById(4);
//        manager.getSubtaskById(5);
//        manager.getSubtaskById(6);
//        manager.getEpicById(7);
//        manager.getTaskById(1);
//        manager.getTaskById(2);
//        manager.getEpicById(3);
//        manager.getTaskById(1);
//        manager.getTaskById(1);
//
//
//        System.out.println("История просмотров:" + "\n" + manager.getHistory());
//
//    }
}

