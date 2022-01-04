import java.util.Objects;

public class Task {

    String name;
    String descriptionTask;
    String status;

    public Task(String name, String descriptionTask, String status) {

        this.name = name;
        this.descriptionTask = descriptionTask;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return name.equals(task.name) && descriptionTask.equals(task.descriptionTask) && status.equals(task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, descriptionTask, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

