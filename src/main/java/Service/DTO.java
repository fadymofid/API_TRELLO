package Service;
import Entities.Role;
public class DTO {
	
	    private String username;
	    private String email;
	    private String password;
        private Role   role;
        private Long id;
	    
	    public String getUsername() {
	        return username;
	    }

	    public void setUsername(String username) {
	        this.username = username;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }
	    public void setId(Long ID) {
	        this.id = ID;
	    }
	    public Long getId() {
	        return id;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
	    
	    public Role getRole() {
	        return role;
	    }

	    public void setRole(Role role) {
	        this.role = role;
	    }
	    
	    
	}
