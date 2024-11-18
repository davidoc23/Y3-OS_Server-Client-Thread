package Server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable 
{
    // User attributes
    private String name;
    private String ppsNumber;
    private String email;
    private String password;
    private String address;
    private double balance;
    private List<String> transactions;

    // Constructor to initialize a user with basic information and an initial balance
    public User(String name, String ppsNumber, String email, String password, String address, double initialBalance) 
    {
        this.name = name;
        this.ppsNumber = ppsNumber;
        this.email = email;
        this.password = password;
        this.address = address;
        this.balance = initialBalance;
        this.transactions = new ArrayList<>();
    }

    // Getter methods for accessing user attributes
    public String getName() 
    {
        return name;
    }

    public String getPpsNumber() 
    {
        return ppsNumber;
    }

    public String getEmail() 
    {
        return email;
    }

    public String getPassword() 
    {
        return password;
    }

    // Setter method to update the user's password
    public void setPassword(String newPassword) 
    {
        this.password = newPassword;
    }

    public String getAddress() 
    {
        return address;
    }

    public double getBalance() 
    {
        return balance;
    }

    public List<String> getTransactions() 
    {
        return transactions;
    }

    // Method to deposit money into the user's account
    public void lodgeMoney(double amount) 
    {
        balance += amount;
        transactions.add("Lodged " + amount + " EUR");
    }

    // Method to transfer money from this user to another user
    public boolean transferMoney(User recipient, double amount) 
    {
        if (balance >= amount) 
        {
            balance -= amount;
            recipient.lodgeMoney(amount);

            // Recording the transaction details for both sender and recipient
            transactions.add("Transferred " + amount + " EUR to " + recipient.getName());
            recipient.getTransactions().add("Received " + amount + " EUR from " + this.getName());

            return true;
        } 
        else 
        {
            return false; // Insufficient funds for the transfer
        }
    }

    // Override toString() method to provide a string representation of the User object
    @Override
    public String toString() 
    {
        return "User{" +
                "name='" + name + '\'' +
                ", ppsNumber='" + ppsNumber + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                '}';
    }
}
