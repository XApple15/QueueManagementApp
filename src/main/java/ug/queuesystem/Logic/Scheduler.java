package ug.queuesystem.Logic;

import ug.queuesystem.Model.Server;
import ug.queuesystem.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int nbOfServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int nbOfServers, int maxTasksPerServer) {
        this.nbOfServers = nbOfServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.servers = new ArrayList<>();

        for (int i = 0; i < nbOfServers; i++) {
            Server server = new Server();
            server.setId(i);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
        this.strategy = new TimeStrategy();
    }

    public void changeStrategy() {
        int shortestWaitingTime = Integer.MAX_VALUE;
        for (Server server : servers) {
            int currentWaitingTime = server.getWaitingPeriod().intValue();
            if (currentWaitingTime < shortestWaitingTime) {
                shortestWaitingTime = currentWaitingTime;
            }
        }
        if (shortestWaitingTime == Integer.MAX_VALUE) {
            strategy = new QueueStrategy();
        } else {
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task t) {
        if (strategy != null) {
            changeStrategy();
            strategy.addTask(servers, t);
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public Integer getTotalWaitingTime() {
        int waitingTime = 0;
        for (Server server : servers) {
            waitingTime += server.getWaitingTime();
        }
        return waitingTime;
    }

    public Integer getTotalServiceTime() {
        int serviceTime = 0;
        for (Server server : servers) {
            serviceTime += server.getTotalServiceTime();
        }
        return serviceTime;
    }
}