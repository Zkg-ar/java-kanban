package controllers;

import model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void add(Task task) {
        if (historyList.size() > SIZE_OF_HISTORY_LIST) {
            historyList.remove(0);
        }
        historyList.add(task);
    }
}
