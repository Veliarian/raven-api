package com.raven.api.notes.mapper;

import com.raven.api.notes.dto.NoteResponse;
import com.raven.api.notes.entity.Note;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoteMapper {
    public NoteResponse toResponse(Note note) {
        if (note == null) return null;

        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .creationTime(note.getCreationTime())
                .reminderTime(note.getReminderTime())
                .build();
    }

    public List<NoteResponse> toResponse(List<Note> notes) {
        return notes.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
