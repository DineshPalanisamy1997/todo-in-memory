package com.apress.todo.repository;

import com.apress.todo.domain.Todo;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ToDoRepository implements CommonRepository<Todo>{
    private final Map<String, Todo> todo = new HashMap<>();

    @Override
    public Todo save(Todo domain) {
        Todo result = todo.get(domain.getId());
        if(result != null)
        {
            result.setModified(LocalDateTime.now());
            result.setDescription(domain.getDescription());
            result.setCompleted(domain.isCompleted());
            domain = result;
        }
        todo.put(domain.getId(),domain);
        return todo.get(domain.getId());
    }

    @Override
    public void delete(Todo domain) {
        todo.remove(domain.getId());
    }

    @Override
    public Iterable<Todo> save(Collection<Todo> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public Todo findById(String Id) {
        return todo.get(Id);
    }

    @Override
    public Iterable<Todo> findAll() {
        return todo.entrySet().stream().sorted(comparator).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    private final Comparator<Map.Entry<String, Todo>> comparator = Comparator.comparing((Map.Entry<String, Todo> o) -> o.getValue().getCreated());
}