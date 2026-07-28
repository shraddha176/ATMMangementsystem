package com.ATMSyatem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connectivity {
	
	public static Connection con()
	{
		Connection con=null;
		
		try {
			Class.forName("org.postgresql.Driver");
		con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/atmdb", "postgres", "1234567");
		
			if(con!=null)
			{
				System.out.println("Connected...");
			}
			else
			{
				System.out.println("Not Connected.....");
			}
			
			
		} catch (ClassNotFoundException | SQLException  e) {
			 System.out.println("Database Connection Failed");
			e.printStackTrace();
		}
		
		return con;
		
	}

}
