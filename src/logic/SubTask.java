package logic;

import java.util.Objects;

public class SubTask extends Task {

    public int epicId;

    public SubTask(String name, String description, String status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
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



