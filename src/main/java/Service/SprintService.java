package Service;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import javax.persistence.PersistenceContext;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Entities.SprintReport;
import Entities.Sprint;
import Entities.Task;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateful
@Path("/sprint")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SprintService {

    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;
    
    // Constructor

    private Sprint sprint; // Global sprint variable

    public Response endCurrentSprintAndStartNewOne() {
        // Implement logic to end the current sprint and start a new one
        // Move unfinished tasks to the new sprint
        Sprint currentSprint = createSprint();
        if (currentSprint != null) {
            currentSprint.setEndDate(new Date()); // Set end date of current sprint
            entityManager.merge(currentSprint);

            // Start a new sprint
            Sprint newSprint = new Sprint();
            newSprint.setStartDate(new Date()); // Set start date of new sprint
            entityManager.persist(newSprint);

            List<Task> unfinishedTasks = new ArrayList<>();
            for (Task task : currentSprint.getTasks()) {
                if (!task.isCompleted()) {
                    Task newTask = new Task(); // Create a new task for the new sprint
                    newTask.setname(task.getname());
                    newTask.setstoryPoints(task.getStoryPoints());
                    newTask.setCompleted(task.isCompleted());
                    newTask.SetSprint(newSprint);
                    entityManager.persist(newTask);
                    unfinishedTasks.add(newTask);
                }
            }
            newSprint.setTasks(unfinishedTasks);
            return Response.ok("Current sprint ended and new sprint started successfully and the current id of the new sprint is : " + " " + newSprint.getId()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No active sprint found").build();
        }
    }
    
    public Response generateSprintReport(Long sprintId) {
        // Implement logic to generate sprint report based on sprint ID
        Sprint sprint = entityManager.find(Sprint.class, sprintId);
        if (sprint == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Sprint not found").build();
        }
        SprintReport sprintReport = generateReport(sprint);
        return Response.ok(sprintReport).build();
    }

   

    private SprintReport generateReport(Sprint sprint) {
        // Implement logic to generate sprint report
        // Calculate total completed story points and total uncompleted story points
        int totalCompletedStoryPoints = 0;
        int totalUncompletedStoryPoints = 0;
        for (Task task : sprint.getTasks()) {
            if (task.isCompleted()) {
                totalCompletedStoryPoints += task.getStoryPoints();
            } else {
                totalUncompletedStoryPoints += task.getStoryPoints();
            }
        }
        SprintReport sprintReport = new SprintReport();
        sprintReport.setSprintId(sprint.getId());
        sprintReport.setTotalCompletedStoryPoints(totalCompletedStoryPoints);
        sprintReport.setTotalUncompletedStoryPoints(totalUncompletedStoryPoints);
        return sprintReport;
    }

    private Sprint createSprint() {
        Sprint newSprint = new Sprint(); // Initialize a new sprint
        // Create a list of dummy tasks
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setname("Task 1");
        task1.setstoryPoints(10);
        task1.setCompleted(false); // Set as incomplete
        tasks.add(task1);
        Task task2 = new Task();
        task2.setname("Task 2");
        task2.setstoryPoints(8);
        task2.setCompleted(true); // Set as completed
        tasks.add(task2);
        // Add more tasks as needed
        newSprint.setTasks(tasks); // Set tasks for the new sprint
        return newSprint;
    }

}
