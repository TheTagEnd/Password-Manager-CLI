import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Scanner;

public class PasswordManager {
    private HashMap<String, String> passwordMap;
    private SecureRandom random;

    public PasswordManager() {
        passwordMap = new HashMap<String, String>();
        random = new SecureRandom();
    }

    public void addPassword(String website, String username, String password) {
        passwordMap.put(website, username + ":" + password);
    }

    public void removePassword(String website) {
        passwordMap.remove(website);
    }

    public String getPassword(String website) {
        String savedPassword = passwordMap.get(website);
        if (savedPassword == null) {
            return null;
        }
        String[] parts = savedPassword.split(":");
        return parts[1];
    }

    public void generatePassword(String website, String username) {
        String symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{}|;:,.<>/?";
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            int index = random.nextInt(symbols.length());
            passwordBuilder.append(symbols.charAt(index));
        }
        String password = passwordBuilder.toString();
        addPassword(website, username, password);
        System.out.println("Generated password for " + website + ":\n" + password);
    }

    public void analyzePassword(String password) {
        int length = password.length();
        int uppercaseCount = 0;
        int lowercaseCount = 0;
        int digitCount = 0;
        int symbolCount = 0;
        for (int i = 0; i < length; i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                uppercaseCount++;
            } else if (Character.isLowerCase(c)) {
                lowercaseCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else {
                symbolCount++;
            }
        }
        System.out.println("Password analysis for " + password + ":");
        System.out.println("Length: " + length);
        System.out.println("Uppercase letters: " + uppercaseCount);
        System.out.println("Lowercase letters: " + lowercaseCount);
        System.out.println("Digits: " + digitCount);
        System.out.println("Symbols: " + symbolCount);
    }

    public void searchPasswords(String query) {
        System.out.println("Search results for \"" + query + "\":");
        for (String website : passwordMap.keySet()) {
            if (website.contains(query)) {
                String savedPassword = passwordMap.get(website);
                String[] parts = savedPassword.split(":");
                System.out.println(website + " - " + parts[0] + ":" + parts[1]);
            }
        }
    }
    public int ratePassword(String password) {
        int score = 0;
        int length = password.length();
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
    
        // Check password length and complexity
        if (length >= 8) {
            score += 2;
        }
        if (length >= 12) {
            score += 2;
        }
        if (password.matches(".*[A-Z].*")) {
            hasUppercase = true;
            score += 2;
        }
        if (password.matches(".*[a-z].*")) {
            hasLowercase = true;
            score += 2;
        }
        if (password.matches(".*\\d.*")) {
            hasDigit = true;
            score += 2;
        }
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            hasSymbol = true;
            score += 2;
        }
    
        // Check password uniqueness
        if (!passwordIsUnique(password)) {
            score -= 4;
        }
    
        return score;
    }
    
    public boolean passwordIsUnique(String password) {
        for (String savedPassword : passwordMap.values()) {
            if (savedPassword.equals(password)) {
                return false;
            }
        }
        return true;
    }
    public void enableTwoFactorAuth(String website) {
        if (passwordMap.containsKey(website)) {
            passwordMap.put(website, passwordMap.get(website) + "2fa");
        }
    }
   
            

    public static void main(String[] args) {
        PasswordManager pm = new PasswordManager();
        Scanner scanner = new Scanner(System.in);

        boolean quit = false;
        while (!quit) {
            System.out.println("Enter a command (add, remove, get, generate, analyze, search , enableTwoFactorAuth , quit):");
            String command = scanner.nextLine();

            switch (command) {
                case "add":
                    System.out.println("Enter website:");
                    String website = scanner.nextLine();
                    System.out.println("Enter username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();
                    pm.addPassword(website, username, password);
                    break;
                case "remove":
                    System.out.println("Enter website:");
                    website = scanner.nextLine();
                    pm.removePassword(website);
                    System.out.println("Password removed successfully!");
                    break;

                case "get":
                    System.out.println("Enter website:");
                    website = scanner.nextLine();
                    password = pm.getPassword(website);
                    if (password == null) {
                        System.out.println("No password found for " + website);
                    } else {
                        System.out.println("Password for " + website + ":\n" + password);
                    }
                    break;

                case "generate":
                    System.out.println("Enter website:");
                    website = scanner.nextLine();
                    System.out.println("Enter username:");
                    username = scanner.nextLine();
                    pm.generatePassword(website, username);
                    break;

                case "analyze":
                    System.out.println("Enter password:");
                    password = scanner.nextLine();
                    pm.analyzePassword(password);
                    break;

                case "search":
                    System.out.println("Enter search query:");
                    String query = scanner.nextLine();
                    pm.searchPasswords(query);
                    break;

                case "quit":
                    quit = true;
                    break;
                case "enableTwoFactorAuth":
                  System.out.println("Enter website");
                  website = scanner.nextLine();
                  pm.enableTwoFactorAuth(website);
                  break;

                default:
                    System.out.println("Invalid command!");
            }
        }
    }
}
