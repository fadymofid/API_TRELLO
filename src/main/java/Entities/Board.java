package Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "boards")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;	

    @ManyToOne
    @JoinColumn(name = "team_leader_id", nullable = false)
    private User teamLeader;
    
    @ManyToMany
    @JoinTable(name = "board_collaborators",
               joinColumns = @JoinColumn(name = "board_id"),
               inverseJoinColumns = @JoinColumn(name = "user_id"))
    
    private List<User> collaborators = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<ListBoard> lists; // Change the list type to List<ListBoard>

    public Board() {
        this.lists = new ArrayList<>(); // Initialize the list in the constructor
    }

    public Board(String name, User teamLeader, ListType listType) {
        this.name = name;
        this.teamLeader = teamLeader;
        this.lists = new ArrayList<>(); // Initialize the list in the constructor
    }

    // Getters and setters...

    public String getName() {
        return name;
    }
    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public User getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(User teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<ListBoard> getLists() {
        return lists;
    }

    public void setLists(List<ListBoard> lists) {
        this.lists = lists;
    }

    public void addList(ListBoard list) {
        lists.add(list);
        list.setBoard(this); // Set the board for the list
    }

    public void removeList(ListBoard list) {
        lists.remove(list);
        list.setBoard(null); // Remove the association from the list
    }
    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    }

    public void inviteCollaborator(User user) {
        collaborators.add(user);
    }
}
