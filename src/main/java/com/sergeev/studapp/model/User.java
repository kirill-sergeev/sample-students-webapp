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
    public User setId(String id) {
        this.id = id;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public User setAccount(Account account) {
        this.account = account;
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public User setGroup(Group group) {
        this.group = group;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public Role getType() {
        return type;
    }

    public User setType(Role type) {
        this.type = type;
        return this;
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

        STUDENT("1", "student"),
        TEACHER("2", "teacher"),
        ADMIN("3", "admin");

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
