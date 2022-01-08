package Logic;

public class SubTask extends Task {
    private int epicId;

    public SubTask(int epicId, String name, String description, String status) {
        super(name, description, status);
        this.epicId = epicId;
    }
}
