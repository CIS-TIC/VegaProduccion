package com.eeae.Vega.repository;

import com.eeae.Vega.domain.Aula;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AulaRepository extends JpaRepository<Aula, String> {

    public Aula findByIdaula(String idaula);
    
    public List<Aula> findByEdificio(String edificio);

}