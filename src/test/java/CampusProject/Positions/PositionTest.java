package CampusProject.Positions;

import CampusProject.Positions.Model.Position;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PositionTest {

    Cookies cookies;

    @BeforeClass
    public void loginCampus() {
        baseURI = "https://demo.mersys.io/";

        Map<String, String> credential = new HashMap<>();
        credential.put("username", "richfield.edu");
        credential.put("password", "Richfield2020!");
        credential.put("rememberMe", "true");

        cookies =
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)

                        .when()
                        .post("auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()
        ;
    }

    String positionId;
    String positionName;
    String positionShortName;
    String tenantId;
    boolean active;


    @Test
    public void createPosition() {

        positionName=getRandomName();
        positionShortName=getRandomShortName();

        Position position =new Position();
        position.setName(positionName);
        position.setShortName(positionShortName);
        position.setTenantId("5fe0786230cc4d59295712cf");
        position.setActive(true);

        positionId=
        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .log().body()
                .body(position)

                .when()
                .post("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(201)
                .extract().jsonPath().getString("id")
        ;

    }

    public String getRandomName() {
        return RandomStringUtils.randomAlphabetic(30).toLowerCase();
    }
    public String getRandomShortName() {
        return RandomStringUtils.randomAlphabetic(3).toLowerCase();
    }

    @Test(dependsOnMethods = "createPosition")
    public void createPositionNegative() {

        Position position =new Position();
        position.setName(positionName);
        position.setShortName(positionShortName);
        position.setTenantId("5fe0786230cc4d59295712cf");
        position.setActive(true);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(position)

                .when()
                .post("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("The Position with Name \""+positionName+ "\" already exists."))
        ;

    }

    @Test(dependsOnMethods = "createPosition")
    public void updatePosition() {

        positionName=getRandomName();

        Position position =new Position();
        position.setId(positionId);
        position.setName(positionName);
        position.setShortName(positionShortName);
        position.setTenantId("5fe0786230cc4d59295712cf");
        position.setActive(true);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(position)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(positionName))
        ;
    }

    @Test(dependsOnMethods = "updatePosition")
    public void deletePositionById() {

        given()
                .cookies(cookies)
                .pathParam("positionId", positionId)

                .when()
                .delete("school-service/api/employee-position/{positionId}")

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deletePositionById")
    public void deletePositionByIdNegative() {

        given()
                .cookies(cookies)
                .pathParam("positionId", positionId)
                .log().uri()

                .when()
                .delete("school-service/api/employee-position/{positionId}")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }

    @Test(dependsOnMethods = "deletePositionById")
    public void updatePositionNegative() {

        positionName=getRandomName();

        Position position =new Position();
        position.setId(positionId);
        position.setName(positionName);
        position.setShortName(positionShortName);
        position.setTenantId("5fe0786230cc4d59295712cf");
        position.setActive(true);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(position)

                .when()
                .put("school-service/api/employee-position")

                .then()
                .log().body()
                .statusCode(400)
                .body("message", equalTo("Can't find Position"))
        ;
    }

}














