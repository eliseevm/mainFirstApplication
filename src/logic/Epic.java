package logic;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> listSubTask;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        listSubTask = new ArrayList<>(); // Список подзадач.
        duration = createDuration(); // Продолжительность Эпика в завизимости от кол-ва подзадач.
        startTime = createStartTime(); // Время начала выполнения эпика в завизимости от подзадач.
        endTime = createEndTime(); // Время окончания выполнения эпика в завизимости от подзадач.
    }

    // Метод возвращает список подзадач.
    public ArrayList<SubTask> getListSubTask() {
        return listSubTask;
    }

    // Метод устанавливает статус эпика в зависимости от состояния статуса подзадач в списвке.
    public Status createStatus() {
        Status stat = Status.NEW;
        if (!this.getListSubTask().isEmpty()) {
            int j = 0;
            int s = 0;
            for (int i = 0; i < listSubTask.size(); i++) {
                SubTask temp = listSubTask.get(i);
                if (temp.getStatus().equals(Status.DONE)) {
                    j = j + 1;
                } else if (temp.getStatus().equals(Status.NEW)) {
                    s = s + 1;
                }
            }
            if (j == listSubTask.size()) {
                stat = Status.DONE;
            } else if (s == listSubTask.size()) {
                stat = Status.NEW;
            } else {
                stat = Status.IN_PROGRESS;
            }
        } else if (getListSubTask().isEmpty()) {
            stat = Status.NEW;
        }
        return stat;
    }

    // Метод устанавливает продолжительность эпика в зависимости от listSubTask.
    public int createDuration() {
        int duration = 0;
        ArrayList<SubTask> temp = getListSubTask();
        if (!temp.isEmpty()) {
            for (SubTask subTask : temp) {
                int durationST = subTask.getDuration();
                duration = duration + durationST;
            }
        }
        return duration;
    }

    // Метод устанавливает время окончания выполнения эпика в зависимости от listSubTask.
    @Override
    public LocalDateTime createEndTime() {
        LocalDateTime endTime = LocalDateTime.now();
        if (!listSubTask.isEmpty()) {
            for (int i = 0; i < listSubTask.size(); i++) {
                SubTask temp = listSubTask.get(i);
                LocalDateTime sT = temp.getEndTime();
                endTime = listSubTask.get(0).getEndTime();
                if (sT.isAfter(endTime)) {
                    endTime = sT;
                }
            }
            return endTime;
        }
        return endTime;
    }

    // Метод устанавливает время начала выполнения эпика в зависимости от listSubTask.
    public LocalDateTime createStartTime() {
        LocalDateTime startTime = LocalDateTime.now();
        if (!listSubTask.isEmpty()) {
            for (int i = 0; i < listSubTask.size(); i++) {
                SubTask temp = listSubTask.get(i);
                LocalDateTime sT = temp.getStartTime();
                startTime = listSubTask.get(0).getStartTime();
                if (sT.isBefore(startTime)) {
                    startTime = sT;
                }
            }
            return startTime;
        }
        return startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return listSubTask.equals(epic.listSubTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listSubTask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listSubTask=" + listSubTask +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", id=" + id +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}





