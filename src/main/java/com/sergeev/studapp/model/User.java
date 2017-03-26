package com.sergeev.studapp.model;

public class User implements Identified {

    private String id;

    private Account account;

    private Group group;
    private String firstName;
    private String lastName;
    private Role type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getType() {
        return type;
    }

    public void setType(Role type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", account=" + account +
                ", group=" + group +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", type=" + type +
                '}';
    }

    public enum Role {

        STUDENT("1", "Student"),
        TEACHER("2", "Teacher"),
        ADMIN("3", "Admin");

        private String id;
        private String type;

        Role(String id, String type) {
            this.id = id;
            this.type = type;
        }

        public static Role getById(String id) {
            for (Role type : values()) {
                if (type.id.equals(id)) return type;
            }
            return null;
        }

        public String getId() {
            return id;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toString() {
            return "Role{" +
                    "id=" + id +
                    ", type='" + type +
                    '}';
        }
    }
}
