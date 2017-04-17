package com.sergeev.studapp.model;

import java.io.Serializable;

public interface Identified extends Serializable {
    String getId();
    Identified setId(String id);
}
