package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Epic extends Task {
    private Map<Integer, Subtask> subtasks;


    private LocalDateTime endTime;


    public Epic(int id, String name, String description) {
        super(id, name, description);
        subtasks = new TreeMap<>();
        duration = Duration.ofMinutes(0);
        startTime = LocalDateTime.MAX;
    }

    public Epic(int id,String name, String description,Status status){
        super(id,name,description,status);
        subtasks = new TreeMap<>();
        duration = Duration.ofMinutes(0);
        startTime = LocalDateTime.MAX;
    }
    public Epic(int id, String name, String description, LocalDateTime startTime, Duration duration) {
        super(id, name, description);
        this.startTime = startTime;
        this.duration = duration;
        subtasks = new TreeMap<>();
    }

    public Epic(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        subtasks = new TreeMap<>();
    }

    @Override
    public String toString() {
        return id +
                "," + Types.EPIC +
                "," + name +
                "," + description +
                "," + getStatus() +
                "," + getStartTime().get().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) +
                "," + setDuration().toMinutes();
    }

    public Optional<LocalDateTime> getStartTime() {

        if (subtasks.isEmpty()) {
            return Optional.ofNullable(startTime);
        }
        List<Subtask> subtaskList = subtasks.values()
                .stream()
                .sorted(Comparator.comparing(task->task.getStartTime().orElse(null),Comparator.nullsLast(LocalDateTime::compareTo)))
                .collect(Collectors.toList());

        startTime = subtaskList.get(0).getStartTime().get();

        return Optional.of(startTime);
    }

    public LocalDateTime getEndTime() {
        List<Subtask> subtaskList = subtasks.values()
                .stream()
                .sorted(Comparator.comparing(Subtask::getEndTime).reversed())
                .collect(Collectors.toList());
        endTime = subtaskList.get(0).getEndTime();
        return endTime;
    }

    public Duration setDuration() {
        if (!subtasks.isEmpty()) {
            duration = Duration.ofMinutes(0);
            for (Integer key : subtasks.keySet()) {
                duration = duration.plus(subtasks.get(key).getDuration());
            }
        }
        return duration;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasks, epic.subtasks);
    }


    public void setSubtasks(Map<Integer, Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }
}
