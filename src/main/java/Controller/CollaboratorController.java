package Controller;


import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



import Service.CalloboratorService;

@Stateful
@Path("/Ccontrol")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class CollaboratorController {
	  @Inject
	    private CalloboratorService collaboratorService;

	    @POST
	    @Path("/CreateCard/{username}/{cardName}/{boardName}/{listName}")
	    public Response createCard(
	            @PathParam("username") String username,
	            @PathParam("cardName") String cardName,
	            @PathParam("boardName") String boardName,
	            @PathParam("listName") String listName) {
	        return collaboratorService.createCard(username, boardName, cardName, listName);
	    }

	    @POST
	    @Path("/addComment/{cardName}/{comment}/{listname}")
	    public Response addCommentToCard(
	            @PathParam("cardName") String cardName,
	            @PathParam("comment") String comment,
	            @PathParam("listname") String listname) {
	        return collaboratorService.addCommentToCard(cardName, comment, listname);
	    }

	    @POST
	    @Path("/addDes/{cardName}/{description}/{listName}")
	    public Response addDescriptionToCard(
	            @PathParam("cardName") String cardName,
	            @PathParam("description") String description,
	            @PathParam("listName") String listName) {
	        return collaboratorService.addDescriptionToCard(cardName, description, listName);
	    }

	    @GET
	    @Path("/viewList/{listName}")
	    public Response viewList(@PathParam("listName") String listName) {
	        return collaboratorService.viewList(listName);
	    }

	    @POST
	    @Path("/assignCard/{cardName}")
	    public Response assignCardToUsers(
	            @PathParam("cardName") String cardName,
	            List<String> usernames) {
	        return collaboratorService.assignCardToUsers(cardName, usernames);
	    }

	    @POST
	    @Path("/moveCard/{cardName}/{sourceListName}/{targetListName}")
	    public Response moveCard(
	            @PathParam("cardName") String cardName,
	            @PathParam("sourceListName") String sourceListName,
	            @PathParam("targetListName") String targetListName) {
	        return collaboratorService.moveCard(cardName, sourceListName, targetListName);
	    }
}
