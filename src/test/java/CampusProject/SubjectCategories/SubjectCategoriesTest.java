package CampusProject.SubjectCategories;

import CampusProject.SubjectCategories.Model.Subject;
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

public class SubjectCategoriesTest {
    Cookies cookies;

    @BeforeClass
    public void loginCampus(){
        baseURI="https://demo.mersys.io/";
        Map<String,String> credential=new HashMap<>();
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
                        .extract().response().getDetailedCookies();

    }

    String subjectID;
    String subjectName;
    String subjectCode;

    @Test
    public void addSubjectCategories(){
        subjectName=getRandomName();
        subjectCode=getRandomCode();
        Subject subject=new Subject();
        subject.setName(subjectName);
        subject.setCode(subjectCode);


        subjectID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(subject)
                        .when()
                        .post("school-service/api/subject-categories")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");

    }

    @Test (dependsOnMethods = "addSubjectCategories")
    public void addSubjectCategoriesNegative(){

        Subject subject=new Subject();
        subject.setName(subjectName);
        subject.setCode(subjectCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(subject)
                .when()
                .post("school-service/api/subject-categories")
                .then()
                .statusCode(400)
                .body("message",equalTo("The Subject Category with Name \""+subjectName+"\" already exists."));

    }

    @Test(dependsOnMethods = "addSubjectCategories")
    public void updateSubjectCategories(){
        subjectName=getRandomName();
        Subject subject=new Subject();
        subject.setId(subjectID);
        subject.setCode(subjectCode);
        subject.setName(subjectName);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(subject)
                .when()
                .put("school-service/api/subject-categories")
                .then()
                .statusCode(200)
                .body("name",equalTo(subjectName));



    }

    @Test(dependsOnMethods = "updateSubjectCategories")
    public void DeleteSubjectCategoriesByID(){
        given()
                .cookies(cookies)
                .pathParam("subjectID",subjectID)
                .when()
                .delete("school-service/api/subject-categories/{subjectID}")
                .then()
                .log().body()
                .statusCode(200);


    }
    @Test(dependsOnMethods = "DeleteSubjectCategoriesByID")
    public void deleteSubjectCategoriesNegative(){
        given()
                .cookies(cookies)
                .pathParam("subjectID",subjectID)
                .log().uri()
                .when()
                .delete("school-service/api/subject-categories/{subjectID}")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "DeleteSubjectCategoriesByID")
    public void updateSubjectCategoriesNegative(){
        subjectName=getRandomName();
        Subject subject=new Subject();
        subject.setId(subjectID);
        subject.setName(subjectName);
        subject.setCode(subjectCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(subject)
                .when()
                .put("school-service/api/subject-categories")
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Can't find Subject Category"));
    }











    public String getRandomName(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }
    public String getRandomCode(){return RandomStringUtils.randomNumeric(3);}


}
