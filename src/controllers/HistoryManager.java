package controllers;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();
    void remove(int id);
    void add(Task task);

}
