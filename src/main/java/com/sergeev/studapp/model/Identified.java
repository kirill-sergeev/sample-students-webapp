package com.sergeev.studapp.model;

import java.io.Serializable;

public interface Identified extends Serializable {

    Integer getId();

    Identified setId(Integer id);

}
