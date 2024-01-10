package shibakir.project.api.converter;

import shibakir.project.api.dto.CommentDTO;
import shibakir.project.api.dto.UserDTO;
import shibakir.project.business.CommentService;
import shibakir.project.business.MemeService;
import shibakir.project.domain.CommentEntity;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserConverter {

    static MemeService memeService;
    static CommentService commentService;

    public static UserEntity toModel(UserDTO userDTO) {
        List<MemeEntity> memes = new ArrayList<>();
        List<CommentEntity> comments = new ArrayList<>();
        if(userDTO.getMemes() != null) {
            for (String id : userDTO.getMemes()) {
                memes.add(memeService.readById(id).orElseThrow());
            }
        }
        if(userDTO.getComments() != null) {
            for (Long id : userDTO.getComments()) {
                comments.add(commentService.readById(id).orElseThrow());
            }
        }
        return new UserEntity(userDTO.getUsername(), userDTO.getPassword(), memes, comments);
    }

    public static UserDTO fromModel(UserEntity userEntity) {

        List<String> memes = new ArrayList<>();
        List<Long> comments = new ArrayList<>();
        for(MemeEntity meme : userEntity.getMemes()) {
            memes.add(meme.getName());
        }
        for(CommentEntity comment : userEntity.getComments()) {
            comments.add(comment.getId());
        }
        return new UserDTO(userEntity.getUsername(), userEntity.getPassword(), memes, comments);
    }

    public static List<UserEntity> toModels(List<UserDTO> userDTOS) {
        return userDTOS.stream()
                .map(UserConverter::toModel)
                .collect(Collectors.toList());
    }

    public static List<UserDTO> fromModels(List<UserEntity> userEntities) {
        List<UserDTO> userDTOS = new ArrayList<>();
        userEntities.forEach((u) -> userDTOS.add(fromModel(u)));
        return userDTOS;
    }
}
