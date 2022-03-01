package logic;

import manager.Status;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    Status status;
    private ArrayList<SubTask> listSubTask;

    public Epic(String name, String description, int id) {
        super(name, description, id);
        listSubTask = new ArrayList<>();
    }

    public ArrayList<SubTask> getListSubTask() {
        return listSubTask;
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
                '}';
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
}





