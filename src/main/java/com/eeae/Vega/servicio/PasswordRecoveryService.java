package com.eeae.Vega.servicio;

import com.eeae.Vega.domain.Usuario;
import com.eeae.Vega.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PasswordRecoveryService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JavaMailSender mailSender;

    public Usuario findUserByEmail(String email) {
        return usuarioRepository.findByMail(email);
    }

    public Integer generateAndSendPin(Usuario usuario) {
        Integer pin = generatePin();
        usuario.setPinTemporal(pin);
        usuario.setPinExpiracion(LocalDateTime.now().plusMinutes(15));
        usuarioRepository.save(usuario);
        sendPinEmail(usuario.getMail(), pin);
        return pin;
    }

    private Integer generatePin() {
        Random random = new Random();
        int pin = 100000 + random.nextInt(900000); // Genera un PIN de 6 dígitos
        return Integer.valueOf(pin);
    }

    private void sendPinEmail(String email, Integer pin) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("PIN de recuperación de contraseña");
        message.setText("Su PIN de recuperación es: " + pin);
        mailSender.send(message);
    }

    public boolean validatePin(Usuario usuario, Integer pin) {
        return pin.equals(usuario.getPinTemporal()) && LocalDateTime.now().isBefore(usuario.getPinExpiracion());
    }

    public void clearPin(Usuario usuario) {
        usuario.setPinTemporal(null);
        usuario.setPinExpiracion(null);
        usuarioRepository.save(usuario);
    }
}
