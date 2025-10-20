package com.poramordemuitos.repository;

import com.poramordemuitos.model.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoRepository extends JpaRepository<Foto, Long> {}