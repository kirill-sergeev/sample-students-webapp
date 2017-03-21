package com.sergeev.studapp.model;

import org.mongodb.morphia.annotations.*;

@Entity("students")
@Indexes(
        {@Index(value = "firstName", fields = @Field("firstName")),
                @Index(value = "lastName", fields = @Field("lastName"))}
)
public class Student implements Identified {
    @Id
    private String id;
    @Reference
    private Group group;
    private String firstName;
    private String lastName;

    public String getId() {
        return id;
    }

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

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", group=" + group +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

}
