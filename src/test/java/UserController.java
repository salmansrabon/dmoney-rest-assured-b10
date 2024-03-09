import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class UserController extends Setup {
    public UserController() throws IOException {
        intiConfig();
    }
    public void doLogin(String email, String password) throws ConfigurationException, IOException {
        RestAssured.baseURI= prop.getProperty("baseUrl");
        UserModel model=new UserModel();
        model.setEmail(email);
        model.setPassword(password);
        Response res= given().contentType("application/json").body(model).when().post("/user/login")
                .then().assertThat().statusCode(200).extract().response();

        System.out.println(res.asString());
        JsonPath jsonObj=res.jsonPath();
        String token= jsonObj.get("token");
        System.out.println(token);
        Utils.setEnvVar("token",token);
    }
    public JsonPath searchUser(String userId){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        Response res= given().contentType("application/json")
                .header("Authorization",prop.getProperty("token"))
                        .when().get("/user/search/id/"+userId);
        System.out.println(res.asString());
        return res.jsonPath();
    }
    public JsonPath createUser(UserModel model){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        Response res= given().contentType("application/json").body(model)
                .header("Authorization",prop.getProperty("token"))
                .header("X-AUTH-SECRET-KEY","ROADTOSDET")
                        .when().post("/user/create")
                .then().assertThat().statusCode(201).extract().response();

        System.out.println(res.asString());
        return res.jsonPath();
    }

}
