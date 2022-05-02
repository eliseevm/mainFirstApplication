package logic;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> listSubTask;
    Status status;
    Duration duration = Duration.ofMinutes(0);

    public Epic(String name, String description, int id, LocalDateTime startTime) {
        super(name, description, id, startTime);
        listSubTask = new ArrayList<>();

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
        duration = Duration.ofMinutes(0);
        ArrayList<SubTask> temp = getListSubTask();
        for (SubTask subTask : temp) {
            Duration durationST = subTask.getDuration();
            duration = duration.plus(durationST);
        }
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return listSubTask.equals(epic.listSubTask)
                && status == epic.status && duration.equals(epic.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), listSubTask, status, duration);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listSubTask=" + listSubTask +
                '}';
    }
}





