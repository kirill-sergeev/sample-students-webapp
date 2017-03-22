package com.sergeev.studapp.model;

public class User implements Identified {

    private String id;
    private Group group;
    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private AccountType type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", group=" + group +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public enum AccountType {

        STUDENT("1", "Student"),
        TEACHER("2", "Teacher"),
        ADMIN("3", "Admin");

        private String id;
        private String type;

        AccountType(String id, String type) {
            this.id = id;
            this.type = type;
        }

        public static AccountType getById(String id) {
            for (AccountType type : values()) {
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
            return "AccountType{" +
                    "id=" + id +
                    ", type='" + type +
                    '}';
        }
    }
}
