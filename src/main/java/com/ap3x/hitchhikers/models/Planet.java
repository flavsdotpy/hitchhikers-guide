package com.ap3x.hitchhikers.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Planet {

    private @Id @GeneratedValue @Setter(AccessLevel.PROTECTED) Integer id;

    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    private String climate;
    @NotNull
    private String terrain;

    private Integer numberOfFilmsApparitions;

}
