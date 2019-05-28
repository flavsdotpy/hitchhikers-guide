package com.ap3x.hitchhikers.doc;

import com.ap3x.hitchhikers.dto.PlanetDTO;
import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;


public interface PlanetControllerSwaggerConfig {

    @ApiImplicitParams({
            @ApiImplicitParam(name = "sw", value = "Flag to set retrieve from SW's database",
                                dataType = "boolean", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "Page #", dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "Name for search", dataType = "string", paramType = "query")
    })
    @ApiOperation(  value = "Get planets",
                    notes = "List all planets, paginated by 10. " +
                            "If a name is passed as query parameter, it filters by name. " +
                            "If statusw is passed as true, it will retrieve from SW API database instead."
    )
    public PaginatedResponse<Planet> list(Boolean sw, Integer page, String name);

    @ApiOperation(  value = "Create planet",
            notes = "Create a planet. It if exists in SW API planet catalog, it will retrieve its films apparitions. " +
                    "If not, it will set films apparitions to 0. Obs: planet name is unique."
    )
    public Planet create(PlanetDTO planetDTO);

    @ApiOperation(  value = "Update planet",
            notes = "Update a planet by the Id, if it exists. It will never change its films aparitions."
    )
    public Planet update(Integer id, PlanetDTO planetDTO) throws NotFoundException;

    @ApiOperation(  value = "Get planet by Id.",
            notes = "Get a planet by the Id, if it exists."
    )
    public Planet getById(Integer id) throws NotFoundException ;

    @ApiOperation(  value = "Delete planet",
            notes = "Delete a planet by the Id, if it exists."
    )
    public void delete(Integer id) throws NotFoundException;

}
