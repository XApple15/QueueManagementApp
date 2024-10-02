package ug.queuesystem.Logic;

import ug.queuesystem.Model.Server;
import ug.queuesystem.Model.Task;

import java.util.List;

public interface Strategy {
    public void addTask(List<Server> servers, Task t);
}


