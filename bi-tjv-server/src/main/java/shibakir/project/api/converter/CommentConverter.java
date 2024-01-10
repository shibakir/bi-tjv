package shibakir.project.api.converter;

import shibakir.project.api.dto.CommentDTO;
import shibakir.project.api.dto.MemeDTO;
import shibakir.project.business.CommentService;
import shibakir.project.business.MemeService;
import shibakir.project.business.UserService;
import shibakir.project.domain.CommentEntity;
import shibakir.project.domain.MemeEntity;
import shibakir.project.domain.UserEntity;

import java.util.ArrayList;
import java.util.List;

public class CommentConverter {

    static UserService userService;
    static MemeService memeService;

    public static CommentEntity toModel(CommentDTO commentDTO) {
        return new CommentEntity(commentDTO.getId(), commentDTO.getText(),
                memeService.readById(commentDTO.getMeme()).orElseThrow(),
                userService.readById(commentDTO.getUser()).orElseThrow());
    }

    public static CommentDTO fromModel(CommentEntity commentEntity) {
        return new CommentDTO(commentEntity.getId(), commentEntity.getText(),
                commentEntity.getMeme().getName(),
                commentEntity.getUser().getUsername());
    }

    public static List<CommentEntity> toModels(List<CommentDTO> commentDTOS) {
        return commentDTOS.stream().map(CommentConverter::toModel).toList();
    }

    public static List<CommentDTO> fromModels(List<CommentEntity> commentEntities) {
        return commentEntities.stream().map(CommentConverter::fromModel).toList();
    }
}
