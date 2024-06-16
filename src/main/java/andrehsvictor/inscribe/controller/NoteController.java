package andrehsvictor.inscribe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import andrehsvictor.inscribe.payload.Payload;
import andrehsvictor.inscribe.payload.request.NoteRequest;
import andrehsvictor.inscribe.payload.response.NoteResponse;
import andrehsvictor.inscribe.service.NoteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @GetMapping
    public ResponseEntity<Payload<List<NoteResponse>>> findAllByUser(JwtAuthenticationToken jwt, Pageable pageable) {
        return ResponseEntity.ok(noteService.findAllByUser(jwt, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payload<NoteResponse>> findByPublicIdAndUser(JwtAuthenticationToken jwt,
            @PathVariable String id) {
        return ResponseEntity.ok(noteService.findByPublicIdAndUser(jwt, id));
    }

    @PostMapping
    public ResponseEntity<Payload<NoteResponse>> create(JwtAuthenticationToken jwt,
            @RequestBody @Valid NoteRequest noteRequest) {
        return ResponseEntity.ok(noteService.create(jwt, noteRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Payload<NoteResponse>> update(JwtAuthenticationToken jwt, @PathVariable String id,
            @RequestBody @Valid NoteRequest noteRequest) {
        return ResponseEntity.ok(noteService.update(jwt, id, noteRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(JwtAuthenticationToken jwt, @PathVariable String id) {
        noteService.delete(jwt, id);
        return ResponseEntity.noContent().build();
    }
}
