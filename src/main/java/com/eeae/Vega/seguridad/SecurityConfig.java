package com.eeae.Vega.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.eeae.Vega.servicio.UsuarioServicio;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(usuarioServicio);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/registroUsuario", "/recuperarClave", "/recover-password", "/reset-password", "/invalid-token", "/enter-pin", "/js/**", "/css/**", "/img/**", "/login**").permitAll()
                .antMatchers("/administrador/", "/aceptarSolicitud/**", "/editarAula/**", "/editarUsuario/**", "/registroAula/**", "/solicitudesPendientes/**").hasRole("ADMINISTRADOR")
                .antMatchers("/coordinador/**").hasRole("COORDINADOR")
                .antMatchers("/profesor/**").hasRole("USER")
                .antMatchers("/mostrarUsuarios/**").hasAnyRole("ADMINISTRADOR", "COORDINADOR")
                .antMatchers("/mostrarReservas/**", "/editarReserva/**", "solicitudAula").hasAnyRole("USER", "COORDINADOR")
                .antMatchers("/mostrarAulas/**").hasAnyRole("USER", "COORDINADOR", "ADMINISTRADOR")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .permitAll();
    }
}