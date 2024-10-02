package ug.queuesystem.Logic;

import ug.queuesystem.Model.Server;
import ug.queuesystem.Model.Task;

import java.util.List;

public class QueueStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        Server minTimeServer = servers.get(0);
        for (Server server : servers) {
            if (server.getTotalExpectedTime() < minTimeServer.getTotalExpectedTime()) {
                minTimeServer = server;
            }
        }
        minTimeServer.addTask(t);
    }
}
