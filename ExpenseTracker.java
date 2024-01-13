import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.List;

class Expense {
    private String date;
    private String category;
    private double amount;

    public Expense(String date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    // Getters and setters (omitted for brevity)

    @Override
    public String toString() {
        return "Date: " + date + ", Category: " + category + ", Amount: $" + amount;
    }
}
        class User implements Serializable {
        private String username;
        private String password;
        private List<Expense> expenses;

        public User(String username, String password) {
            this.username = username;
            this.password = password;
            this.expenses = new ArrayList<>();
        }

        public String getPassword() {
            return password;
        }

        // Other getters and setters (omitted for brevity)

        public void addExpense(Expense expense) {
            expenses.add(expense);
        }

        public List<Expense> getExpenses() {
            return expenses;
        }
    }
public class ExpenseTracker {
    private static final String FILE_PATH = "expense_data.txt";
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser;

    public static void main(String[] args) {
        loadUserData();
        showMenu();
    }

    private static void showMenu() {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nExpense Tracker Menu:");
            System.out.println("1. Register");
            System.out.println("2. Log In");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    break;
                case 3:
                    saveUserData();
                    System.out.println("Exiting Expense Tracker. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);

        scanner.close();
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        // Check if the username is already taken
        if (users.containsKey(username)) {
            System.out.println("Username already exists. Please choose another one.");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User newUser = new User(username, password);
        users.put(username, newUser);
        currentUser = newUser;

        System.out.println("Registration successful. Welcome, " + username + "!");
        expenseTrackerOptions(scanner);
    }

    private static void loginUser(Scanner scanner) {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User user = users.get(username);

        // Check if the user exists and the password is correct
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            System.out.println("Login successful. Welcome back, " + username + "!");
            expenseTrackerOptions(scanner);
        } else {
            System.out.println("Invalid username or password. Please try again.");
        }
    }

    private static void expenseTrackerOptions(Scanner scanner) {
        int choice;

        do {
            System.out.println("\nExpense Tracker Options:");
            System.out.println("1. Add Expense");
            System.out.println("2. View Expenses");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addExpense(scanner);
                    break;
                case 2:
                    viewExpenses();
                    break;
                case 3:
                    saveUserData();
                    currentUser = null;
                    System.out.println("Logged out successfully.");
                    showMenu();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 3);
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter date (MM/DD/YYYY): ");
        String date = scanner.nextLine();

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter amount: $");
        double amount = scanner.nextDouble();

        Expense expense = new Expense(date, category, amount);
        currentUser.addExpense(expense);

        System.out.println("Expense added successfully.");
    }

    private static void viewExpenses() {
        List<Expense> expenses = currentUser.getExpenses();

        if (expenses.isEmpty()) {
            System.out.println("No expenses to display.");
        } else {
            System.out.println("\nExpense List:");
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
        }
    }

    private static void loadUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            users = (Map<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Handle exceptions (e.g., file not found)
        }
    }

    private static void saveUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            // Handle exceptions (e.g., cannot write to file)
        }
    }
}
