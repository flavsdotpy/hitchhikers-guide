package com.ap3x.hitchhikers.service.impl;

import com.ap3x.hitchhikers.dto.PlanetDTO;
import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import com.ap3x.hitchhikers.model.SWPlanet;
import com.ap3x.hitchhikers.repository.PlanetRepository;
import com.ap3x.hitchhikers.service.PlanetService;
import com.ap3x.hitchhikers.service.SWService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.Optional;

@Service
public class PlanetServiceImpl implements PlanetService {

    @Autowired
    private Environment env;

    @Autowired
    private PlanetRepository repository;

    @Autowired
    private SWService swService;

    public PaginatedResponse<Planet> listAll(Integer page, String name){
        if (name != null)
            return getByName(page, name);
        return listAll(page);
    }

    public Planet getById(Integer id) throws NotFoundException {
        Optional<Planet> planet = repository.findById(id);
        if (!planet.isPresent())
            throw new NotFoundException(String.format("Planet with id %d not found", id));
        return planet.get();
    }

    public Planet create(PlanetDTO planetDTO){
        SWPlanet swPlanet = swService.getByName(planetDTO.getName());
        Integer numberOfFilmsApparitions = swPlanet == null ? 0 : swPlanet.getFilms().size();

        Planet planet = new Planet(planetDTO);
        planet.setNumberOfFilmsApparitions(numberOfFilmsApparitions);
        try {
            return repository.save(planet);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityExistsException(String.format("Planet %s already exists!", planet.getName()));
        }
    }

    public Planet update(Integer id, PlanetDTO planetDTO) throws NotFoundException {
        Optional<Planet> savedPlanet = repository.findById(id);
        if (!savedPlanet.isPresent())
            throw new NotFoundException(String.format("Planet with id %d not found", id));
        Planet planet = new Planet(planetDTO);
        planet.setId(savedPlanet.get().getId());
        planet.setNumberOfFilmsApparitions(savedPlanet.get().getNumberOfFilmsApparitions());
        return repository.save(planet);
    }

    public void delete(Integer id) throws NotFoundException {
        Optional<Planet> planet = repository.findById(id);
        if (!planet.isPresent())
            throw new NotFoundException(String.format("Planet with id %d not found", id));
        repository.deleteById(id);
    }

    private PaginatedResponse<Planet> listAll(Integer page) {
        Pageable pageConfig = PageRequest.of(page - 1, 10);
        Page<Planet> planets = repository.findAll(pageConfig);

        return buildResponseBody(page, planets);
    }

    private PaginatedResponse<Planet> getByName(Integer page, String name) {
        Pageable pageConfig = PageRequest.of(page - 1, 10);
        Page<Planet> planets = repository.findByNameIgnoreCaseContaining(name, pageConfig);

        return buildResponseBody(page, planets);
    }

    private PaginatedResponse<Planet> buildResponseBody(Integer page, Page<Planet> planets) {
        PaginatedResponse<Planet> body = new PaginatedResponse<>();
        body.setCount(planets.getTotalElements());
        if (page != 1)
            body.setPreviousPage(
                    "http://" + env.getProperty("server.host") + ":" + env.getProperty("server.port") + "/api/v1/planets?page=" + (page - 1));
        if (planets.hasNext())
            body.setNextPage(
                    "http://" + env.getProperty("server.host") + ":" + env.getProperty("server.port") + "/api/v1/planets?page=" + (page + 1));
        body.setData(planets.getContent());
        return body;
    }
}
