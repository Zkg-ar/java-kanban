package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Subtask extends Task {
    private int epicId;

    public Subtask(int id, String name, String description, Status status, int epicId, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status,startTime,duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startDate);
    }
    @Override
    public String toString() {
        return id +
                "," + Types.SUBTASK +
                "," + name +
                "," + description +
                "," + getStatus() +
                "," + getStartTime().get().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) +
                "," + getDuration().toMinutes() +
                "," + epicId;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
