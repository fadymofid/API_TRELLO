package Entities;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int storyPoints;
    private boolean completed;
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @ManyToOne
    private Sprint sprint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }
    public void setstoryPoints(int storyPoints) {
        this.storyPoints=storyPoints;
    }
    public int getStoryPoints () {
        return storyPoints;
    }
    public void SetSprint(Sprint Sprint) {
        this.sprint=Sprint;
    }
    public Sprint getSprint() {
        return sprint;
    }

}