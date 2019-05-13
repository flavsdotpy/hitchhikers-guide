package com.ap3x.hitchhikers.controllers;

import com.ap3x.hitchhikers.models.PaginatedResponse;
import com.ap3x.hitchhikers.models.Planet;
import com.ap3x.hitchhikers.models.SWPlanet;
import com.ap3x.hitchhikers.services.PlanetService;
import com.ap3x.hitchhikers.services.SWService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/planets")
public class PlanetController {

    @Autowired
    private SWService swService;

    @Autowired
    private PlanetService planetService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PaginatedResponse<Planet> list(
            @RequestParam(defaultValue = "false") Boolean sw,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false) String name) {
        if (sw)
            return listFromSW(page);
        else {
            return listFromBase(page, name);
        }
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Planet getById(@PathVariable("id") Integer id) throws NotFoundException {
        return planetService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planet create(@RequestBody Planet planet) {
        SWPlanet swPlanet = swService.getByName(planet.getName());
        Integer numberOfFilmsApparitions = swPlanet == null ? 0 : swPlanet.getFilms().size();
        planet.setNumberOfFilmsApparitions(numberOfFilmsApparitions);
        return planetService.create(planet);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Planet update(@PathVariable("id") Integer id, @RequestBody Planet planet) throws NotFoundException {
        return planetService.update(id, planet);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) throws NotFoundException {
        planetService.delete(id);
    }


    private PaginatedResponse<Planet> listFromSW(Integer page) {
        return swService.listAll(page);
    }

    private PaginatedResponse<Planet> listFromBase(Integer page, String name) {
        return planetService.listAll(page, name);
    }

}
