package com.spring.main.controller;

import org.springframework.web.bind.annotation.RestController;

import com.spring.main.enitity.Note;
import com.spring.main.enitity.User;
import com.spring.main.repository.UserRepository;
import com.spring.main.service.NoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/notes") 
@RequiredArgsConstructor
public class NoteController {
    @Autowired
    private NoteService noteService;

    @Autowired 
    private UserRepository userRepository;

    //create
    @PostMapping("/{userId}")
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note notes,@PathVariable Long userId){
        User user = userRepository.findUserById(userId);
        Note note =  noteService.createNote(notes, user);
        return new ResponseEntity<>(note,HttpStatus.CREATED);
    }
    
    //retrieve all notes by user
    @GetMapping("/{userId}/notes")
    public ResponseEntity<List<Note>> retrieveAllNotes(@PathVariable Long userId) {
        User user = userRepository.findUserById(userId);
        List<Note> notes = noteService.findAllNoteByUser(user);
        return new ResponseEntity<>(notes,HttpStatus.OK);
    }

    @DeleteMapping("/notes/{noteId}")
    public ResponseEntity<String> deleteNote(@PathVariable Long noteId) {
        noteService.deleteNote(noteId);
        return new ResponseEntity<>("Deleted Successfully", HttpStatus.OK);
    }

    @PutMapping("notes/{noteId}")
    public ResponseEntity<Note> updateNote(@PathVariable Long noteId, @RequestBody Note note) {
        Note updatedNote = noteService.updateNote(noteId, note);
        
        return new ResponseEntity<>(updatedNote, HttpStatus.OK);
    }

    //retrieve all notes by user in date descending order
    @GetMapping("/{userId}/notesDescInDate")
    public ResponseEntity<List<Note>> retrieveAllNotesInDateDescOrder(@PathVariable Long userId) {
        // User user = userRepository.findUserById(userId);
        // List<Note> notes = noteService.findNoteByUserInDateDescOrder(user.getId());
        List<Note> notes = noteService.findNoteByUserInDateDescOrder(userId);

        return new ResponseEntity<>(notes,HttpStatus.OK);
    }

    //retrieve all notes by user in priority descending order
    @GetMapping("/{userId}/notesDescInPriority")
    public ResponseEntity<List<Note>> retrieveAllNotesInPriorityDescOrder(@PathVariable Long userId) {
        // User user = userRepository.findUserById(userId);
        List<Note> notes = noteService.findNoteByUserInPriorityDescOrder(userId);
        return new ResponseEntity<>(notes,HttpStatus.OK);
    }
    

    @GetMapping("/{userId}/searchNotes")
    public ResponseEntity<List<Note>> searchNotesByTitleOrDescription(@RequestParam String keyword, @PathVariable Long userId) {
        // User user = userRepository.findUserById(userId);
        List<Note> notes = noteService.searchByKeyword(keyword, userId);
        return new ResponseEntity<>(notes,HttpStatus.OK);
    }
}
