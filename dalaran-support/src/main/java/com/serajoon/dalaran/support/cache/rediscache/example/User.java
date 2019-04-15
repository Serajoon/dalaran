package com.serajoon.dalaran.support.cache.rediscache.example;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = -5053662935175578293L;
    private String id;
    private String name;
}
