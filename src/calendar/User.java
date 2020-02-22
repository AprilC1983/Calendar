/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Month;

/**
 *
 * @author April
 */

public class User {
    String db = "U04Eq8";
    String url = "jdbc:mysql://3.227.166.251/" + db;
    String user = "U04Eq8";
    String pass = "53688216264";
    
    public User(){
        
    }

//Retrieves user id number based on username and password    
    public int getID(String username, String password) throws SQLException{
        int userID = 0;
        //Establish connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
        
        //Perform query
        ResultSet set = statement.executeQuery("Select userId FROM user WHERE userName = '" + username + "'" + " AND "
                + "password = '" + password + "'");
        ResultSetMetaData meta = set.getMetaData();
        int columns  = meta.getColumnCount();
        
        //Set value of userID
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                userID = set.getInt(i);
                return userID;
            }
        }
        
        //Close connection 
        conn.close();
        return userID;
    }
    
    public String getUserName(int userID) throws SQLException{
        String username = "";
        
        //Establish connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
        
        //Perform the query
        ResultSet set = statement.executeQuery("SELECT userName FROM user WHERE userId = " + userID);
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
        
        //Set the value of username
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                username = set.getString(i);
                return username;
            }
        }
        
        //Close the connection
        conn.close();
        return username;
    }
 
//This method returns appointment data for the specified user
    public String[][][] getSchedule(int userId)throws SQLException{
        //create the arrays
        String[] startTimes;
        String[] endTimes;
        String[] titles;
        String[] locations;
        String[][] times;
        String[][] data;
        String[][][] results;
        
        //declare the size int, used to initialize the arrays
        int size = 0;
        
        //Create the LocalDate objects to use in query
        LocalDate date = LocalDate.now();
        Month month = date.getMonth();
        int year = date.getYear();
        int firstDay = 1;
        int lastDay = date.lengthOfMonth();
        LocalDate begin = LocalDate.of(year, month, firstDay);
        LocalDate end = LocalDate.of(year, month, lastDay);
        
        //Establish the connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        
        //Run query and set size value
        String query = "SELECT COUNT(*) FROM appointment WHERE userId = "
                + userId + " AND start BETWEEN '" + begin + "' AND '" + end + "'";
        PreparedStatement ps = conn.prepareStatement(query);
        
        ResultSet set = ps.executeQuery(query);
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                size = set.getInt(i);
            }
        }  
        
        //initialize the arrays
        startTimes = new String[size];
        endTimes = new String[size];
        titles = new String[size];
        locations = new String[size];
        
        //Populate the startTimes array
        int x = 0;
        query = "SELECT start FROM appointment WHERE userId = " + userId 
                + " AND start BETWEEN '" + begin + "' AND '" + end + "'";
        ps = conn.prepareStatement(query);
        
        set = ps.executeQuery(query);
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                String value = set.getString(i);
                startTimes[x] = value;
                x++;
            }
        } 
        
        //Populate the endTimes array
        x = 0;
        set = ps.executeQuery("SELECT end FROM appointment WHERE userId = " + userId  + 
                                    " AND start BETWEEN '" + begin + "' AND '" + end + "'");
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                String value = set.getString(i);
                endTimes[x] = value;
                x++;
            }
        }
        
        //Populate the titles array
        x = 0;
        set = ps.executeQuery("SELECT title FROM appointment WHERE userId = " + userId  + 
                                    " AND start BETWEEN '" + begin + "' AND '" + end + "'");
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                String value = set.getString(i);
                titles[x] = value;
                x++;
            }
        }
        
        //Populate the descriptions array
        x = 0;
        set = ps.executeQuery("SELECT location FROM appointment WHERE userId = " + userId  + 
                                    " AND start BETWEEN '" + begin + "' AND '" + end + "'");
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                String value = set.getString(i);
                locations[x] = value;
                x++;
            }
        }
        
        //initialize the matrices
        data = new String[][]{titles, locations};
        times = new String[][]{startTimes, endTimes};
        
        results = new String[][][]{data, times};
        return results;
        
    }
 
//This method validates the username and password provided as arguments and returns a Boolean value
    public Boolean validate(String username, String password)throws SQLException{
        //Create Boolean valid, false by default
        Boolean valid = false;////////////////////////////////////////////////////////////////////////////////////////////////////
        
        //Establish connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        
        //Create PreparedStatement
        String query = "SELECT userID FROM user WHERE userName = '" + username + "' AND password = '" + password + "'";
        PreparedStatement ps = conn.prepareStatement(query);
        
        //Run query to valdate username and password
        ResultSet set = ps.executeQuery(query);
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){

                int result = set.getInt(i);
               if(result == 1){// if(result >= 100){
                    valid = true;
                    return valid;
                }  
                else{
                    valid = false;////////////////////////////////////////////////////////////////////////////////////////////////
                }
            }
        }
        //Close the connection 
        conn.close();
        return valid;
    }
}
