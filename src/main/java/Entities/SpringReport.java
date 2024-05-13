package Entities;
import java.util.Date;
import java.util.List;
import DTO.TaskDto;
public class SpringReport {

    private int totalCompletedStoryPoints;
    private int totalUncompletedStoryPoints;
    private Long id;
    private Date startDate;
    private Date endDate;
    private List<TaskDto> tasks;
    public SpringReport() {}
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public List<TaskDto> getTasks() {
        return tasks;
    }
    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
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