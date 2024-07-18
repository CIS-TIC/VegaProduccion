package com.eeae.Vega.controladorDTO;

import com.eeae.Vega.domain.Aula;
import com.eeae.Vega.domain.Usuario;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservaRegistroDTO {

    private Integer idreserva;
    private Date dia_inicio;
    private Date dia_fin;
    private LocalTime hora_inicio;
    private LocalTime hora_fin;
    private Usuario fk_solicitante;
    private Usuario fk_autorizador;
    private Aula fkaula;
    private Integer num_alumnos;
    private Boolean civil_militar;
    private String ejercito;
    private Boolean estado_reserva;
    private String observaciones;
    private String nombre_curso;

    public ReservaRegistroDTO() {
    }

    public ReservaRegistroDTO(Integer idreserva, Date dia_inicio, Date dia_fin,
            LocalTime hora_inicio, LocalTime hora_fin, Usuario fk_solicitante,
            Usuario fk_autorizador, Aula fkaula, Integer num_alumnos,
            Boolean civil_militar, String ejercito, Boolean estado_reserva,
            String observaciones, String nombre_curso) {
        this.idreserva = idreserva;
        this.dia_inicio = dia_inicio;
        this.dia_fin = dia_fin;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.fk_solicitante = fk_solicitante;
        this.fk_autorizador = fk_autorizador;
        this.fkaula = fkaula;
        this.num_alumnos = num_alumnos;
        this.civil_militar = civil_militar;
        this.ejercito = ejercito;
        this.estado_reserva = estado_reserva;
        this.observaciones = observaciones;
        this.nombre_curso = nombre_curso;
    }

    public ReservaRegistroDTO(Date dia_inicio, Date dia_fin, LocalTime hora_inicio,
            LocalTime hora_fin, Usuario fk_solicitante, Usuario fk_autorizador,
            Aula fkaula, Integer num_alumnos, Boolean civil_militar,
            String ejercito, Boolean estado_reserva, String observaciones,
            String nombre_curso) {
        this.dia_inicio = dia_inicio;
        this.dia_fin = dia_fin;
        this.hora_inicio = hora_inicio;
        this.hora_fin = hora_fin;
        this.fk_solicitante = fk_solicitante;
        this.fk_autorizador = fk_autorizador;
        this.fkaula = fkaula;
        this.num_alumnos = num_alumnos;
        this.civil_militar = civil_militar;
        this.ejercito = ejercito;
        this.estado_reserva = estado_reserva;
        this.observaciones = observaciones;
        this.nombre_curso = nombre_curso;
    }

    public Integer getIdreserva() {
        return idreserva;
    }

    public void setIdreserva(Integer idreserva) {
        this.idreserva = idreserva;
    }

    public Date getDia_inicio() {
        return dia_inicio;
    }

    public void setDia_inicio(Date dia_inicio) {
        this.dia_inicio = dia_inicio;
    }

    public Date getDia_fin() {
        return dia_fin;
    }

    public void setDia_fin(Date dia_fin) {
        this.dia_fin = dia_fin;
    }

    public LocalTime getHora_inicio() {
        return hora_inicio;
    }

    public void setHora_inicio(LocalTime hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public LocalTime getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(LocalTime hora_fin) {
        this.hora_fin = hora_fin;
    }

    public Usuario getFk_solicitante() {
        return fk_solicitante;
    }

    public void setFk_solicitante(Usuario fk_solicitante) {
        this.fk_solicitante = fk_solicitante;
    }

    public Usuario getFk_autorizador() {
        return fk_autorizador;
    }

    public void setFk_autorizador(Usuario fk_autorizador) {
        this.fk_autorizador = fk_autorizador;
    }

    public Aula getFkaula() {
        return fkaula;
    }

    public void setFkaula(Aula fkaula) {
        this.fkaula = fkaula;
    }

    public Integer getNum_alumnos() {
        return num_alumnos;
    }

    public void setNum_alumnos(Integer num_alumnos) {
        this.num_alumnos = num_alumnos;
    }

    public Boolean getCivil_militar() {
        return civil_militar;
    }

    public void setCivil_militar(Boolean civil_militar) {
        this.civil_militar = civil_militar;
    }

    public String getEjercito() {
        return ejercito;
    }

    public void setEjercito(String ejercito) {
        this.ejercito = ejercito;
    }

    public Boolean getEstado_reserva() {
        return estado_reserva;
    }

    public void setEstado_reserva(Boolean estado_reserva) {
        this.estado_reserva = estado_reserva;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getNombre_curso() {
        return nombre_curso;
    }

    public void setNombre_curso(String nombre_curso) {
        this.nombre_curso = nombre_curso;
    }
}
