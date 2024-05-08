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
import Entities.Card;
import Entities.Role;
import Entities.ListBoard;
import Entities.ListType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Service.ListBoardDto;
import Service.BoardDto;
@Stateful
@Path("/Cservice")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CalloboratorService {
	@PersistenceContext(unitName = "hello")
    private EntityManager entityManager;

	

    public Response createCard( String username,
                               String boardName,
                                String cardName,
                               String listName) {
        try {
             User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                     .setParameter("username", username)
                     .getSingleResult();
             Board board = entityManager.createQuery("SELECT b FROM Board b WHERE b.name = :boardName", Board.class)
                     .setParameter("boardName", boardName)
                     .getSingleResult();
             ListBoard lBoard= entityManager.createQuery("SELECT b FROM ListBoard b WHERE b.name = :listName", ListBoard.class)
                     .setParameter("listName", listName)
                     .getSingleResult();
             Card card = new Card();
             card.Name=cardName;
             card.setListboard(lBoard);
             entityManager.persist(card);





             return Response.status(Response.Status.OK)
                .entity("Card created successfully")
                .build();
        }
        catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to create card")
                           .build();
        }

    }
  
    public Response addCommentToCard( String cardName,
                                     String comment,
                                     String listname) {
        try {
        	Card card = entityManager.createQuery("SELECT NEW Card(c.id, c.Name, c.description, c.comments, b) " +
                    "FROM Card c JOIN c.listboard b " +
                    "WHERE c.Name = :cardName AND b.name = :listName", Card.class)
       .setParameter("cardName", cardName)
       .setParameter("listName", listname)
       .getSingleResult();

            // Append the new comment to the existing comments with a separator
            String updatedComments = card.getComments() != null ? card.getComments() + ", " + comment : comment;
            card.setComments(updatedComments);
            entityManager.merge(card);

            return Response.status(Response.Status.OK)
                           .entity("Comment added successfully")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to add comment to card")
                           .build();
        }
    }
    
 
    public Response addDescriptionToCard( String cardName,
                                         String description,
                                        String listName) {
        try {
            // Retrieve the card based on the cardName and listName
            Card card = entityManager.createQuery("SELECT NEW Card(c.id, c.Name, c.description, c.comments, b) " +
                                                  "FROM Card c JOIN c.listboard b " +
                                                  "WHERE c.Name = :cardName AND b.name = :listName", Card.class)
                                     .setParameter("cardName", cardName)
                                     .setParameter("listName", listName)
                                     .getSingleResult();

            // Update the card's description
            card.setDescription(description);
            entityManager.merge(card);

            return Response.status(Response.Status.OK)
                           .entity("Description added successfully")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to add description to card")
                           .build();
        }
    }
    

    public Response viewList( String listName) {
        try {
            // Retrieve all cards in the specified list along with assigned users
            List<Object[]> results = entityManager.createQuery("SELECT c, u.username " +
                                                                 "FROM Card c " +
                                                                 "JOIN c.assignees u " +
                                                                 "JOIN c.listboard b " +
                                                                 "WHERE b.name = :listName", Object[].class)
                                                    .setParameter("listName", listName)
                                                    .getResultList();

            // Create a list to hold CardDTOs
            List<CardDTO> cardDTOs = new ArrayList<>();

            // Populate the list with CardDTOs
            for (Object[] result : results) {
                Card card = (Card) result[0];
                String username = (String) result[1];

                // Check if a CardDTO for this card already exists in the list
                CardDTO cardDTO = cardDTOs.stream()
                                           .filter(dto -> dto.getId().equals(card.getId()))
                                           .findFirst()
                                           .orElse(null);

                // If a CardDTO doesn't exist, create a new one and add it to the list
                if (cardDTO == null) {
                    cardDTO = new CardDTO();
                    cardDTO.setId(card.getId());
                    cardDTO.Name=card.Name;
                    cardDTO.setDescription(card.getDescription());
                    cardDTO.setComments(card.getComments());
                    cardDTO.setListBoardId(card.getListboard().getId());
                    cardDTO.setAssignedUsers(new ArrayList<>());
                    cardDTOs.add(cardDTO);
                }

                // Add the assigned user to the CardDTO
                cardDTO.getAssignedUsers().add(username);
            }

            // Return the list of CardDTOs
            return Response.status(Response.Status.OK)
                           .entity(cardDTOs)
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to view list")
                           .build();
        }
    }


    public Response assignCardToUsers(@PathParam("cardName") String cardName,
                                      List<String> usernames) {
        try {
            Card card = entityManager.createQuery("SELECT c FROM Card c WHERE c.Name = :cardName", Card.class)
                                     .setParameter("cardName", cardName)
                                     .getSingleResult();

            for (String username : usernames) {
                User user = entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                         .setParameter("username", username)
                                         .getSingleResult();
                if (!card.getListboard().getBoard().getCollaborators().contains(user)&&!card.getListboard().getBoard().getTeamLeader().equals(user)) {
                    return Response.status(Response.Status.UNAUTHORIZED).entity("This user is not a collaborator").build();
            	}
                if (card.getAssignee().contains(user)) {
                    return Response.status(Response.Status.UNAUTHORIZED).entity("This "+user.getUsername()+" is already assigned").build();
            	}
                card.getAssignee().add(user);
            }
            	
            entityManager.merge(card);

            return Response.status(Response.Status.OK)
                           .entity("Card assigned to users successfully")
                           .build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("One or more users or card not found")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Failed to assign card to users")
                           .build();
        }
    }
   
    public Response moveCard(
            String cardName,
            String sourceListName,
            String targetListName) {
        try {
            // Find the card by name
            Card card = entityManager.createQuery("SELECT c FROM Card c WHERE c.Name = :cardName", Card.class)
                                    .setParameter("cardName", cardName)
                                    .getSingleResult();
            if (card == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Card not found")
                        .build();
            }

            // Find the source list by name
            ListBoard sourceList = entityManager.createQuery("SELECT l FROM ListBoard l WHERE l.name = :sourceListName", ListBoard.class)
                                               .setParameter("sourceListName", sourceListName)
                                               .getSingleResult();
            if (sourceList == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Source list not found")
                        .build();
            }

            // Find the target list by name
            ListBoard targetList = entityManager.createQuery("SELECT l FROM ListBoard l WHERE l.name = :targetListName", ListBoard.class)
                                               .setParameter("targetListName", targetListName)
                                               .getSingleResult();
            if (targetList == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Target list not found")
                        .build();
            }

            // Move the card to the target list
            card.setListboard(targetList);

            entityManager.merge(card);

            // Construct a message to return
            String message = String.format("Card '%s' moved from list '%s' to list '%s'", cardName, sourceListName, targetListName);

            return Response.status(Response.Status.OK)
                    .entity(message)
                    .build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("One or more lists or card not found")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to move card")
                    .build();
        }
    }
    

}
