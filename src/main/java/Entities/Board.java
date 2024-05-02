//package Entities;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//@Entity
//@Table(name = "boards")
//public class Board {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    
//    @Column(nullable = false, unique = true)
//    private String name;	
//
//    @ManyToOne
//    @JoinColumn(name = "team_leader_id", nullable = false)
//    private User teamLeader;
//
//    
//    
//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
//    private ArrayList<ListBoard> list;
//    
//
//
//    public Board() {
//        this.list = new ArrayList<>();
//    }
//
//    public Board(String name, User teamLeader, ListType listType) {
//        this.name = name;
//        this.teamLeader = teamLeader;
//        
//        this.list = new ArrayList<>();
//    }
//
//    // Getters and setters
//    
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public User getTeamLeader() {
//        return teamLeader;
//    }
//
//    public void setTeamLeader(User teamLeader) {
//        this.teamLeader = teamLeader;
//    }
//}
//
// 
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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<ListBoard> lists;
    


    public Board() {
        this.lists = new ArrayList<>();
    }

    public Board(String name, User teamLeader, ListType listType) {
        this.name = name;
        this.teamLeader = teamLeader;
     
        this.lists = new ArrayList<>();
    }

    // Getters and setters...

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    }

    public void removeList(ListBoard list) {
        lists.remove(list);
    }
    
}

