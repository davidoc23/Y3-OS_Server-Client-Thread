package Server;

import java.net.Socket;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ServerThread extends Thread 
{

    // Socket for communication
    Socket myConnection;
    // Output and input streams for communication with the client
    ObjectOutputStream out;
    ObjectInputStream in;
    // Message received from the client
    String message;

    // Flag to track login status
    private boolean loggedIn = false;

    // Map to store user data (for simplicity, data is stored in-memory)
    private static Map<String, User> userDatabase = new HashMap<>();

    // Constructor
    public ServerThread(Socket s) 
    {
        myConnection = s;
    }

    // Run method for the thread
    public void run() 
    {
        try 
        {
            // Initialize output and input streams
            out = new ObjectOutputStream(myConnection.getOutputStream());
            out.flush();
            in = new ObjectInputStream(myConnection.getInputStream());

            do 
            {
                // Display menu based on login status
                if (!loggedIn) 
                {
                    // Only show options 1 and 2
                    sendMessage("Please select an option\n 1: Register\n 2: Log in\n -1 to EXIT.");
                }
                else 
                {
                    // Show the full menu
                    sendMessage("Please select an option\n 1: Register\n 2: Log in\n 3: Lodge Money\n 4: Retrieve Users\n 5: Transfer Money\n 6: View Transactions\n 7: Update Password\n -1 to EXIT.");
                }

                // Read the selected option from the client
                message = (String) in.readObject();
                int option = Integer.parseInt(message);

                // Perform actions based on the selected option
                switch (option) 
                {
                    case 1:
                        registerUser();
                        break;
                        
                    case 2:
                        loginUser();
                        break;
                        
                    case 3:
                        lodgeMoney();
                        break;
                        
                    case 4:
                        retrieveUsers();
                        break;
                        
                    case 5:
                        transferMoney();
                        break;
                        
                    case 6:
                        viewTransactions();
                        break;
                        
                    case 7:
                        updatePassword();
                        break;
                        
                    case -1:
                        sendMessage("Closing connection.");
                        break;
                        
                    default:
                        sendMessage("Invalid option.");
                        break;
                        
                }

            } while (!message.equals("-1"));

            // Close streams
            in.close();
            out.close();
        } 
        catch (ClassNotFoundException classnot) 
        {
            System.err.println("Data received in an unknown format");
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    // Method to handle user registration
    private void registerUser() 
    {
        try
        {
            sendMessage("Enter Name:");
            String name = (String) in.readObject();
            System.out.println("Name Received: " + name);

            sendMessage("Enter PPS Number :");
            String ppsNumber = (String) in.readObject();
            System.out.println("PPS Received: " + ppsNumber);

            sendMessage("Enter Email:");
            String email = (String) in.readObject();
            System.out.println("Email Received: " + email);

            sendMessage("Enter Password:");
            String password = (String) in.readObject();
            System.out.println("Password Received: " + password);

            sendMessage("Enter Address:");
            String address = (String) in.readObject();
            System.out.println("Address Received: " + address);

            sendMessage("Enter Initial Balance:");
            double initialBalance = Double.parseDouble((String) in.readObject());
            System.out.println("Balance Received: " + initialBalance);

            // Check if the user already exists
            if (userDatabase.containsKey(ppsNumber) || emailExists(email)) 
            {
                sendMessage("Registration failed. PPS Number or Email already exists.");
            }
            else
            {
                // Create a new user and add it to the database
                User newUser = new User(name, ppsNumber, email, password, address, initialBalance);
                userDatabase.put(ppsNumber, newUser);
                sendMessage("Registration successful.");
            }

        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
    }

    // Method to check if an email already exists in the database
    private boolean emailExists(String email) 
    {
        return userDatabase.values().stream().anyMatch(user -> user.getEmail().equals(email));
    }

    // Method to handle user login
    private void loginUser() 
    {
        boolean loginSuccess = false;

        while (!loginSuccess)
        {
            try 
            {
                sendMessage("Enter Email:");
                String email = (String) in.readObject();

                sendMessage("Enter Password:");
                String password = (String) in.readObject();

                // Get the user with the provided email
                User user = getUserByEmail(email);

                // Check if the login is successful
                if (user != null && user.getPassword().equals(password)) 
                {
                    sendMessage("Login successful.");
                    loggedIn = true; // Update login status
                    loginSuccess = true; // Exit the loop after successful login
                }
                else
                {
                    sendMessage("Login failed. Incorrect email or password. Please try again.");
                }
                
            }
            catch (IOException | ClassNotFoundException e) 
            {
                e.printStackTrace();
            }
        }
    }

    // Method to handle lodging money into a user's account
    private void lodgeMoney() 
    {
        try
        {
            sendMessage("Enter PPS Number:");
            String ppsNumber = (String) in.readObject();

            sendMessage("Enter amount to lodge:");
            String amountStr = (String) in.readObject();

            // Check if the amount string is not empty before attempting to parse
            if (!amountStr.isEmpty()) 
            {
                double amount = Double.parseDouble(amountStr);
                User user = userDatabase.get(ppsNumber);

                // Check if the user exists
                if (user != null) 
                {
                    user.lodgeMoney(amount);
                    sendMessage("Money lodged successfully. New balance: " + user.getBalance());
                }
                else
                {
                    sendMessage("\n\nUser not found. Please check user details or add a new user!!\n");
                }
                
            } 
            else
            {
                sendMessage("Invalid amount format. Please enter a valid amount.");
            }
            
            // Ask the user if they want to return to the main menu
            sendMessage("Do you want to return to the main menu? (Y/N)");
            String choice = (String) in.readObject();

            if ("Y".equalsIgnoreCase(choice)) 
            {
                sendMessage("Returning to the main menu.");
                return;
            } 
            else
            {
                sendMessage("Returning to the main menu.");
                return;
            }
            
        } 
        catch (IOException | ClassNotFoundException | NumberFormatException e) 
        {
            e.printStackTrace();
        }
    }

    // Method to retrieve and display all registered users
    private void retrieveUsers() 
    {
        if (userDatabase.isEmpty())
        {
            System.err.println("\n\nNo users in the database, please add one first!\n");
        }
        else
        {
            StringBuilder userList = new StringBuilder("Registered Users:\n");

            for (User user : userDatabase.values()) 
            {
                userList.append(user.toString()).append("\n");
            }

            sendMessage(userList.toString());
        }
    }

    // Method to handle transferring money between users
    private void transferMoney() 
    {
        try 
        {
            sendMessage("Enter your PPS Number:");
            String senderPPS = (String) in.readObject();

            sendMessage("Enter your Email:");
            String senderEmail = (String) in.readObject();

            sendMessage("Enter recipient's Email:");
            String recipientEmail = (String) in.readObject();

            sendMessage("Enter amount to transfer:");
            double amount = Double.parseDouble((String) in.readObject());

            // Get the sender and recipient users
            User sender = getUserByPPSAndEmail(senderPPS, senderEmail);
            User recipient = getUserByEmail(recipientEmail);

            if (sender != null && recipient != null) 
            {
                // Attempt to transfer money between users
                if (sender.transferMoney(recipient, amount)) 
                {
                    sendMessage("Transfer successful. New balance: " + sender.getBalance());
                }
                else 
                {
                    sendMessage("Insufficient funds for transfer or incorrect sender details.");
                }
            } 
            else 
            {
                sendMessage("Sender or recipient not found or incorrect sender details.");
            }
            
        	// Ask the user if they want to return to the main menu
            sendMessage("Do you want to return to the main menu? (Y/N)");
            String choice = (String) in.readObject();

            if ("Y".equalsIgnoreCase(choice)) 
            {
                sendMessage("Returning to the main menu.");
                return;
            } 
            else
            {
                sendMessage("Returning to the main menu.");
                return;
            }
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
    }

    // Method to get a user by PPS number and email
    private User getUserByPPSAndEmail(String ppsNumber, String email) 
    {
        return userDatabase.values().stream()
                .filter(user -> user.getPpsNumber().equals(ppsNumber) && user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    // Method to get a user by email
    private User getUserByEmail(String email) 
    {
        return userDatabase.values().stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }

    // Method to view a user's transaction history
    private void viewTransactions() 
    {
        try 
        {
            sendMessage("Enter PPS Number:");
            String ppsNumber = (String) in.readObject();

            User user = userDatabase.get(ppsNumber);

            if (user != null) 
            {
                // Check if the user has any transactions
                if (user.getTransactions().isEmpty()) 
                {
                    sendMessage("No transactions found.");
                }
                else
                {
                    sendMessage("Transaction history:\n" + String.join("\n", user.getTransactions()));
                }
                
            } 
            else
            {
                sendMessage("User not found.");
                return; // Exit the method and return to the main menu
            }

            // Ask the user if they want to return to the main menu
            sendMessage("Do you want to return to the main menu? (Y/N)");
            String choice = (String) in.readObject();

            if ("Y".equalsIgnoreCase(choice)) 
            {
                sendMessage("Returning to the main menu.");
                return;
            }
            else
            {
                sendMessage("Returning to the main menu.");
                return;
            }

        }
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
    }

    // Method to update a user's password
    private void updatePassword() 
    {
        try
        {
            sendMessage("Enter PPS Number:");
            String ppsNumber = (String) in.readObject();

            sendMessage("Enter current password:");
            String currentPassword = (String) in.readObject();

            sendMessage("Enter new password:");
            String newPassword = (String) in.readObject();

            // Get the user by PPS number
            User user = userDatabase.get(ppsNumber);

            if (user != null && user.getPassword().equals(currentPassword)) 
            {
                // Update the user's password
                user.setPassword(newPassword);
                sendMessage("Password updated successfully.");
            }
            else
            {
                sendMessage("Password update failed. Incorrect current password.");
            }

            // Ask the user if they want to return to the main menu
            sendMessage("Do you want to return to the main menu? (Y/N)");
            String choice = (String) in.readObject();

            if ("Y".equalsIgnoreCase(choice)) 
            {
                sendMessage("Returning to the main menu.");
            }
            else 
            {
                sendMessage("Returning to the main menu.");
            }
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
    }

    // Method to send a message to the client
    void sendMessage(String msg) 
    {
        try
        {
            out.writeObject("server> " + msg);
            out.flush();
            System.out.println("server>" + msg);
        }
        catch (IOException ioException) 
        {
            ioException.printStackTrace();
        }
    }
}



