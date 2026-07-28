package com.ATMSyatem;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
public class ATMManagement {
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Scanner sc = new Scanner(System.in);
        Class.forName("org.postgresql.Driver");
        // this line is compulsory to connect database connectivity to java application
		Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/atmdb","postgres","1234567");

        ATM atm = new ATM();

        System.out.println("****************************************");
        System.out.println("*                                                       *");
        System.out.println("*       ATM MANAGEMENT SYSTEM  *");
        System.out.println("*                                                       *");
        System.out.println("****************************************");
        try { // using for voice

            Process p = Runtime.getRuntime().exec(
                "powershell -Command \"Add-Type -AssemblyName System.Speech; " +
                "(New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('Please select language English Hindi or Marathi')\""
            );

            p.waitFor(); // Voice complated to use then show the screen

        
     //==========================language===========================   
        System.out.println("1. English");
        System.out.println("2. Hindi");
        System.out.println("3. Marathi");
        } catch (Exception e) {
            System.out.println("Voice Not Supported");
        }
        System.out.print("Select Language : ");
        int lang = sc.nextInt();

        if (lang == 1)
            System.out.println("Welcome To ATM");
        else if (lang == 2)
            System.out.println("ATM में आपका स्वागत है");
        else
            System.out.println("ATM मध्ये आपले स्वागत आहे");
        try {

            Process p = Runtime.getRuntime().exec(
                "powershell -Command \"Add-Type -AssemblyName System.Speech; " +
                "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                "$speak.SelectVoice('Microsoft Zira Desktop'); " +
                "$speak.Speak('Please enter your account no');\""
            );

            p.waitFor();
       //=============================================account logic========= 

        if (lang == 1)
            System.out.print("Enter Account Number : ");
        else if (lang == 2)
            System.out.print("खाता संख्या दर्ज करें : ");
        else
            System.out.print("खाते क्रमांक टाका : ");
        } catch (Exception e) {
            System.out.println("Voice Not Supported");
        }

        long accNo = sc.nextLong();
//=====================valid pin logic=========================================
        boolean validPin = false;

        for (int i = 1; i <= 3; i++) {

            if (lang == 1)
                System.out.print("Enter PIN : ");
            else if (lang == 2)
                System.out.print("PIN दर्ज करें : ");
            else
                System.out.print("PIN टाका : ");

            String pin = sc.next();

            if (atm.verifyPin(accNo, pin)) {
            

                validPin = true;
                break;

            } else {

                if (lang == 1)
                    System.out.println("Wrong PIN");
                else if (lang == 2)
                    System.out.println("गलत PIN");
                else
                    System.out.println("चुकीचा PIN");
            }
        }

        if (!validPin) {
        	 atm.blockAccount(accNo);
        	 // voice altert 
        	 try {
                 Runtime.getRuntime().exec(
                     "PowerShell -Command \"Add-Type -AssemblyName System.Speech; " +
                     "$speak = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                     "$speak.Speak('चेतावणी. तुमचे खाते ब्लॉक केले आहे'); " +
                     "$speak.Speak('Warning. Your account has been blocked');\""
                 );

                 Thread.sleep(1000);

             } catch (Exception e) {
                 System.out.println("Voice error");
             }
        	 if (lang == 1) {
        		    System.out.println("Your Account Has Been Blocked.");
        		    System.out.println("Please Contact Your Bank.");
        		}
        		else if (lang == 2) {
        		    System.out.println("आपका खाता ब्लॉक हो गया है।");
        		    System.out.println("कृपया अपने बैंक से संपर्क करें।");
        		}
        		else {
        		    System.out.println("तुमचे खाते ब्लॉक झाले आहे.");
        		    System.out.println("कृपया आपल्या बँकेशी संपर्क साधा.");
        		}
            sc.close();
            return;
        }
        int choice;

        do{

            if (lang == 1) {

                System.out.println("\n1.Check Balance");
                System.out.println("2.Deposit Money");
                System.out.println("3.Withdraw Money");
                System.out.println("4.Change PIN");
                System.out.println("5.Mini Statement");
                System.out.println("6.Exit");

            } else if (lang == 2) {

                System.out.println("\n1.बैलेंस जांचें");
                System.out.println("2.पैसे जमा करें");
                System.out.println("3.पैसे निकालें");
                System.out.println("4.PIN बदलें");
                System.out.println("5.मिनी स्टेटमेंट");
                System.out.println("6.बाहर निकलें");

            } else {

                System.out.println("\n1.शिल्लक तपासा");
                System.out.println("2.रक्कम जमा करा");
                System.out.println("3.रक्कम काढा");
                System.out.println("4.PIN बदला");
                System.out.println("5.मिनी स्टेटमेंट");
                System.out.println("6.बाहेर पडा");
            }

            System.out.print("Choice : ");
            choice = sc.nextInt();

            switch (choice){
            case 1:
                System.out.println("Balance : ₹" + atm.getBalance(accNo));
                break;
                
            case 2:

                System.out.print("Enter Amount : ₹");
                double depAmount = sc.nextDouble();

                atm.deposit(accNo, depAmount);
                if (lang == 1) {
                System.out.print("Do you want receipt? (Y/N): ");
                }
                else if (lang == 2){
                	System.out.println("क्या आपको रसीद चाहिए?(Y/N):");
                }
                else {
                	System.out.println("तुम्हाला पावती हवी आहे का?(Y/N):");
                }
                char r1 = sc.next().charAt(0);

                if (r1 == 'Y' || r1 == 'y') {
                    double balance = atm.getBalance(accNo);
                    atm.printReceipt("Deposit", depAmount, balance);
                }
                

                break;
                
            case 3:

                System.out.print("Enter Amount : ₹");
                double wdAmount = sc.nextDouble();

                atm.withdraw(accNo, wdAmount);
                
                if (lang == 1) {
                    System.out.print("Do you want receipt? (Y/N): ");
                    }
                    else if (lang == 2){
                    	System.out.println("क्या आपको रसीद चाहिए?(Y/N):");
                    }
                    else {
                    	System.out.println("तुम्हाला पावती हवी आहे का?(Y/N):");
                    }
                char r2 = sc.next().charAt(0);
                if (r2 == 'Y' || r2 == 'y') {
                    double balance = atm.getBalance(accNo);
                    atm.printReceipt("Withdraw", wdAmount, balance);
                }

                break;
                
            case 4:

                System.out.print("Enter Old PIN : ");
                String oldPin = sc.next();

                System.out.print("Enter New PIN : ");
                String newPin = sc.next();

                atm.changePin(accNo, oldPin, newPin);

                break;
                
            case 5:

            	 atm.miniStatement(accNo);

                break;
                
            case 6:

                if (lang == 1)
                    System.out.println("Thank You For Using ATM");
                else if (lang == 2)
                    System.out.println("ATM उपयोग करने के लिए धन्यवाद");
                else
                    System.out.println("ATM वापरल्याबद्दल धन्यवाद");
                
                try {

                    Runtime.getRuntime().exec(
                        "powershell -Command \"Add-Type -AssemblyName System.Speech; " +
                        "(New-Object System.Speech.Synthesis.SpeechSynthesizer).Speak('Thank you for using ATM')\""
                    );

                } catch (Exception e) {

                    System.out.println("Voice Error");
                }

                break;
            
            

        default:

            if (lang == 1)
                System.out.println("Invalid Choice");
            else if (lang == 2)
                System.out.println("अमान्य विकल्प");
            else
                System.out.println("अवैध पर्याय");
            
        }
        }while(choice != 6);
        sc.close();   
	}
	}
	




	


