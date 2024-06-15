package andrehsvictor.inscribe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import andrehsvictor.inscribe.entity.Note;
import andrehsvictor.inscribe.entity.User;
import andrehsvictor.inscribe.exception.InscribeException;
import andrehsvictor.inscribe.payload.Payload;
import andrehsvictor.inscribe.payload.request.NoteRequest;
import andrehsvictor.inscribe.payload.response.NoteResponse;
import andrehsvictor.inscribe.repository.NoteRepository;
import andrehsvictor.inscribe.repository.UserRepository;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    public Payload<List<NoteResponse>> findAllByUser(JwtAuthenticationToken jwt, Pageable pageable) {
        User user = findUserByEmail(jwt);
        Page<Note> notes = noteRepository.findAllByUser(user, pageable);
        List<NoteResponse> noteResponses = toNoteResponses(notes);
        return buildPayload(noteResponses, "Notes retrieved successfully", notes);
    }

    public Payload<NoteResponse> findByPublicIdAndUser(JwtAuthenticationToken jwt, String publicId) {
        User user = findUserByEmail(jwt);
        Note note = findByPublicIdAndUser(publicId, user);
        return buildPayload(note.toResponse(), "Note retrieved successfully");
    }

    public Payload<NoteResponse> create(JwtAuthenticationToken jwt, NoteRequest noteRequest) {
        User user = findUserByEmail(jwt);
        Note note = noteRequest.toEntity(user);
        note = noteRepository.save(note);
        return buildPayload(note.toResponse(), "Note created successfully");
    }

    public Payload<NoteResponse> update(JwtAuthenticationToken jwt, String publicId, NoteRequest noteRequest) {
        User user = findUserByEmail(jwt);
        Note note = findByPublicIdAndUser(publicId, user);
        note.update(noteRequest.getTitle(), noteRequest.getContent());
        note = noteRepository.save(note);
        return buildPayload(note.toResponse(), "Note updated successfully");
    }

    public void delete(JwtAuthenticationToken jwt, String publicId) {
        User user = findUserByEmail(jwt);
        Note note = findByPublicIdAndUser(publicId, user);
        note.delete();
        noteRepository.save(note);
    }

    private <T> Payload<T> buildPayload(T data, String message) {
        return Payload.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public <T> Payload<T> buildPayload(T data, String message, Page<?> page) {
        return Payload.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .sort(page.getSort().toString())
                .build();
    }

    private Note findByPublicIdAndUser(String publicId, User user) {
        return noteRepository.findByPublicIdAndUser(publicId, user)
                .orElseThrow(() -> new InscribeException("Note not found", 404));
    }

    private List<NoteResponse> toNoteResponses(Page<Note> notes) {
        List<NoteResponse> noteResponses = new ArrayList<>();
        for (Note note : notes) {
            noteResponses.add(note.toResponse());
        }
        return noteResponses;
    }

    private User findUserByEmail(JwtAuthenticationToken jwt) {
        return userRepository.findByEmail(jwt.getName())
                .orElseThrow(() -> new InscribeException("User not found", 404));
    }
}
