package manager;

public class Managers {
    private  static HttpTaskManager manager;
    public Managers() {
    }

    public static TaskManager getDefault() {
             manager = new HttpTaskManager("http://localhost:8078");
        return manager;
   }
}

