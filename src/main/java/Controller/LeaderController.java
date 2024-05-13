package Controller;

import Service.*;
import javax.ejb.Stateful;
<<<<<<< HEAD
=======
import javax.ejb.Stateless;
>>>>>>> aa476ec8eee61aceacd684d97b8698580fa760ad
import javax.inject.Inject;
import javax.ws.rs.*;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import Entities.ListType;


<<<<<<< HEAD
@Stateful
=======
@Stateless
>>>>>>> aa476ec8eee61aceacd684d97b8698580fa760ad
@Path("/Lcontrol")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

   
public class LeaderController {

<<<<<<< HEAD
	 @Inject
	    private LeaderService leaderService;
	 @POST
	    @Path("/boards/{username}/{boardName}")
	    public Response createBoard(
	            @PathParam("username") String username,
	            @PathParam("boardName") String boardName) {
	        return leaderService.createBoard(username, boardName);
	    }
	 @GET
	    @Path("/list_boards/{username}")
	    public Response listBoards(@PathParam("username") String username) {
	        return leaderService.listBoards(username);
	    }

	    @POST
	    @Path("/lists/{username}/{boardName}/{listName}/{listType}")
	    public Response createList(
	            @PathParam("username") String username,
	            @PathParam("boardName") String boardName,
	            @PathParam("listName") String listName,
	            @PathParam("listType") ListType listType) {
	        return leaderService.createList(username, boardName, listName, listType);
	    }

	    @DELETE
	    @Path("/dlists/{username}/{boardName}/{listName}")
	    public Response deleteList(
	            @PathParam("username") String username,
	            @PathParam("boardName") String boardName,
	            @PathParam("listName") String listName) {
	        return leaderService.deleteList(username, boardName, listName);
	    }

	    @POST
	    @Path("/inviteCollaborator/{leadername}/{boardName}/{username}")
	    public Response inviteCollaborator(
	            @PathParam("leadername") String leadername,
	            @PathParam("boardName") String boardName,
	            @PathParam("username") String username) {
	        return leaderService.inviteCollaborator(leadername, boardName, username);
	    }

}
=======
 @Inject
    private LeaderService leaderService;
 @POST
    @Path("/boards/{username}/{boardName}")
    public Response createBoard(
            @PathParam("username") String username,
            @PathParam("boardName") String boardName) {
        return leaderService.createBoard(username, boardName);
    }
 @GET
    @Path("/list_boards/{username}")
    public Response listBoards(@PathParam("username") String username) {
        return leaderService.listBoards(username);
    }

    @POST
    @Path("/lists/{username}/{boardName}/{listName}/{listType}")
    public Response createList(
            @PathParam("username") String username,
            @PathParam("boardName") String boardName,
            @PathParam("listName") String listName,
            @PathParam("listType") ListType listType) {
        return leaderService.createList(username, boardName, listName, listType);
    }

    @DELETE
    @Path("/dlists/{username}/{boardName}/{listName}")
    public Response deleteList(
            @PathParam("username") String username,
            @PathParam("boardName") String boardName,
            @PathParam("listName") String listName) {
        return leaderService.deleteList(username, boardName, listName);
    }
    @DELETE
    @Path("/dboard/{username}/{boardName}")
    public Response deleteboard(
    		@PathParam("username") String username,
    		@PathParam("boardName") String boardName) {
    	return leaderService.deleteBoard(username, boardName);
    }

    @POST
    @Path("/inviteCollaborator/{leadername}/{boardName}/{username}")
    public Response inviteCollaborator(
            @PathParam("leadername") String leadername,
            @PathParam("boardName") String boardName,
            @PathParam("username") String username) {
        return leaderService.inviteCollaborator(leadername, boardName, username);
    }

}
>>>>>>> aa476ec8eee61aceacd684d97b8698580fa760ad
