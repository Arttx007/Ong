package com.poramordemuitos.controller;

import com.poramordemuitos.model.Usuario;
import com.poramordemuitos.model.Permissao;
import com.poramordemuitos.repository.UsuarioRepository;
import com.poramordemuitos.repository.PermissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PermissaoRepository permissaoRepository;

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já cadastrado.");
        }

        if (usuario.getPermissao() != null) {
            permissaoRepository.save(usuario.getPermissao());
        }

        usuarioRepository.save(usuario);
        return ResponseEntity.ok(usuario);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }

        usuarioRepository.deleteById(id);
        return ResponseEntity.ok("Usuário removido com sucesso.");
    }
}