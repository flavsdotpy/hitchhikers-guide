package com.ap3x.hitchhikers.model;

import com.ap3x.hitchhikers.dto.PlanetDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "planets")
public class Planet {

    public Planet(@NotNull String name, @NotNull String climate, @NotNull String terrain, Integer numberOfFilmsApparitions) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
        this.numberOfFilmsApparitions = numberOfFilmsApparitions;
    }

    public Planet(PlanetDTO dto) {
        this.name = dto.getName();
        this.climate = dto.getClimate();
        this.terrain = dto.getTerrain();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    private String climate;
    @NotNull
    private String terrain;

    @Column(name = "no_of_films_apparitions")
    private Integer numberOfFilmsApparitions;

}
