package CampusProject.Documents.Model;

public class Document {

    private String id;
    private String name;
    private String description;
    private String[] attachmentStages;
    private Boolean active;
    private Boolean required;
    private Boolean useCamera;
    private String[] translateName;
    private String schoolId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAttachmentStages() {
        return attachmentStages;
    }

    public void setAttachmentStages(String[] attachmentStages) {
        this.attachmentStages = attachmentStages;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean getUseCamera() {
        return useCamera;
    }

    public void setUseCamera(Boolean useCamera) {
        this.useCamera = useCamera;
    }

    public String[] getTranslateName() {
        return translateName;
    }

    public void setTranslateName(String[] translateName) {
        this.translateName = translateName;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

//    {
//        "id": null,
//            "name": "adads",
//            "description": "sadsda",
//            "attachmentStages": [
//        "STUDENT_REGISTRATION",
//                "EXAMINATION"
//  ],
//        "active": true,
//            "required": true,
//            "useCamera": false,
//            "translateName": [],
//        "schoolId": "6343bf893ed01f0dc03a509a"
//    }

}
