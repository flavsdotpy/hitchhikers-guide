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

    public PaginatedResponse<Planet> listAll(final Integer page, final String name){
        if (name != null)
            return getByName(page, name);
        return listAll(page);
    }

    public Planet getById(final Integer id) throws NotFoundException {
        final Optional<Planet> planet = repository.findById(id);
        if (!planet.isPresent())
            throw new NotFoundException(String.format("Planet with id %d not found", id));
        return planet.get();
    }

    public Planet create(final PlanetDTO planetDTO){
        final SWPlanet swPlanet = swService.getByName(planetDTO.getName());
        final Integer numberOfFilmsApparitions = swPlanet == null || swPlanet.getFilms() == null ? 0 :
                                                    swPlanet.getFilms().size();

        final Planet planet = new Planet(planetDTO);
        planet.setNumberOfFilmsApparitions(numberOfFilmsApparitions);
        try {
            return repository.save(planet);
        } catch (DataIntegrityViolationException ex) {
            throw new EntityExistsException(String.format("Planet %s already exists!", planet.getName()));
        }
    }

    public Planet update(final Integer id, final PlanetDTO planetDTO) throws NotFoundException {
        final Optional<Planet> savedPlanet = repository.findById(id);
        if (!savedPlanet.isPresent())
            throw new NotFoundException(String.format("Planet with id %d not found", id));
        final Planet planet = new Planet(planetDTO);
        planet.setId(savedPlanet.get().getId());
        planet.setNumberOfFilmsApparitions(savedPlanet.get().getNumberOfFilmsApparitions());
        return repository.save(planet);
    }

    public void delete(final Integer id) throws NotFoundException {
        final Optional<Planet> planet = repository.findById(id);
        if (!planet.isPresent())
            throw new NotFoundException(String.format("Planet with id %d not found", id));
        repository.deleteById(id);
    }

    private PaginatedResponse<Planet> listAll(final Integer page) {
        final Pageable pageConfig = PageRequest.of(page - 1, 10);
        final Page<Planet> planets = repository.findAll(pageConfig);

        return buildResponseBody(page, planets);
    }

    private PaginatedResponse<Planet> getByName(final Integer page, final String name) {
        final Pageable pageConfig = PageRequest.of(page - 1, 10);
        final Page<Planet> planets = repository.findByNameIgnoreCaseContaining(name, pageConfig);

        return buildResponseBody(page, planets);
    }

    private PaginatedResponse<Planet> buildResponseBody(final Integer page, final Page<Planet> planets) {
        final PaginatedResponse<Planet> body = new PaginatedResponse<>();
        body.setCount(planets.getTotalElements());
        if (page != 1)
            body.setPreviousPage(
                    String.format("http://%s:%s/api/v1/planets?page=%d",
                            env.getProperty("server.host"), env.getProperty("server.port"), page - 1));
        if (planets.hasNext())
            body.setNextPage(
                    String.format("http://%s:%s/api/v1/planets?page=%d",
                            env.getProperty("server.host"), env.getProperty("server.port"), page + 1));
        body.setData(planets.getContent());
        return body;
    }
}
