package com.raven.api.notes.services;

import com.raven.api.notes.dto.CreateNoteRequest;
import com.raven.api.notes.entity.Note;
import com.raven.api.notes.repositories.NoteRepository;
import com.raven.api.users.entity.User;
import com.raven.api.users.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Note> getAllByCurrentUser() {
        User user = userService.getCurrentUser();
        return noteRepository.findAllByOwner_Id(user.getId());
    }
}
