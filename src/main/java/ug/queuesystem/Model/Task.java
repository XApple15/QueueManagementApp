package ug.queuesystem.Model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int firstServiceTime;
    private int startProcessingTime;

    public Task(int id, int processingTime, int arrivalTime) {
        this.id = id;
        this.serviceTime = processingTime;
        this.arrivalTime = arrivalTime;
    }

    public int getId() {
        return id;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public String toString() {

        return "Task{" +
                "serviceTime=" + serviceTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }

    public int getProcessingTime() {
        return serviceTime;
    }

    public void setStartProcessingTime(int startProcessingTime) {
        this.startProcessingTime = startProcessingTime;
    }

    public int getWaitingTime() {
        return startProcessingTime - arrivalTime;
    }

    public void setInitialServiceTime(int firstServiceTime) {
        this.firstServiceTime = firstServiceTime;
    }
}

