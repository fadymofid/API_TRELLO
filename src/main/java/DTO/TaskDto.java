package DTO;

public class TaskDto {
     private Long id;
        private String name;
        private int storyPoints;
        private boolean completed;
        private Long sprintId;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getStoryPoints() {
            return storyPoints;
        }
        public void setStoryPoints(int storyPoints) {
            this.storyPoints = storyPoints;
        }
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public boolean isCompleted() {
            return completed;
        }
        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
        public Long getSprintId() {
            return sprintId;
        }
        public void setSprintId(Long sprintId) {
            this.sprintId = sprintId;
        }

}