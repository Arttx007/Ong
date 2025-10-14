package com.poramordemuitos.controller;

import com.poramordemuitos.model.Usuario;
import com.poramordemuitos.model.Permissao;
import com.poramordemuitos.repository.UsuarioRepository;
import com.poramordemuitos.repository.PermissaoRepository;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PermissaoRepository permissaoRepository;
    private final PasswordEncoder passwordEncoder;

    // Injeção via construtor (recomendado)
    public UsuarioController(UsuarioRepository usuarioRepository,
                             PermissaoRepository permissaoRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.permissaoRepository = permissaoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já cadastrado.");
        }

        // Criptografa a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        if (usuario.getPermissao() != null) {
            permissaoRepository.save(usuario.getPermissao());
        }

        Usuario salvo = usuarioRepository.save(usuario);
        // Não retornar a senha no body por segurança
        salvo.setSenha(null);
        return ResponseEntity.ok(salvo);
    }

    @PutMapping("/{id}/permissoes")
    public ResponseEntity<?> atualizarPermissoes(@PathVariable Long id, @RequestBody Permissao novaPermissao) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        permissaoRepository.save(novaPermissao);
        usuario.setPermissao(novaPermissao);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Permissões atualizadas com sucesso.");
    }

    @PutMapping("/{id}/senha")
    public ResponseEntity<?> atualizarSenha(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        String novaSenha = body.get("senha");
        if (novaSenha == null || novaSenha.isBlank()) {
            return ResponseEntity.badRequest().body("Senha inválida.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        return ResponseEntity.ok("Senha atualizada com sucesso.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuário removido com sucesso.");
    }
}
