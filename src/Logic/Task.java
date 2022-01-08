package Logic;

import java.util.Objects;

public class Task {

    private String name;
    private String description;
    public String status;

    public Task(String name, String description, String status) {

        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) && description.equals(task.description)
                && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, status);
    }

    @Override
    public String toString() {
        return "Logic.Task{" +
                "name='" + name + '\'' +
                ", descriptionTask='" + description+ '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

