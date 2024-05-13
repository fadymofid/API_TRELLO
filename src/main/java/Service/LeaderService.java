package Service;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.ws.rs.*;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import DTO.BoardDto;
import DTO.DTO;
import DTO.ListBoardDto;
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
    
    @Inject
    JMSContext context;
    @Resource(mappedName = "java:/jms/queue/MyTrelloQueue")
   private Queue MyTrelloQueue;
    
    
    public Response createBoard( String username,  String boardName) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            if (user.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.FORBIDDEN).entity("Only TeamLeader can create boards").build();
            }
            String messageContent = (username + " created board named : " + boardName);
            JMSProducer producer = context.createProducer();
            TextMessage message = context.createTextMessage(messageContent);
            producer.send(MyTrelloQueue, message);
       	 
            JMSConsumer consumer = context.createConsumer(MyTrelloQueue);
          	 TextMessage msg = (TextMessage) consumer.receiveNoWait();
          	 consumer.close();
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

   
    public Response listBoards( String username) {
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



   
    public Response createList( String username,
                                String boardName,
                                String listName,
                                ListType listType) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            if (user.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.FORBIDDEN)
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


    public Response deleteList( String username,
                                String boardName,
                                String listName) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            if (user.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.FORBIDDEN)
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

    public Response deleteBoard(String username, String boardName) {
        try {
            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                     .setParameter("username", username)
                                     .getSingleResult();
            if (user.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.FORBIDDEN)
                               .entity("Only TeamLeader can delete boards")
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

            entityManager.remove(board);

            return Response.status(Response.Status.OK)
                           .entity("Board deleted successfully")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to delete board")
                           .build();
        }
    }

    
    public Response inviteCollaborator(
            String leadername,
            String boardName,
           String username) {
        

        try {
            Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
                    .setParameter("boardName", boardName)
                    .getSingleResult();

            User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            User leader = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :leadername", User.class)
                    .setParameter("leadername", leadername)
                    .getSingleResult();
            JMSProducer producer = context.createProducer();
            TextMessage message = context.createTextMessage("You have been invited ");
            producer.send(MyTrelloQueue, message);
            JMSConsumer consumer = context.createConsumer(MyTrelloQueue);
            TextMessage msg = (TextMessage) consumer.receiveNoWait();
            consumer.close();
            
            if (leader.getRole() != Role.TEAM_LEADER) {
                return Response.status(Response.Status.FORBIDDEN).entity("Only TeamLeader can invite collaborator").build();
            }
            if (user.getRole() != Role.COLLABORATOR) {
                return Response.status(Response.Status.FORBIDDEN).entity("Only collaborator can be invited").build();
            }
            if (board.getCollaborators().contains(user)) {
                return Response.status(Response.Status.FORBIDDEN).entity("User "+user.getUsername()+":is already exsist").build();
            }
            
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