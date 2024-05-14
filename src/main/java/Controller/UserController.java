package Controller;
import javax.ws.rs.*;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import Entities.User;
import Service.UserService;

@Stateless
@Path("/Ucontrol")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

public class UserController {
    @Inject
    private UserService userService;

    @POST
    @Path("/register")
    public Response registerUser(User user) {
        return userService.registerUser(user);
    }

    @GET
    @Path("/login/{username}/{password}")
    public Response login(@PathParam("username") String username, @PathParam("password") String password) {
        return userService.login(username, password);
    }

    @PUT
    @Path("/profile/{username}")
    public Response updateProfile(@PathParam("username") String username, User profileUpdate) {
        return userService.updateProfile(username, profileUpdate);
    }
    @POST
    @Path("/list_users")
    public Response list() {
        return userService.listUsers();
    }

}