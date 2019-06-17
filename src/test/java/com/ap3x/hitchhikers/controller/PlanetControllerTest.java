package com.ap3x.hitchhikers.controller;

import com.ap3x.hitchhikers.dto.PlanetDTO;
import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import com.ap3x.hitchhikers.service.PlanetService;
import com.ap3x.hitchhikers.service.SWService;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class PlanetControllerTest {

    @Mock
    private SWService swService;

    @Mock
    private PlanetService planetService;

    @InjectMocks
    private PlanetController planetController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void list_local() {
        PaginatedResponse<Planet> response = new PaginatedResponse<>();
        List<Planet> planets = IntStream.range(1, 3)
                .mapToObj(PlanetControllerTest::getPlanet).collect(Collectors.toList());
        response.setCount(2L);
        response.setData(planets);
        response.setNextPage(null);
        response.setPreviousPage(null);

        when(planetService.listAll(1, null)).thenReturn(response);
        when(swService.listAll(1)).thenReturn(null);

        PaginatedResponse<Planet> testResponse = planetController.list(false, 1, null);

        assertEquals(2, testResponse.getData().size());
        assertEquals(2L, testResponse.getCount().longValue());
        assertNull(testResponse.getNextPage());
        assertNull(testResponse.getPreviousPage());

        verify(planetService).listAll(1, null);
        verify(swService, times(0)).listAll(1);
    }

    @Test
    public void list_sw() {
        PaginatedResponse<Planet> response = new PaginatedResponse<>();
        List<Planet> planets = IntStream.range(1, 3)
                .mapToObj(PlanetControllerTest::getPlanet).collect(Collectors.toList());
        response.setCount(2L);
        response.setData(planets);
        response.setNextPage(null);
        response.setPreviousPage(null);

        when(swService.listAll(1)).thenReturn(response);
        when(planetService.listAll(1, null)).thenReturn(null);

        PaginatedResponse<Planet> testResponse = planetController.list(true, 1, null);

        assertEquals(2, testResponse.getData().size());
        assertEquals(2L, testResponse.getCount().longValue());
        assertNull(testResponse.getNextPage());
        assertNull(testResponse.getPreviousPage());

        verify(planetService, times(0)).listAll(1, null);
        verify(swService).listAll(1);
    }

    @Test
    public void getById() throws NotFoundException {
        final Planet planet = getPlanet(1);

        when(planetService.getById(1)).thenReturn(planet);

        assertEquals(planet, planetController.getById(1));

        verify(planetService).getById(1);
    }

    @Test
    public void create() {
        final Planet planet = getPlanet(1);
        final PlanetDTO planetDTO = new PlanetDTO();

        when(planetService.create(planetDTO)).thenReturn(planet);

        assertEquals(planet, planetController.create(planetDTO));

        verify(planetService).create(planetDTO);
    }

    @Test
    public void update() throws NotFoundException {
        final Planet planet = getPlanet(1);
        final PlanetDTO planetDTO = new PlanetDTO();

        when(planetService.update(1, planetDTO)).thenReturn(planet);

        assertEquals(planet, planetController.update(1, planetDTO));

        verify(planetService).update(1, planetDTO);
    }

    @Test
    public void delete() throws NotFoundException {
        doNothing().when(planetService).delete(1);

        planetController.delete(1);

        verify(planetService).delete(1);
    }

    private static Planet getPlanet(int i) {
        return new Planet(String.format("Planet %d", i), String.format("Climate %d", i), String.format("Terrain %d", i), i
        );
    }
}