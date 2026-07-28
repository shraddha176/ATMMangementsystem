package com.ATMSyatem;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
public class ATM {
	private  Connection con = Connectivity.con();
	 public double getBalance(long accNo) throws SQLException {
		 String sql = "SELECT balance FROM atm_users WHERE account_no=?";
		 PreparedStatement ps = con.prepareStatement(sql);
	        ps.setLong(1, accNo);

	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            return rs.getDouble("balance");
	        }

	        return 0;
	    }
	 //========================verifypin method====================================
	 public boolean verifyPin(long accNo, String pin) throws SQLException {

	        String sql = "SELECT * FROM atm_users WHERE account_no=  ?  AND pin= ? AND status='ACTIVE' ";

	        PreparedStatement ps = con.prepareStatement(sql);

	        ps.setLong(1, accNo);
	        ps.setString(2, pin);

	        ResultSet rs = ps.executeQuery();

	        return rs.next();
	    }
	 //==============deposite method======================================================
	 public void deposit(long accNo, double amount) throws SQLException {

		    String sql = "UPDATE atm_users SET balance = balance + ? WHERE account_no = ?";

		    PreparedStatement ps = con.prepareStatement(sql);

		    ps.setDouble(1, amount);
		    ps.setLong(2, accNo);

		    int rows = ps.executeUpdate();

		    if (rows> 0) {

		        System.out.println("Amount Deposited Successfully");
		        double balance = getBalance(accNo);
		       

		        String transactionSql =
		                "INSERT INTO transactions(account_no, transaction_type, amount) VALUES(?,?,?)";

		        PreparedStatement ps2 = con.prepareStatement(transactionSql);

		        ps2.setLong(1, accNo);
		        ps2.setString(2, "Deposit");
		        ps2.setDouble(3, amount);

		        ps2.executeUpdate();
		    }
	
}
	 //==================withdraw method ====================================================
	 public void withdraw(long accNo, double amount) throws SQLException {

		    double balance = getBalance(accNo);

		    if (amount <= balance) {

		        String sql =
		                "UPDATE atm_users SET balance = balance - ? WHERE account_no = ?";

		        PreparedStatement ps = con.prepareStatement(sql);

		        ps.setDouble(1, amount);
		        ps.setLong(2, accNo);

		        int rows = ps.executeUpdate();

		        if (rows > 0) {

		            System.out.println("Amount Withdraw Successfully");
		            double newBalance = getBalance(accNo);
		           

		            String transactionSql =
		                    "INSERT INTO transactions(account_no, transaction_type, amount) VALUES(?,?,?)";

		            PreparedStatement ps2 = con.prepareStatement(transactionSql);

		            ps2.setLong(1, accNo);
		            ps2.setString(2, "Withdraw");
		            ps2.setDouble(3, amount);

		            ps2.executeUpdate();
		        }

		    } else {

		        System.out.println("Insufficient Balance");
		    }
		}
	 //===================change pin logic===========================================
	 public void changePin(long accNo, String oldPin, String newPin) throws SQLException {

		    String sql =
		            "SELECT * FROM atm_users WHERE account_no=? AND pin=?";

		    PreparedStatement ps = con.prepareStatement(sql);

		    ps.setLong(1, accNo);
		    ps.setString(2, oldPin);

		    ResultSet rs = ps.executeQuery();

		    if (rs.next()) {

		        String updateSql =
		                "UPDATE atm_users SET pin=? WHERE account_no=?";

		        PreparedStatement ps2 = con.prepareStatement(updateSql);

		        ps2.setString(1, newPin);
		        ps2.setLong(2, accNo);

		        ps2.executeUpdate();

		        System.out.println("PIN Changed Successfully");

		    } else {

		        System.out.println("Incorrect Old PIN");
		    }
		}
	 //==============mini statment=======================================================
	 public void miniStatement(long accNo) throws SQLException {

		    String sql =
		            "SELECT transaction_type, amount, transaction_date " +
		            "FROM transactions WHERE account_no=? " +
		            "ORDER BY transaction_date DESC";

		    PreparedStatement ps = con.prepareStatement(sql);

		    ps.setLong(1, accNo);

		    ResultSet rs = ps.executeQuery();

		    System.out.println("\n===== MINI STATEMENT =====");

		    while (rs.next()) {

		        System.out.println(
		                rs.getString("transaction_type")
		                + "\t₹" + rs.getDouble("amount")
		                + "\t" + rs.getTimestamp("transaction_date"));
		    }
		}
	 //=====================Recipt method==============
	 public void printReceipt(String type, double amount, double balance) {

		    DateTimeFormatter dtf =
		            DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a");

		    System.out.println("\n================================");
		    System.out.println("         ATM RECEIPT");
		    System.out.println("=================================");
		    System.out.println("Transaction : " + type);
		    System.out.println("Amount      : ₹" + amount);
		    System.out.println("Date & Time : " + dtf.format(LocalDateTime.now()));
		    System.out.println("Balance     : ₹" + balance);
		    System.out.println("================================");
		    System.out.println("         Thank You!                                                    ");
		    System.out.println("================================");
		} 
	 //============================================blocked==============
	 public void blockAccount(long accNo) throws SQLException {

		    String sql =
		            "UPDATE atm_users SET status='BLOCKED' WHERE account_no=?";

		    PreparedStatement ps = con.prepareStatement(sql);

		    ps.setLong(1, accNo);

		    ps.executeUpdate();

		    System.out.println("Account Blocked Successfully");
		}
	 //==========================unblock===============================
	 public void unblockAccount(long accNo) {

		    String sql = "UPDATE atm_users SET is_blocked = false WHERE account_no = ?";

		    try {
		        PreparedStatement pst = con.prepareStatement(sql);
		        pst.setLong(1, accNo);

		        int rows = pst.executeUpdate();

		        if (rows > 0)
		            System.out.println("Account Activated Successfully");
		        else
		            System.out.println("Account Not Found");

		    } catch (Exception e) {
		        System.out.println(e);
		    }
		}
	 
	 
}
