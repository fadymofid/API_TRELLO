package DTO;

import java.util.ArrayList;
import java.util.List;

import Entities.ListBoard;
import Entities.ListType;
import Entities.User;

public class BoardDto {
    public BoardDto () {}
    
    private String name;
    private DTO teamLeader;
    private List<ListBoardDto> lists;
    private List<User> collaborators = new ArrayList<>();



    public List<User> getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(List<User> collaborators) {
        this.collaborators = collaborators;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DTO getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(DTO teamLeader) {
        this.teamLeader = teamLeader;
    }

    public List<ListBoardDto> getLists() {
        return lists;
    }

    public void setLists(List<ListBoardDto> lists) {
        this.lists = lists;
    }
}
