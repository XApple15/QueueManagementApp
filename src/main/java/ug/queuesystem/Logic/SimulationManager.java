package ug.queuesystem.Logic;

import javafx.scene.control.TextArea;
import ug.queuesystem.Model.LogFile;
import ug.queuesystem.Model.Server;
import ug.queuesystem.Model.Task;

import java.util.*;

public class SimulationManager implements Runnable {


    public int timeLimit = 40;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 1;
    public int numberOfServers = 2;
    public int numberOfClients = 20;

    public int minArrivalTime = 2;
    public int maxArrivalTime = 10;
    private int totalWaitingTime = 0;
    private int totalServiceTime = 0;
    private int[] clientsPerHour = new int[201];

    private Scheduler scheduler;
    private TextArea waitingClients;
    private TextArea queue;
    private TextArea result;
    private TextArea time;
    private List<Task> generatedTasks;

    public SimulationManager() {
        int maxTasksPerServer = 10;
        scheduler = new Scheduler(numberOfServers, maxTasksPerServer);
        generatedTasks = new ArrayList<>();
        generateNRandomTasks();
    }

    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int numberOfServers,
                             int numberOfClients, int minArrivalTime, int maxArrivalTime, TextArea waitingClients,
                             TextArea queue, TextArea result,TextArea time) {
        int maxTasksPerServer = 10;

        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.waitingClients = waitingClients;
        this.queue = queue;
        this.time = time;
        this.result = result;
        scheduler = new Scheduler(numberOfServers, maxTasksPerServer);
        generatedTasks = new ArrayList<>();
        generateNRandomTasks();
        LogFile.cleanLogFile();
    }

    private void generateNRandomTasks() {
        Random rand = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = rand.nextInt(maxProcessingTime - minProcessingTime) + minProcessingTime;
            int arrivalTime = rand.nextInt(maxArrivalTime - minArrivalTime) + minArrivalTime;
            generatedTasks.add(new Task(i, processingTime, arrivalTime));
        }
        for (Task t : generatedTasks) {
            clientsPerHour[t.getArrivalTime()]++;
        }
        Collections.sort(generatedTasks, Comparator.comparing(Task::getArrivalTime));
        System.out.println("Generated tasks: " + generatedTasks);
    }

    @Override
    public void run() {
        int currentTime = 0;
        while (currentTime < timeLimit) {
            LogFile.soutAndPrintToFile("\nTime " + currentTime + "\n");
            Iterator<Task> taskIterator = generatedTasks.iterator();
            LogFile.soutAndPrintToFile("Waiting clients");
            while (taskIterator.hasNext()) {
                Task t = taskIterator.next();
                if (t.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(t);
                    totalWaitingTime += currentTime - t.getArrivalTime();
                    taskIterator.remove();
                } else {
                    LogFile.soutAndPrintToFile("(" + t.getId() + "," + t.getArrivalTime() + "," + t.getProcessingTime() + ");");
                }
            }
            LogFile.soutAndPrintToFile("\n");
            for (Server s : scheduler.getServers()) {
                LogFile.soutAndPrintToFile("Queue " + s.getId() + ": ");
                if (s.getTasks().length == 0) {
                    LogFile.soutAndPrintToFile("closed");
                } else {
                    for (Task t : s.getTasks()) {
                        //  totalWaitingTime += t.getWaitingTime();
                        totalServiceTime += t.getProcessingTime();
                        LogFile.soutAndPrintToFile("(" + t.getId() + "," + t.getArrivalTime() + "," + t.getProcessingTime() + ");");
                    }
                }
                LogFile.soutAndPrintToFile("\n");
            }
            waitingClients.setText("Waiting clients : "+ getWaitingClients());
            queue.setText(getQueue());
            time.setText("Time: " + currentTime);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTime++;

        }
        String message = "Average waiting time: " + (double) scheduler.getTotalWaitingTime() / numberOfClients
                + " \nAverage service time: " + (double) scheduler.getTotalServiceTime() / numberOfClients +
                " \nPeak hour: " + getPeakHour();
        LogFile.soutAndPrintToFile(message);
        result.setText(message);
    }

    public int getPeakHour() {
        int peakHour = 0;
        for (int i = 1; i < clientsPerHour.length; i++) {
            if (clientsPerHour[i] > clientsPerHour[peakHour]) {
                peakHour = i;
            }
        }
        return peakHour;
    }

    public String getWaitingClients() {
        StringBuilder waitingClients = new StringBuilder();
        for (Task task : generatedTasks) {
            waitingClients.append("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getProcessingTime() + ");");
        }
        return waitingClients.toString();
    }

    public String getQueue() {
        StringBuilder queues = new StringBuilder();
        for (Server server : scheduler.getServers()) {
            queues.append("Queue " + server.getId() + ": ");
            if (server.getTasks().length == 0) {
                queues.append("closed\n");
            } else {
                for (Task task : server.getTasks()) {
                    queues.append("(" + task.getId() + "," + task.getArrivalTime() + "," + task.getProcessingTime() + ");");
                }
                queues.append("\n");
            }
        }
        return queues.toString();
    }

    public static void main(String[] args) {
        LogFile.cleanLogFile();
        SimulationManager simManager = new SimulationManager();
        Thread t = new Thread(simManager);
        t.start();
    }
}