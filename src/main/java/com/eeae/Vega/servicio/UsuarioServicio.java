package com.eeae.Vega.servicio;

import com.eeae.Vega.controladorDTO.UsuarioRegistroDTO;
import com.eeae.Vega.domain.Usuario;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioServicio extends UserDetailsService {

    public List<Usuario> listarUsuarios();
    
    public List<Usuario> listarUsuariosAutorizados();
    
    public List<Usuario> listarUsuariosPendientes();
    
    public Usuario guardar(UsuarioRegistroDTO registroDTO);

    public void eliminar(UsuarioRegistroDTO registroDTO);

    public Usuario buscarUsuarioPorMail(UsuarioRegistroDTO registroDTO);
    
    public Usuario buscarUsuarioPorId(Integer idusuario);
    
    public Usuario actualizar(UsuarioRegistroDTO registroDTO); 
}