package controllers;

import model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    public void add(Task task) {
        if (historyList.size() < SIZE_OF_HISTORY_LIST) {
            historyList.add(task);
        } else {
            historyList.remove(1);
            historyList.add(task);
        }
    }
}
