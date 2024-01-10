package shibakir.project.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shibakir.project.model.MemeWebModel;
import shibakir.project.model.UserDTO;
import shibakir.project.model.UserWebModel;

@Component
public class UserClient {

    private static final String ONE_URI = "/{id}"   ;
    private final WebClient userWebClient;


    public UserClient(@Value("${app_backend_url}") String backendUrl) {
        userWebClient = WebClient.create(backendUrl + "/users");
    }

    public Mono<UserWebModel> create(UserDTO userDTO) {
        return userWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(UserWebModel.class);
    }

    public Mono<UserWebModel> update(UserDTO userDTO) {
        return userWebClient.put()
                .uri(ONE_URI, userDTO.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .retrieve()
                .bodyToMono(UserWebModel.class);
    }

    public Mono<UserWebModel> readById(String username) {
        return userWebClient.get()
                .uri(ONE_URI, username).retrieve()
                .bodyToMono(UserWebModel.class);
    }

    public Flux<UserWebModel> readAll() {
        return userWebClient.get()
                .retrieve()
                .bodyToFlux(UserWebModel.class);
    }

    public Mono<Void> delete(String id) {
        return userWebClient.delete()
                .uri(ONE_URI, id)
                .retrieve()
                .bodyToMono(Void.TYPE);
    }

    public Flux<UserWebModel> getUsersNotLinkedToMeme(String memeName) {
        return userWebClient.get()
                .uri("/not-linked-to-meme/" + memeName)
                .retrieve()
                .bodyToFlux(UserWebModel.class);
    }

    public Flux<MemeWebModel> getMemesLinkedToUser(String id) {

        return userWebClient.get()
                .uri("/" + id + "/memes")
                .retrieve()
                .bodyToFlux(MemeWebModel.class);
    }
}