package manager;

public class Managers {

    private TaskManager manager;

    public TaskManager getDefault() {
        manager = new InMemoryTaskManager();
        return manager;
    }
}
