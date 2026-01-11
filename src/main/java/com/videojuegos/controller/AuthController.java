package com.videojuegos.controller;

import com.videojuegos.dto.auth.LoginRequest;
import com.videojuegos.dto.auth.RegisterRequest;
import com.videojuegos.model.Usuario;
import com.videojuegos.repository.UsuarioRepository;
import com.videojuegos.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthController(UsuarioRepository usuarioRepository,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtService jwtService) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
    if (req.getEmail() == null || req.getPassword() == null) {
      return ResponseEntity.badRequest().body("email y password son requeridos");
    }
    if (usuarioRepository.findByEmail(req.getEmail()).isPresent()) {
      return ResponseEntity.badRequest().body("El email ya está registrado");
    }

    Usuario u = Usuario.builder()
        .nombre(req.getNombre())
        .apellido(req.getApellido())
        .email(req.getEmail())
        .password(passwordEncoder.encode(req.getPassword()))
        .telefono(req.getTelefono())
        .fechaIngreso(LocalDate.now())
        .rol(req.getRol() == null ? "USER" : req.getRol())
        .estado(req.getEstado() == null ? "ACTIVO" : req.getEstado())
        .build();

    Usuario saved = usuarioRepository.save(u);
    String token = jwtService.generateToken(saved.getEmail());
    return ResponseEntity.ok(new AuthResponse(saved.getIdUsuario(), saved.getEmail(), saved.getRol(), token));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
    );
    String token = jwtService.generateToken(req.getEmail());
    Usuario u = usuarioRepository.findByEmail(req.getEmail()).orElse(null);
    Long id = u != null ? u.getIdUsuario() : null;
    String rol = u != null ? u.getRol() : null;
    return ResponseEntity.ok(new AuthResponse(id, req.getEmail(), rol,  token));
  }

  public record AuthResponse(Long idUsuario, String email, String rol, String token) {}
}
