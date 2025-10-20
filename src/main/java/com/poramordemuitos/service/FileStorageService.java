package com.poramordemuitos.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    private final Path root = Paths.get("uploads");

    public FileStorageService() throws IOException {
        if (!Files.exists(root)) Files.createDirectories(root);
    }

    public String store(MultipartFile file) throws IOException {
        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename().replaceAll("\\s+", "_");
        Path target = this.root.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return "/uploads/" + filename; // caminho público
    }

    public void delete(String relativePath) throws IOException {
        Path p = root.resolve(Paths.get(relativePath).getFileName());
        Files.deleteIfExists(p);
    }
}