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

@Stateful
@Path("/service")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;
 	@POST
	@Path("/aaa")
	public Response aaa() {
		
		 
	    return Response.status(Response.Status.OK).entity(entityManager.createQuery("SELECT a FROM User a ").getResultList()).build();
        
	}
        

    @POST
    @Path("/register")
    @Transactional
    public Response registerUser(DTO userDTO) {
        try {
            // Check if username or email already exists
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

            // Create and save the user entity
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

        

  
  

    @GET
    @Path("/login/{username}/{password}")
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

    
    
    @PUT
    @Path("/profile/{username}")
    public Response updateProfile(@PathParam("username") String username, DTO profileUpdateDTO) {
        try {
            // Retrieve the user from the database
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            
            User user = query.getSingleResult();
            
            // Check if the user exists
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }
            
            // Update the user's profile information
            if (profileUpdateDTO.getUsername() != null && !profileUpdateDTO.getUsername().isEmpty()) {
                user.setUsername(profileUpdateDTO.getUsername());
            }
            if (profileUpdateDTO.getEmail() != null && !profileUpdateDTO.getEmail().isEmpty()) {
                user.setEmail(profileUpdateDTO.getEmail());
            }
            if (profileUpdateDTO.getPassword() != null && !profileUpdateDTO.getPassword().isEmpty()) {
                user.setPassword(profileUpdateDTO.getPassword());
            }
            
            // Persist the updated user entity
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

