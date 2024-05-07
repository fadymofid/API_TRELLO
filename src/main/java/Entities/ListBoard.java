package Entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "lists")
public class ListBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true,nullable = false)
    private String name;
    
    @OneToMany(mappedBy = "listboard", cascade = CascadeType.ALL)
    private List<Card> cards;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ListType listType; 
    
    public ListBoard() {
        this.cards = new ArrayList<>();
    }
    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    public ListBoard(String name, Board board) {
        this.name = name;
        this.board = board;
        this.cards = new ArrayList<>();
        this.listType = listType.TODO;
    }

    // Getters and setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public void addCard(Card card) {
        cards.add(card);
        card.setListboard(this); // Set the listboard for the card
    }

    public void removeCard(Card card) {
        cards.remove(card);
        card.setListboard(null); // Remove the association from the card
    }

    public ListType getListType() {
        return listType;
    }

    public void setListType(ListType listType) {
        this.listType = listType;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public Board getBoard() {
    	return board;
    } 
    
}
