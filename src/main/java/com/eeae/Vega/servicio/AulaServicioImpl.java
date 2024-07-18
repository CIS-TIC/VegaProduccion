package com.eeae.Vega.servicio;

import com.eeae.Vega.controladorDTO.AulaRegistroDTO;
import com.eeae.Vega.domain.Aula;
import com.eeae.Vega.repository.AulaRepository;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AulaServicioImpl implements AulaServicio {

    private AulaRepository aulaRepository;

    //Constructor
    public AulaServicioImpl(AulaRepository aulaRepository) {
        super();
        this.aulaRepository = aulaRepository;
    }

    @Override
    public Aula guardar(AulaRegistroDTO aulaRegistroDTO) {
        Aula aula = new Aula(aulaRegistroDTO.getIdaula(), aulaRegistroDTO.getEdificio(),
                aulaRegistroDTO.getNombre(), aulaRegistroDTO.getCapacidad(), aulaRegistroDTO.getPuestos(),
                aulaRegistroDTO.getModulo(), aulaRegistroDTO.getZar(), aulaRegistroDTO.getRap(),
                aulaRegistroDTO.getRed_ossorio(), aulaRegistroDTO.getWan_pg(), aulaRegistroDTO.getProyector(),
                aulaRegistroDTO.getPizarra_digital(), aulaRegistroDTO.getExtras());
        return aulaRepository.save(aula);
    }

    @Override
    public List<Aula> listarAulas() {
        return aulaRepository.findAll();
    }

    @Override
    public void eliminar(AulaRegistroDTO aulaRegistroDTO) {
        Aula aula = aulaRepository.findByIdaula(aulaRegistroDTO.getIdaula());
        if (aula != null) {
            aulaRepository.delete(aula);
        } else {
            throw new EntityNotFoundException("Aula no encontrado");
        }
    }

    @Override
    public Aula encontrarAula(AulaRegistroDTO aulaRegistroDTO) {
        Aula aula = aulaRepository.findByIdaula(aulaRegistroDTO.getIdaula());
        if (aula != null) {
            return aula;
        } else {
            throw new EntityNotFoundException("Aula no encontrado");
        }
    }

    @Override
    public Aula buscarAulaPorId(String idaula) {
        Aula aula = aulaRepository.findByIdaula(idaula);
        if (aula != null) {
            return aula;
        } else {
            throw new EntityNotFoundException("Aula no encontrada");
        }
    }

    @Override
    public Aula actualizar(AulaRegistroDTO registroDTO) {
        Aula aulaEncontrada = aulaRepository.findByIdaula(registroDTO.getIdaula());
        if (aulaEncontrada != null) {
            aulaEncontrada.setEdificio(registroDTO.getEdificio());
            aulaEncontrada.setNombre(registroDTO.getNombre());
            aulaEncontrada.setCapacidad(registroDTO.getCapacidad());
            aulaEncontrada.setPuestos(registroDTO.getPuestos());
            aulaEncontrada.setModulo(registroDTO.getModulo());
            aulaEncontrada.setZar(registroDTO.getZar());
            aulaEncontrada.setRap(registroDTO.getRap());
            aulaEncontrada.setRed_ossorio(registroDTO.getRed_ossorio());
            aulaEncontrada.setWan_pg(registroDTO.getWan_pg());
            aulaEncontrada.setProyector(registroDTO.getProyector());
            aulaEncontrada.setPizarra_digital(registroDTO.getPizarra_digital());
            aulaEncontrada.setExtras(registroDTO.getExtras());
            return aulaRepository.save(aulaEncontrada);
        } else {
            throw new EntityNotFoundException("Aula no encontrada");
        }
    }

    
    @Override
    public List<String> obtenerEdificiosUnicos() {
        List<Aula> listaAulas = aulaRepository.findAll();
        List<String> listaEdificiosUnicos = listaAulas.stream()
                .map(Aula::getEdificio)
                .distinct()
                .collect(Collectors.toList());
        return listaEdificiosUnicos;
    }
}
