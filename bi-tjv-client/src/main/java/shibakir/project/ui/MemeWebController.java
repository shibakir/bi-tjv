package shibakir.project.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shibakir.project.data.MemeClient;
import shibakir.project.data.UserClient;
import shibakir.project.model.MemeDTO;
import shibakir.project.model.MemeWebModel;
import shibakir.project.model.UserWebModel;

import java.util.List;

@Controller
@RequestMapping("/memes")
public class MemeWebController {

    private final MemeClient memeClient;
    private final UserClient userClient;

    public MemeWebController(MemeClient memeClient, UserClient userClient) {
        this.memeClient = memeClient;
        this.userClient = userClient;
    }

    @GetMapping
    public String allMemes(Model model) {
        Flux<MemeWebModel> memesFlux = memeClient.readAll();
        // блокирующий вызов для получения списка пользователей
        List<MemeWebModel> memesList = memesFlux.collectList().block();

        model.addAttribute("memes", memesList);
        return "memes";
    }

    @GetMapping("/{name}")
    public String viewMeme(@PathVariable String name, Model model) {
        model.addAttribute("memeDTO", memeClient.readById(name).block());
        model.addAttribute("userCount", memeClient.getUserCountForMeme(name).block());
        model.addAttribute("users", userClient.getUsersNotLinkedToMeme(name).collectList().block());
        return "memeView";
    }

    @GetMapping("/add")
    public String addMeme(Model model) {
        model.addAttribute("memeWebModel", new MemeWebModel());
        return "memeAdd";
    }

    @PostMapping("/add")
    public Mono<String> addMemeSubmit(@ModelAttribute MemeWebModel memeWebModel, Model model) {
        return memeClient.create(memeWebModel)
                .flatMap(createdMeme -> Mono.just("redirect:/memes"))
                .onErrorResume(WebClientResponseException.Conflict.class, e -> {
                    model.addAttribute("error", "Meme with this name already exists");
                    model.addAttribute("memeWebModel", memeWebModel);  // чтобы сохранить данные формы
                    return Mono.just("memeAdd");
                });
    }


    @GetMapping("/edit")
    public Mono<String> editMeme(@RequestParam String name, Model model) {
        return memeClient.readById(name)
                .flatMap(memeDTO -> {
                    model.addAttribute("memeDTO", memeDTO);
                    return Mono.just("memeEdit");
                })
                .onErrorResume(throwable -> {
                    model.addAttribute("error", "Meme not found");
                    return Mono.just("errorPage");
                });
    }

    @PostMapping("/edit")
    public Mono<String> editMemeSubmit(@ModelAttribute MemeDTO memeDTO, Model model) {
        return memeClient.update(memeDTO)
                .flatMap(updatedMeme -> {
                    model.addAttribute("memeDTO", updatedMeme);
                    return Mono.just("redirect:/memes");
                });
    }

    @GetMapping("/delete")
    public Mono<String> deleteMeme(@RequestParam String name) {
        return memeClient.delete(name)
                .thenReturn("redirect:/memes")
                .onErrorResume(throwable -> {
                    return Mono.just("errorPage");
                });
    }

    @PostMapping("/{name}/users")
    public Mono<String> addUserToMeme(@PathVariable String name, @RequestParam String username) {
        return memeClient.addUserToMeme(name, username).thenReturn("redirect:/memes");
    }

    @GetMapping("/{name}/users")
    public Mono<String> viewMemeUsers(@PathVariable String name, Model model) {
        return memeClient.getUsersByMemeName(name)
                .collectList()
                .doOnSuccess(users -> model.addAttribute("users", users))
                .thenReturn("likes");
    }
}
