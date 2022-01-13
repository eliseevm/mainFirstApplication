package logic;

import java.util.Objects;

public class SubTask extends Task {

    private int epicId;
    private String name;
    private String description;
    private String status;
    private int id;

    public SubTask(String name, String description, String status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                '}';
    }
}



