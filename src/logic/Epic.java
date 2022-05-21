package logic;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> listSubTask;
    private Status status;
    private Duration duration;
    private LocalDateTime endTime = LocalDateTime.now().plusSeconds(1);
    private LocalDateTime startTime;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        listSubTask = new ArrayList<>();
        status = getStatus();
        startTime = LocalDateTime.now().plusSeconds(id);
    }

    public ArrayList<SubTask> getListSubTask() {
        return listSubTask;
    }

    // Метод актуализации статуса эпика в зависимости от состояния статуса подзадач
    @Override
    public Status getStatus() {
        status = Status.NEW;
        if (!(listSubTask.isEmpty())) {
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
                status = Status.DONE;
            } else if (s == listSubTask.size()) {
                status = Status.NEW;
            } else {
                status = Status.IN_PROGRESS;
            }
        } else {
            status = Status.NEW;
        }
        return status;
    }

    // Метод устанавливает продолжительность эпика в зависимости от saubtask, возвращает новую
    // продолжительность эпика
    @Override
    public Duration getDuration() {
        ArrayList<SubTask> temp = getListSubTask();
        for (SubTask subTask : temp) {
            Duration durationST = subTask.getDuration();
            this.duration = duration.plus(durationST);
        }
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (listSubTask.size() != 0) {
            int i = (listSubTask.size()) - 1;
            SubTask temp = listSubTask.get(i);
            LocalDateTime endTimeSt = temp.getEndTime();
            if (endTimeSt.isAfter(endTime)) {
                endTime = endTimeSt;

            }
            return endTime;
        }
        return endTime;
    }

    // Метод возвращает время начала Эпика
    @Override
    public LocalDateTime getStartTime() {
        if (listSubTask != null) {
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
        return listSubTask.equals(epic.listSubTask) && status == epic.status
                && duration.equals(epic.duration) && endTime.equals(epic.endTime)
                && startTime.equals(epic.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listSubTask, status, duration, endTime, startTime);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listSubTask=" + listSubTask +
                ", status=" + status +
                ", duration=" + duration +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                '}';
    }
}





