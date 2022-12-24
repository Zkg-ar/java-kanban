
import java.util.*;

public class Manager {

    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Subtask> subtasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();


    private int id = 0;

    public int getId() {
        return id;
    }

    public Map<Integer, Task> getTasks() {
        return tasks;
    }

    public List<Subtask> getEpicsSubtasks(Epic epic) {
        ArrayList<Subtask> epicsSubtasks = new ArrayList<>();
        int epicId = epic.getId();
        for (Integer key : subtasks.keySet()) {
            if (subtasks.get(key).getEpicId() == epicId) {
                epicsSubtasks.add(subtasks.get(key));
            }
        }
        return epicsSubtasks;
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public Map<Integer, Epic> getEpics() {
        return epics;
    }

    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким идентификатором не существует");
        }
    }

    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        } else {
            System.out.println("Подзадачи с таким идентификатором не существует");
        }
    }

    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            for (Iterator<Map.Entry<Integer, Subtask>> it = subtasks.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Subtask> entry = it.next();
                if (entry.getValue().getEpicId()==id) {
                    it.remove();
                }
            }
            epics.remove(id);
        } else {
            System.out.println("Эпика с таким идентификатором не существует");
        }
    }


    public int generateId() {
        return ++id;
    }

    public void clearTasksMap() {
        tasks.clear();
    }

    public void clearSubtasksMap() {
        epics.clear();
        subtasks.clear();

    }

    public void clearEpicsMap() {
        epics.clear();
    }

    public void addTask(Task task) {
        tasks.put(id, task);
    }

    public void addSubtask(Subtask subtask) {
        subtasks.put(id, subtask);
    }

    public void addEpic(Epic epic) {

        epics.put(id, epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }


    public void setEpicStatus(Epic epic) {
        List<Subtask> subtasksList = getEpicsSubtasks(epic);
        int counterNewStatus = 0;
        int counterDoneStatus = 0;

        if (subtasksList.isEmpty()) {
            epic.setStatus(Status.NEW);
        }

        for (Subtask subtask : subtasksList) {
            if (subtask.getStatus() == Status.NEW) {
                counterNewStatus++;
            } else if (subtask.getStatus() == Status.DONE) {
                counterDoneStatus++;
            }
        }
        if (counterNewStatus == subtasksList.size()) {
            epic.setStatus(Status.NEW);
        } else if (counterDoneStatus == subtasksList.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

        epics.put(epic.getId(), epic);
    }

}
