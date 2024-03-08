import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;

public class MyAPICalling {
    Properties prop;
    FileInputStream file;

    public void loadConfigData() throws IOException {
        prop=new Properties();
        file=new FileInputStream("./src/test/resources/config.properties");
        prop.load(file);
    }
    @Test
    public void doLogin() throws ConfigurationException, IOException {
        loadConfigData();
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
    public static void setEnvVar(String key, String value) throws ConfigurationException {
        PropertiesConfiguration config=new PropertiesConfiguration("./src/test/resources/config.properties");
        config.setProperty(key,value);
        config.save();
    }

}
