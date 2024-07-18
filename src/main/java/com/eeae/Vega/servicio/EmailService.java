/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.eeae.Vega.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String variableMail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Notificacion de VEGA");
        message.setText("Aqui metemos texto para mandar por mail junto con la variable\n" + variableMail);
        mailSender.send(message);
    }
    
    //Metodos sin implementar (podria ser un exceso de mails...)
    public void confirmacionReserva(String to, Integer idreserva, String nombreaula) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Notificacion de VEGA");
        message.setText("Su reserva con ID " + idreserva + " del aula " + nombreaula + " ha sido aceptada.");
        mailSender.send(message);
    }
    
    public void rechazoReserva(String to, Integer idreserva, String nombreaula) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Notificacion de VEGA");
        message.setText("Su reserva con ID " + idreserva + " del aula " + nombreaula + " ha sido rechazada.");
        mailSender.send(message);
    }
}

