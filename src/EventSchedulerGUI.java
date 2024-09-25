import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class EventSchedulerGUI extends JFrame {

    private final JTextField titleField, descriptionField, dateField, reminderField;
    private final JTable eventTable;
    private final DefaultTableModel tableModel;
    private int selectedRow = -1;  // Tracks selected row
    private final Timer reminderTimer;  // Using java.util.Timer explicitly
    private static int reminderInterval = 30;  // Global reminder interval (default 30 minutes)

    public EventSchedulerGUI() {
        // Set up layout
        setLayout(new BorderLayout());

        // Top panel (input fields)
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Title:"), gbc);
        titleField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Description:"), gbc);
        descriptionField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        dateField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Reminder (HH:MM):"), gbc);
        reminderField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(reminderField, gbc);

        // Button to open reminder interval setting window
        gbc.gridx = 1;
        gbc.gridy = 4;
        JButton setReminderIntervalButton = new JButton("Set Reminder Interval");
        inputPanel.add(setReminderIntervalButton, gbc);
        setReminderIntervalButton.addActionListener(e -> openReminderIntervalWindow());

        // Buttons for adding/updating events
        gbc.gridx = 1;
        gbc.gridy = 5;
        JButton addButton = new JButton("Add/Update Event");
        inputPanel.add(addButton, gbc);
        addButton.addActionListener(e -> addOrUpdateEvent());

        // Delete event button
        gbc.gridy = 6;
        JButton deleteButton = new JButton("Delete Event");
        inputPanel.add(deleteButton, gbc);
        deleteButton.addActionListener(e -> deleteEvent());

        add(inputPanel, BorderLayout.NORTH);

        // Event table (without the ID column)
        String[] columnNames = {"Title", "Description", "Date", "Reminder"};
        tableModel = new DefaultTableModel(columnNames, 0);
        eventTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);

                // Highlight the selected row
                if (isRowSelected(row)) {
                    component.setBackground(Color.YELLOW);  // Selected row is highlighted
                } else {
                    String eventDate = (String) getValueAt(row, 2);  // Get event date
                    String reminderTime = (String) getValueAt(row, 3);  // Get reminder time
                    Date now = new Date();
                    SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    try {
                        Date eventDateTime = dateTimeFormat.parse(eventDate + " " + reminderTime);

                        // Mark past events as gray
                        if (eventDateTime.before(now)) {
                            component.setBackground(Color.LIGHT_GRAY);  // Past event row is gray
                        } else {
                            component.setBackground(Color.WHITE);  // Future event row is white
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                return component;
            }
        };

        // Enable row selection and disable column selection
        eventTable.setRowSelectionAllowed(true);   // Enable row selection
        eventTable.setColumnSelectionAllowed(false); // Disable column selection
        eventTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only one row to be selected at a time

        eventTable.getSelectionModel().addListSelectionListener(e -> loadEventForEdit());

        JScrollPane scrollPane = new JScrollPane(eventTable);
        add(scrollPane, BorderLayout.CENTER);

        setTitle("Event Scheduler");
        setSize(600, 400);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the reminder timer
        reminderTimer = new Timer();  // Using java.util.Timer explicitly
        startReminderCheck();

        // Load events
        loadAllEvents();
    }

    // Custom cell renderer to color past event rows in gray
    private class CustomEventCellRenderer extends DefaultTableCellRenderer {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String eventDate = (String) table.getValueAt(row, 2);  // Get event date
            String reminderTime = (String) table.getValueAt(row, 3);  // Get reminder time
            Date now = new Date();

            try {
                Date eventDateTime = dateTimeFormat.parse(eventDate + " " + reminderTime);

                if (eventDateTime.before(now)) {
                    cellComponent.setBackground(Color.LIGHT_GRAY);  // Past events are marked gray
                } else {
                    cellComponent.setBackground(Color.WHITE);  // Future or current events remain white
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return cellComponent;
        }
    }

    // Load all events from DB into table and sort them by date
    private void loadAllEvents() {
        tableModel.setRowCount(0);  // Clear the table

        ArrayList<Event> events = EventManager.getAllEvents();
        
        // Sort events by event date and time
        Collections.sort(events, new Comparator<Event>() {
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            @Override
            public int compare(Event e1, Event e2) {
                try {
                    Date date1 = dateTimeFormat.parse(e1.getEventDate() + " " + e1.getReminderTime());
                    Date date2 = dateTimeFormat.parse(e2.getEventDate() + " " + e2.getReminderTime());
                    return date1.compareTo(date2);  // Sort in ascending order (earliest first)
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;  // If there's a parsing error, keep the order unchanged
                }
            }
        });

        // Add sorted events to the table
        for (Event event : events) {
            Object[] rowData = {
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getReminderTime()
            };
            tableModel.addRow(rowData);
        }
    }

    // Add or update event
    private void addOrUpdateEvent() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();
        String eventDate = dateField.getText().trim();
        String reminderTime = reminderField.getText().trim();

        // Validate inputs
        if (title.isEmpty() || description.isEmpty() || eventDate.isEmpty() || reminderTime.isEmpty()) {
            showError("All fields are required.");
            clearFields();  // Clear input fields
            return;
        }

        if (!isValidDate(eventDate)) {
            showError("Invalid date format! Please enter date as YYYY-MM-DD.");
            clearFields();  // Clear input fields
            return;
        }

        if (!isValidTime(reminderTime)) {
            showError("Invalid time format! Please enter time as HH:MM.");
            clearFields();  // Clear input fields
            return;
        }

        if (selectedRow == -1) {
            // Add new event to the list
            Event newEvent = new Event(title, description, eventDate, reminderTime);
            EventManager.addEvent(newEvent);  // Add to the event list
        } else {
            // Get the selected event from the list using the selectedRow index
            Event eventToUpdate = EventManager.getAllEvents().get(selectedRow);

            // Update the event's fields
            eventToUpdate.setTitle(title);
            eventToUpdate.setDescription(description);
            eventToUpdate.setEventDate(eventDate);
            eventToUpdate.setReminderTime(reminderTime);

            // Update the event in the database
            EventManager.updateEvent(eventToUpdate);  // This should update the correct event in the database
        }

        // Clear the input fields and reset the selectedRow
        clearFields();
        selectedRow = -1;

        // Reload the sorted events into the table
        loadAllEvents();  // Re-sort and reload all events
    }

    // Delete selected event
    private void deleteEvent() {
        selectedRow = eventTable.getSelectedRow();  // Get the selected row index
        if (selectedRow != -1) {
            Event event = EventManager.getAllEvents().get(selectedRow);  // Get the event by row
            EventManager.deleteEvent(event);  // Delete from the database

            // Remove from the table model
            tableModel.removeRow(selectedRow);

            // Reset selectedRow after deletion
            selectedRow = -1;

            // Clear input fields
            clearFields();

            // Reload events to ensure the table is in sync
            loadAllEvents();
        } else {
            showError("Please select an event to delete.");
        }
    }

    // Load selected event for editing
    private void loadEventForEdit() {
        selectedRow = eventTable.getSelectedRow();  // Get the selected row index
        if (selectedRow != -1) {
            // Ensure we are getting the correct row from the table and the corresponding event
            String selectedTitle = (String) tableModel.getValueAt(selectedRow, 0);
            String selectedDescription = (String) tableModel.getValueAt(selectedRow, 1);
            String selectedDate = (String) tableModel.getValueAt(selectedRow, 2);
            String selectedReminder = (String) tableModel.getValueAt(selectedRow, 3);

            // Populate the input fields with the values from the selected row
            titleField.setText(selectedTitle);
            descriptionField.setText(selectedDescription);
            dateField.setText(selectedDate);
            reminderField.setText(selectedReminder);
        }
    }

    // Clear input fields after add/update or error
    private void clearFields() {
        titleField.setText("");
        descriptionField.setText("");
        dateField.setText("");
        reminderField.setText("");
        selectedRow = -1;  // Reset selection
    }

    // Show error messages with JOptionPane
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    // Validate the date format (YYYY-MM-DD)
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);  // Strict date parsing
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Validate the time format (HH:MM)
    private boolean isValidTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);  // Strict time parsing
        try {
            sdf.parse(time);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    // Start a background thread to check for reminders every minute
    private void startReminderCheck() {
        reminderTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkForUpcomingEvents();
            }
        }, 0, 60000);  // Check every 60 seconds (1 minute)
    }

    // Check for events that are within the user-defined interval of their reminder time
    private void checkForUpcomingEvents() {
        ArrayList<Event> events = EventManager.getAllEvents();
        Date now = new Date();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (Event event : events) {
            try {
                // Parse the event date and reminder time
                Date eventTime = dateTimeFormat.parse(event.getEventDate() + " " + event.getReminderTime());

                // Check if the event is within the specified reminder interval
                long timeDifference = (eventTime.getTime() - now.getTime()) / (60 * 1000);  // Difference in minutes
                if (timeDifference > 0 && timeDifference <= reminderInterval) {
                    showReminder(event);  // Show reminder if within user-defined interval
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    // Show reminder popup for the specified event
    private void showReminder(Event event) {
        JOptionPane.showMessageDialog(this, "Reminder: Event \"" + event.getTitle() + "\" is happening soon!", "Event Reminder", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to open the reminder interval window
    private void openReminderIntervalWindow() {
        JDialog reminderIntervalDialog = new JDialog(this, "Set Reminder Interval", true);
        reminderIntervalDialog.setSize(300, 150);
        reminderIntervalDialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Reminder Interval (1-60 min):"), gbc);

        JTextField reminderIntervalField = new JTextField(String.valueOf(reminderInterval), 10);  // Show current interval
        gbc.gridx = 1;
        panel.add(reminderIntervalField, gbc);

        JButton saveButton = new JButton("Save");
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(saveButton, gbc);

        // Save the reminder interval when the button is clicked
        saveButton.addActionListener(e -> {
            try {
                int interval = Integer.parseInt(reminderIntervalField.getText().trim());
                if (interval < 1 || interval > 60) {
                    showError("Reminder interval must be between 1 and 60 minutes.");
                } else {
                    reminderInterval = interval;  // Set the global reminder interval
                    reminderIntervalDialog.dispose();  // Close the window
                }
            } catch (NumberFormatException ex) {
                showError("Invalid reminder interval! Please enter a number between 1 and 60.");
            }
        });

        reminderIntervalDialog.add(panel, BorderLayout.CENTER);
        reminderIntervalDialog.setVisible(true);
    }

    public static void main(String[] args) {
        new EventSchedulerGUI();
    }
}
