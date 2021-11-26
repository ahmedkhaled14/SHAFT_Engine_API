package API.Booker.ObjectModels;

import com.shaft.api.RestActions;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

@SuppressWarnings("unchecked")
public class RestFullBookerAPI {
    private final RestActions ApiObject;
    public static final String BASE_URl = System.getProperty("BaseUrl");
    public static final int SUCCESS = Integer.parseInt(System.getProperty("SuccessValue"));
    public static final int SUCCESS_DELETE = Integer.parseInt(System.getProperty("SuccessDeleteValue"));
    private final String Auth_ServiceName = System.getProperty("AuthServiceName");

    public RestFullBookerAPI(RestActions ApiObject) {
        this.ApiObject = ApiObject;
    }

    /**
     * Authentication & Create Token
     *
     * @param username String Value from RestFullBooker.properties
     * @param password String Value from RestFullBooker.properties
     */
    public void Login(String username, String password) {
        JSONObject AUTH = new JSONObject();
        AUTH.put("username", username);
        AUTH.put("password", password);

        Response CreateToken = ApiObject.buildNewRequest(Auth_ServiceName, RestActions.RequestType.POST)
                .setContentType(ContentType.JSON)
                .setRequestBody(AUTH).performRequest();

        String Token = RestActions.getResponseJSONValue(CreateToken, "token");
        ApiObject.addHeaderVariable("Cookie", "token=" + Token);
    }

}
