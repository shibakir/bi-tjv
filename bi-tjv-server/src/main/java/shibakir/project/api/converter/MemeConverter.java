package shibakir.project.api.converter;

import shibakir.project.api.dto.MemeDTO;
import shibakir.project.business.CommentService;
import shibakir.project.business.UserService;
import shibakir.project.domain.CommentEntity;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.stream.Collectors;

public class MemeConverter {

    static UserService userService;
    static CommentService commentService;

    public static MemeEntity toModel(MemeDTO memeDTO) {
        List<UserEntity> users = new ArrayList<>();
        List<CommentEntity> comments = new ArrayList<>();
        if(memeDTO.getUsers() != null) {
            for (String id : memeDTO.getUsers()) {
                users.add(userService.readById(id).orElseThrow());
            }
        }
        if(memeDTO.getComments() != null) {
            for (Long id : memeDTO.getComments()) {
                comments.add(commentService.readById(id).orElseThrow());
            }
        }
        return new MemeEntity(memeDTO.getName(), memeDTO.getPath_name(), users, comments);
    }

    public static MemeDTO fromModel(MemeEntity memeEntity) {
        List<String> users = new ArrayList<>();
        List<Long> comments = new ArrayList<>();
        for(UserEntity user : memeEntity.getUsers()) {
            users.add(user.getUsername());
        }
        for(CommentEntity comment : memeEntity.getComments()) {
            comments.add(comment.getId());
        }
        return new MemeDTO(memeEntity.getName(), memeEntity.getPath_name(), users, comments);
    }

    public static List<MemeEntity> toModels(List<MemeDTO> memeDTOS) {
        return memeDTOS.stream()
                .map(MemeConverter::toModel)
                .collect(Collectors.toList());
    }

    public static List<MemeDTO> fromModels(List<MemeEntity> memeEntities) {
        List<MemeDTO> memeDTOS = new ArrayList<>();
        memeEntities.forEach((u) -> memeDTOS.add(fromModel(u)));
        return memeDTOS;
    }
}
