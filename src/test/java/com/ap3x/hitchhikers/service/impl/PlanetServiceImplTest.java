package com.ap3x.hitchhikers.service.impl;

import com.ap3x.hitchhikers.controller.PlanetControllerTest;
import com.ap3x.hitchhikers.dto.PlanetDTO;
import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import com.ap3x.hitchhikers.model.SWPlanet;
import com.ap3x.hitchhikers.repository.PlanetRepository;
import javassist.NotFoundException;
import org.h2.table.Plan;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.persistence.EntityExistsException;
import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlanetServiceImplTest {

    @Mock
    private Environment env;

    @Mock
    private PlanetRepository repository;

    @Mock
    private SWServiceImpl swService;

    @InjectMocks
    private PlanetServiceImpl planetService;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);

        when(env.getProperty("server.host")).thenReturn("localhost");
        when(env.getProperty("server.port")).thenReturn("8080");
    }

    @Test
    public void listAll_nameSearch() {
        final List<Planet> planets = IntStream.range(1, 3)
                .mapToObj(PlanetServiceImplTest::getPlanet).collect(Collectors.toList());
        final Page<Planet> planetPage = new PageImpl<>(planets);

        when(repository.findAll(any(PageRequest.class))).thenReturn(planetPage);
        when(repository.findByNameIgnoreCaseContaining(eq("name"), any(PageRequest.class)))
                .thenReturn(planetPage);

        final PaginatedResponse<Planet> receivedPage = planetService.listAll(1, "name");

        assertEquals(2, receivedPage.getCount().longValue());
        assertEquals(planets, receivedPage.getData());

        verify(repository, times(0)).findAll(any(PageRequest.class));
        verify(repository)
                .findByNameIgnoreCaseContaining(eq("name"), any(PageRequest.class));
    }

    @Test
    public void listAll() {
        final List<Planet> planets = IntStream.range(1, 3)
                .mapToObj(PlanetServiceImplTest::getPlanet).collect(Collectors.toList());
        final Page<Planet> planetPage = new PageImpl<>(planets);

        when(repository.findAll(any(PageRequest.class))).thenReturn(planetPage);

        final PaginatedResponse<Planet> receivedPage = planetService.listAll(1, null);

        assertEquals(2, receivedPage.getCount().longValue());
        assertEquals(planets, receivedPage.getData());

        verify(repository).findAll(any(PageRequest.class));
        verify(repository, times(0))
                .findByNameIgnoreCaseContaining(eq("name"), any(PageRequest.class));
    }

    @Test
    public void getById() throws NotFoundException {
        final Optional<Planet> optionalPlanet = Optional.of(getPlanet(1));

        when(repository.findById(1)).thenReturn(optionalPlanet);

        final Planet planet = planetService.getById(1);

        assertEquals(optionalPlanet.get(), planet);

        verify(repository).findById(1);
    }

    @Test(expected = NotFoundException.class)
    public void getById_exception() throws NotFoundException {
        final Optional<Planet> optionalPlanet = Optional.empty();

        when(repository.findById(1)).thenReturn(optionalPlanet);

        planetService.getById(1);

        verify(repository).findById(1);
    }

    @Test
    public void create() {
        final SWPlanet swPlanet = new SWPlanet();
        swPlanet.setName("planet");
        final PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setName("planet");
        final Planet planet = new Planet();

        when(swService.getByName("planet")).thenReturn(swPlanet);
        when(repository.save(any(Planet.class))).thenReturn(planet);

        final Planet savedPlanet = planetService.create(planetDTO);

        assertEquals(planet, savedPlanet);

        verify(swService).getByName("planet");
        verify(repository).save(any(Planet.class));
    }

    @Test(expected = EntityExistsException.class)
    public void create_exception() {
        final SWPlanet swPlanet = new SWPlanet();
        swPlanet.setName("planet");
        final PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setName("planet");

        when(swService.getByName("planet")).thenReturn(swPlanet);
        when(repository.save(any(Planet.class))).thenThrow(new DataIntegrityViolationException("Fail"));

        planetService.create(planetDTO);

        verify(swService).getByName("planet");
        verify(repository).save(any(Planet.class));
    }

    @Test
    public void update() throws NotFoundException {
        final Planet planet = getPlanet(1);
        final Optional<Planet> optionalPlanet = Optional.of(planet);

        when(repository.findById(1)).thenReturn(optionalPlanet);
        when(repository.save(any(Planet.class))).thenReturn(planet);

        final Planet savedPlanet = planetService.update(1, new PlanetDTO());

        assertEquals(planet, savedPlanet);

        verify(repository).findById(1);
        verify(repository).save(any(Planet.class));
    }

    @Test(expected = NotFoundException.class)
    public void update_exception() throws NotFoundException {
        final Optional<Planet> optionalPlanet = Optional.empty();

        when(repository.findById(1)).thenReturn(optionalPlanet);

        planetService.update(1, new PlanetDTO());

        verify(repository).findById(1);
        verify(repository, times(0)).save(any(Planet.class));
    }

    @Test
    public void delete() throws NotFoundException {
        final Planet planet = getPlanet(1);
        final Optional<Planet> optionalPlanet = Optional.of(planet);

        when(repository.findById(1)).thenReturn(optionalPlanet);
        doNothing().when(repository).delete(planet);

        planetService.delete(1);

        verify(repository).findById(1);
        verify(repository).deleteById(1);
    }

    @Test(expected = NotFoundException.class)
    public void delete_exception() throws NotFoundException {
        final Optional<Planet> optionalPlanet = Optional.empty();

        when(repository.findById(1)).thenReturn(optionalPlanet);

        planetService.delete(1);

        verify(repository).findById(1);
        verify(repository, times(0)).deleteById(1);
    }

    private static Planet getPlanet(int i) {
        return new Planet(String.format("Planet %d", i), String.format("Climate %d", i), String.format("Terrain %d", i), i
        );
    }
}