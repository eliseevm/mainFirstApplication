package logic;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {

    private ArrayList<SubTask> listSubTask;
    private String name;
    private String description;
    private String status;
    private int id;


    public Epic(String name, String description, int id) {
        super(name, description, id);
        this.listSubTask = listSubTask;

        listSubTask = new ArrayList<>();
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = getStatus();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public ArrayList<SubTask> getListSubTask() {
        return listSubTask;
    }

    public String getStatus() {

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





