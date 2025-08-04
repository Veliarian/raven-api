package com.raven.api.notes.repositories;

import com.raven.api.notes.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByOwner_IdOrderByCreationTimeDesc(Long id);
}
