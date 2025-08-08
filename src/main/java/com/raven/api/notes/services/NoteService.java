package com.raven.api.notes.services;

import com.raven.api.notes.dto.CreateNoteRequest;
import com.raven.api.notes.dto.UpdateNoteRequest;
import com.raven.api.notes.dto.UpdateReminderTimeRequest;
import com.raven.api.notes.entity.Note;
import com.raven.api.notes.repositories.NoteRepository;
import com.raven.api.users.entity.User;
import com.raven.api.users.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;

    private Note save(Note note){
        return noteRepository.save(note);
    }

    public Note create(CreateNoteRequest request){
        if(request == null) {
            throw new IllegalArgumentException();
        }

        Note note = Note.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .creationTime(request.getCreationTime())
                .reminderTime(request.getReminderTime())
                .build();
        note.setOwner(userService.getCurrentUser());

        return save(noteRepository.save(note));
    }


    public Note updateNote(Long noteId, UpdateNoteRequest request) {
        if (noteId == null || noteId < 0) {
            throw new RuntimeException("Invalid note id");
        }

        if (request == null) {
            throw new RuntimeException("Invalid request");
        }

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));

        if (!Objects.equals(request.getTitle(), note.getTitle())) {
            note.setTitle(request.getTitle());
        }

        if (!Objects.equals(request.getContent(), note.getContent())) {
            note.setContent(request.getContent());
        }

        if (!Objects.equals(request.getReminderTime(), note.getReminderTime())) {
            note.setReminderTime(request.getReminderTime());
        }

        note.setCreationTime(LocalDateTime.now());
        return save(noteRepository.save(note));
    }

    public Note updateReminderTime(Long noteId, UpdateReminderTimeRequest request) {
        if (noteId == null || noteId < 0) {
            throw new RuntimeException("Invalid note id");
        }

        if (request == null) {
            throw new RuntimeException("Invalid request");
        }

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));

        if (!Objects.equals(note.getReminderTime(), request.getReminderTime())) {
            note.setReminderTime(request.getReminderTime());
        }

        return save(note);
    }

    public List<Note> getAllByCurrentUser() {
        User user = userService.getCurrentUser();
        return noteRepository.findAllByOwner_IdOrderByCreationTimeDesc(user.getId());
    }

    public void deleteNoteById(Long noteId) {
        if(noteId == null || noteId < 0) {
            throw new RuntimeException("Invalid note id");
        }

        if(!noteRepository.existsById(noteId)) {
            throw new RuntimeException("Note with id " + noteId + " does not exist");
        }

        noteRepository.deleteById(noteId);
    }
}
