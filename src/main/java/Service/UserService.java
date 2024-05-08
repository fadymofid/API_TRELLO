package Service;



import javax.ws.rs.*;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import Entities.User;


@Stateful
@Path("/Uservice")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserService {

    @PersistenceContext(unitName = "hello")
    private EntityManager entityManager;

    public Response listUsers() {
        return Response.status(Response.Status.OK)
                       .entity(entityManager.createQuery("SELECT a FROM User a ").getResultList())
                       .build();
    }

    
    public Response registerUser(User user) {
        try {
            
            long countByUsername = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.username = :username", Long.class)
                                                .setParameter("username", user.getUsername())
                                                .getSingleResult();

            if (countByUsername > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Username already exists").build();
            }

            long countByEmail = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                                             .setParameter("email", user.getEmail())
                                             .getSingleResult();

            if (countByEmail > 0) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Email already exists").build();
            }

           
            
            entityManager.persist(user);

            return Response.status(Response.Status.OK).entity(user).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to register user").build();
        }
    }

        

  
  

  
    public Response login ( String username,  String password) {
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

    
   
    public Response updateProfile( String username, User profileUpdate) {
        try {
          
            TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);

            User user = query.getSingleResult();

            
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found.").build();
            }

           
            if (profileUpdate.getUsername() != null && !profileUpdate.getUsername().isEmpty()) {
                user.setUsername(profileUpdate.getUsername());
            }
            if (profileUpdate.getEmail() != null && !profileUpdate.getEmail().isEmpty()) {
                user.setEmail(profileUpdate.getEmail());
            }
            if (profileUpdate.getPassword() != null && !profileUpdate.getPassword().isEmpty()) {
                user.setPassword(profileUpdate.getPassword());
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

