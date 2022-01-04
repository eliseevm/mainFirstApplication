import java.util.Objects;

public class Epic {

    String epicName;
    String descriptionEpic;
    String status;

    public Epic(String epicName, String descriptionEpic, String status) {
        this.epicName = epicName;
        this.descriptionEpic = descriptionEpic;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return epicName.equals(epic.epicName) && descriptionEpic.equals(epic.descriptionEpic) && status.equals(epic.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(epicName, descriptionEpic, status);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "epicName='" + epicName + '\'' +
                ", descriptionEpic='" + descriptionEpic + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}


