package API.tests;

import API.Booker.ObjectModels.RestFullBookerAPI;
import API.Booker.ObjectModels.RestFullBookerAPIBookingCRUD;
import com.shaft.api.RestActions;
import com.shaft.driver.DriverFactory;
import com.shaft.tools.io.JSONFileManager;
import com.shaft.validation.Validations;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
@Epic("RestFull Booker")
@Feature("API")
public class RestFullBooker {

    private RestActions ApiObject;
    private RestFullBookerAPI restFullBookerAPI;
    private RestFullBookerAPIBookingCRUD restFullBookerAPIBookingCRUD;
    private JSONFileManager jsonTestData;
    private JSONFileManager UpdatedjsonTestData;

    @BeforeClass(description = " BeforeClass ==> Authentication & Create Token ")
    public void beforeClass() {
        jsonTestData = new JSONFileManager(System.getProperty("testDataFolderPath") + "RestFullBooker/APIBookerTestData.json");
        UpdatedjsonTestData = new JSONFileManager(System.getProperty("testDataFolderPath") + "RestFullBooker/Updatebooking.json");
        ApiObject = DriverFactory.getAPIDriver(RestFullBookerAPI.BASE_URl);
        restFullBookerAPI = new RestFullBookerAPI(ApiObject);
        restFullBookerAPIBookingCRUD = new RestFullBookerAPIBookingCRUD(ApiObject);
        restFullBookerAPI.Login(System.getProperty("username"), System.getProperty("password"));
    }

    @Description("When I ask for all Booking IDs, Then I get all the Booking IDs ")
    @Story("CRUD Operations")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = " GET All Booking IDs ")
    public void GetBookingIDs() {
        restFullBookerAPIBookingCRUD.GetBookingIDs();
    }

    @Description("When I have a Booking, And I ask for the Booking,Then I get the Specific Booking By ID")
    @Story("CRUD Operations")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = " GET Specific Booking By ID ")
    public void GetBooking() {
        Response CreateBookingResponse = restFullBookerAPIBookingCRUD.CreateBooking
                (
                        jsonTestData.getTestData("firstName"),
                        jsonTestData.getTestData("lastName"),
                        Integer.parseInt(jsonTestData.getTestData("totalprice")),
                        Boolean.parseBoolean(jsonTestData.getTestData("depositpaid")),
                        jsonTestData.getTestData("checkinDate"),
                        jsonTestData.getTestData("checkoutDate"),
                        jsonTestData.getTestData("additionalNeeds")
                );
        String BookingID = RestActions.getResponseJSONValue(CreateBookingResponse, "bookingid");
        Response getBookingByID = restFullBookerAPIBookingCRUD.GetBooking(BookingID);
        Validations.assertThat()
                .response(getBookingByID)
                .isEqualToFileContent(System.getProperty("testDataFolderPath") + "/RestFullBooker/booking.json")
                .withCustomReportMessage(" Get the desired Booking Done ")
                .perform();
    }

    @Description("When I Ask for Create New Booking Request, And Enter all data, Then a new Booking should be created ")
    @Story("CRUD Operations")
    @Severity(SeverityLevel.CRITICAL)
    @Test(description = " Create New Booking ")
    public void CreateBooking() {
        Response CreateBookingResponse = restFullBookerAPIBookingCRUD.CreateBooking
                (
                        jsonTestData.getTestData("firstName"),
                        jsonTestData.getTestData("lastName"),
                        Integer.parseInt(jsonTestData.getTestData("totalprice")),
                        Boolean.parseBoolean(jsonTestData.getTestData("depositpaid")),
                        jsonTestData.getTestData("checkinDate"),
                        jsonTestData.getTestData("checkoutDate"),
                        jsonTestData.getTestData("additionalNeeds")
                );
        String BookingID = RestActions.getResponseJSONValue(CreateBookingResponse, "bookingid");
        Response getBookingResponse = restFullBookerAPIBookingCRUD.GetBooking(BookingID);

        Validations.assertThat()
                .response(getBookingResponse)
                .isEqualToFileContent
                        (System.getProperty("testDataFolderPath") + "/RestFullBooker/booking.json")
                .withCustomReportMessage(" The Booking is Created ")
                .perform();
    }

    @Description("When I Ask for update existing booking, And And Enter all new data, Then Check that the Booking is Updated with the exactly updated data ")
    @Story("CRUD Operations")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = "CreateBooking", description = " Update Booking ")
    public void UpdateBooking() {
        Response GetBookingID = restFullBookerAPIBookingCRUD.GetBookingIDs(jsonTestData.getTestData("firstName"), jsonTestData.getTestData("lastName"));
        String Bookingid = RestActions.getResponseJSONValue(GetBookingID, "bookingid[0]");

        Response UpdateBookingByID = restFullBookerAPIBookingCRUD.UpdateBooking
                (
                        Bookingid,
                        UpdatedjsonTestData.getTestData("firstname"),
                        UpdatedjsonTestData.getTestData("lastname"),
                        Integer.parseInt(UpdatedjsonTestData.getTestData("totalprice")),
                        Boolean.parseBoolean(UpdatedjsonTestData.getTestData("depositpaid")),
                        UpdatedjsonTestData.getTestData("bookingdates.checkin"),
                        UpdatedjsonTestData.getTestData("bookingdates.checkout"),
                        UpdatedjsonTestData.getTestData("additionalneeds")
                );

        Validations.assertThat()
                .response(UpdateBookingByID)
                .isEqualToFileContent(System.getProperty("testDataFolderPath") + "RestFullBooker/Updatebooking.json")
                .withCustomReportMessage(" the Booking is Updated with the exactly updated data")
                .perform();
    }

    @Description("When i Ask for Booking by Arguments: firstname & lastname, And i get this booking, Then this Booking should be Deleted  ")
    @Story("CRUD Operations")
    @Severity(SeverityLevel.CRITICAL)
    @Test(dependsOnMethods = {"UpdateBooking"}, description = " Delete Booking ")
    public void DeleteBooking() {
        Response GetBookingID = restFullBookerAPIBookingCRUD.GetBookingIDs(UpdatedjsonTestData.getTestData("firstname"), UpdatedjsonTestData.getTestData("lastname"));
        String Bookingid = RestActions.getResponseJSONValue(GetBookingID, "bookingid[0]");
        Response Delete = restFullBookerAPIBookingCRUD.DeleteBooking(Bookingid);
        String DeleteBookingBody = RestActions.getResponseBody(Delete);

        Validations.assertThat()
                .object(DeleteBookingBody)
                .isEqualTo("Created")
                .withCustomReportMessage("the booking is Deleted ")
                .perform();
    }

}
