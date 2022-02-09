package manager;
import logic.Task;

import java.util.List;

public interface HistoryManager {

    /* Метод добавляет новый и удаляет старый двойник элемента из всех коллекций хранящих историю
    просмотра задач */
    void add(Task task);

    // Метод удаления объекта из вспомогательного списка истории просмотра
    void remove(int id);

    // Метод возвращает перечень просмотренных задач
    List<Task> getHistory();
}
