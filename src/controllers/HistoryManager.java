package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    static final int SIZE_OF_HISTORY_LIST = 10;
    List<Task> historyList = new ArrayList<>();
    List<Task> getHistory();
    void add(Task task);

}
