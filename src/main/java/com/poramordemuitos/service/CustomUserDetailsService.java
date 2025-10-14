package com.poramordemuitos.service;

import com.poramordemuitos.model.Permissao;
import com.poramordemuitos.model.Usuario;
import com.poramordemuitos.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        Permissao p = usuario.getPermissao();
        if (p.isPodeAdicionar()) authorities.add(new SimpleGrantedAuthority("PERM_ADICIONAR"));
        if (p.isPodeEditar()) authorities.add(new SimpleGrantedAuthority("PERM_EDITAR"));
        if (p.isPodeExcluir()) authorities.add(new SimpleGrantedAuthority("PERM_EXCLUIR"));
        if (p.isPodeGerenciarUsuarios()) authorities.add(new SimpleGrantedAuthority("PERM_GERENCIAR_USUARIOS"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                authorities
        );
    }
}