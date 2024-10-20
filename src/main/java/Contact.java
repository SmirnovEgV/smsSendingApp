import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Contact {
    private String name;
    private String phoneNumber;
    private String carrier; // Added carrier field
    private LocalDate reminderDate;
    private boolean selected;

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public Contact(String name, String phoneNumber, String carrier, String reminderDate) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.carrier = carrier; // Initialize the carrier
        this.reminderDate = LocalDate.parse(reminderDate, dateFormatter);
        this.selected = false;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCarrier() {
        return carrier; // Getter for the carrier
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public boolean isSelected() {
        return selected;
    }

    public void toggleSelected() {
        this.selected = !this.selected;
    }

    // Returns the formatted reminder date as a string
    public String getFormattedReminderDate() {
        return reminderDate.format(dateFormatter);
    }

    @Override
    public String toString() {
        return name + " (" + phoneNumber + ", " + carrier + ") - Reminder Date: " + reminderDate.format(dateFormatter)
                + (selected ? " [Selected]" : " [Not selected]");
    }
}
