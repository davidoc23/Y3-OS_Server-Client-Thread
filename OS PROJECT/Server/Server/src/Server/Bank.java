package Server;

import java.util.HashMap;
import java.util.Map;

public class Bank 
{
    // Map to store users with their PPS number as the key
    private Map<String, User> userDatabase;

    // Constructor initializes the user database as a HashMap
    public Bank() 
    {
        this.userDatabase = new HashMap<>();
    }

    // Adds a new user to the database if the PPS number and email do not already exist
    public boolean addUser(User user) 
    {
        if (!userDatabase.containsKey(user.getPpsNumber()) && !emailExists(user.getEmail())) 
        {
            userDatabase.put(user.getPpsNumber(), user);
            return true;
        } 
        else 
        {
            return false;
        }
    }

    // Logs in a user by checking the provided email and password
    public boolean loginUser(String email, String password) 
    {
        User user = getUserByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    // Retrieves a user based on their email
    public User getUserByEmail(String email) 
    {
        return userDatabase.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Deposits money into a user's account based on their PPS number
    public void lodgeMoney(String ppsNumber, double amount) 
    {
        User user = userDatabase.get(ppsNumber);
        if (user != null) 
        {
            user.lodgeMoney(amount);
        }
    }

    // Transfers money from one user to another based on their PPS numbers and email
    public boolean transferMoney(String senderPPS, String recipientEmail, double amount) 
    {
        User sender = userDatabase.get(senderPPS);
        User recipient = getUserByEmail(recipientEmail);

        if (sender != null && recipient != null) 
        {
            return sender.transferMoney(recipient, amount);
        } 
        else 
        {
            return false;
        }
    }

    // Retrieves a user's transaction history based on their PPS number
    public String viewTransactions(String ppsNumber) 
    {
        User user = userDatabase.get(ppsNumber);
        return (user != null) ? String.join("\n", user.getTransactions()) : "User not found.";
    }

    // Updates a user's password based on their PPS number and current password
    public void updatePassword(String ppsNumber, String currentPassword, String newPassword) 
    {
        User user = userDatabase.get(ppsNumber);
        
        if (user != null && user.getPassword().equals(currentPassword)) 
        {
            user.setPassword(newPassword);
        }
    }

    // Returns a string containing details of all registered users
    public String getAllUsers() 
    {
        StringBuilder userList = new StringBuilder("Registered Users:\n");
        userDatabase.values().forEach(user -> userList.append(user.toString()).append("\n"));
        return userList.toString();
    }

    // Private helper method to check if an email already exists in the user database
    private boolean emailExists(String email) 
    {
        return userDatabase.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }
}
