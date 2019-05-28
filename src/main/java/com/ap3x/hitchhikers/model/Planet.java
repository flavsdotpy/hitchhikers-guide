package com.ap3x.hitchhikers.model;

import com.ap3x.hitchhikers.dto.PlanetDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Planet {

    public Planet(PlanetDTO dto) {
        this.name = dto.getName();
        this.climate = dto.getClimate();
        this.terrain = dto.getTerrain();
    }

    @Id
    @GeneratedValue
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    private String climate;
    @NotNull
    private String terrain;
    private Integer numberOfFilmsApparitions;

}
