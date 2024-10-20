import java.util.Scanner;

public class PhoneBook {

    private static BookActions bookActions = new BookActions();

    public static void main(String[] args) {
        bookActions.createOrLoadCSV();  // Ensure CSV is created or loaded

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Select numbers for SMS");
            System.out.println("2. Send SMS");
            System.out.println("3. Check for reminders");
            System.out.println("4. Exit");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    bookActions.selectNumbers(scanner);
                    break;
                case "2":
                    bookActions.sendSms();
                    break;
                case "3":
                    bookActions.checkForReminders();
                    break;
                case "4":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}
