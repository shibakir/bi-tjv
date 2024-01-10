package shibakir.project.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import shibakir.project.model.MemeDTO;
import shibakir.project.model.MemeWebModel;
import shibakir.project.model.UserDTO;
import shibakir.project.model.UserWebModel;

@Component
public class MemeClient {

    private static final String ONE_URI = "/{id}"   ;
    private final WebClient memeWebClient;

    public MemeClient(@Value("${app_backend_url}") String backendUrl) {
        memeWebClient = WebClient.create(backendUrl + "/memes");
    }

    public Mono<MemeWebModel> create(MemeDTO memeDTO) {
        return memeWebClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(memeDTO)
                .retrieve()
                .bodyToMono(MemeWebModel.class);
    }

    public Mono<MemeWebModel> update(MemeDTO memeDTO) {
        return memeWebClient.put()
                .uri(ONE_URI, memeDTO.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(memeDTO)
                .retrieve()
                .bodyToMono(MemeWebModel.class);
    }

    public Mono<MemeWebModel> readById(String name) {
        return memeWebClient.get()
                .uri(ONE_URI, name).retrieve()
                .bodyToMono(MemeWebModel.class);
    }

    public Flux<MemeWebModel> readAll() {
        return memeWebClient.get()
                .retrieve()
                .bodyToFlux(MemeWebModel.class);
    }

    public Mono<Void> delete(String name) {
        return memeWebClient.delete()
                .uri(ONE_URI, name)
                .retrieve()
                .bodyToMono(Void.TYPE);
    }

    public Mono<MemeWebModel> addUserToMeme(String memeName, String username) {
        return memeWebClient.post()
                .uri(ONE_URI + "/users", memeName)
                .bodyValue(username)
                .retrieve()
                .bodyToMono(MemeWebModel.class);
    }

    public Mono<Integer> getUserCountForMeme(String memeName) {
        return memeWebClient.get()
                .uri("/{id}/userCount", memeName)
                .retrieve()
                .bodyToMono(Integer.class);
    }

    public Flux<UserWebModel> getUsersByMemeName(String memeName) {
        return memeWebClient.get()
                .uri("/" + memeName + "/users")
                .retrieve()
                .bodyToFlux(UserWebModel.class);
    }
}
