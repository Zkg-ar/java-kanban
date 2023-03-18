package controllers;


import model.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private HashMap<Integer,Node<Task>> historyMap = new HashMap<>();
    private Node<Task>head = null;
    private Node<Task>tail = null;


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id){
        removeNode(historyMap.get(id));
        historyMap.remove(id);
    }
    private void removeNode(Node<Task> node) {
        if (node != null) {
            Node<Task> next = node.next;
            Node<Task> previous = node.previous;
            node.data = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail != node) {
                head = next;
                head.previous = null;
            } else if (head != node && tail == node) {
                tail = previous;
                tail.next = null;
            } else {
                previous.next = next;
                next.previous = previous;
            }

        }
    }

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task, null, tail);

        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;

        historyMap.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {

        List <Task >tasksList = new ArrayList<>();
        Node<Task> currentNode = head;

        while (currentNode !=null) {
            tasksList.add(currentNode.data);
            currentNode = currentNode.next;
        }

        return tasksList;
    }
    private class Node<T>{
        T data;
        Node next;
        Node previous;

        public Node(T data, Node next, Node previous) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }
    }
}


