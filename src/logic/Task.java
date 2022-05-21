package logic;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {

    private String name;
    private String description;
    private Status status;
    private int id;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Task(String name, String description, Status status, int id, Duration duration
            , LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        this.endTime = startTime.plusSeconds(this.getDuration().toSeconds());
        return endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && name.equals(task.name)
                && description.equals(task.description) && status == task.status
                && duration.equals(task.duration) && startTime.equals(task.startTime)
                && endTime.equals(task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status, id, duration, startTime, endTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}


