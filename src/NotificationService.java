import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService {

    // Check if any event is due soon and remind the user
    public static void checkForReminders(ArrayList<Event> events) {
        Timer timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String currentTime = new SimpleDateFormat("HH:mm").format(new Date());

                for (Event event : events) {
                    if (currentTime.equals(event.getReminderTime())) {
                        sendNotification(event);
                    }
                }
            }
        }, 0, 60000);  // Check every minute
    }

    // Mock notification method
    public static void sendNotification(Event event) {
        System.out.println("Reminder: Event \"" + event.getTitle() + "\" is coming up at " + event.getEventDate());
    }
}
