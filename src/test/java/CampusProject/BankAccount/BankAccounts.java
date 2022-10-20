package CampusProject.BankAccount;

import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class BankAccounts {

    Cookies cookies;

    @BeforeClass
    public void login() {
        baseURI = "https://demo.mersys.io/school-service/api/";

        LoginInformation login = new BankAccounts().new LoginInformation();
        login.setUsername("richfield.edu");
        login.setPassword("Richfield2020!");
        login.setRememberMe("true");

        cookies =


                given()
                        .contentType(ContentType.JSON)
                        .body(login)

                        .when()
                        .post("https://demo.mersys.io/auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies()

        ;
    }


    String id;

    @Test
    public void createBankAccount() {

        BankAccountInformation bai = new BankAccounts().new BankAccountInformation();
        bai.setActive(false);
        bai.setCurrency("USD");
        bai.setIban("TR" + RandomInformation.randomNumber(20));
        bai.setIntegrationCode(RandomInformation.randomNumber(2));
        bai.setName(RandomInformation.randomName());
        bai.setSchoolId("6343bf893ed01f0dc03a509a");
        id =

                given()
                        .cookies(cookies)
                        .log().body()
                        .contentType(ContentType.JSON)
                        .body(bai)

                        .when()
                        .post("bank-accounts")


                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().path("id")

        ;

    }

    String name;
    String integrationCode;
    String iban;

    @Test(dependsOnMethods = "createBankAccount")
    public void updateBankAccount(){
        BankAccountInformation bai=new BankAccountInformation();
        bai.setActive(true);
        bai.setCurrency("TRY");
        bai.setIban("TR"+RandomInformation.randomNumber(20));
        bai.setId(id);
        bai.setIntegrationCode(RandomInformation.randomNumber(3));
        bai.setName(RandomInformation.randomName());
        bai.setSchoolId("6343bf893ed01f0dc03a509a");
        Response response =

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(bai)
                .log().body()


                .when()
                .put("bank-accounts")


                .then()
                .statusCode(200)
                .extract().response();

                ;
        name = response.path("name");
        integrationCode=response.path("integrationCode");
        iban = response.path("iban");
    }

    @Test(dependsOnMethods = "updateBankAccount")
    public void createBankAccountNegative() {
        BankAccountInformation bai=new BankAccountInformation();
        bai.setActive(true);
        bai.setCurrency("TRY");
        bai.setIban(iban);
        bai.setIntegrationCode(integrationCode);
        bai.setName(name);
        bai.setSchoolId("6343bf893ed01f0dc03a509a");

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(bai)

                .when()
                .post("bank-accounts")

                .then()
                .statusCode(400)
                ;

    }

    @Test(dependsOnMethods = "createBankAccountNegative")
    public void deleteBankAccount() {

        given()
                .cookies(cookies)
                .pathParam("accountId",id)


                .when()
                .delete("bank-accounts/{accountId}")

                .then()
                .statusCode(200)
                ;


    }

    @Test(dependsOnMethods = "deleteBankAccount")
    public void deleteBankAccountNegative() {

        given()
                .cookies(cookies)
                .pathParam("accountId",id)

                .when()
                .delete("bank-accounts/{accountId}")

                .then()
                .statusCode(400)

                ;


    }

    @Test(dependsOnMethods = "deleteBankAccountNegative")
    public void updateBankAccountNegative() {
        BankAccountInformation bai=new BankAccountInformation();
        bai.setActive(true);
        bai.setCurrency("TRY");
        bai.setIban("TR"+RandomInformation.randomNumber(20));
        bai.setId(id);
        bai.setIntegrationCode(RandomInformation.randomNumber(3));
        bai.setName(RandomInformation.randomName());
        bai.setSchoolId("6343bf893ed01f0dc03a509a");

                given()
                        .cookies(cookies)
                        .contentType(ContentType.JSON)
                        .body(bai)
                        .log().body()


                        .when()
                        .put("bank-accounts")


                        .then()
                        .statusCode(400)


        ;


    }



    public class BankAccountInformation {

        private boolean active;
        private String currency;
        private String iban;
        private String id;
        private String integrationCode;
        private String name;
        private String schoolId;


        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }


        public String getIban() {
            return iban;
        }

        public void setIban(String iban) {
            this.iban = iban;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntegrationCode() {
            return integrationCode;
        }

        public void setIntegrationCode(String integrationCode) {
            this.integrationCode = integrationCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }
    }

    public class LoginInformation {
        private String username;
        private String password;
        private String rememberMe;

        public LoginInformation() {

        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRememberMe() {
            return rememberMe;
        }

        public void setRememberMe(String rememberMe) {
            this.rememberMe = rememberMe;
        }
    }

}
