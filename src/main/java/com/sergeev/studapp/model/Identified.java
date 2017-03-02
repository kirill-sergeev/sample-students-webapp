package com.sergeev.studapp.model;

import java.io.Serializable;

public interface Identified<PK extends Serializable> {
    PK getId();
}
