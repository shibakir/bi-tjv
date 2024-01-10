package shibakir.project.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shibakir.project.data.UserClient;
import shibakir.project.model.MemeWebModel;
import shibakir.project.model.UserDTO;
import shibakir.project.model.UserWebModel;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserWebController {

    private final UserClient userClient;

    public UserWebController(UserClient userClient) {
        this.userClient = userClient;
    }

    @GetMapping
    public String allUsers(Model model) {
        Flux<UserWebModel> usersFlux = userClient.readAll();
        // блокирующий вызов для получения списка пользователей
        List<UserWebModel> userList = usersFlux.collectList().block();

        model.addAttribute("users", userList);
        return "users";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable String id, Model model) {
        model.addAttribute("memes", userClient.getMemesLinkedToUser(id).collectList().block());
        model.addAttribute("user", userClient.readById(id).block());
        return "user";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("userWebModel", new UserWebModel());
        return "userAdd";
    }

    @PostMapping("/add")
    public Mono<String> addUserSubmit(@ModelAttribute UserWebModel userWebModel, Model model) {
        return userClient.create(userWebModel)
                .flatMap(createdUser -> {
                    model.addAttribute("userWebModel", createdUser);
                    return Mono.just("redirect:/users");
                })
                .onErrorResume(WebClientResponseException.Conflict.class, e -> {
                    model.addAttribute("error", "User with this username already exists");
                    model.addAttribute("userWebModel", userWebModel);
                    return Mono.just("userAdd"); // Имя представления для ошибки
                });
    }

    @GetMapping("/edit")
    public Mono<String> editUser(@RequestParam String username, Model model) {
        return userClient.readById(username)
                .flatMap(userDTO -> {
                    model.addAttribute("userDTO", userDTO);
                    return Mono.just("userEdit");
                })
                .onErrorResume(throwable -> {
                    model.addAttribute("error", "User not found");
                    return Mono.just("errorPage"); // Имя представления для ошибки
                });
    }

    @PostMapping("/edit")
    public Mono<String> editUserSubmit(@ModelAttribute UserDTO userDTO, Model model) {
        return userClient.update(userDTO)
                .flatMap(updatedUser -> {
                    model.addAttribute("userDTO", updatedUser);
                    return Mono.just("redirect:/users");
                });
    }

    @GetMapping("/delete")
    public Mono<String> deleteUser(@RequestParam String username) {
        return userClient.delete(username)
                .thenReturn("redirect:/users")
                .onErrorResume(throwable -> {
                    return Mono.just("errorPage");
                });
    }

    @GetMapping("/{memeName}/notLiked")
    public Mono<String> showUsersNotLinkedToMeme(@PathVariable String memeName, Model model) {
        Flux<UserWebModel> users = userClient.getUsersNotLinkedToMeme(memeName);

        model.addAttribute("users", users.collectList().block());
        return Mono.just("someTemplate");
    }
}
