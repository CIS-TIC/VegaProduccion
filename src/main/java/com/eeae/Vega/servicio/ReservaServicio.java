package com.eeae.Vega.servicio;

import com.eeae.Vega.controladorDTO.ReservaRegistroDTO;
import com.eeae.Vega.domain.Aula;
import com.eeae.Vega.domain.Reserva;
import java.util.List;

public interface ReservaServicio {

    public List<Reserva> listarReservas();

    public List<Reserva> listarReservasPendientes();
  
    public List<Reserva> listarReservasAceptadas();
    
    public List<Reserva> listarReservasRechazadas();
    
    public List<Reserva> listarReservasPorAula(Aula aula);

    public Reserva guardar(ReservaRegistroDTO registroDTO);

    public void eliminar(ReservaRegistroDTO registroDTO);
    
    public Reserva buscarReservaPorId(Integer idreserva);

    public Reserva encontrarReserva(ReservaRegistroDTO registroDTO);
    
    public Reserva actualizarReserva(ReservaRegistroDTO registroDTO);
    
    public Reserva aceptarReserva(ReservaRegistroDTO registroDTO);
    
    public Reserva rechazarReserva(ReservaRegistroDTO registroDTO);

}
