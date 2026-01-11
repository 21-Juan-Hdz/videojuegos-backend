package com.videojuegos.controller;

import com.videojuegos.dto.auth.LoginRequest;
import com.videojuegos.dto.auth.RegisterRequest;
import com.videojuegos.model.Usuario;
import com.videojuegos.repository.UsuarioRepository;
import com.videojuegos.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
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
                .rol("USER")
                .estado("ACTIVO")
                .build();

        Usuario saved = usuarioRepository.save(u);
        String token = jwtService.generateToken(saved.getEmail());

        // Enviamos el usuario completo
        return ResponseEntity.ok(new AuthResponse(
                saved.getIdUsuario(),
                saved.getNombre(),
                saved.getApellido(),
                saved.getEmail(),
                saved.getRol(),
                token
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        Usuario u = usuarioRepository.findByEmail(req.getEmail()).orElseThrow();
        String token = jwtService.generateToken(req.getEmail());

        // Enviamos el usuario completo
        return ResponseEntity.ok(new AuthResponse(
                u.getIdUsuario(),
                u.getNombre(),
                u.getApellido(),
                u.getEmail(),
                u.getRol(),
                token
        ));
    }

    // Definimos qué datos se envían al Frontend
    public record AuthResponse(
            Long idUsuario,
            String nombre,
            String apellido,
            String email,
            String rol,
            String token
    ) {}
}