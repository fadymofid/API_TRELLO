package Service;

import Service.DTO;
import javax.ejb.Stateful;
import javax.ws.rs.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import Entities.User;
import Entities.Board;
import Entities.Role;
import Entities.ListBoard;
import Entities.Card;
import Entities.ListType;
import java.util.List;
@Stateful
@Path("/Lservice")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LeaderService {
	 @PersistenceContext(unitName = "hello")
	    private EntityManager entityManager;
	 
	   @POST
	    @Path("/boards/{username}/{boardName}")
	    @Transactional
	    public Response createBoard(@PathParam("username") String username, @PathParam("boardName") String boardName) {
	        try {
	            // Check if the user is a TeamLeader
	            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
	                                     .setParameter("username", username)
	                                     .getSingleResult();
	            if (user.getRole() != Role.TEAM_LEADER) {
	                return Response.status(Response.Status.UNAUTHORIZED).entity("Only TeamLeader can create boards").build();
	            }

	            // Create and save the board entity
	            Board board = new Board();
	            board.setName(boardName);
	            board.setTeamLeader(user);
	            entityManager.persist(board);

	            return Response.status(Response.Status.OK).entity(board).build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to create board").build();
	        }
	    }
	    
	    
	    
	    @DELETE
	    @Path("/D_boards/{username}/{boardName}")
	    @Transactional
	    public Response deleteBoard(@PathParam("boardName") String boardName, @PathParam("username") String username) {
	        try {
	            // Check if the user is a TeamLeader
	            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
	                                     .setParameter("username", username)
	                                     .getSingleResult();
	            if (user.getRole() != Role.TEAM_LEADER) {
	                return Response.status(Response.Status.UNAUTHORIZED).entity("Only TeamLeader can delete boards").build();
	            }

	            // Find the board by name
	            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
	                                      .setParameter("boardName", boardName)
	                                      .getSingleResult();
	            if (board == null) {
	                return Response.status(Response.Status.NOT_FOUND).entity("Board not found").build();
	            }

	            entityManager.remove(board);

	            return Response.status(Response.Status.OK).entity("Board deleted successfully").build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete board").build();
	        }
	    }
	    
	    
	    
	    
	    @GET
	    @Path("/List_Boards/{username}")
	    public Response listBoards(@PathParam("username") String username) {
	        try {
	            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
	                                     .setParameter("username", username)
	                                     .getSingleResult();

	            List<Board> boards = entityManager.createQuery("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.lists WHERE b.teamLeader = :user", Board.class)
	                                              .setParameter("user", user)
	                                              .getResultList();

	            return Response.status(Response.Status.OK).entity(boards).build();
	        } catch (NoResultException e) {
	            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to list boards").build();
	        }
	    }

	    
	    
	    @POST
	    @Path("/lists/{username}/{boardName}/{listName}/{listType}")
	    
	    public Response createList(@PathParam("username") String username, 
	                               @PathParam("boardName") String boardName, 
	                               @PathParam("listName") String listName,
	                               @PathParam("listType") ListType listType) {
	        try {
	            // Check if the user is a TeamLeader
	            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
	                                     .setParameter("username", username)
	                                     .getSingleResult();
	            if (user.getRole() != Role.TEAM_LEADER) {
	                return Response.status(Response.Status.UNAUTHORIZED)
	                               .entity("Only TeamLeader can create lists")
	                               .build();
	            }

	            // Find the board by name
	            List<Board> boards = entityManager.createQuery("SELECT DISTINCT b FROM Board b LEFT JOIN FETCH b.lists WHERE b.teamLeader = :user", Board.class)
                        .setParameter("user", user)
                        .getResultList();  
	            
	            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
                        .setParameter("boardName", boardName)
                        .getSingleResult();
board.getLists().size(); // Initialize the lists collection
	            
//	            if (board == null) {
//	                return Response.status(Response.Status.NOT_FOUND)
//	                               .entity("Board not found")
//	                               .build();
//	            }

	            // Create the list and associate it with the board
	            ListBoard newList = new ListBoard(listName, board);
	            newList.setListType(listType); // Set the default list type
	            entityManager.persist(newList);

	            return Response.status(Response.Status.OK)
	                           .entity(newList)
	                           .build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity("Failed to create list")
	                           .build();
	        }
	    }
	    @DELETE
	    @Path("/dlists/{username}/{boardName}/{listName}")
	    @Transactional
	    public Response deleteList(@PathParam("username") String username,
	                               @PathParam("boardName") String boardName,
	                               @PathParam("listName") String listName) {
	        try {
	            // Check if the user is a TeamLeader
	            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
	                                     .setParameter("username", username)
	                                     .getSingleResult();
	            if (user.getRole() != Role.TEAM_LEADER) {
	                return Response.status(Response.Status.UNAUTHORIZED)
	                               .entity("Only TeamLeader can delete lists")
	                               .build();
	            }

	            // Find the board by name
	            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
	                                      .setParameter("boardName", boardName)
	                                      .getSingleResult();
	            if (board == null) {
	                return Response.status(Response.Status.NOT_FOUND)
	                               .entity("Board not found")
	                               .build();
	            }

	            // Find the list by name within the specified board
	            ListBoard list = entityManager.createQuery("SELECT l FROM ListBoard l WHERE l.name = :listName AND l.board = :board", ListBoard.class)
	                                          .setParameter("listName", listName)
	                                          .setParameter("board", board)
	                                          .getSingleResult();
	            if (list == null) {
	                return Response.status(Response.Status.NOT_FOUND)
	                               .entity("List not found")
	                               .build();
	            }

	            // Remove the list
	            entityManager.remove(list);

	            return Response.status(Response.Status.OK)
	                           .entity("List deleted successfully")
	                           .build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                           .entity("Failed to delete list")
	                           .build();
	        }
	    }

}
