package com.sergeev.studapp.model;

public interface Identified<PK extends Integer> {
    PK getId();
    void setId(Integer id);
}
