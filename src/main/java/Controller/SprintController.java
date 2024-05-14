package Controller;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import Service.SprintService;

@Stateless
@Path("/Scontrol")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SprintController {

    @Inject
    private SprintService sprintService;

    @POST
    @Path("/end")
    public Response endCurrentSprintAndStartNewOne() {
        return sprintService.endCurrentSprintAndStartNewOne();
    }

    @GET
    @Path("/report/{sprintId}")
    public Response generateSprintReport(@PathParam("sprintId") Long sprintId) {
        return sprintService.generateSprintReport(sprintId);
    }
}