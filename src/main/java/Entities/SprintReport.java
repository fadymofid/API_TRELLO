package Entities;

public class SprintReport {
    private Long sprintId;
    private int totalCompletedStoryPoints;
    private int totalUncompletedStoryPoints;
    
   
    public SprintReport() {}


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

   
}