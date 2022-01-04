import java.util.Objects;

public class SubTask {
    String name;
    String descriptionSubTask;
    String status;

    public SubTask(String name, String descriptionSubTask, String status) {
        this.name = name;
        this.descriptionSubTask = descriptionSubTask;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return name.equals(subTask.name) && descriptionSubTask.equals(subTask.descriptionSubTask)
                && status.equals(subTask.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, descriptionSubTask, status);
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + name + '\'' +
                ", descriptionSubTask='" + descriptionSubTask + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
