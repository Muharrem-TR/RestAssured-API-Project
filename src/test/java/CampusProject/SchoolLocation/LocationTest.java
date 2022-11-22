package CampusProject.SchoolLocation;

import CampusProject.SchoolLocation.Model.Location;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class LocationTest {

    Cookies cookies;

    @Test
    public void Login() {
        baseURI = "https://demo.mersys.io/";

        Map<String, String> account = new HashMap<>();
        account.put("username", "richfield.edu");
        account.put("password", "Richfield2020!");
        account.put("rememberMe", "true");


        cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(account)

                        .when()
                        .post("auth/login")


                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }

    public String getRandomCode() {
        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }

    public String getRandomCapacity() {
        return RandomStringUtils.randomNumeric(4);
    }

    String locationName;
    String locationCode;
    String locationID;
    String locationCapacity;
    String type="CLASS";

    public Map<String,String> school(){
        Map<String, String> school = new HashMap<>();
        school.put("id","5fe07e4fb064ca29931236a5");

        return school;
    }


    @Test(dependsOnMethods = "Login")
    public void createLocation() {

        locationName = getRandomName();
        locationCode = getRandomCode();
        locationCapacity = getRandomCapacity();

        Location location = new Location();
        location.setName(locationName);
        location.setCode(locationCode);
        location.setCapacity(locationCapacity);
        location.setSchool(school());
        location.setType(type);


        locationID =
                given()

                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(location)
                        .when()
                        .post("school-service/api/location")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")
        ;
    }

    @Test(dependsOnMethods = "createLocation")
    public void createLocationNegative() {

        Location location = new Location();
        location.setName(locationName);
        location.setCode(locationCode);
        location.setCapacity(locationCapacity);
        location.setSchool(school());
        location.setType(type);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(location)

                .when()
                .post("school-service/api/location")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }

    @Test(dependsOnMethods = "createLocationNegative")
    public void updateLocation() {

        locationName = getRandomName();
        Location location = new Location();
        location.setId(locationID);
        location.setName(locationName);
        location.setCode(locationCode);
        location.setCapacity(locationCapacity);
        location.setSchool(school());
        location.setType(type);

        given()

                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(location)

                .when()
                .put("school-service/api/location")

                .then()
                .statusCode(200)
                .body("name", equalTo(locationName))
        ;
    }

    @Test(dependsOnMethods = "updateLocation")
    public void deleteLocation() {

        given()
                .cookies(cookies)
                .pathParam("locationID", locationID)


                .when()
                .delete("school-service/api/location/{locationID}")

                .then()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteLocation")
    public void deleteLocationNegative() {

        given()
                .cookies(cookies)
                .pathParam("locationID", locationID)

                .when()
                .delete("school-service/api/location/{locationID}")

                .then()
                .statusCode(400)
        ;
    }

    @Test(dependsOnMethods = "deleteLocationNegative")
    public void updateLocationNegative() {

        locationName = getRandomName();
        Location location = new Location();
        location.setId(locationID);
        location.setName(locationName);
        location.setCode(locationCode);
        location.setCapacity(locationCapacity);
        location.setSchool(school());
        location.setType(type);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(location)

                .when()
                .put("school-service/api/location")

                .then()
                .statusCode(400)
        ;
    }
}
