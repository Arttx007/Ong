package com.poramordemuitos.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("minhaSenha123"); // coloque a senha que quer
        System.out.println(hash);
    }
}
