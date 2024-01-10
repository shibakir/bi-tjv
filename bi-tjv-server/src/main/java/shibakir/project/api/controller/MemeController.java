package shibakir.project.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import shibakir.project.api.converter.MemeConverter;
import shibakir.project.api.dto.MemeDTO;
import shibakir.project.api.dto.UserDTO;
import shibakir.project.api.exceptions.EntityStateException;
import shibakir.project.business.MemeService;

import java.util.List;

@RestController
@AllArgsConstructor
public class MemeController {

    private final MemeService memeService;

    @PostMapping("/memes")
    MemeDTO create(@RequestBody MemeDTO memeDTO) {
        try {
            memeDTO = MemeConverter.fromModel(memeService.create(MemeConverter.toModel(memeDTO)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Meme already exists");
        }
        return memeDTO;
    }

    @GetMapping("/memes")
    List<MemeDTO> getAll() {
        return MemeConverter.fromModels(memeService.readAll());
    }

    @GetMapping("/memes/{id}")
    MemeDTO getOne(@PathVariable String id) {
        return MemeConverter.fromModel(memeService.readById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Meme not found")
        ));
    }

    @PutMapping("/memes/{id}")
    MemeDTO update(@PathVariable String id, @RequestBody MemeDTO memeDTO) {
        if (!memeDTO.getName().equals(id))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Meme ids do not match");
        try {
            memeDTO = MemeConverter.fromModel(memeService.update(MemeConverter.toModel(memeDTO)));
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meme not found");
        }
        return memeDTO;
    }

    @DeleteMapping("/memes/{id}")
    void delete(@PathVariable String id) {
        if (memeService.readById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meme to delete not found");
        }
        memeService.deleteById(id);
    }

    @PostMapping("/memes/{id}/users")
    public void addUserToMeme(@PathVariable String id, @RequestBody String userId) {
        try {
            memeService.addUserToMeme(id, userId);
        } catch (EntityStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error adding user to meme");
        }
    }

    @GetMapping("/memes/{id}/userCount")
    public ResponseEntity<Integer> getMemeUserCount(@PathVariable String id) {
        int userCount = memeService.getUserCountForMeme(id);
        return ResponseEntity.ok(userCount);
    }

    @GetMapping("/memes/{id}/users")
    public List<UserDTO> getUsersOfMeme(@PathVariable String id) {
        try {
            return memeService.getUsersForMeme(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error users by meme");
        }
    }
}
