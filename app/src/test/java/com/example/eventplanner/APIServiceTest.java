package com.example.eventplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.example.eventplanner.API.APIService;
import com.example.eventplanner.Models.Event;
import com.example.eventplanner.Models.Person;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServiceTest { // RUNNING JUNIT TESTS HERE BECAUSE WE AREN'T INTERACTING WITH UI COMPONENTS.
    private Retrofit retrofit;  // Retrofit client used for HTTP requests
    private APIService apiService;  // Interface for Retrofit API methods
    private MockWebServer mockWebServer;  // Mock server for handling requests without real network use

    @Before
    public void setUp() throws Exception {
        mockWebServer = new MockWebServer();  // Create a new instance of MockWebServer
        mockWebServer.start();  // Start the server so it begins listening for connections

        // Configure Retrofit with the base URL from the running MockWebServer
        retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/").toString())  // Set the server URL
                .addConverterFactory(GsonConverterFactory.create())  // Add Gson converter for JSON parsing
                .build();  // Build the Retrofit instance

        apiService = retrofit.create(APIService.class);  // Create the API service from the Retrofit configuration
    }

    // EVENT ENDPOINT TESTS:

    @Test
    public void testEventFetch() throws Exception {
        // Setting expected JSON response for during test.
        String jsonResponse = "[{" +
                "\"eventId\":1," +
                "\"title\":\"TestEvent1\"," +
                "\"date\":\"2024-04-29T00:00:00Z\"," +
                "\"description\":\"A sure look\"," +
                "\"eventsPeople\":[{" +
                "\"personId\":1," +
                "\"name\":\"Alice Smith\"," +
                "\"age\":29" +
                "},{" +
                "\"personId\":2," +
                "\"name\":\"Bob Jones\"," +
                "\"age\":35" +
                "},{" +
                "\"personId\":3," +
                "\"name\":\"Charlie Davis\"," +
                "\"age\":22" +
                "}]" +
                "}]";

        // Specifying expected response.
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).setResponseCode(200));

        // Making an asynchronous request to the mocked API.
        apiService.getEvents().enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                assertTrue(response.isSuccessful());  // Checking successful response.
                assertNotNull(response.body());  // Making sure that we are actually getting back data.

                // Get the first event from the response, even though I only made one mock event.
                Event event = response.body().get(0);

                // Check the event details.
                assertEquals(1, response.body().size());  // Verify only one event is returned
                assertEquals(1, event.getId());  // Verify the event's ID
                assertEquals("TestEvent1", event.getTitle());  // Verify the event title
                assertEquals("2024-04-29T00:00:00Z", event.getDate());  // Verify the event date
                assertEquals("A sure look", event.getDescription());  // Verify the event description

                // Checking that there is a list of people present, and that there are three of them.
                assertNotNull(event.getEventsPeople());
                assertEquals(3, event.getEventsPeople().size());

                // Checking the details of each person in the event.
                assertEquals(1, event.getEventsPeople().get(0).getId());  // Verifying the person's ID
                assertEquals("Alice Smith", event.getEventsPeople().get(0).getName());  // Verifying the person's name
                assertEquals(29, event.getEventsPeople().get(0).getAge());  // Verify the person's age

                assertEquals(2, event.getEventsPeople().get(1).getId());
                assertEquals("Bob Jones", event.getEventsPeople().get(1).getName());
                assertEquals(35, event.getEventsPeople().get(1).getAge());

                assertEquals(3, event.getEventsPeople().get(2).getId());
                assertEquals("Charlie Davis", event.getEventsPeople().get(2).getName());
                assertEquals(22, event.getEventsPeople().get(2).getAge());

            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                fail("Request failed: " + t.getMessage());  // Fail the test if the request fails
            }
        });
    }

    @Test
    public void testGetEventById() throws Exception {
        String jsonResponse = "{" +
                "\"eventId\": 101," +
                "\"title\": \"Annual Tech Conference\"," +
                "\"date\": \"2024-05-15T00:00:00Z\"," +
                "\"description\": \"This is a comprehensive tech event covering the latest in technology.\"," +
                "\"eventsPeople\": [{" +
                "\"personId\": 10," +
                "\"name\": \"John Doe\"," +
                "\"age\": 30" +
                "}]" +
                "}";

        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).setResponseCode(200));

        apiService.getEventById(101).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                assertTrue(response.isSuccessful());
                assertNotNull(response.body());

                Event event = response.body();
                assertEquals(101, event.getId());
                assertEquals("Annual Tech Conference", event.getTitle());
                assertEquals("2024-05-15T00:00:00Z", event.getDate());
                assertEquals("This is a comprehensive tech event covering the latest in technology.", event.getDescription());
                assertNotNull(event.getEventsPeople());
                assertEquals(1, event.getEventsPeople().size());
                assertEquals(10, event.getEventsPeople().get(0).getId());
                assertEquals("John Doe", event.getEventsPeople().get(0).getName());
                assertEquals(30, event.getEventsPeople().get(0).getAge());
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                fail("Request failed: " + t.getMessage());
            }
        });
    }

    @Test
    public void testAddEvent() throws Exception {
        String jsonResponse = "{" +
                "\"eventId\": 202," +
                "\"title\": \"New Workshop\"," +
                "\"date\": \"2024-06-20T00:00:00Z\"," +
                "\"description\": \"A workshop on new technologies.\"," +
                "\"eventsPeople\": []" +
                "}";

        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).setResponseCode(200));

        // Making a mock event to add to the pretend server.
        Event newEvent = new Event();
        newEvent.setTitle("New Workshop");
        newEvent.setDate("2024-06-20T00:00:00Z");
        newEvent.setDescription("A workshop on new technologies.");
        newEvent.setPeople(new ArrayList<>());

        apiService.addEvent(newEvent).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                assertTrue(response.isSuccessful());
                assertNotNull(response.body());

                Event event = response.body();
                assertEquals(202, event.getId());
                assertEquals("New Workshop", event.getTitle());
                assertEquals("2024-06-20T00:00:00Z", event.getDate());
                assertEquals("A workshop on new technologies.", event.getDescription());
                assertTrue(event.getEventsPeople().isEmpty());
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                fail("Request failed: " + t.getMessage());
            }
        });
    }

    @Test
    public void testUpdateEvent() throws Exception {
        String jsonResponse = "{" +
                "\"eventId\": 101," +
                "\"title\": \"Updated Event\"," +
                "\"date\": \"2024-07-15T00:00:00Z\"," +
                "\"description\": \"Updated description.\"," +
                "\"eventsPeople\": []" +
                "}";

        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).setResponseCode(200));

        // Pretend Original event.
        Event originalEvent = new Event();
        originalEvent.setId(101);
        originalEvent.setTitle("Original Event");
        originalEvent.setDescription("Original description.");
        originalEvent.setDate("2024-07-10T00:00:00Z");

        // Pretend updated event (updated pretend original event).
        Event eventForUpdate = new Event();
        eventForUpdate.setId(101);
        eventForUpdate.setTitle("Updated Event");
        eventForUpdate.setDescription("Updated description.");
        eventForUpdate.setDate("2024-07-15T00:00:00Z");

        apiService.updateEvent(originalEvent.getId(), eventForUpdate).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                assertTrue(response.isSuccessful());
                Event updatedEvent = response.body();

                // Verifying the updated event details.
                assertEquals("Updated Event", updatedEvent.getTitle());
                assertEquals("2024-07-15T00:00:00Z", updatedEvent.getDate());
                assertEquals("Updated description.", updatedEvent.getDescription());
            }
            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                fail("Request failed: " + t.getMessage());
            }
        });
    }

    @Test
    public void testDeleteEvent() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("Event deleted successfully"));
        apiService.deleteEvent(101).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                assertTrue("Event deletion should be successful", response.isSuccessful());
                assertEquals("Event deleted successfully", response.message());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                fail("Request failed: " + t.getMessage());
            }
        });
    }

    // PERSON ENDPOINT TESTS:

    @Test
    public void testAddPersonToEvent() throws Exception {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody("Person added to event successfully"));
        apiService.addPersonToEvent(101, 202).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                assertTrue("Adding person to event should be successful", response.isSuccessful());
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                fail("Request failed: " + t.getMessage());
            }
        });
    }

    @Test
    public void testGetPeople() throws Exception {
        String jsonResponse = "[{" +
                "\"personId\": 1," +
                "\"name\": \"Alice Smith\"," +
                "\"age\": 29" +
                "}, {" +
                "\"personId\": 2," +
                "\"name\": \"Bob Jones\"," +
                "\"age\": 35" +
                "}]";
        mockWebServer.enqueue(new MockResponse().setBody(jsonResponse).setResponseCode(200));
        apiService.getPeople().enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                assertTrue(response.isSuccessful());
                assertNotNull(response.body());
                assertEquals(2, response.body().size());
                assertEquals("Alice Smith", response.body().get(0).getName());
                assertEquals(29, response.body().get(0).getAge());
                assertEquals("Bob Jones", response.body().get(1).getName());
                assertEquals(35, response.body().get(1).getAge());
            }
            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                fail("Request failed: " + t.getMessage());
            }
        });
    }

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();  // Stop the MockWebServer after tests to clean up resources
    }
}