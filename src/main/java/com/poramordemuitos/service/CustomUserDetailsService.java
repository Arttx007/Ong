package com.poramordemuitos.service;

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
    // Usando o username passado pelo Spring para buscar pelo nome
    Usuario usuario = usuarioRepository.findByNome(username);
    if (usuario == null) {
        throw new UsernameNotFoundException("Usuário não encontrado");
    }

    List<GrantedAuthority> authorities = new ArrayList<>();
    if (usuario.getPermissao() != null && usuario.getPermissao().isPodeGerenciarUsuarios()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    } else {
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
    }

    return User.builder()
            .username(usuario.getNome())
            .password(usuario.getSenha()) // senha já criptografada com BCrypt
            .authorities(authorities)
            .build();
}
}
