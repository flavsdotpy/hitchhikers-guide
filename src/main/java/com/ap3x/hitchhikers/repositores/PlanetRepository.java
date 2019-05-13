package com.ap3x.hitchhikers.repositores;

import com.ap3x.hitchhikers.models.Planet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Integer> {

    public Page<Planet> findByNameIgnoreCaseContaining(String name, Pageable pageable);
}
