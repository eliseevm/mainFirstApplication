package manager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class Managers {
    private  static HttpTaskManager manager;
    public Managers() {
    }

    public static TaskManager getDefault() {
             manager = new HttpTaskManager("http://localhost:8078");
        return manager;
   }
}

