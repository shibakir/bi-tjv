package shibakir.project.model;

public class MemeWebModel extends MemeDTO {

    public MemeWebModel() {
    }

    public MemeWebModel(MemeDTO memeDTO) {
        super(memeDTO.getName(), memeDTO.getPath_name());
    }
}
