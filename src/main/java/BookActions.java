import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BookActions {

    private List<Contact> contacts;
    private List<Contact> selectedContacts;
    private static final String CSV_FILE = "phonebook.csv";
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    final String fromEmail = "YOUR_MAIL_GOES_HERE";  // Your Gmail address, no tricks and things use the address you want to send messages from
    final String password = "YOUR_STUFF_GOES_HERE";  // Generate an App password or access token, can be done at google account settings (or just search google)

    public BookActions() {
        this.contacts = new ArrayList<>();
        this.selectedContacts = new ArrayList<>();
    }

    // Creates the CSV if not found or loads it if exists
    public void createOrLoadCSV() {
        Path path = Paths.get(CSV_FILE);
        if (!Files.exists(path)) {
            System.out.println("CSV file not found, creating one...");
            generateSampleCSV();
        }
        loadContactsFromCSV();
    }

    // Generates a sample CSV file
    private void generateSampleCSV() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_FILE))) {
            writer.write("Name,PhoneNumber,Carrier,ReminderDate\n");
            writer.write("John Doe,+15551234567,vtext.com,10/17/2024\n");  // Verizon
            writer.write("Jane Smith,+15559876543,tmomail.net,10/18/2024\n");  // T-Mobile
            writer.write("Viktor Smirnov,+12383575281,tmomail.net,10/19/2024\n");  // AT&T
            System.out.println("Sample CSV created.");
        } catch (IOException e) {
            System.out.println("Error creating CSV: " + e.getMessage());
        }
    }

    // Loads contacts from the CSV file into the list
    private void loadContactsFromCSV() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(CSV_FILE))) {
            contacts = reader.lines()
                    .skip(1)  // Skip header
                    .map(line -> {
                        String[] parts = line.split(",");
                        return new Contact(parts[0], parts[1], parts[2], parts[3]);
                    })
                    .collect(Collectors.toList());
            System.out.println("Contacts loaded from CSV.");
        } catch (IOException e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }
    }

    // CLI option to select numbers for SMS
    public void selectNumbers(Scanner scanner) {
        System.out.println("\nSelect numbers for SMS (type number to select):");

        for (int i = 0; i < contacts.size(); i++) {
            Contact contact = contacts.get(i);
            String status = contact.isSelected() ? "[Selected]" : "[Not selected]";
            System.out.printf("%d. %s%n", i + 1, contact.toString());
        }

        System.out.println("\nType the number of the contact to select/deselect (or press 'q' to go back):");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("q")) {
                break;
            }
            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < contacts.size()) {
                    Contact contact = contacts.get(index);
                    contact.toggleSelected();
                    System.out.printf("%s%n", contact.toString());
                } else {
                    System.out.println("Invalid number. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please type a number.");
            }
        }
    }

    // CLI option to send SMS (pass selected contacts to another function)
    public void sendSms() {
        selectedContacts = contacts.stream()
                .filter(Contact::isSelected)
                .collect(Collectors.toList());

        if (selectedContacts.isEmpty()) {
            System.out.println("No contacts selected.");
        } else {
            System.out.println("Sending SMS to selected contacts:");
            for (Contact contact : selectedContacts) {
                System.out.printf("Sending to %s%n", contact.toString());
            }
            performSmsSending(selectedContacts);
        }
    }

    // Sends SMS messages using Gmail
    private void performSmsSending(List<Contact> selectedContacts) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        for (Contact contact : selectedContacts) {
            try {
                String toEmail = contact.getPhoneNumber() + "@" + contact.getCarrier();  // e.g., 1234567890@vtext.com
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject("SMS Notification");
                message.setText("Some random text or whatever, gmail works tho");

                Transport.send(message);
                System.out.printf("SMS sent to %s at %s%n", contact.getName(), toEmail);
            } catch (MessagingException e) {
                System.out.printf("Failed to send SMS to %s: %s%n", contact.getName(), e.getMessage());
            }
        }
    }

    // Check for reminders due tomorrow and send SMS
    public void checkForReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Contact> reminderContacts = contacts.stream()
                .filter(contact -> contact.getReminderDate().isEqual(tomorrow))
                .collect(Collectors.toList());

        if (reminderContacts.isEmpty()) {
            System.out.println("No reminders for tomorrow.");
        } else {
            System.out.println("Reminder: The following contacts need a reminder tomorrow:");
            for (Contact contact : reminderContacts) {
                System.out.printf("Reminder for %s%n", contact.toString());
                performSmsSending(Collections.singletonList(contact));
            }
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
