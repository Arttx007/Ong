package com.poramordemuitos.controller;

import com.poramordemuitos.model.Beneficiario;
import com.poramordemuitos.model.Foto;
import com.poramordemuitos.model.Usuario;
import com.poramordemuitos.repository.BeneficiarioRepository;
import com.poramordemuitos.repository.FotoRepository;
import com.poramordemuitos.repository.UsuarioRepository;
import com.poramordemuitos.service.BeneficiarioService;
import com.poramordemuitos.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/beneficiarios")
@CrossOrigin(origins = "*")
public class BeneficiarioController {

    private final BeneficiarioService service;
    private final FileStorageService fileStorage;
    private final FotoRepository fotoRepo;
    private final UsuarioRepository usuarioRepository;
    private final BeneficiarioRepository beneficiarioRepository;

    public BeneficiarioController(
            BeneficiarioService service,
            FileStorageService fileStorage,
            FotoRepository fotoRepo,
            UsuarioRepository usuarioRepository,
            BeneficiarioRepository beneficiarioRepository) {
        this.service = service;
        this.fileStorage = fileStorage;
        this.fotoRepo = fotoRepo;
        this.usuarioRepository = usuarioRepository;
        this.beneficiarioRepository = beneficiarioRepository;
    }

    @GetMapping
    public List<Beneficiario> listar() {
        return service.listar();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> criarBeneficiario(
            @RequestParam("nome") String nome,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "foto", required = false) MultipartFile foto,
            @RequestHeader("usuarioId") Long usuarioId
    ) {
        try {
            Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
            if (usuario == null || usuario.getPermissao() == null || !usuario.getPermissao().isPodeAdicionar()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não tem permissão para adicionar beneficiários.");
            }

            Beneficiario b = new Beneficiario();
            b.setNome(nome);
            b.setDescricao(descricao);
            b.setDataAtualizacao(LocalDate.now());
            Beneficiario salvo = beneficiarioRepository.save(b);

            if (foto != null && !foto.isEmpty()) {
                String url = fileStorage.store(foto);
                Foto f = new Foto();
                f.setUrl(url);
                f.setBeneficiario(salvo);
                fotoRepo.save(f);
            }

            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao criar beneficiário: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> editarBeneficiario(
            @PathVariable Long id,
            @RequestParam("nome") String nome,
            @RequestParam(value = "descricao", required = false) String descricao,
            @RequestParam(value = "foto", required = false) MultipartFile novaFoto,
            @RequestHeader("usuarioId") Long usuarioId
    ) throws IOException {

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null || usuario.getPermissao() == null || !usuario.getPermissao().isPodeEditar()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para editar beneficiários.");
        }

        Beneficiario beneficiario = beneficiarioRepository.findById(id).orElse(null);
        if (beneficiario == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Beneficiário não encontrado.");
        }

        beneficiario.setNome(nome);
        beneficiario.setDescricao(descricao);

        Beneficiario atualizado = beneficiarioRepository.save(beneficiario);

        if (novaFoto != null && !novaFoto.isEmpty()) {
            String url = fileStorage.store(novaFoto);
            Foto foto = new Foto();
            foto.setUrl(url);
            foto.setBeneficiario(atualizado);
            fotoRepo.save(foto);
        }

        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirBeneficiario(
            @PathVariable Long id,
            @RequestHeader("usuarioId") Long usuarioId
    ) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null || usuario.getPermissao() == null || !usuario.getPermissao().isPodeExcluir()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Você não tem permissão para excluir beneficiários.");
        }

        if (!beneficiarioRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Beneficiário não encontrado.");
        }

        beneficiarioRepository.deleteById(id);
        return ResponseEntity.ok("Beneficiário excluído com sucesso.");
    }

    @PostMapping("/{id}/fotos")
    public ResponseEntity<?> uploadFoto(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (file.getSize() > 5_000_000)
            return ResponseEntity.badRequest().body("Tamanho máximo: 5MB");

        String ct = file.getContentType();
        if (ct == null || !(ct.contains("jpeg") || ct.contains("png")))
            return ResponseEntity.badRequest().body("Formato inválido");

        Beneficiario b = service.buscar(id);
        String url = fileStorage.store(file);

        Foto foto = new Foto();
        foto.setUrl(url);
        foto.setBeneficiario(b);
        fotoRepo.save(foto);

        return ResponseEntity.ok(foto);
    }

    @DeleteMapping("/fotos/{fotoId}")
    public ResponseEntity<?> removerFoto(@PathVariable Long fotoId) throws IOException {
        Foto f = fotoRepo.findById(fotoId).orElseThrow();
        fileStorage.delete(f.getUrl());
        fotoRepo.delete(f);
        return ResponseEntity.noContent().build();
    }
}