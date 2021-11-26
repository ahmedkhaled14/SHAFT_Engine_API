package API.Booker.ObjectModels;

import com.shaft.api.RestActions;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;


@SuppressWarnings("ALL")
public class RestFullBookerAPIBookingCRUD {
    private RestActions ApiObject;
    private String BookingServiceName = System.getProperty("bookingServiceName");

    public RestFullBookerAPIBookingCRUD(RestActions ApiObject) {
        this.ApiObject = ApiObject;
    }

    /**
     * Get All Bookings
     *
     * @return All Bookings
     */
    public Response GetBookingIDs() {
        return ApiObject.buildNewRequest(BookingServiceName, RestActions.RequestType.GET).performRequest();
    }

    /**
     * Get All Bookings by using First & Last Name Filter
     *
     * @param firstName String value from UpdatedbookinkBody.json
     * @param lastName  String value from UpdatedbookinkBody.json
     * @return All Bookings with the same First & Last Name
     */
    public Response GetBookingIDs(String firstName, String lastName) {
        return ApiObject.buildNewRequest(BookingServiceName, RestActions.RequestType.GET)
                .setUrlArguments("firstname=" + firstName + "&lastname=" + lastName)
                .setTargetStatusCode(RestFullBookerAPI.SUCCESS)
                .performRequest();
    }

    /**
     * Get Booking By ID
     *
     * @param bookingID
     * @return Specific Booking
     */
    public Response GetBooking(String bookingID) {
        return ApiObject.buildNewRequest(BookingServiceName + "/" + bookingID, RestActions.RequestType.GET)
                .setTargetStatusCode(RestFullBookerAPI.SUCCESS)
                .performRequest();
    }

    private JSONObject createBookingBody(String firstname, String lastname, int totalprice, boolean depositpaid,
                                         String checkinDate, String checkoutDate, String additionalneeds) {
        JSONObject createBookingBody = new JSONObject();
        createBookingBody.put("firstname", firstname);
        createBookingBody.put("lastname", lastname);
        createBookingBody.put("totalprice", totalprice);
        createBookingBody.put("depositpaid", depositpaid);
        JSONObject bookingdates = new JSONObject();
        bookingdates.put("checkin", checkinDate);
        bookingdates.put("checkout", checkoutDate);
        createBookingBody.put("bookingdates", bookingdates);
        createBookingBody.put("additionalneeds", additionalneeds);
        return createBookingBody;
    }

    /**
     * Create Booking Request
     *
     * @param firstname       String value from APIBookerTestData.json
     * @param lastname        String value from APIBookerTestData.json
     * @param totalprice      int value from APIBookerTestData.json
     * @param depositpaid     boolean value from APIBookerTestData.json
     * @param checkinDate     Date as String Value from APIBookerTestData.json
     * @param checkoutDate    Date as String Value from APIBookerTestData.json
     * @param additionalneeds String value from APIBookerTestData.json
     * @return New Booking Response
     */
    public Response CreateBooking(String firstname, String lastname, int totalprice, boolean depositpaid,
                                  String checkinDate, String checkoutDate, String additionalneeds) {
        return ApiObject.buildNewRequest(BookingServiceName, RestActions.RequestType.POST)
                .setContentType(ContentType.JSON)
                .setRequestBody(createBookingBody(firstname, lastname, totalprice, depositpaid,
                        checkinDate, checkoutDate, additionalneeds))
                .setTargetStatusCode(RestFullBookerAPI.SUCCESS)
                .performRequest();
    }

    private JSONObject UpdatedBookingBody(String UpdatedFirstname, String UpdatedLastname, int UpdatedTotalprice, boolean UpdatedDepositpaid,
                                          String UpdatedCheckinDate, String UpdatedCheckoutDate, String UpdatedAdditionalneeds) {
        JSONObject UpdatedBookingBody = new JSONObject();
        UpdatedBookingBody.put("firstname", UpdatedFirstname);
        UpdatedBookingBody.put("lastname", UpdatedLastname);
        UpdatedBookingBody.put("totalprice", UpdatedTotalprice);
        UpdatedBookingBody.put("depositpaid", UpdatedDepositpaid);
        JSONObject Updatedbookingdates = new JSONObject();
        Updatedbookingdates.put("checkin", UpdatedCheckinDate);
        Updatedbookingdates.put("checkout", UpdatedCheckoutDate);
        UpdatedBookingBody.put("bookingdates", Updatedbookingdates);
        UpdatedBookingBody.put("additionalneeds", UpdatedAdditionalneeds);
        return UpdatedBookingBody;
    }

    /**
     * Update Booking Request
     *
     * @param Bookingid              BookingID
     * @param UpdatedFirstname       String value from UpdatedbookinkBody.json
     * @param UpdatedLastname        String value from UpdatedbookinkBody.json
     * @param UpdatedTotalprice      int value from UpdatedbookinkBody.json
     * @param UpdatedDepositpaid     boolean value from UpdatedbookinkBody.json
     * @param UpdatedCheckinDate     Date as String Value from UpdatedbookinkBody.json
     * @param UpdatedCheckoutDate    Date as String Value from UpdatedbookinkBody.json
     * @param UpdatedAdditionalneeds String value from UpdatedbookinkBody.json
     * @return Updated Booking Response
     */
    public Response UpdateBooking(String Bookingid, String UpdatedFirstname, String UpdatedLastname, int UpdatedTotalprice
            , boolean UpdatedDepositpaid, String UpdatedCheckinDate, String UpdatedCheckoutDate, String UpdatedAdditionalneeds) {
        return ApiObject.buildNewRequest(BookingServiceName + "/" + Bookingid, RestActions.RequestType.PUT)
                .setContentType(ContentType.JSON)
                .setRequestBody(UpdatedBookingBody(UpdatedFirstname, UpdatedLastname, UpdatedTotalprice, UpdatedDepositpaid,
                        UpdatedCheckinDate, UpdatedCheckoutDate, UpdatedAdditionalneeds))
                .performRequest();
    }

    /**
     * Delete Booking
     *
     * @param Bookingid
     * @return Success Delete Booking
     */
    public Response DeleteBooking(String Bookingid) {
        return ApiObject.buildNewRequest(BookingServiceName + "/" + Bookingid, RestActions.RequestType.DELETE)
                .setContentType(ContentType.JSON)
                .setTargetStatusCode(RestFullBookerAPI.SUCCESS_DELETE)
                .performRequest();
    }

}
