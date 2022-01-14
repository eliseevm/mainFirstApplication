package logic;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

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
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id=" + getId() +
                '}';
    }

    @Override
    public String getStatus() {
        String status = "";
        if (!(listSubTask.isEmpty())) {
            int j = 0;
            int s = 0;
            for (int i = 0; i < listSubTask.size(); i++) {
                SubTask temp = listSubTask.get(i);
                if (temp.getStatus() == "Done") {
                    j = j + 1;
                } else if (temp.getStatus() == "New") {
                    s = s + 1;
                }
            }
            if (j == listSubTask.size()) {
                status = "Done";
            } else if (s == listSubTask.size()) {
                status = "New";
            } else {
                status = "in progress";
            }
        } else {
            status = "New";
        }
        return status;
    }
}





