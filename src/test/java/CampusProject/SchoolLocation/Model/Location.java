package CampusProject.SchoolLocation.Model;

import java.util.Map;

public class Location {

    private String name;
    private String code;
    private String id;
    private String capacity;
    private Map<String,String> school;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getSchool() {
        return school;
    }

    public void setSchool(Map<String, String> school) {
        this.school = school;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Country{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", id='" + id + '\'' +
                ", capacity='" + capacity + '\'' +
                ", school=" + school +
                ", type='" + type + '\'' +
                '}';
    }
}
