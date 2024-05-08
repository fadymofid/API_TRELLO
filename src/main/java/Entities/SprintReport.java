package Entities;

public class SprintReport {
    private Long sprintId;
    private int totalCompletedStoryPoints;
    private int totalUncompletedStoryPoints;
    
    // Remove the reference to Sprint
    // private Sprint sprintMetadata;

    // Constructor
    public SprintReport() {}

    // Getters and Setters

    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public int getTotalCompletedStoryPoints() {
        return totalCompletedStoryPoints;
    }

    public void setTotalCompletedStoryPoints(int totalCompletedStoryPoints) {
        this.totalCompletedStoryPoints = totalCompletedStoryPoints;
    }

    public int getTotalUncompletedStoryPoints() {
        return totalUncompletedStoryPoints;
    }

    public void setTotalUncompletedStoryPoints(int totalUncompletedStoryPoints) {
        this.totalUncompletedStoryPoints = totalUncompletedStoryPoints;
    }

    // Remove this method
    /*
    public Sprint getSprintMetadata() {
        return sprintMetadata;
    }

    public void setSprintMetadata(Sprint sprint) {
        this.sprintMetadata = sprint;
    }
    */
}