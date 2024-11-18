package Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Requester 
{
    Socket requestSocket;
    ObjectOutputStream out;
    ObjectInputStream in;
    String message;
    String response;
    Scanner input;
    boolean isLoggedIn = false;  // Keep track of login status, it will be false at the start until a user logs in.

    Requester() 
    {
        input = new Scanner(System.in);
    }

    void run() 
    {
        try 
        {
            // Establish a socket connection to the server on localhost and port 2004
            requestSocket = new Socket("127.0.0.1", 2004); // Adjust port number to match your server
            System.out.println("Connected to localhost in port 2004");

            // Initialize output stream and input stream for communication with the server
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(requestSocket.getInputStream());

            try 
            {
                // Main loop for client-server interaction
                do 
                {
                    // Read a message from the server and print it
                    message = (String) in.readObject();
                    System.out.println(message);
                    
                    // Get user input for response and send it to the server
                    response = input.nextLine();
                    sendMessage(response);

                    // Switch based on user response
                    switch (response) 
                    {
                        case "1":
                            // Register user if response is "1"
                            registerUser();
                            break;
                            
                        case "2":
                            // Login user if response is "2"
                            loginUser();
                            // Set the login status to true upon successful login
                            isLoggedIn = true;  
                            break;
                            
                        case "3": //Wont show until the user is logged in
                        case "4": //Wont show until the user is logged in
                        case "5": //Wont show until the user is logged in
                        case "6": //Wont show until the user is logged in
                        case "7": //Wont show until the user is logged in
                        	
                            // Display options 3-7 only if logged in
                            if (isLoggedIn) 
                            {
                                handleLoggedInOption(response);
                            } 
                            else
                            {
                                System.out.println("Please log in first.");
                            }
                            break;
                            
                        case "-1":
                            // Read and print the server's final message
                            message = (String) in.readObject();
                            System.out.println(message);
                            break;
                            
                        default:
                            // Read and print the server's message for invalid option
                            message = (String) in.readObject();
                            System.out.println(message);
                            break;
                            
                    }
                } 
                while (!response.equals("-1"));
            } 
            catch (ClassNotFoundException e) 
            {
                // Handle class not found exception
                e.printStackTrace();
            }
        } 
        catch (UnknownHostException unknownHost) 
        {
            // Handle the case when trying to connect to an unknown host
            System.err.println("You are trying to connect to an unknown host!");
        } 
        catch (IOException ioException) 
        {
            // Handle IO exceptions
            ioException.printStackTrace();
        } 
        finally 
        {
            try 
            {
                // Close input stream, output stream, and the socket in the finally block
                in.close();
                out.close();
                requestSocket.close();
            } 
            catch (IOException ioException) 
            {
                // Handle IO exceptions during closing
                ioException.printStackTrace();
            }
        }
    }
 
    // Method to handle user options when logged in
    void handleLoggedInOption(String option) 
    {
        // Switch statement to determine the action based on the user's chosen option
        switch (option) 
        {
            case "3":
                // Call the method to handle lodging money
                lodgeMoney();
                break;
                
            case "4":
                // Call the method to handle retrieving user information
                retrieveUsers();
                break;
                
            case "5":
                // Call the method to handle transferring money
                transferMoney();
                break;
                
            case "6":
                // Call the method to handle viewing transaction history
                viewTransactions();
                break;
                
            case "7":
                // Call the method to handle updating user password
                updatePassword();
                break;
                
            default:
                // If the user provides an invalid option, send a message to the server
                sendMessage("Invalid option.");
                break;
                
        }
    }

    // Method to handle user registration
    void registerUser() 
    {
        // Flag to track if registration is successful
        boolean registrationSuccess = false;

        // Loop until registration is successful
        while (!registrationSuccess) 
        {
            try 
            {
                // Read and print the instruction to enter name
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for name and send it to the server
                String name = input.nextLine();
                sendMessage(name);

                // Read and print the instruction to enter PPS Number
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for PPS Number and send it to the server
                String ppsNumber = input.nextLine();
                sendMessage(ppsNumber);

                // Read and print the instruction to enter email
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for email and send it to the server
                String email = input.nextLine();
                sendMessage(email);

                // Read and print the instruction to enter password
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for password and send it to the server
                String password = input.nextLine();
                sendMessage(password);

                // Read and print the instruction to enter address
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for address and send it to the server
                String address = input.nextLine();
                sendMessage(address);

                // Read and print the instruction to enter initial balance
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for initial balance as a String and send it to the server
                String initialBalanceStr = input.nextLine();
                sendMessage(initialBalanceStr);

                // Read and print the final message from the server
                message = (String) in.readObject();
                System.out.println(message);

                // Check if registration is successful
                registrationSuccess = message.contains("Registration successful");

                // If registration fails, print an error message and return
                if (!registrationSuccess) 
                {
                    System.out.println("Registration failed. Please try again.");
                    return;
                }

            } 
            catch (IOException | ClassNotFoundException e) 
            {
                // Handle IO or ClassNotFound exceptions
                e.printStackTrace();
            }
        }
    }

    // Method to handle user login
    void loginUser() 
    {
        // Flag to track if login is successful
        boolean loginSuccess = false;

        // Loop until login is successful
        while (!loginSuccess) 
        {
            try 
            {
                // Read and print the instruction to enter email
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for email and send it to the server
                String email = input.nextLine();
                sendMessage(email);

                // Read and print the instruction to enter password
                message = (String) in.readObject();
                System.out.println(message);

                // Get user input for password and send it to the server
                String password = input.nextLine();
                sendMessage(password);

                // Read and print the final message from the server
                message = (String) in.readObject();
                System.out.println(message);

                // Check if login is successful
                loginSuccess = message.contains("Login successful");
                
                // If login fails, print an error message and return
                if (!loginSuccess) 
                {
                    System.out.println("Login failed. Incorrect email or password. Please try again.");
                    return;
                }

            } 
            catch (IOException | ClassNotFoundException e) 
            {
                // Handle IO or ClassNotFound exceptions
                e.printStackTrace();
            }
        }
    }

    // Method to handle lodging money
    void lodgeMoney() 
    {
        try 
        {
            // Read and print the instruction to enter PPS Number
            message = (String) in.readObject();
            System.out.println(message);

            // Get user input for PPS Number and send it to the server
            String ppsNumber = input.nextLine();
            sendMessage(ppsNumber);

            // Read and print the instruction to enter the amount
            message = (String) in.readObject();
            System.out.println(message);

            // Get user input for the amount to lodge
            String amountStr = input.nextLine();

            try 
            {
                // Parse the amount string
                double amount = Double.parseDouble(amountStr);

                // Send the amount to the server
                sendMessage("" + amount);

            } 
            catch (NumberFormatException e) 
            {
                // Handle the case where the user enters an invalid amount format
                System.out.println("Invalid amount format. Please enter a valid amount.");
            }

            // Read and print the amount from the server
            message = (String) in.readObject();
            System.out.println(message);
            
            //Ask the user if they want to return to the main menu.
            message = (String) in.readObject();
            System.out.println(message);
            
            //Ask the user if they want to return to the main menu.
            String answer = input.nextLine();
            sendMessage(answer);
            
            //Print the menu based on the answer from the user
            message = (String) in.readObject();
            System.out.println(message);
            
            
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            // Handle IO or ClassNotFound exceptions
            e.printStackTrace();
        }
    }

    // Method to retrieve user information from the server
    void retrieveUsers() 
    {
        try 
        {
            // Read and print the instruction to retrieve users
            message = (String) in.readObject();
            System.out.println(message);

            // Print the user list received from the server
            message = (String) in.readObject();
            System.out.println(message);

            // Prompt the user for the next choice
            System.out.println("Enter your choice:");
            String nextChoice = input.nextLine();
            // Send the user's choice to the server
            sendMessage(nextChoice);

        } 
        catch (IOException | ClassNotFoundException e) 
        {
            // Handle IO or ClassNotFound exceptions
            e.printStackTrace();
        }
    }

    // Method to handle money transfer operation
    void transferMoney() 
    {
        try
        {
            // Read and print the instruction to enter sender's PPS Number
            message = (String) in.readObject();
            System.out.println(message);

            // Send the sender's PPS Number to the server
            String senderPPS = input.nextLine();
            sendMessage(senderPPS);

            // Read and print the instruction to enter sender's Email
            message = (String) in.readObject();
            System.out.println(message);

            // Send the sender's Email to the server
            String senderEmail = input.nextLine();
            sendMessage(senderEmail);

            // Read and print the instruction to enter recipient's Email
            message = (String) in.readObject();
            System.out.println(message);

            // Send the recipient's Email to the server
            String recipientEmail = input.nextLine();
            sendMessage(recipientEmail);

            // Read and print the instruction to enter amount
            message = (String) in.readObject();
            System.out.println(message);

            // Send the amount to the server
            String amount = input.nextLine();
            sendMessage(amount);
            
            // Read and print additional server response
            message = (String) in.readObject();
            System.out.println(message);
            
            //Ask the user if they want to return to the menu
            message = (String) in.readObject();
            System.out.println(message);
            
            //Store the result
            String answer = input.nextLine();
            sendMessage(answer);
            
            // Print the menu for the user
            message = (String) in.readObject();
            System.out.println(message);
        } 
        catch (IOException | ClassNotFoundException e) 
        {
            // Handle IO or ClassNotFound exceptions
            e.printStackTrace();
        }
    }

    // Method to view user transactions and handle return to the main menu
    void viewTransactions() 
    {
        try 
        {
            // Read and print the instruction to enter PPS Number
            message = (String) in.readObject();
            System.out.println(message);

            // Send the user's PPS Number to the server
            String ppsNumber = input.nextLine();
            sendMessage(ppsNumber);

            // Read and print the server response containing transaction history
            message = (String) in.readObject();
            System.out.println(message);

            // Read and print additional server response
            message = (String) in.readObject();
            System.out.println(message);

            // Ask the user if they want to return to the main menu
            String choice = input.nextLine();
            sendMessage(choice);

            //Read the menu
            message = (String) in.readObject();
            System.out.println(message);

        } 
        catch (IOException | ClassNotFoundException e) 
        {
            // Handle IO or ClassNotFound exceptions
            e.printStackTrace();
        }
    }

    // Method to update user password and handle return to the main menu
    void updatePassword() 
    {
        try 
        {
            // Read and print the instruction to enter PPS Number
            message = (String) in.readObject();
            System.out.println(message);

            // Send the user's PPS Number to the server
            String ppsNumber = input.nextLine();
            sendMessage(ppsNumber);

            // Read and print the instruction to enter current password
            message = (String) in.readObject();
            System.out.println(message);

            // Send the current password to the server
            String currentPassword = input.nextLine();
            sendMessage(currentPassword);

            // Read and print the instruction to enter new password
            message = (String) in.readObject();
            System.out.println(message);

            // Send the new password to the server
            String newPassword = input.nextLine();
            sendMessage(newPassword);

            // Read and print the server response
            message = (String) in.readObject();
            System.out.println(message);

            // Read and print additional server response
            message = (String) in.readObject();
            System.out.println(message);

            // Ask the user if they want to return to the main menu
            String choice = input.nextLine();
            sendMessage(choice);

            // Read and print the server response
            boolean returnToMainMenu = Boolean.parseBoolean(in.readObject().toString());

            if (returnToMainMenu) 
            {
                System.out.println("Returning to the main menu.");
            } 
            else 
            {
                System.out.println("Returning to the main menu.");
                return; // Exit the method and return to the main menu
            }

        } 
        catch (IOException | ClassNotFoundException e) 
        {
            // Handle IO or ClassNotFound exceptions
            e.printStackTrace();
        }
    }

    void sendMessage(String msg) 
    {
        try 
        {
            out.writeObject(msg);
            out.flush();
            System.out.println("client> " + msg);
        }
        catch (IOException ioException) 
        {
            ioException.printStackTrace();
        }
    }

    public static void main(String args[]) 
    {
        Requester client = new Requester();
        client.run();
    }
}






