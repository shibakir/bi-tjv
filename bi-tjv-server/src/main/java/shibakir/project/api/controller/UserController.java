package shibakir.project.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shibakir.project.api.converter.MemeConverter;
import shibakir.project.api.converter.UserConverter;
import shibakir.project.api.dto.MemeDTO;
import shibakir.project.api.dto.UserDTO;
import shibakir.project.api.exceptions.*;
import shibakir.project.business.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    UserDTO create(@RequestBody UserDTO userDTO) {
        try {
            userDTO = UserConverter.fromModel(userService.create(UserConverter.toModel(userDTO)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }
        return userDTO;
    }

    @GetMapping("/users")
    List<UserDTO> getAll() {
        return UserConverter.fromModels(userService.readAll());
    }

    @GetMapping("/users/{id}")
    UserDTO getOne(@PathVariable String id) {
        return UserConverter.fromModel(userService.readById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        ));
    }

    @PutMapping("/users/{id}")
    UserDTO update(@PathVariable String id, @RequestBody UserDTO userDTO) {
        if (!userDTO.getUsername().equals(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User ids do not match");
        try {
            userDTO = UserConverter.fromModel(userService.update(UserConverter.toModel(userDTO)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userDTO;
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable String id) {
        if (userService.readById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User to delete not found");
        }
        userService.deleteById(id);
    }

    @PostMapping("/users/{userId}/memes")
    public void addMemeToUser(@PathVariable String userId, @RequestBody String memeId) {
        try {
            userService.addMemeToUser(userId, memeId);
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding meme to user");
        }
    }

    @GetMapping("/users/not-linked-to-meme/{memeName}")
    public List<UserDTO> getUsersNotLinkedToMeme(@PathVariable String memeName) {
        try {
            return userService.getUsersNotLinkedToMeme(memeName);
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/users/{id}/memes")
    public List<MemeDTO> getMemesOfUser(@PathVariable String id) {
        try {
            return userService.getMemesOfUser(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error memes by user");
        }
    }
}
