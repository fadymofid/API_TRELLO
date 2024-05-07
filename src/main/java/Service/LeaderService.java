package Service;

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
import Entities.ListType;
import java.util.List;
import java.util.stream.Collectors;

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
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            if (user.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Only TeamLeader can create boards").build();
            }

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

    @GET
    @Path("/List_Boards/{username}")
    public Response listBoards(@PathParam("username") String username) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();

            List<Board> boards = entityManager.createQuery("SELECT b FROM Board b WHERE b.teamLeader = :user", Board.class)
                                              .setParameter("user", user)
                                              .getResultList();

            List<BoardDto> boardDTOs = boards.stream()
                    .map((Board board) -> {
                        BoardDto dto = new BoardDto();
                        dto.setName(board.getName());
                        dto.setTeamLeader(mapToUserDto(board.getTeamLeader()));
                        dto.setLists(board.getLists().stream()
                                .map(this::mapToListBoardDto)
                                .collect(Collectors.toList()));
                        
                        // Fetch collaborators for the board
                        List<User> collaborators = entityManager.createQuery("SELECT u FROM Board b JOIN b.collaborators u WHERE b = :board", User.class)
                                .setParameter("board", board)
                                .getResultList();
                        dto.setCollaborators(collaborators);
                        
                        return dto;
                    })
                    .collect(Collectors.toList());

            return Response.status(Response.Status.OK).entity(boardDTOs).build();
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
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            if (user.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity("Only TeamLeader can create lists")
                               .build();
            }

            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
                                      .setParameter("boardName", boardName)
                                      .getSingleResult();

            ListBoard newList = new ListBoard(listName, board);
            newList.setListType(listType);
            entityManager.persist(newList);

            ListBoardDto newListDTO = new ListBoardDto();
            newListDTO.setId(newList.getId());
            newListDTO.setName(newList.getName());
            newListDTO.setListType(newList.getListType());
            newListDTO.setBoardId(newList.getBoard().getId());

            return Response.status(Response.Status.OK)
                           .entity(newListDTO)
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
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            if (user.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.UNAUTHORIZED)
                               .entity("Only TeamLeader can delete lists")
                               .build();
            }

            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
                                      .setParameter("boardName", boardName)
                                      .getSingleResult();
            if (board == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Board not found")
                               .build();
            }

            ListBoard list = entityManager.createQuery("SELECT l FROM ListBoard l WHERE l.name = :listName AND l.board = :board", ListBoard.class)
                                          .setParameter("listName", listName)
                                          .setParameter("board", board)
                                          .getSingleResult();
            if (list == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("List not found")
                               .build();
            }

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

    @POST
    @Path("/inviteCollaborator/{leadername}/{boardName}/{username}")
    @Transactional
    public Response inviteCollaborator(
            @PathParam("leadername") String leadername,
            @PathParam("boardName") String boardName,
            @PathParam("username") String username) {
        

        try {
            // Find the board by name
            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
                    .setParameter("boardName", boardName)
                    .getSingleResult();

            // Find the user by username
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            User leader = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :leadername", User.class)
                    .setParameter("leadername", leadername)
                    .getSingleResult();
            
            if (leader.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Only TeamLeader can invite collaborator").build();
            }
            if (user.getRole() != Role.COLLABORATOR) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Only collaborator can be invited").build();
            }
            if (board.getCollaborators().contains(user)) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("User "+user.getUsername()+":is already exsist").build();
            }
            
            // Invite the user to collaborate on the board
            board.inviteCollaborator(user);
            entityManager.merge(board);

            return Response.status(Response.Status.OK)
                    .entity("User invited to collaborate on the board successfully")
                    .build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Board or user not found")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to invite user to collaborate on the board")
                    .build();
        }
    }
    private DTO mapToUserDto(User user) {
        DTO userDto = new DTO();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private ListBoardDto mapToListBoardDto(ListBoard list) {
        ListBoardDto listBoardDto = new ListBoardDto();
        listBoardDto.setId(list.getId());
        listBoardDto.setName(list.getName());
        listBoardDto.setListType(list.getListType());
        listBoardDto.setBoardId(list.getBoard().getId());
        return listBoardDto;
    }
}
