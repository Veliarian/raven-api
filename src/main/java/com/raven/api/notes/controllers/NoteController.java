package com.raven.api.notes.controllers;

import com.raven.api.notes.dto.CreateNoteRequest;
import com.raven.api.notes.dto.NoteResponse;
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
        return ResponseEntity.status(HttpStatus.OK).body(noteMapper.toResponse(notes));
    }

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(@RequestBody CreateNoteRequest createNoteRequest){
        System.out.println(createNoteRequest.toString());
        Note note = noteService.create(createNoteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(noteMapper.toResponse(note));
    }
}
