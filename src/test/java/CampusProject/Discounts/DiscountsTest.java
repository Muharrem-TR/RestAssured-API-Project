package CampusProject.Discounts;

import CampusProject.Discounts.Model.DiscountsModel;
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

public class DiscountsTest {
    Cookies cookies;

    @BeforeClass
    public void loginCampus() {
        baseURI = "https://demo.mersys.io/";
        Map<String, String> credential = new HashMap<>();
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


    String discountsID;
    String discountsDescription;
    String discountsCode;
    String discountsPriority;

    @Test
    public void addDiscounts(){
        discountsDescription=getRandomDescription();
        discountsCode=getRandomCode();
        discountsPriority=getRandomPriority();

        DiscountsModel discounts=new DiscountsModel();
        discounts.setDescription(discountsDescription);
        discounts.setCode(discountsCode);
        discounts.setPriority(discountsPriority);

        discountsID=
                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(discounts)
                        .when()
                        .post("school-service/api/discounts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getString("id");
    }

    @Test(dependsOnMethods = "addDiscounts")
    public void addDiscountsNegative(){
        DiscountsModel discounts=new DiscountsModel();
        discounts.setDescription(discountsDescription);
        discounts.setPriority(discountsPriority);
        discounts.setCode(discountsCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(discounts)
                .when()
                .post("school-service/api/discounts")
                .then()
                .statusCode(400)
                .body("message",equalTo("The Discount with Description \""+discountsDescription+"\" already exists."));
    }

    @Test(dependsOnMethods = "addDiscounts")
    public void updateDiscounts(){
        discountsDescription=getRandomDescription();
        DiscountsModel discounts=new DiscountsModel();
        discounts.setDescription(discountsDescription);
        discounts.setPriority(discountsPriority);
        discounts.setCode(discountsCode);
        discounts.setId(discountsID);


        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(discounts)
                .when()
                .put("school-service/api/discounts")
                .then()
                .statusCode(200)
                .body("description",equalTo(discountsDescription));
    }

    @Test(dependsOnMethods = "updateDiscounts")
    public void deleteDiscountsByID(){
        given()
                .cookies(cookies)
                .pathParam("discountsID",discountsID)
                .when()
                .delete("school-service/api/discounts/{discountsID}")
                .then()
                .log().body()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "deleteDiscountsByID")
    public void deleteDiscountsByIDNegative(){
        given()
                .cookies(cookies)
                .pathParam("discountsID",discountsID)
                .log().uri()
                .when()
                .delete("school-service/api/discounts/{discountsID}")
                .then()
                .log().body()
                .statusCode(400);
    }

    @Test(dependsOnMethods = "deleteDiscountsByID")
    public void updateDiscountsNegative(){
        discountsDescription=getRandomDescription();
        DiscountsModel discounts=new DiscountsModel();
        discounts.setDescription(discountsDescription);
        discounts.setId(discountsID);
        discounts.setCode(discountsCode);
        discounts.setPriority(discountsPriority);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(discounts)
                .when()
                .put("school-service/api/discounts")
                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Discount.Error.DISCOUNT_NOT_FOUND"));
    }








    public String getRandomDescription(){
        return RandomStringUtils.randomAlphabetic(8).toLowerCase();
    }
    public String getRandomCode(){
        return RandomStringUtils.randomAlphabetic(5);
    }
    public String getRandomPriority(){
        return RandomStringUtils.randomNumeric(4);
    }

}
