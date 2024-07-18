package com.eeae.Vega.servicio;

import com.eeae.Vega.controladorDTO.ReservaRegistroDTO;
import com.eeae.Vega.domain.Aula;
import com.eeae.Vega.domain.Reserva;
import com.eeae.Vega.repository.ReservaRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ReservaServicioImpl implements ReservaServicio {

    private ReservaRepository reservaRepository;

    //Constructor
    public ReservaServicioImpl(ReservaRepository reservaRepository) {
        super();
        this.reservaRepository = reservaRepository;
    }

    //Metodos
    @Override
    public List<Reserva> listarReservas() {
        return reservaRepository.findAll();
    }

    @Override
    public List<Reserva> listarReservasPendientes() {
        return reservaRepository.findAll().stream()
                .filter(reserva -> reserva.getEstado_reserva() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> listarReservasAceptadas() {
        return reservaRepository.findAll().stream()
                .filter(reserva -> reserva.getEstado_reserva() != null && reserva.getEstado_reserva() == true)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reserva> listarReservasRechazadas() {
        return reservaRepository.findAll().stream()
                .filter(reserva -> reserva.getEstado_reserva() != null && reserva.getEstado_reserva() == false)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Reserva> listarReservasPorAula(Aula aula) {
        return reservaRepository.findByFkaula(aula);
    }

    @Override
    public Reserva encontrarReserva(ReservaRegistroDTO reservaRegistroDTO) {
        Reserva reserva = reservaRepository.findByIdreserva(reservaRegistroDTO.getIdreserva());
        if (reserva != null) {
            return reserva;
        } else {
            throw new EntityNotFoundException("Reserva no encontrado");
        }
    }

    @Override
    public Reserva buscarReservaPorId(Integer idreserva) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(idreserva);
        if (reservaEncontrada != null) {
            return reservaEncontrada;
        } else {
            throw new EntityNotFoundException("Reserva no encontrada");
        }
    }

    @Override
    public Reserva guardar(ReservaRegistroDTO registroDTO) {
        Reserva reserva = new Reserva(registroDTO.getIdreserva(), registroDTO.getDia_inicio(),
                registroDTO.getDia_fin(), registroDTO.getHora_inicio(), registroDTO.getHora_fin(),
                registroDTO.getFk_solicitante(), registroDTO.getFk_autorizador(), registroDTO.getFkaula(),
                registroDTO.getNum_alumnos(), registroDTO.getCivil_militar(), registroDTO.getEjercito(),
                registroDTO.getEstado_reserva(), registroDTO.getObservaciones(), registroDTO.getNombre_curso());
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva actualizarReserva(ReservaRegistroDTO registroDTO) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(registroDTO.getIdreserva());
        if (reservaEncontrada != null) {
            reservaEncontrada.setDia_inicio(registroDTO.getDia_inicio());
            reservaEncontrada.setDia_fin(registroDTO.getDia_fin());
            reservaEncontrada.setHora_inicio(registroDTO.getHora_inicio());
            reservaEncontrada.setHora_fin(registroDTO.getHora_fin());
            reservaEncontrada.setFk_solicitante(registroDTO.getFk_solicitante());
            reservaEncontrada.setFk_autorizador(registroDTO.getFk_autorizador());
            reservaEncontrada.setFkaula(registroDTO.getFkaula());
            reservaEncontrada.setNum_alumnos(registroDTO.getNum_alumnos());
            reservaEncontrada.setCivil_militar(registroDTO.getCivil_militar());
            reservaEncontrada.setEjercito(registroDTO.getEjercito());
            reservaEncontrada.setEstado_reserva(registroDTO.getEstado_reserva());
            reservaEncontrada.setObservaciones(registroDTO.getObservaciones());
            reservaEncontrada.setNombre_curso(registroDTO.getNombre_curso());

            return reservaRepository.save(reservaEncontrada);
        } else {
            throw new EntityNotFoundException("Reserva no encontrada");
        }
    }

    @Override
    public Reserva aceptarReserva(ReservaRegistroDTO reservaRegistroDTO) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(reservaRegistroDTO.getIdreserva());
        if (reservaEncontrada != null) {
            reservaEncontrada.setEstado_reserva(true);
            reservaEncontrada.setFk_autorizador(reservaRegistroDTO.getFk_autorizador());
            return reservaRepository.save(reservaEncontrada);
        } else {
            throw new EntityNotFoundException("Reserva no encontrada");
        }
    }

    @Override
    public Reserva rechazarReserva(ReservaRegistroDTO reservaRegistroDTO) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(reservaRegistroDTO.getIdreserva());
        if (reservaEncontrada != null) {
            reservaEncontrada.setEstado_reserva(false);
            reservaEncontrada.setFk_autorizador(reservaRegistroDTO.getFk_autorizador());
            return reservaRepository.save(reservaEncontrada);
        } else {
            throw new EntityNotFoundException("Reserva no encontrada");
        }
    }

    @Override
    public void eliminar(ReservaRegistroDTO registroDTO) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(registroDTO.getIdreserva());
        if (reservaEncontrada != null) {
            reservaRepository.delete(reservaEncontrada);
        } else {
            throw new EntityNotFoundException("Aula no encontrado");
        }
    }
}
