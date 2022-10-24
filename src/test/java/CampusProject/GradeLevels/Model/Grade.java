package CampusProject.GradeLevels.Model;

public class Grade {
    private String name;
    private String shortName;
    private String order;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", order='" + order + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
