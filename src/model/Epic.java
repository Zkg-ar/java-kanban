package model;

import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {
    private Map<Integer,Subtask> subtasks;

    public Epic(int id, String name, String description, Status status) {

        super(id, name, description, status);
        subtasks = new HashMap<>();
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
        subtasks = new HashMap<>();
    }

    @Override
    public void setStatus(Status status){
    }

    @Override
    public String toString() {
        return  id +
                "," + Types.EPIC +
                "," + name +
                "," + description +
                "," + getStatus();
    }

    @Override
    public Status getStatus() {

        int counterNewStatus = 0;
        int counterDoneStatus = 0;

        if (subtasks.isEmpty()) {
            return Status.NEW;
        }

        for (Integer key : subtasks.keySet()) {
            if (subtasks.get(key).getStatus() == Status.NEW) {
                counterNewStatus++;
            } else if (subtasks.get(key).getStatus() == Status.DONE) {
                counterDoneStatus++;
            }
        }
        if (counterNewStatus == subtasks.size()) {
            return Status.NEW;
        } else if (counterDoneStatus == subtasks.size()) {
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    public Map<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Map<Integer,Subtask> subtasks) {
        this.subtasks = subtasks;
    }
}
