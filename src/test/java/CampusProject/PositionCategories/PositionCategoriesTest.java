package CampusProject.PositionCategories;

import CampusProject.PositionCategories.Model.PositionCategories;
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

public class PositionCategoriesTest {

    Cookies cookies;

    @BeforeClass
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


    String PositionCategoriesID;
    String PositionCategoriesName;

    @Test
    public void createPositionCategories(){

        PositionCategoriesName=getRandomName();

        PositionCategories positionCategories=new PositionCategories();
        positionCategories.setName(PositionCategoriesName);

        PositionCategoriesID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(positionCategories)

                        .when()
                        .post("school-service/api/position-category")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id")

        ;
    }


    @Test(dependsOnMethods = "createPositionCategories")
    public void createPositionCategoriesNegative(){

        PositionCategories positionCategories=new PositionCategories();
        positionCategories.setName(PositionCategoriesName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(positionCategories)

                .when()
                .post("school-service/api/position-category")

                .then()
                .log().body()
                .statusCode(400)
        ;
    }


    @Test(dependsOnMethods = "createPositionCategoriesNegative")
    public void updatePositionCategories(){

        PositionCategories positionCategories=new PositionCategories();
        positionCategories.setId(PositionCategoriesID);
        positionCategories.setName("|ESK26|"+PositionCategoriesName+"|TR26|");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(positionCategories)

                .when()
                .put("school-service/api/position-category")

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo("|ESK26|"+PositionCategoriesName+"|TR26|"))
        ;
    }


    @Test(dependsOnMethods = "updatePositionCategories")
    public void deletePositionCategoriesByID(){

        given()
                .cookies(cookies)
                .pathParam("PositionCategoriesID",PositionCategoriesID)

                .when()
                .delete("school-service/api/position-category/{PositionCategoriesID}")

                .then()
                .log().body()
                .statusCode(204)

        ;
    }


    @Test(dependsOnMethods = "deletePositionCategoriesByID")
    public void deletePositionCategoriesByIDNegative(){

        given()
                .cookies(cookies)
                .pathParam("PositionCategoriesID",PositionCategoriesID)

                .when()
                .delete("school-service/api/position-category/{PositionCategoriesID}")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("PositionCategory not  found"))
        ;
    }


    @Test(dependsOnMethods = "deletePositionCategoriesByIDNegative")
    public void updatePositionCategoriesNegative(){

        PositionCategories positionCategories=new PositionCategories();
        positionCategories.setId(PositionCategoriesID);
        positionCategories.setName(PositionCategoriesName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(positionCategories)

                .when()
                .put("school-service/api/position-category")

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Can't find Position Category"))
        ;
    }

















    public String getRandomName() {
        String rdm= RandomStringUtils.randomAlphabetic(3).toLowerCase();
        return rdm+"->MK26TR<-"+rdm;
    }

}
