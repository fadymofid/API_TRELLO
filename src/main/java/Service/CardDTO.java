package Service;

import java.util.List;

public class CardDTO {
    private Long id;
    private String description;
    public String Name;
    private String comments;
    private Long listBoardId; 
    private List<String> assignedUsers;

    public CardDTO() {
    }

    public CardDTO(Long id, String name, String description, String comments, Long listBoardId, List<String> assignedUsers) {
        this.id = id;
        this.Name = name;
        this.description = description;
        this.comments = comments;
        this.listBoardId = listBoardId;
        this.assignedUsers = assignedUsers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

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

    public Long getListBoardId() {
        return listBoardId;
    }

    public void setListBoardId(Long listBoardId) {
        this.listBoardId = listBoardId;
    }

    public List<String> getAssignedUsers() {
        return assignedUsers;
    }

    public void setAssignedUsers(List<String> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }
}