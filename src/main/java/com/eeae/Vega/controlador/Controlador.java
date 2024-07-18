package com.eeae.Vega.controlador;

import com.eeae.Vega.controladorDTO.*;
import com.eeae.Vega.domain.*;
import com.eeae.Vega.repository.*;
import com.eeae.Vega.servicio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; 

@Controller
public class Controlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private AulaServicio aulaServicio;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private AulaRepository aulaRepository;

    @Autowired
    private ReservaServicio reservaServicio;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private PasswordRecoveryService passwordRecoveryService;

    @ModelAttribute("aula")
    public AulaRegistroDTO retornarNuevaAulaRegistroDTO() {
        return new AulaRegistroDTO();
    }

    @ModelAttribute("usuario")
    public UsuarioRegistroDTO retornarNuevoUsuarioRegistroDTO() {
        return new UsuarioRegistroDTO();
    }
    
    @ModelAttribute("reserva")
    public ReservaRegistroDTO retornarNuevaReservaRegistroDTO() {
        return new ReservaRegistroDTO();
    }
    
    //Muestra la ventana de login
    @GetMapping("/login")
    public String iniciarSesion() {
        return "login";
    }

    //Muestra la pagina de inicio (despues de haber iniciado sesion)
    @GetMapping("/")
    public String verPaginaDeInicio() {
        return "index";
    }
    
    // Muestra el formulario para recuperar la contraseña
    @GetMapping("/recover-password")
    public String mostrarFormularioRecuperarClave() {
        return "recover-password";
    }

    // Enviar PIN de recuperación
    @PostMapping("/recover-password")
    public String recoverPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        Usuario usuario = passwordRecoveryService.findUserByEmail(email);
        if (usuario != null) {
            passwordRecoveryService.generateAndSendPin(usuario);
            redirectAttributes.addFlashAttribute("message", "Se ha enviado un PIN a su correo electrónico.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró una cuenta con ese correo electrónico.");
        }
        return "redirect:/recover-password";
    }

    // Muestra el formulario para ingresar el PIN y cambiar la contraseña
    @GetMapping("/enter-pin")
    public String mostrarFormularioIngresarPin() {
        return "enter-pin";
    }

    // Validar el PIN e iniciar el cambio de contraseña
    @PostMapping("/enter-pin")
    public String validatePin(@RequestParam("email") String email, @RequestParam("pin") Integer pin, RedirectAttributes redirectAttributes, Model model) {
        Usuario usuario = passwordRecoveryService.findUserByEmail(email);
        if (usuario != null && passwordRecoveryService.validatePin(usuario, pin)) {
            model.addAttribute("email", email);
            passwordRecoveryService.clearPin(usuario);
            return "reset-password";
        } else {
            redirectAttributes.addFlashAttribute("error", "PIN inválido o expirado.");
            return "redirect:/enter-pin";
        }
    }

    // Muestra el formulario para cambiar la contraseña
    @GetMapping("/reset-password")
    public String mostrarFormularioCambiarClave(@RequestParam("email") String email, Model model) {
        model.addAttribute("email", email);
        return "reset-password";
    }

   // Cambiar la contraseña
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {
        Usuario usuario = passwordRecoveryService.findUserByEmail(email);
        if (usuario != null) {
            usuario.setClave(new BCryptPasswordEncoder().encode(newPassword));
            passwordRecoveryService.clearPin(usuario);

            return "redirect:/login?claveCambiada";
        } else {
            return "redirect:/reset-password?error";
        }
    }

    //////////////////////    USUARIOS   //////////////////////
    //Muestra el formulario de registro de un nuevo usuario
    @GetMapping("/registroUsuario")
    public String mostrarRegistroUsuario() {
        return "registroUsuario";
    }

    //Guarda el nuevo usuario en la BD
    @PostMapping("/registroUsuario")
    public String registroCuentaDeUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO) {
        usuarioServicio.guardar(registroDTO);
        return "redirect:/login?registroExito";
    }

    //Muestra el perfil del usuario
    @GetMapping("/perfilUsuario")
    public String verPerfilUsuario(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioIdentificado = authentication.getName();
        Usuario usuarioIdentificado = usuarioRepository.findByMail(emailUsuarioIdentificado);
        model.addAttribute("usuario", usuarioIdentificado);
        return "perfilUsuario";
    }

    //Efectua la actualizacion de datos de un usuario
    @PostMapping("/perfilUsuario")
    public String editarMisDatosUsuario(@ModelAttribute("usuario") UsuarioRegistroDTO registroDTO) {
        usuarioServicio.actualizar(registroDTO);
        return "redirect:/perfilUsuario?exito";
    }

    //Inicio administrador
    @GetMapping("/administrador")
    public String verPaginaAdministrador() {
        return "administrador";
    }

    /*
    Ver Listado de Usuarios Autorizados, desde aqui se puede ir a editar usuarios y eliminarlos
     */
    @GetMapping("/mostrarUsuarios")
    public String verPaginaListaUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioServicio.listarUsuariosAutorizados());
        return "mostrarUsuarios";
    }

    //Muestra el formulario para actualizar los datos de un usuario
    @GetMapping("/editarUsuario/{idusuario}")
    public String mostrarActualizarUsuario(@PathVariable("idusuario") Integer idusuario, Model model) {
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(idusuario);
        List<String> rolesDeUsuario = usuario.getRoles().stream()
                .map(Rol::getRol)
                .collect(Collectors.toList());
        model.addAttribute("usuario", usuario);
        model.addAttribute("rolesDeUsuario", rolesDeUsuario);
        return "editarUsuario";
    }

    //Efectua la actualizacion de datos de un usuario
    @PostMapping("/editarUsuario/{idusuario}")
    public String actualizarUsuario(@PathVariable("idusuario") Integer idusuario,
            @ModelAttribute("usuario") UsuarioRegistroDTO registroDTO,
            @RequestParam("roles") List<String> rolesSeleccionados) {

        Usuario usuario = usuarioServicio.buscarUsuarioPorId(idusuario);
        // Actualiza los roles del usuario con los seleccionados
        List<Rol> nuevosRoles = new ArrayList<>();
        for (String rol : rolesSeleccionados) {
            Rol rolSeleccionado = rolRepository.findByRol(rol);
            if (rolSeleccionado != null) {
                nuevosRoles.add(rolSeleccionado);
            }
        }
        usuario.setRoles(nuevosRoles);
        usuarioServicio.actualizar(registroDTO);
        return "redirect:/mostrarUsuarios?usuarioActualizado";
    }

    //Elimina un usuario de la BD
    @GetMapping("/eliminarUsuario/{idusuario}")
    public String eliminarUsuario(@PathVariable("idusuario") Integer idusuario) {
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(idusuario);
        if (usuario != null) {
            UsuarioRegistroDTO registroDTO = new UsuarioRegistroDTO();
            registroDTO.setMail(usuario.getMail());
            usuarioServicio.eliminar(registroDTO);
            return "redirect:/mostrarUsuarios?usuarioEliminado";
        }
        return "redirect:/mostrarUsuarios?error";
    }

    //Ver Listado de usuarios con SOLICITUDES PENDIENTES de alta
    @GetMapping("/solicitudesPendientes")
    public String verSolicitudesPendientes(Model model) {
        model.addAttribute("listaUsuarios", usuarioServicio.listarUsuariosPendientes());
        return "solicitudesPendientes";
    }

    //Muestra el formulario para Asignar un Rol a un usuario que este Pendiente.
    @GetMapping("/aceptarSolicitud/{idusuario}")
    public String mostrarAsignarRolUsuario(@PathVariable("idusuario") Integer idusuario, Model model) {
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(idusuario);
        List<String> rolesDeUsuario = usuario.getRoles().stream()
                .map(Rol::getRol)
                .collect(Collectors.toList());
        model.addAttribute("usuario", usuario);
        model.addAttribute("rolesDeUsuario", rolesDeUsuario);
        return "aceptarSolicitud";
    }

    //Efectua la Asignacion de uno o varios Roles a un usuario que estaba pendiente.
    @PostMapping("/aceptarSolicitud/{idusuario}")
    public String asignarRolUsuario(@PathVariable("idusuario") Integer idusuario,
            @ModelAttribute("usuario") UsuarioRegistroDTO registroDTO,
            @RequestParam("roles") List<String> rolesSeleccionados) {

        Usuario usuario = usuarioServicio.buscarUsuarioPorId(idusuario);
        // Actualiza los roles del usuario con los seleccionados
        List<Rol> nuevosRoles = new ArrayList<>();
        for (String rol : rolesSeleccionados) {
            Rol rolSeleccionado = rolRepository.findByRol(rol);
            if (rolSeleccionado != null) {
                nuevosRoles.add(rolSeleccionado);
            }
        }
        usuario.setRoles(nuevosRoles);
        usuarioServicio.actualizar(registroDTO);
        return "redirect:/solicitudesPendientes?exito";
    }

    //Elimina un usuario de la BD
    @GetMapping("/eliminarSolicitud/{idusuario}")
    public String eliminarSolicitud(@PathVariable("idusuario") Integer idusuario) {
        Usuario usuario = usuarioServicio.buscarUsuarioPorId(idusuario);
        if (usuario != null) {
            UsuarioRegistroDTO registroDTO = new UsuarioRegistroDTO();
            registroDTO.setMail(usuario.getMail());
            usuarioServicio.eliminar(registroDTO);
            return "redirect:/solicitudesPendientes?solicitudEliminada";
        } else {
            return "redirect:/solicitudesPendientes?error";
        }
    }

    /////////////////////  AULAS    ///////////////////////
    //Muestra la ventana con el formulario de registrar un aula nueva
    @GetMapping("/registroAula")
    public String mostrarRegistroAula() {
        return "registroAula";
    }

    //Guarda un nuevo aula en la BD
    @PostMapping("/registroAula")
    public String registroAula(@ModelAttribute("aula") AulaRegistroDTO aulaRegistroDTO) {
        aulaServicio.guardar(aulaRegistroDTO);
        return "redirect:/mostrarAulas?registroAula";
    }

    /*
    Ver listado de Aulas, desde aqui se pueden modificar Aulas y eliminarlas
     */
    @GetMapping("/mostrarAulas")
    public String verPaginaListaAulas(Model model) {
        model.addAttribute("listaAulas", aulaServicio.listarAulas());
        return "mostrarAulas";
    }
    //Igual que el anterior pero desde aqui solo pueden verse los detalles, es para usuarios, no se puede modificar
    @GetMapping("/mostrarAulasNoAdmin")
    public String verPaginaListaAulasNoAdmin(Model model) {
        model.addAttribute("listaAulas", aulaServicio.listarAulas());
        return "mostrarAulasNoAdmin";
    }

    // Muestra el formulario para modificar un aula
    @GetMapping("/editarAula/{idaula}")
    public String mostrarActualizarAula(@PathVariable("idaula") String idaula, Model model) {
        Aula aula = aulaServicio.buscarAulaPorId(idaula);
        model.addAttribute("aula", aula);
        return "editarAula";
    }
    
    // Muestra os datos de un aula
    @GetMapping("/verAula/{idaula}")
    public String verAula(@PathVariable("idaula") String idaula, Model model) {
        Aula aula = aulaServicio.buscarAulaPorId(idaula);
        model.addAttribute("aula", aula);
        return "verAula";
    }

    //Actualiza los datos de un aula en la BD
    @PostMapping("/editarAula/{idaula}")
    public String actualizarAula(@PathVariable("idaula") String idaula,
            @ModelAttribute("aula") AulaRegistroDTO registroDTO) {
        aulaServicio.actualizar(registroDTO);
        return "redirect:/mostrarAulas?aulaModificada";
    }

    //Elimina un aula de la BD
    @GetMapping("/eliminarAula/{idaula}")
    public String eliminarAula(@PathVariable("idaula") String idaula) {
        Aula aula = aulaServicio.buscarAulaPorId(idaula);
        if (aula != null) {
            AulaRegistroDTO registroDTO = new AulaRegistroDTO();
            registroDTO.setIdaula(aula.getIdaula());
            aulaServicio.eliminar(registroDTO);
            return "redirect:/mostrarAulas?aulaEliminada";
        } else {
            return "redirect:/mostarAulas?error";
        }
    }

    /////////////////////////// RESERVAS ////////////////////////

    //Muestra el formulario para solicitar el reserva de un aula
    @GetMapping("/solicitudAula")
    public String solicitarAula(Model model) {
        List<String> listaEdificios = aulaServicio.obtenerEdificiosUnicos();
        List<Aula> listaAulas = aulaServicio.listarAulas();
        model.addAttribute("listaEdificios", listaEdificios);
        model.addAttribute("aulas", listaAulas);
        return "solicitudAula";
    }

    //manejar la solicitud AJAX, para mostrar las aulas de un edificio
    @GetMapping("/aulasPorEdificio")
    @ResponseBody
    public List<Aula> getAulasPorEdificio(@RequestParam String edificio) {
        return aulaRepository.findByEdificio(edificio);
    }

    //Maneja solicitud AJAX, para mostrar capacidad y puestos de un aula
    @GetMapping("/aulaPorId")
    @ResponseBody
    public Aula getAulaPorId(@RequestParam String idaula) {
        return aulaServicio.buscarAulaPorId(idaula);
    }

    //Guarda una solicitud de reserva en la BD
    @PostMapping("/solicitudAula")
    public String guardarSolicitud(@ModelAttribute("reserva") ReservaRegistroDTO nuevaReserva) {
        //El usuario identificado es el UsuarioSolicitante
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioIdentificado = authentication.getName();
        Usuario usuarioIdentificado = usuarioRepository.findByMail(emailUsuarioIdentificado);
        nuevaReserva.setFk_solicitante(usuarioIdentificado);
        //Mientras no este confirmada la reserva el autorizador sera provisionalmente el propio usuario
        nuevaReserva.setFk_autorizador(usuarioIdentificado);

        if (nuevaReserva.getCivil_militar() == true) {
            nuevaReserva.setEjercito("Civil");
        }

        java.sql.Date inicio = nuevaReserva.getDia_inicio();
        java.sql.Date fin = nuevaReserva.getDia_fin();
        do {
            nuevaReserva.setDia_inicio(inicio);
            nuevaReserva.setDia_fin(inicio);
            reservaServicio.guardar(nuevaReserva);
            inicio = new java.sql.Date(inicio.getTime() + (1000 * 60 * 60 * 24)); // Suma un día
        } while (inicio.before(fin) || inicio.equals(fin));
        return "redirect:/profesor?solicitudAula";
    }

    //Inicio profesor
    @GetMapping("/profesor")
    public String verPaginaProfesor() {
        return "profesor";
    }

    // Método para manejar la lógica común de mostrar reservas
    private void mostrarReservas(Model model, List<Reserva> reservas) {
        // Obtener la autenticación del usuario actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioIdentificado = authentication.getName();
        // Buscar el usuario en el repositorio por su email
        Usuario usuarioIdentificado = usuarioRepository.findByMail(emailUsuarioIdentificado);

        // Obtener los roles del usuario
        List<String> rolesDeUsuario = usuarioIdentificado.getRoles().stream()
                .map(Rol::getRol)
                .collect(Collectors.toList());

        // Si el usuario es un coordinador, mostrar todas las reservas
        if (rolesDeUsuario.contains("ROLE_COORDINADOR")) {
            model.addAttribute("listaReservas", reservas);
        } else {
            // Si no, mostrar solo las reservas solicitadas por cada usuario
            List<Reserva> reservasDelUsuario = reservas.stream()
                    .filter(reserva -> reserva.getFk_solicitante().equals(usuarioIdentificado))
                    .collect(Collectors.toList());
            model.addAttribute("listaReservas", reservasDelUsuario);
        }
    }

    // Método para mostrar todas las reservas
    @GetMapping("/mostrarReservas")
    public String verPaginaListaReservas(Model model) {
        mostrarReservas(model, reservaServicio.listarReservas());
        return "mostrarReservas";
    }

    // Método para mostrar las reservas pendientes
    @GetMapping("/mostrarReservasPendientes")
    public String verPaginaListaReservasPendientes(Model model) {
        mostrarReservas(model, reservaServicio.listarReservasPendientes());
        return "mostrarReservas";
    }

    // Método para mostrar las reservas aceptadas
    @GetMapping("/mostrarReservasAceptadas")
    public String verPaginaListaReservasAceptadas(Model model) {
        mostrarReservas(model, reservaServicio.listarReservasAceptadas());
        return "mostrarReservas";
    }

    // Método para mostrar las reservas rechazadas
    @GetMapping("/mostrarReservasRechazadas")
    public String verPaginaListaReservasRechazadas(Model model) {
        mostrarReservas(model, reservaServicio.listarReservasRechazadas());
        return "mostrarReservas";
    }

    // Muestra el formulario para MODIFICAR una RESERVA
    @GetMapping("/editarReserva/{idreserva}")
    public String mostrarEditarReserva(@PathVariable("idreserva") Integer idreserva, Model model) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(idreserva);
        model.addAttribute("reserva", reservaEncontrada);
        
        List<String> listaEdificios = aulaServicio.obtenerEdificiosUnicos();
        model.addAttribute("listaEdificios", listaEdificios);

        List<Aula> listaAulas = aulaServicio.listarAulas();
        model.addAttribute("aulas", listaAulas);

        return "editarReserva";
    }

    //Se ACTUALIZA los datos de una reserva en la BD
    @PostMapping("/editarReserva/{idreserva}")
    public String actualizarReserva(@PathVariable("idreserva") Integer idreserva,
            @ModelAttribute("reserva") ReservaRegistroDTO reservaRegistroDTO) {
        //El usuario identificado como coordinador, que modifica la reserva es el UsuarioAutorizador
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioIdentificado = authentication.getName();
        Usuario usuarioIdentificado = usuarioRepository.findByMail(emailUsuarioIdentificado);

        reservaRegistroDTO.setFk_autorizador(usuarioIdentificado);

        reservaServicio.actualizarReserva(reservaRegistroDTO);
        return "redirect:/mostrarReservas?modificadaReserva";
    }

    //Se ELIMINA una reserva de la BD
    @GetMapping("/eliminarReserva/{idreserva}")
    public String eliminarReserva(@PathVariable("idreserva") Integer idreserva) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(idreserva);
        if (reservaEncontrada != null) {
            ReservaRegistroDTO registroDTO = new ReservaRegistroDTO();
            registroDTO.setIdreserva(reservaEncontrada.getIdreserva());

            reservaServicio.eliminar(registroDTO);
            return "redirect:/mostrarReservas?eliminadaReserva";
        } else {
            return "redirect:/mostrarReservas?error";
        }
    }

    //Se ACEPTA una reserva de la BD (estado_reserva = true)
    @GetMapping("/aceptarReserva/{idreserva}")
    public String aceptarReserva(@PathVariable("idreserva") Integer idreserva) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(idreserva);
        if (reservaEncontrada != null) {
            //El usuario identificado como coordinador es el UsuarioAutorizador
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String emailUsuarioIdentificado = authentication.getName();
            Usuario usuarioIdentificado = usuarioRepository.findByMail(emailUsuarioIdentificado);

            ReservaRegistroDTO reservaRegistroDTO = new ReservaRegistroDTO();
            //Se modifican los datos
            reservaRegistroDTO.setFk_autorizador(usuarioIdentificado);
            reservaRegistroDTO.setIdreserva(reservaEncontrada.getIdreserva());

            reservaServicio.aceptarReserva(reservaRegistroDTO);
            return "redirect:/mostrarReservas?aceptadaReserva";
        } else {
            return "redirect:/mostrarReservas?error";
        }
    }

    //Se RECHAZA una reserva de la BD (estado_reserva = false)
    @GetMapping("/rechazarReserva/{idreserva}")
    public String rechazarReserva(@PathVariable("idreserva") Integer idreserva) {
        Reserva reservaEncontrada = reservaRepository.findByIdreserva(idreserva);
        if (reservaEncontrada != null) {
            //El usuario identificado como coordinador es el UsuarioAutorizador
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String emailUsuarioIdentificado = authentication.getName();
            Usuario usuarioIdentificado = usuarioRepository.findByMail(emailUsuarioIdentificado);

            ReservaRegistroDTO reservaRegistroDTO = new ReservaRegistroDTO();
            //Se modifican los datos
            reservaRegistroDTO.setFk_autorizador(usuarioIdentificado);
            reservaRegistroDTO.setIdreserva(reservaEncontrada.getIdreserva());
            reservaServicio.rechazarReserva(reservaRegistroDTO);
            return "redirect:/mostrarReservas?rechazadaReserva";
        } else {
            return "redirect:/mostrarReservas?error";
        }
    }

    @GetMapping("/estadoOcupacion")
    public String estadoOcupacion(Model model) {
        List<Aula> aulas = aulaRepository.findAll();
        model.addAttribute("aulas", aulas);
        Aula aulaSeleccionada = new Aula();
        model.addAttribute("aulaSeleccionada", aulaSeleccionada);
        return"estadoOcupacion";
    }
    
    @GetMapping("/eventos")
    public @ResponseBody
    void obtenerEventos(HttpServletResponse response) {
        // Crear un ArrayList para almacenar los eventos
        List<Event> events = new ArrayList<>();

        // Obtener la lista de reservas
        List<Reserva> listaReservas = reservaServicio.listarReservas();
        //Obtener usuario actual
        //El usuario identificado es el UsuarioSolicitante
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioIdentificado = authentication.getName();
        Usuario usuarioIdentificado = usuarioRepository.findByMail(emailUsuarioIdentificado);

        // Recorrer los resultados y agregarlos al ArrayList
        for (Reserva reserva : listaReservas) {
            if (reserva.getFk_solicitante().getMail().equals(usuarioIdentificado.getMail())) {
                Event event = new Event();
                String GFHini = new SimpleDateFormat("yyyy-MM-dd").format(reserva.getDia_inicio()) + "T" + reserva.getHora_inicio().toString();
                String GFHfin = new SimpleDateFormat("yyyy-MM-dd").format(reserva.getDia_fin()) + "T" + reserva.getHora_fin().toString();
                String codigo_color = "";
                if (reserva.getEstado_reserva() != null) {
                    if (reserva.getEstado_reserva()) {
                        codigo_color = "green";
                    } else {
                        codigo_color = "red";
                    }
                } else {
                    codigo_color = "orange";
                }
                event.setTitle(reserva.getFkaula().getNombre());
                event.setStart(GFHini);
                event.setEnd(GFHfin);
                event.setColor(codigo_color);
                events.add(event);
            }
        }

        // Serializar los eventos en formato JSON
        JSONArray jsonArray = new JSONArray();
        for (Event event : events) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", event.getTitle());
            jsonObject.put("start", event.getStart());
            jsonObject.put("end", event.getEnd());
            jsonObject.put("color", event.getColor());
            jsonArray.put(jsonObject);
        }

        // Enviar la respuesta JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(jsonArray.toString());
        } catch (Exception e) {
            // Manejar la excepción
        }
    }
    
    //Listar los eventos de un aula concreta
    @GetMapping("/eventos/{idaula}")
    public @ResponseBody
    void obtenerEventosPorAula(@PathVariable ("idaula") String idaula, HttpServletResponse response) {
        List<Event> events = new ArrayList<>();
        Aula aula = aulaRepository.findByIdaula(idaula);
        List<Reserva> listaReservas = reservaServicio.listarReservasPorAula(aula);
        // Recorrer los resultados y agregarlos al ArrayList
        //Para evitar errores en la respuesta del JSON, se añaden todos los elementos, pero sólo se mostrarán los que sean reservas aceptadas,
        // las pendientes y rechazadas estarán en el JSON pero no se mostrarán en el calendario
        for (Reserva reserva : listaReservas) {
                Event event = new Event();
                String GFHini = new SimpleDateFormat("yyyy-MM-dd").format(reserva.getDia_inicio()) + "T" + reserva.getHora_inicio().toString();
                String GFHfin = new SimpleDateFormat("yyyy-MM-dd").format(reserva.getDia_fin()) + "T" + reserva.getHora_fin().toString();
                String codigo_color = "";
                String visible = "none";
                if (reserva.getEstado_reserva() != null) {
                    if (reserva.getEstado_reserva()) {
                        codigo_color = "green";
                        visible = "auto";
                    } else {
                        codigo_color = "red";
                    }
                } else {
                    codigo_color = "orange";
                }
                event.setTitle(reserva.getNombre_curso());
                event.setStart(GFHini);
                event.setEnd(GFHfin);
                event.setColor(codigo_color);
                event.setDisplay(visible);
                events.add(event);
            }

        // Serializar los eventos en formato JSON
        JSONArray jsonArray = new JSONArray();
        for (Event event : events) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("title", event.getTitle());
            jsonObject.put("start", event.getStart());
            jsonObject.put("end", event.getEnd());
            jsonObject.put("color", event.getColor());
            jsonObject.put("display", event.getDisplay());
            jsonArray.put(jsonObject);
        }

        // Enviar la respuesta JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(jsonArray.toString());
        } catch (Exception e) {
            // Manejar la excepción
        }
    }
    
}
