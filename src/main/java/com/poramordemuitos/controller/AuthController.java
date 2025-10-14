package com.poramordemuitos.controller;

import com.poramordemuitos.model.Usuario;
import com.poramordemuitos.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeção via construtor (recomendado)
    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");

        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null || !passwordEncoder.matches(senha, usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuário ou senha inválidos!"));
        }

        String token = UUID.randomUUID().toString();

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("token", token);
        resposta.put("usuarioId", usuario.getId());
        resposta.put("nome", usuario.getNome());
        resposta.put("permissao", usuario.getPermissao());

        return ResponseEntity.ok(resposta);
    }
}
