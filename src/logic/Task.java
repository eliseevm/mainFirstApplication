package logic;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected String name; // Название задачи.
    protected String description; // Описание задачи.
    protected Status status; // Статус задачи.
    protected int id; // Идентификатор задачи.
    protected int duration; // Продолжительность выполнения задачи
    protected LocalDateTime startTime; // Время начала выполнения задачи.
    protected LocalDateTime endTime; // Время окончания выполнения задачи.

    // Конструктор для задач и подзадач.
    public Task(String name, String description, Status status, int id, int duration
            , LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
        this.endTime = createEndTime();
    }

    // Конструктор для эпиков.
    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    // Метод для расчета времени окончания выполнения задач и подзадач.
    public LocalDateTime createEndTime() {
        endTime = startTime.plusMinutes(getDuration());
        return endTime;
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

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && name.equals(task.name)
                && description.equals(task.description) && status == task.status
                && startTime.equals(task.startTime) && endTime.equals(task.endTime);
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


