package API.tests;

import com.shaft.api.RestActions;
import com.shaft.api.RestActions.RequestType;
import com.shaft.driver.DriverFactory;
import com.shaft.validation.Validations;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
public class RestFullBookerLinearDesign {
    private RestActions ApiObject;

    @BeforeClass(description = " BeforeClass ==> Authentication & Create Token ")
    public void beforeClass() {
        ApiObject = DriverFactory.getAPIDriver("https://restful-booker.herokuapp.com/");

        JSONObject AUTH = new JSONObject();
        AUTH.put("username", "admin");
        AUTH.put("password", "password123");

        Response CreateToken = ApiObject.buildNewRequest("auth", RequestType.POST)
                .setContentType(ContentType.JSON)
                .setRequestBody(AUTH).performRequest();

        String Token = RestActions.getResponseJSONValue(CreateToken, "token");
        ApiObject.addHeaderVariable("Cookie", "token=" + Token);
    }

    @Test(description = " GET All Bookings ")
    public void GetBookingIDs() {
        ApiObject.buildNewRequest("booking", RequestType.GET).performRequest();
    }

    @Test(description = " GET Specific Booking By ID ")
    public void GetBooking() {
       ApiObject.buildNewRequest("booking/" + "5", RequestType.GET).performRequest();
    }

    @Test(description = " Create New Booking ")
    public void CreateBooking() {

        JSONObject createBookingBody = new JSONObject();
        createBookingBody.put("firstname", "Ahmed");
        createBookingBody.put("lastname", "Khaled");
        createBookingBody.put("totalprice", 100);
        createBookingBody.put("depositpaid", true);
        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2018-01-01");
        bookingdates.put("checkout", "2019-01-01");
        createBookingBody.put("bookingdates", bookingdates);
        createBookingBody.put("additionalneeds", "Breakfast");

        Response CreateBookingResponse = ApiObject.buildNewRequest("booking", RequestType.POST)
                .setContentType(ContentType.JSON)
                .setRequestBody(createBookingBody)
                .performRequest();

        String BookingID = RestActions.getResponseJSONValue(CreateBookingResponse, "bookingid");
        Response getBookingResponse = ApiObject.buildNewRequest("booking/" + BookingID, RequestType.GET).performRequest();

        Validations.assertThat()
                .response(getBookingResponse)
                .isEqualToFileContent
                        (System.getProperty("testDataFolderPath") + "/RestFullBooker/booking.json")
                .withCustomReportMessage("Check that getBookingResponse is equal to file Content")
                .perform();
    }

    @Test(dependsOnMethods = {"CreateBooking"},description = " Update Booking  ")
    public void UpdateBooking() {

        Response GetBookingID = ApiObject.buildNewRequest("booking", RequestType.GET)
                .setUrlArguments("firstname=Ahmed&lastname=Khaled")
                .performRequest();

        String BookingID = RestActions.getResponseJSONValue(GetBookingID, "bookingid[0]");

        JSONObject UpdateBookingBody = new JSONObject();
        UpdateBookingBody.put("firstname", "mohamed");
        UpdateBookingBody.put("lastname", "ali");
        UpdateBookingBody.put("totalprice", 200);
        UpdateBookingBody.put("depositpaid", false);
        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", "2018-02-02");
        bookingdates.put("checkout", "2019-02-02");
        UpdateBookingBody.put("bookingdates", bookingdates);
        UpdateBookingBody.put("additionalneeds", "dinner");

        Response UpdateBookingByID = ApiObject.buildNewRequest("booking/"+BookingID,RequestType.PUT)
                .setContentType(ContentType.JSON)
                .setRequestBody(UpdateBookingBody)
                .performRequest();

        Validations.assertThat().response(UpdateBookingByID)
                .isEqualToFileContent(System.getProperty("testDataFolderPath")+"RestFullBooker/Updatebooking.json")
                .withCustomReportMessage("Check that the Booking is Updated with the exactly updated data  ")
                .perform();

    }

    @Test(dependsOnMethods = {"UpdateBooking"}, description = " Delete Booking ")
    public void DeleteBooking() {

        Response GetBookingID = ApiObject.buildNewRequest("booking", RequestType.GET)
                .setUrlArguments("firstname=mohamed&lastname=ali")
                .performRequest();

        String Bookingid = RestActions.getResponseJSONValue(GetBookingID, "bookingid[0]");
        Response Delete = ApiObject.buildNewRequest("booking/" + Bookingid, RequestType.DELETE)
                .setTargetStatusCode(201)
                .setContentType(ContentType.JSON)
                .performRequest();

        String DeleteBookingBody = RestActions.getResponseBody(Delete);
        Validations.assertThat().object(DeleteBookingBody).isEqualTo("Created")
                .withCustomReportMessage("the booking is Deleted ")
                .perform();

    }

}
