package Entities;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD


=======
>>>>>>> aa476ec8eee61aceacd684d97b8698580fa760ad
@Entity
@Table(name = "cards")
public class Card {
	@ManyToMany
    @JoinColumn(name = "assignee_id", nullable = false)
	 private List<User> assignees = new ArrayList<>();
	@Column
	public String Name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String description;
    
    @Column
    private String comments; 
<<<<<<< HEAD
    @ManyToOne(fetch = FetchType.EAGER)
=======

    @ManyToOne(fetch = FetchType.EAGER) 
>>>>>>> aa476ec8eee61aceacd684d97b8698580fa760ad
    @JoinColumn(name = "listboard_id", nullable = false)
    private ListBoard listboard;
    
    
    public Card(Long id, String name, String description, String comments, ListBoard listboard) {
        this.id = id;
        this.Name = name;
        this.description = description;
        this.comments = comments;
        this.listboard = listboard;
    }
    public Card() {}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


<<<<<<< HEAD
    
=======
  
>>>>>>> aa476ec8eee61aceacd684d97b8698580fa760ad
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ListBoard getListboard() {
        return listboard;
    }

    public void setListboard(ListBoard listboard) {
        this.listboard = listboard;
    }
    public List<User> getAssignee() {
        return assignees;
    }

    public void setAssignee(List<User> assignees) {
        this.assignees = assignees;
    }
}
