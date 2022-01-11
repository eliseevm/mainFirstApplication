package logic;


import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private static HashMap<Integer, ArrayList<SubTask>> subtaskInEpic = new HashMap<>();
    private static ArrayList<SubTask> listSubTask;

    public Epic(String name, String description) {
        super(name, description);
        status = getEpicStatus();
        listSubTask = new ArrayList<>(); // Список для подзадач прикрепляемых к эпику
    }

    public static void inputNewSubTask(String name, String description, String status, int epicId) {
        int id = SubTask.getId();
        listSubTask.add(new SubTask(name, description, status, epicId));
        subtaskInEpic.put(epicId, listSubTask);
        SubTask.setId(id + 1);
    }

    public String getEpicStatus() { // Метод-установщик статуса эпика
        String status = getStatus();
        String indicator = " ";
        for (Integer listr : subtaskInEpic.keySet()) {
            ArrayList<SubTask> temp = subtaskInEpic.get(listr);
            int j = 0;
            for (int i = 0; i < temp.size(); i++) {
                SubTask subTask = temp.get(i);
                if ((subTask.getStatus()).equals("done")) {
                    j = j++;
                }
            }
            if (j == temp.size()) {
                indicator = "done";
            } else if (j == 0) {
                indicator = "new";
            } else {
                indicator = "in progress";
            }
        }
        return indicator;
    }
}





