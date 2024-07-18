package com.eeae.Vega.controlador;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

@Controller
public class ControladorError implements ErrorController {

    @RequestMapping("/error")
    public String manejarError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "Página no encontrada");
            }
            else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "Error interno del servidor");
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "Acceso prohibido");
            }
            else if(statusCode == HttpStatus.BAD_REQUEST.value()) {
                model.addAttribute("error", "Solicitud incorrecta");
            }
            else if(statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("error", "No autorizado");
            }
            else if(statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                model.addAttribute("error", "Método no permitido");
            }
            else if(statusCode == HttpStatus.NOT_ACCEPTABLE.value()) {
                model.addAttribute("error", "No aceptable");
            }
            else if(statusCode == HttpStatus.REQUEST_TIMEOUT.value()) {
                model.addAttribute("error", "Tiempo de solicitud agotado");
            }
            else if(statusCode == HttpStatus.CONFLICT.value()) {
                model.addAttribute("error", "Conflicto");
            }
            else if(statusCode == HttpStatus.GONE.value()) {
                model.addAttribute("error", "Recurso no disponible");
            }
            else {
                model.addAttribute("error", "Error desconocido");
            }
        }
        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}
