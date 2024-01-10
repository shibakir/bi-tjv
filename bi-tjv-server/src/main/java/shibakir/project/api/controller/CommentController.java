package shibakir.project.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shibakir.project.api.converter.CommentConverter;
import shibakir.project.api.dto.CommentDTO;
import shibakir.project.api.exceptions.EntityStateException;
import shibakir.project.api.exceptions.HasRelationException;
import shibakir.project.business.CommentService;

import java.util.List;

@RestController
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    CommentDTO create(@RequestBody CommentDTO entityDTO) {
        try {
            entityDTO = CommentConverter.fromModel(commentService.create(CommentConverter.toModel(entityDTO)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Comment already exists");
        }
        return entityDTO;
    }

    @GetMapping("/comments")
    List<CommentDTO> getAll() {
        return CommentConverter.fromModels(commentService.readAll());
    }

    @GetMapping("/comments/{id}")
    CommentDTO getOne(@PathVariable Long id) {
        return CommentConverter.fromModel(commentService.readById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found")
        ));
    }

    @PutMapping("/comments/{id}")
    CommentDTO update(@PathVariable Long id, @RequestBody CommentDTO entityDTO) {
        if (!entityDTO.getId().equals(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment ids do not match");
        try {
            entityDTO = CommentConverter.fromModel(commentService.update(CommentConverter.toModel(entityDTO)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        return entityDTO;
    }

    @DeleteMapping("/comments/{id}")
    void delete(@PathVariable Long id) {
        if (commentService.readById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment to delete not found");
        }
        try {
            commentService.deleteById(id);
        } catch (HasRelationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Could not delete Comment");
        }
    }
}
