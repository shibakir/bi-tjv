package shibakir.project.model;

public class UserWebModel extends UserDTO {

    public UserWebModel() {
    }

    public UserWebModel(UserDTO userDTO) {
        super(userDTO.getUsername(), userDTO.getPassword());
    }

}
