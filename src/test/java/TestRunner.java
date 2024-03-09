import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestRunner extends Setup {
    @Test(priority = 1, description = "Admin can login successfully")
    public void doLogin() throws IOException, ConfigurationException {
        UserController userController = new UserController();
        userController.doLogin("salman@roadtocareer.net", "1234");
    }
    @Test(priority = 2, description = "Admin creates new user")
    public void createUser() throws IOException, ConfigurationException {
        UserController userController = new UserController();
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.name().firstName().toLowerCase() + "@test.com";
        String password = "1234";
        String phone_number = "01500" + Utils.generateRandom(100000, 999999);
        String nid = "123456789";
        String role = "Customer";
        UserModel model = new UserModel();
        model.setName(name);
        model.setEmail(email);
        model.setPassword(password);
        model.setPhone_number(phone_number);
        model.setNid(nid);
        model.setRole(role);
        JsonPath jsonObj = userController.createUser(model);
        int userId = jsonObj.get("user.id");
        Utils.setEnvVar("userId", String.valueOf(userId));
    }
    @Test(priority = 3, description = "Admin can search user")
    public void searchUser() throws IOException {
        UserController userController = new UserController();
        JsonPath jsonObj= userController.searchUser(prop.getProperty("userId"));
        String messageActual= jsonObj.get("message");
        String messageExpected="User found";
        Assert.assertTrue(messageActual.contains(messageExpected));
    }
}
