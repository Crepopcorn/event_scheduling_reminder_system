import java.sql.*;
import java.util.ArrayList;

public class EventManager {

    // Add a new event to the database
    public static void addEvent(Event event) {
        String query = "INSERT INTO events (title, description, event_date, reminder_time) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getEventDate());
            pstmt.setString(4, event.getReminderTime());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get all events from the database
    public static ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        String query = "SELECT * FROM events";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String eventDate = rs.getString("event_date");
                String reminderTime = rs.getString("reminder_time");

                Event event = new Event(id, title, description, eventDate, reminderTime);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    // Update an existing event in the database
    public static void updateEvent(Event event) {
        String query = "UPDATE events SET title = ?, description = ?, event_date = ?, reminder_time = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, event.getTitle());
            pstmt.setString(2, event.getDescription());
            pstmt.setString(3, event.getEventDate());
            pstmt.setString(4, event.getReminderTime());
            pstmt.setInt(5, event.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete an event from the database
    public static void deleteEvent(Event event) {
        String query = "DELETE FROM events WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, event.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
