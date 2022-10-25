package CampusProject.GradeLevels;

import CampusProject.GradeLevels.Model.Grade;
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

public class GradeLevelsTest {
    Cookies cookies;

    @BeforeClass
    public void loginCampus(){
        baseURI="https://demo.mersys.io/";
        Map<String,String> credential =new HashMap<>();
        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");

        cookies=
                given()
                        .contentType(ContentType.JSON)
                        .body(credential)
                        .when()
                        .post("auth/login")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()

        ;
    }

    String GradeID;
    String Gradename;
    String GradeshortName;
    String Gradeorder;


    @Test
    public void addGradeLevels(){
        Gradename =getRandomName();
        GradeshortName =getRandomShortName();
        Gradeorder=getRandomOrder();

        Grade grade=new Grade();
        grade.setName(Gradename);
        grade.setShortName(GradeshortName);
        grade.setOrder(Gradeorder);


        GradeID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(grade)
                        .when()
                        .post("school-service/api/grade-levels")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");

    }


    @Test(dependsOnMethods = "addGradeLevels")
    public void addGradeLevelNegative(){
        Grade grade=new Grade();
        grade.setName(Gradename);
        grade.setShortName(GradeshortName);
        grade.setOrder(Gradeorder);



        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(grade)
                .when()
                .post("school-service/api/grade-levels")
                .then()
                .statusCode(400)
                .body("message",equalTo("The Grade Level with Name \""+Gradename+"\" already exists."));

    }

    @Test(dependsOnMethods = "addGradeLevels")
    public void updateGradeLevels(){
        Gradename =getRandomName();
        Grade grade=new Grade();
        grade.setId(GradeID);
        grade.setName(Gradename);
        grade.setShortName(GradeshortName);
        grade.setOrder(Gradeorder);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(grade)
                .when()
                .put("school-service/api/grade-levels")
                .then()
                .statusCode(200)
                .body("name",equalTo(Gradename));



    }
    @Test(dependsOnMethods = "updateGradeLevels")
    public void deleteGradeLevelByID(){
        given()
                .cookies(cookies)
                .pathParam("GradeID",GradeID)
                .when()
                .delete("school-service/api/grade-levels/{GradeID}")
                .then()
                .log().body()
                .statusCode(200);


    }

    @Test(dependsOnMethods = "deleteGradeLevelByID")
    public void deleteGradeLevelNegative(){
        given()
                .cookies(cookies)
                .pathParam("GradeID",GradeID)
                .log().uri()
                .when()
                .delete("school-service/api/grade-levels/{GradeID}")
                .then()
                .log().body()
                .statusCode(400);

    }

    @Test(dependsOnMethods = "deleteGradeLevelByID")
    public void updateGradeLevelNegative(){
        Gradename=getRandomName();
        Grade grade=new Grade();
        grade.setId(GradeID);
        grade.setName(Gradename);
        grade.setShortName(GradeshortName);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(grade)
                .when()
                .put("school-service/api/grade-levels")
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Grade Level not found."));
    }




    public String getRandomName(){
        return RandomStringUtils.randomAlphabetic(5).toLowerCase();
    }
    public String getRandomShortName(){
        return RandomStringUtils.randomAlphabetic(5).toLowerCase();
    }
    public  String getRandomOrder(){return RandomStringUtils.randomNumeric(4);}



}
