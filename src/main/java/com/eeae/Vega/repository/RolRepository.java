package com.eeae.Vega.repository;

import com.eeae.Vega.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, String> {
    Rol findByRol(String rol);
}