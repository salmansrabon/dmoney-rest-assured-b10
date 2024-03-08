import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class UserController extends Setup {
    public UserController() throws IOException {
        intiConfig();
    }
    public void doLogin() throws ConfigurationException, IOException {
        RestAssured.baseURI= prop.getProperty("baseUrl");
        Response res= given().contentType("application/json").body("{\n" +
                "    \"email\":\"salman@roadtocareer.net\",\n" +
                "    \"password\":\"1234\"\n" +
                "}").when().post("/user/login")
                .then().assertThat().statusCode(200).extract().response();

        System.out.println(res.asString());
        JsonPath jsonObj=res.jsonPath();
        String token= jsonObj.get("token");
        System.out.println(token);
        setEnvVar("token",token);
    }
    public void searchUser(){
        RestAssured.baseURI= prop.getProperty("baseUrl");
        Response res= given().contentType("application/json")
                .header("Authorization",prop.getProperty("token"))
                        .when().get("/user/search/id/1");
        System.out.println(res.asString());
    }
    public static void setEnvVar(String key, String value) throws ConfigurationException {
        PropertiesConfiguration config=new PropertiesConfiguration("./src/test/resources/config.properties");
        config.setProperty(key,value);
        config.save();
    }

}
