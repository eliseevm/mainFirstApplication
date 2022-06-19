package logic;

import manager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> listSubTask;
    //private Duration duration;
    //private LocalDateTime endTime;
    //private LocalDateTime startTime = LocalDateTime.now();

    public Epic(String name, String description, int id) {
        super(name, description, id);
        listSubTask = new ArrayList<>();
        duration = getDuration();
       //endTime = getEndTime();

    }

    public ArrayList<SubTask> getListSubTask() {
        ArrayList<SubTask>list = null;
        if (listSubTask==null) {
            System.out.println("У этого эпика пока нет списка задач!");
        } else if (listSubTask != null) {
            list = listSubTask;
        } return list;
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
    public int getDuration() {
        ArrayList<SubTask> temp = getListSubTask();
        for (SubTask subTask : temp) {
            int durationST = subTask.getDuration();
            duration = duration + durationST;
        }
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (listSubTask.size() != 0) {
            int i = (listSubTask.size()) - 1;
            SubTask temp = listSubTask.get(i);
            LocalDateTime endTimeSt = temp.getEndTime();
            if (endTimeSt.isAfter(endTime)) {
                this.endTime = endTimeSt;

            }
            return endTime;
        } else if (listSubTask.size() == 0) {
            System.out.println("У Эпика пока нет подзадач, время окончания  не определено!");
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
        System.out.println("У Эпика пока нет подзадач, время начала не определено!");
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
                ", status=" + status +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}





