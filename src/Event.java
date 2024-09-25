public class Event {
    private int id;  // Event ID
    private String title;
    private String description;
    private String eventDate;
    private String reminderTime;

    // Constructor with ID
    public Event(int id, String title, String description, String eventDate, String reminderTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.reminderTime = reminderTime;
    }

    // Constructor without ID (for new events)
    public Event(String title, String description, String eventDate, String reminderTime) {
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.reminderTime = reminderTime;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    // Setters (already added previously)
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }
}
