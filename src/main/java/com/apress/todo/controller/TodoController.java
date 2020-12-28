package com.apress.todo.controller;

import com.apress.todo.domain.ToDoBuilder;
import com.apress.todo.domain.Todo;
import com.apress.todo.repository.CommonRepository;
import com.apress.todo.validation.TodoValidationError;
import com.apress.todo.validation.TodoValidationErrorBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class TodoController {
    private final CommonRepository<Todo> repository;

    @Autowired
    public TodoController(CommonRepository<Todo> repository) {
        this.repository = repository;
    }

    @GetMapping("/todo")
    public ResponseEntity<Iterable<Todo>> getAllTodos(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/todo/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id){
        return ResponseEntity.ok(repository.findById(id));
    }

    @PatchMapping("/todo/{id}")
    public ResponseEntity<Todo> setCompleted(@PathVariable String id){
        Todo todo = repository.findById(id);
        todo.setCompleted(true);
        repository.save(todo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().buildAndExpand(todo.getId()).toUri();
        return ResponseEntity.ok().header("location",location.toString()).build();
    }

    @RequestMapping(value = "/todo", method = {RequestMethod.POST,RequestMethod.PUT})
    public ResponseEntity<?> createToDo(@Valid @RequestBody Todo todo, Errors errors){
        if(errors.hasErrors())
        {
            return ResponseEntity.badRequest().body(TodoValidationErrorBuilder.fromBindingError(errors));
        }
        Todo result = repository.save(todo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Todo> deleteToDo(@PathVariable String id){
        repository.delete(ToDoBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/todo")
    public ResponseEntity<Todo> deleteToDo(Todo todo){
        repository.delete(todo);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public TodoValidationError handleException(Exception exception){
        return new TodoValidationError(exception.getMessage());
    }

}
