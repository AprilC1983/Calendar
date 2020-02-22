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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author April
 */
public class Appointment {
    int appointmentID;
    int customerID;
    int userID;
    String apTitle;
    String description;
    String location;
    String contact;
    String apURL;
    String start;
    String end;
    String createDate;
    String createdBy;
    String lastUpdate;
    String lastUpdatedBy;
    
    String db = "U04Eq8";
    String url = "jdbc:mysql://3.227.166.251/" + db;
    String user = "U04Eq8";
    String pass = "53688216264"; 
    
    public Appointment(){
        
    }
    
    public Appointment(int custID, int uID, String title, String desc, String loc, String con, String apUrl, String st, String e)throws SQLException{
        setCustomerID(custID);
        setUserId(uID);
        setUsername(uID);
        setTitle(title);
        setDescription(desc);
        setLocation(loc);
        setContact(con);
        setURL(apUrl);
        setStart(st);
        setEnd(e);
        
        //Create timestamp to log creation time and most recent update time
       Timestamp ts = new Timestamp(System.currentTimeMillis());
       createDate =  new String(String.valueOf(ts));
       lastUpdate = new String(String.valueOf(ts));
       
        try{
            //Set up a connection with the database
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement statement = conn.createStatement();
            
            //Assign customer id and address id
            ResultSet set = statement.executeQuery("SELECT COUNT(*) FROM appointment");
            ResultSetMetaData meta = set.getMetaData();
            int columns = meta.getColumnCount();
            
            set.next();
            appointmentID = set.getInt(1) + 100;
            
            while(set.next()){
                for(int i = 1; i < columns; i++){
                    int value = set.getInt(i);
                    appointmentID = value + 100;
                }
            }
            
            int size = appointmentID - 100;
            int[] IDs = new int[size];
            
            
            //Set values in IDs array        
            set = statement.executeQuery("SELECT appointmentId FROM appointment");
            meta = set.getMetaData();
            columns = meta.getColumnCount();
            int y = 0;
        
            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    int value = set.getInt(i);
                    IDs[y] = value;
                    y++; 
                }
            }
            
            //Check if appointmentID is duplicate and alter it if it is
            Boolean unique = false;
            do{
                if(size == 0){
                    unique = true;
                }
                for(int i = 0; i < size; i++){
                    if(appointmentID == IDs[i]){
                        appointmentID++;
                    }else{
                        unique = true;
                    }
                }
                for(int i = 0; i < size; i++){
                    if(appointmentID != IDs[i]){
                        unique = true;
                    }
                    
                    else if(appointmentID == IDs[i]){
                        unique = false;
                        break;
                    }

                }
            }while(!unique);
            
            //Find username based on id number and set value of createdBy and lastUpdatedBy
            set = statement.executeQuery("SELECT userName FROM user WHERE userId = " + uID);
            set.next();
            createdBy = set.getString(1);
            lastUpdatedBy = createdBy;
///////////////////////////////////////////////////////////////////////////////////////////////////////////            

           PreparedStatement ps = conn.prepareStatement("INSERT INTO appointment"
                   + "(appointmentId, customerId, userId, title, description, location, contact, type, "
                   + "url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                   + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
   
           ps.setInt(1, appointmentID);//set back to appointmentID
           ps.setInt(2, customerID);
           ps.setInt(3, userID);
           ps.setString(4, apTitle);
           ps.setString(5, description);
           ps.setString(6, location);
           ps.setString(7, contact);
           ps.setString(8, "meeting");
           ps.setString(9, apURL);
           ps.setString(10, start);
           ps.setString(11, end);
           ps.setString(12, createDate);
           ps.setString(13, createdBy);
           ps.setString(14, lastUpdate);
           ps.setString(15, lastUpdatedBy);
           
            ps.execute();
            conn.close();
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Sets customerID. All setter methods are used only in the constructor and do not cumminicate with the database
    public void setCustomerID(int custID) {
        customerID = custID;
    }
   
    //Communicates with the database to retrieve the customerID
    public int getCustomerID(int aptID)throws SQLException{
        String query = "SELECT customerID FROM appointment WHERE appointmentID = " + aptID;
        int result = getID(query);
        return result;
    }
    
    //set username
    public void setUsername(int user){
        String query = "SELECT userName FROM user WHERE userId = " + user;
        createdBy = runQuery(query);
    }

    //Sets userID
    public void setUserId(int user) {
        userID = user;
    }
    
    //Communicates with the database to retrieve the userID
    public int getUserID(int aptID)throws SQLException{
        String query = "SELECT userID FROM appointment WHERE appointmentID = " + aptID;
        int result = getID(query);
        
        return result;
    }

    //Sets the title
    public void setTitle(String title) {
        apTitle = title;
    }
 
    //Communicates with database to return the title of the appointment with matching id
    public String getTitle(int id)throws SQLException{
        String result;
        String search = "SELECT title FROM appointment WHERE appointmentID = " + id;
        result = runQuery(search);
         return result;
    }

    //Sets initial description value
    public void setDescription(String desc) {
        description = desc;
    }
    
    //Retrieves the appointment description from the database
    public String getDescription(int aptID)throws SQLException{
        String query = "SELECT description FROM appointment WHERE appointmentID = " + aptID;
        String result = runQuery(query);
        return result;
    }

    //Sets the initial value of the location
    public void setLocation(String loc) {
        location = loc;
    }
    
    //Retrieves location from the database
    public String getLocation(int aptID)throws SQLException{
        String query = "SELECT location FROM appointment WHERE appointmentID = " + aptID;
        String result = runQuery(query);
        return result;
    }

    //Sets the initial value of contact
    public void setContact(String con) {
        contact = con;
    }
    
    //Retrieve value of contact from the database
    public String getContact(int aptID) throws SQLException{
        String query = "SELECT contact FROM appointment WHERE appointmentID = " + aptID;
        String result = runQuery(query);
        return result;
    }
    
    //Sets the initial value of url
    public void setURL(String u){
        apURL = u;
    }
    
    //Retrieves url from the database
    public String getURL(int aptID) throws SQLException{
        String query = "SELECT url FROM appointment WHERE appointmentID = " + aptID;
        String result = runQuery(query);
        return result;
    }

    //Sets the inital value of start
    public void setStart(String st) {
        start = st;
    }
    
    //Retrieves start from the database
    public String getStart(int aptID) throws SQLException{
        String query = "SELECT start FROM appointment WHERE appointmentID = " + aptID;
        String result = runQuery(query);
        return result;
    }

    
    
    //Sets the initial value of end
    public void setEnd(String e) {
        end = e;
    }
    
    //Retrieve end from the database
    public String getEnd(int aptID) throws SQLException{
        String query = "SELECT end FROM appointment WHERE appointmentID = " + aptID;
        String result = runQuery(query);
        return result;
    }
    
//Runs a query to the database based on string provided as an argument
    public String runQuery(String query){
        String result = new String();
        
        try{
            //Establish connection to database
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement statement = conn.createStatement();
        
            //Run query and set value of apTitle
            ResultSet set = statement.executeQuery(query);
            ResultSetMetaData meta = set.getMetaData();
            int columns = meta.getColumnCount();
        
            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    result = set.getString(i);
                }
            }
            
            //Close the connection
            conn.close();
            
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         return result;
    }

//This method takes a string argument and will be used to get ID numbers from database 
    public int getID(String query){
        int value = 0;
        try{
            //Establish connection to database
            Connection conn = DriverManager.getConnection(url, user, pass);
            Statement statement = conn.createStatement();
        
            //Run query and set value of result
            ResultSet set = statement.executeQuery(query);
            ResultSetMetaData meta = set.getMetaData();
            int columns = meta.getColumnCount();

            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    int result = Integer.parseInt(set.getString(i));
                    return result;
                }
            }
            //Close the connection
            conn.close();
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
         return value; 
    }
    
    public int getID(){
        return appointmentID;
    }
 
//This method updates an appointment based on the arguments passed to it
    public void update(String value, String column, int userID, int aptID) throws SQLException{
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        lastUpdate =  new String(String.valueOf(ts));
        
        Connection conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
            
        ResultSet set = statement.executeQuery("SELECT userName FROM user WHERE userId = " + userID);
        set.next();
        lastUpdatedBy = set.getString(1);
            
        PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET " + column + " = ?, "
                + "lastUpdate = ?, lastUpdateBy = ? WHERE appointmentID = " + aptID);
        ps.setString(1, value);
        ps.setString(2, lastUpdate);
        ps.setString(3, lastUpdatedBy);
        ps.execute();
            
        //Close connection
        conn.close();
    }

//This method deletes the appointment associated with the provided appointment id
    public void delete(int id){
                
        try{
            //Establish connection
            Connection conn = DriverManager.getConnection(url, user, pass);
            
            //Delete the specified record
            PreparedStatement ps = conn.prepareStatement("DELETE FROM appointment WHERE appointmentId = ?");
            
            ps.setInt(1, id);
            ps.execute();
            
            //Close the connection
            conn.close();
        }
        catch(SQLException ex){
            
        }
    }
    
//Retrieve an array containing appointment info for a specified month and specified user
    public StringBuilder[] getAppointments(int selectedMonth, int selectedYear, int dayCount, int userID) throws SQLException{
        
        Connection conn;
  //Create the arrays
        int[] apptIDs;
        int[]customerIDs;
        StringBuilder[] customers;
        StringBuilder[] startTimes;
        StringBuilder[] endTimes;
        StringBuilder[] results;
      
  //Create variables
        StringBuilder first = new StringBuilder(selectedYear + "-" + selectedMonth + "-" + "01");
        StringBuilder last = new StringBuilder(selectedYear + "-" + selectedMonth + "-" + dayCount);
            
        conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
        
  //Get number of appointments in selected month with selected userID
        ResultSet set; 
        ResultSetMetaData meta;
        set = statement.executeQuery("SELECT COUNT(*) FROM appointment WHERE userID = " + userID + " "
                + "AND start BETWEEN '" + first + "' AND '" + last + "'");
        meta = set.getMetaData();
        int columns = meta.getColumnCount();
        int size = 0;
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                size = set.getInt(i);
            }
        }
        apptIDs = new int[size];
        
        
//Set values in apptIDs array        
        set = statement.executeQuery("SELECT appointmentId FROM appointment WHERE userID = " + userID + " "
                + "AND start BETWEEN '" + first + "' AND '" + last + "'");
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        int y = 0;
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                int value = set.getInt(i);
                apptIDs[y] = value;
                y++; 
            }
        }
          
  //Populate the customerIDs array
        y = 0;
        customerIDs = new int[apptIDs.length];
        for(int x = 0; x < apptIDs.length; x++){
            set = statement.executeQuery("SELECT customerID FROM appointment WHERE appointmentId = " + apptIDs[x]);
            meta = set.getMetaData();
            columns = meta.getColumnCount();
    
            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    int value = set.getInt(i);
                    //System.out.println(value);
                    customerIDs[y] = value;
                    y++;
                }
            }
        }

  //Populate the startTimes array
        y = 0;
        startTimes = new StringBuilder[apptIDs.length];
        for(int x = 0; x < apptIDs.length; x++){
            set = statement.executeQuery("SELECT start FROM appointment WHERE appointmentId = " + apptIDs[x]);
            meta = set.getMetaData();
            columns = meta.getColumnCount();
    
            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    StringBuilder value = new StringBuilder(set.getString(i));
                    startTimes[y] = value;
                    y++;
                }
            }
        }
        
  //Populate the endTimes array
        y = 0;
        endTimes = new StringBuilder[apptIDs.length];
        for(int x = 0; x < apptIDs.length; x++){
            set = statement.executeQuery("SELECT end FROM appointment WHERE appointmentID = " + apptIDs[x]);
            meta = set.getMetaData();
            columns = meta.getColumnCount();
            
            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    StringBuilder value = new StringBuilder(set.getString(i));
                    endTimes[y] = value;
                    y++;
                }
            }
        }
      
 //populate the customers array
        y = 0;
        customers = new StringBuilder[customerIDs.length];
        for(int x = 0; x < customerIDs.length; x++){
            set = statement.executeQuery("SELECT customerName FROM customer WHERE customerID = " + customerIDs[x]);
            meta = set.getMetaData();
            columns = meta.getColumnCount();
                
            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    StringBuilder value = new StringBuilder(set.getString(i));
                    customers[y] = value;
                    y++;
                }
            }  
        }
        
     
  //Populate the results array
        results = new StringBuilder[customers.length];
        
        for(int i = 0; i < customers.length; i++){
            StringBuilder value = new StringBuilder(startTimes[i] + " " + endTimes[i] + " " + customers[i]);
            results[i] = value;
        }
           
        return results;
    }


//This method returns an array of the names of each unique type of appointment    
    public String[][] getTypes(int month, int year) throws SQLException{
        //Establish connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
        
        //Create DateTime 
        LocalDate begin = LocalDate.of(year, month, 1);
        int days = begin.lengthOfMonth();
        LocalDate ending = LocalDate.of(year, month, days);
        
        int size = 0;
        String query = "SELECT COUNT(DISTINCT description) FROM appointment WHERE start BETWEEN '" + begin + "' AND '" + ending + "'";
        
        //Run query to determine needed array size
        ResultSet set = statement.executeQuery(query);
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                size = set.getInt(i);
            }
        }
        
        //Create arrays 
        String[] types = new String[size];
        String[] nums = new String[size];
        
        //Populate the types array
        query = "SELECT DISTINCT description FROM appointment WHERE start BETWEEN '" + begin + "' AND '" + ending + "'";
        set = statement.executeQuery(query);
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        int x = 0;
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                String value = new String(set.getString(i));
                types[x] = value;
                x++;
            }
        }
        
        //Populate the nums array
        x = 0;
        for(int i = 0; i < types.length; i++){
            query = "SELECT COUNT(*) FROM appointment WHERE description = '" 
                    + types[i] + "' AND start BETWEEN '" + begin + "' AND '" + ending + "'";
            set = statement.executeQuery(query);
            meta = set.getMetaData();
            columns = meta.getColumnCount();
            
            while(set.next()){
                for(int y = 1; y <= columns; y++){
                    String num = String.valueOf(set.getInt(y));
                    nums[x] = num;
                    x++;
                }
            }
        }
        
        //Close the connection
        conn.close();
        
        //Define and return the results matrix
        String[][] results = new String[][]{nums, types};
        
        return results;
    }
   
}