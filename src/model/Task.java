package model;

import com.google.gson.annotations.SerializedName;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;

    protected LocalDateTime startTime;

    protected Duration duration;

    public Task(int id, String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.MAX;
        this.duration = Duration.ofMinutes(0);
    }


    public Optional<LocalDateTime> getStartTime() {
        if(startTime == null){
            setStartTime(LocalDateTime.MAX);
            return Optional.ofNullable(startTime);
        }
        return Optional.ofNullable(startTime);
    }

    public Duration getDuration() {
        if(duration==null){
            setDuration(Duration.ofMinutes(0));
            return duration;
        }
        return duration;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id +
                "," + getType() +
                "," + name +
                "," + description +
                "," + status +
                "," + getStartTime().orElse(LocalDateTime.MAX).format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"))+
                "," + getDuration().toMinutes();
    }

    public String getName() {
        return name;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Types getType(){
        return Types.TASK;
    }

    public LocalDateTime getEndTime() {

        return startTime.plus(duration);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        if(status == null){
            setStatus(Status.NEW);
        }
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }
}
