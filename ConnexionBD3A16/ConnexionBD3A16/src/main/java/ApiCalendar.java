import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;

public class ApiCalendar {

    private static final String CLIENT_ID = "284082355366-ad9hmnpm5n9uidunp1uv4qua57759ott.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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";
    private static final String REDIRECT_URI = "YOUR_REDIRECT_URI";

    public static void main(String[] args) throws Exception {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        // Set up OAuth 2.0 flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Collections.singleton("https://www.googleapis.com/auth/calendar"))
                .setAccessType("offline").build();

        // Authenticate and authorize
        String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        System.out.println("Please open the following URL in your browser then type the authorization code:");
        System.out.println(url);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine();

        GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
        Credential credential = flow.createAndStoreCredential(response, null);

        // Set up the Calendar service
        Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("YourAppName").build();

        // Example: Create a new event
        Event event = new Event();
        event.setSummary("Sample Event");
        event.setDescription("This is a sample event");

        // Set the start and end time (you can customize this)
        DateTime startDateTime = new DateTime("2022-02-22T10:00:00");
        EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("UTC");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2022-02-22T12:00:00");
        EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("UTC");
        event.setEnd(end);

        // Insert the event
        Event createdEvent = service.events().insert("primary", event).execute();

        System.out.println("Event created: " + createdEvent.getHtmlLink());
    }
}
