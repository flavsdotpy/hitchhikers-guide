package com.ap3x.hitchhikers.models;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Planet {

    private @Id @GeneratedValue @Setter(AccessLevel.PROTECTED) Integer id;
    private String name;
    private String climate;
    private String terrain;
    private Integer numberOfFilmsApparitions;

}
