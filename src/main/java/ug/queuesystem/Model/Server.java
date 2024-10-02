package ug.queuesystem.Model;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private int id;
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int counter;
    private int totalWaitingTime;
    private int totalServiceTime;

    public Server() {
        this.tasks = new LinkedBlockingQueue<>();
        this.waitingPeriod = new AtomicInteger();

    }

    public void addTask(Task newTask) {
        tasks.offer(newTask);
        int firstServiceTime = tasks.peek().getProcessingTime();
        newTask.setInitialServiceTime(newTask.getArrivalTime()+waitingPeriod.get());
        waitingPeriod.addAndGet(newTask.getProcessingTime());
    }

    @Override
    public synchronized void run() {
        while (true) {
            if (!tasks.isEmpty()) {
                try {
                    Task task = tasks.peek();
                    Thread.sleep(1000);
                    counter++;
                    int auxDuration = task.getProcessingTime();
                    totalServiceTime = totalServiceTime + task.getProcessingTime();
                    while (task.getProcessingTime() > 1) {
                        task.setServiceTime(task.getProcessingTime() - 1);
                        totalWaitingTime++;
                        waitingPeriod.decrementAndGet();
                        Thread.sleep(1000);
                        counter++;
                    }
                    waitingPeriod.decrementAndGet();
                    totalWaitingTime = totalWaitingTime + (counter - auxDuration - task.getArrivalTime());
                    tasks.poll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    public Integer getTotalExpectedTime() {
        return waitingPeriod.get();
    }

    public Integer getTotalServiceTime() {
        return totalServiceTime;
    }

    public int getWaitingTime() {
        return totalWaitingTime;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
