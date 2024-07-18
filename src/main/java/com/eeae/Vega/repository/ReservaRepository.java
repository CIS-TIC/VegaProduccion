package com.eeae.Vega.repository;

import com.eeae.Vega.domain.Aula;
import com.eeae.Vega.domain.Reserva;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    public Reserva findByIdreserva(Integer idreserva);
    
    @EntityGraph(attributePaths = "fkaula")
    public List<Reserva> findByFkaula(Aula aula);
  
}
