package Service;

import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
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

import DTO.DTO;
import Entities.User;
import Entities.Board;
import Entities.Role;

import java.util.List;

@Stateful
@Path("/Uservice")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
	 @Inject
	    JMSContext context;
	    @Resource(mappedName = "java:/jms/queue/MyTrelloQueue")
	   private Queue MyTrelloQueue;
	    
	
    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;
 	
	public Response listUsers() {
		
		 
	    return Response.status(Response.Status.OK).entity(entityManager.createQuery("SELECT a FROM User a ").getResultList()).build();
        
	}
        

   
    @Transactional
    public Response registerUser(User userDTO) {
        try {
            long countByUsername = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                                                .setParameter("username", userDTO.getUsername())
                                                .getSingleResult();

            if (countByUsername > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Username already exists").build();
            }

            long countByEmail = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                                             .setParameter("email", userDTO.getEmail())
                                             .getSingleResult();

            if (countByEmail > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Email already exists").build();
            }

            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setRole(userDTO.getRole());
            entityManager.persist(user);

            return Response.status(Response.Status.OK).entity(user).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to register user").build();
        }
    }

        

  
  

    public Response login (@PathParam("username") String username, @PathParam("password") String password) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        
        
        
        try {
            User user = query.getSingleResult();
            if (user.getPassword().equals(password)) {
                return Response.status(Response.Status.OK).entity(user).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid password.").build();
            }
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        }
    }

    
    
    
    public Response updateProfile(@PathParam("username") String username, User profileUpdateDTO) {
        try {
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            
            User user = query.getSingleResult();
            
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }
            
            if (profileUpdateDTO.getUsername() != null && !profileUpdateDTO.getUsername().isEmpty()) {
                user.setUsername(profileUpdateDTO.getUsername());
            }
            if (profileUpdateDTO.getEmail() != null && !profileUpdateDTO.getEmail().isEmpty()) {
                user.setEmail(profileUpdateDTO.getEmail());
            }
            if (profileUpdateDTO.getPassword() != null && !profileUpdateDTO.getPassword().isEmpty()) {
                user.setPassword(profileUpdateDTO.getPassword());
            }
            entityManager.merge(user);
            
            return Response.status(Response.Status.OK).entity("Profile updated successfully.").build();
        } catch (NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to update profile.").build();
        }
    }

}