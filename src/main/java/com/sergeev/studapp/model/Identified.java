package com.sergeev.studapp.model;

import java.io.Serializable;

public interface Identified extends Serializable {
    String getId();
    void setId(String id);
}
