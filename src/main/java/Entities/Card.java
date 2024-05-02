package Entities;
//
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
//@Entity
//@Table(name = "cards")
//public class Card {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    
//    @Column(nullable = false)
//    private String description;
//    
//    @Column
//    private List<String> comments;
//
//    @ManyToOne
//    @JoinColumn(name = "listboard_id", nullable = false) // Corrected join column name
//    private ListBoard listBoard;
//
//   
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public List<String> getComments() {
//        return comments;
//    }
//
//    public void setComments(List<String> comments) {
//        this.comments = comments;
//    }
//
// 
//    public void addComment(String comment) {
//        comments.add(comment);
//    }
//
//    public void removeComment(String comment) {
//        comments.remove(comment);
//    }
//}
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @Column
    private String comments; // Change the type to String

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private ListBoard listboard;

    // Getters and setters...
  
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
 
}
