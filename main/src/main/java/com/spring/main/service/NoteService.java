package com.spring.main.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.main.enitity.Note;
import com.spring.main.enitity.User;
import com.spring.main.repository.NoteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    //create note
    public Note createNote(Note note, User user) {
            Note newNote = Note.builder()
            .title(note.getTitle())
            .description(note.getDescription())
            .priority(note.getPriority())
            .user(user)
            .build();
        return noteRepository.save(newNote);
    }

    //retrieve note by user
    public List<Note> findAllNoteByUser(User user) {
        List<Note> notes = noteRepository.findAllNoteByUser(user);
        return notes;
    }

    //delete note
    public String deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new RuntimeException("Note not found with id: " + noteId));
        noteRepository.delete(note);
        return "Delete Successfully";
    }

    //update note
    public Note updateNote(Long noteId, Note note) {
        Note noteSearch = noteRepository.findById(noteId)
            .orElseThrow(() -> new RuntimeException("Note not found with id: " + noteId));

        noteSearch.setTitle(note.getTitle());
        noteSearch.setDescription(note.getDescription());
        noteSearch.setPriority(note.getPriority());
        noteSearch.setUpdatedAt(new Date());
        return noteRepository.save(noteSearch);
    }

    public List<Note> findNoteByUserInDateDescOrder(Long userId) {
        return noteRepository.findAllNoteByUserIdOrderedByDateDesc(userId);
    }

    public List<Note> findNoteByUserInPriorityDescOrder(Long userId) {
        return noteRepository.findAllNoteByUserIdOrderedByPriorityDesc(userId);
    }

    public List<Note> searchByKeyword(String keyword, Long userId) {
        return noteRepository.searchByKeyword(keyword, userId);    
    }



}
