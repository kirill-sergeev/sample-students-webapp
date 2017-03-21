package com.sergeev.studapp.model;

import org.mongodb.morphia.annotations.*;

@Entity("groups")
@Indexes(
        @Index(value = "title", fields = @Field("title"))
)
public class Group implements Identified {
    @Id
    private String id;
    private String title;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
