package com.raven.api.notes.controllers;

import com.raven.api.notes.dto.CreateNoteRequest;
import com.raven.api.notes.dto.NoteResponse;
import com.raven.api.notes.dto.UpdateNoteRequest;
import com.raven.api.notes.dto.UpdateReminderTimeRequest;
import com.raven.api.notes.entity.Note;
import com.raven.api.notes.mapper.NoteMapper;
import com.raven.api.notes.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/notes")
public class NoteController {

    private final NoteService noteService;
    private final NoteMapper noteMapper;

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getNotes() {
        List<Note> notes = noteService.getAllByCurrentUser();
        notes.forEach(note -> System.out.println(note.getCreationTime()));
        return ResponseEntity.status(HttpStatus.OK).body(noteMapper.toResponse(notes));
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody CreateNoteRequest createNoteRequest) {
        Note note = noteService.create(createNoteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteMapper.toResponse(note));
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long noteId,
                                                   @RequestBody UpdateNoteRequest updateNoteRequest) {
        Note note = noteService.updateNote(noteId, updateNoteRequest);
        return ResponseEntity.status(HttpStatus.OK).body(noteMapper.toResponse(note));
    }

    @PutMapping("/{noteId}/reminderTime")
    public ResponseEntity<NoteResponse> updateReminderTime(@PathVariable Long noteId,
                                                           @RequestBody UpdateReminderTimeRequest request) {
        Note note = noteService.updateReminderTime(noteId, request);
        return ResponseEntity.status(HttpStatus.OK).body(noteMapper.toResponse(note));
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<NoteResponse> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNoteById(noteId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
