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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author April
 */
public class Customer {
    
    int custID = 0;

    String db = "U04Eq8";
    String url = "jdbc:mysql://3.227.166.251/" + db;
    String user = "U04Eq8";
    String pass = "53688216264";
    
    public Customer(){
        
    }
    
    public Customer(String name, String ad1, String ad2, String city, String postal, int countryID, String ph, String username) throws SQLException{
        
        
       //Create timestamp to log creation time and most recent update time
       Timestamp ts = new Timestamp(System.currentTimeMillis());
       String createDate =  new String(String.valueOf(ts));
       String lastUpdate = new String(String.valueOf(ts));
       int isActive = 0;      
        
        //Set up a connection with the database
        Connection conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
            
        //Assign customer id number
        ResultSet set = statement.executeQuery("SELECT COUNT(*) FROM customer");
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
            
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                custID = set.getInt(1) + 100;
            }
        }
        
        //Assign address id number
        set = statement.executeQuery("SELECT COUNT(*) FROM address");
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        int addressID = 0;
            
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                addressID = set.getInt(1) + 100;
            }
        }
            
        PreparedStatement ps = conn.prepareStatement("SELECT *");
       
        //Determine whether city is in the database and set cityID
        set = statement.executeQuery("SELECT cityId FROM city WHERE city = '" + city + "'");
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        int cityID = 0;
        Boolean found = false;
        
        while(set.next()){
            for(int i = 1; i<= columns; i++){
                cityID = set.getInt(1);
                
                if(cityID >= 100){
                    found = true;
                }
            }
        }
           
        //If city is not found in database create new entry for city
        if(!found){
            //Create a city id
            set = statement.executeQuery("SELECT COUNT(*) FROM city");
            meta = set.getMetaData();
            columns = meta.getColumnCount();
            
            while(set.next()){
                for(int i = 1; i <= columns; i++){
                    cityID = set.getInt(i) + 1;
                }
            }
             try{
            //Insert new city into the database
            ////////////////////////////////////////////////////////////////////////////////////////////////////////
            
                ps = conn.prepareStatement("INSERT INTO city VALUES(?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, cityID);
                ps.setString(2, city);
                ps.setInt(3, countryID);
                ps.setString(4, createDate);
                ps.setString(5, username);
                ps.setString(6, lastUpdate);
                ps.setString(7, username);
                ps.execute();

                //////////////////////////////////////////////////////////////////////////////////////////////////////
             }
             catch(SQLException ex){
                 
                 Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
                 
             }
        }
 
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            //Add customer address to database
        try{
            ps = conn.prepareStatement("INSERT INTO address VALUES(" + addressID + ", ?, ?, " + cityID + ", ?, ?, ?, ?, ?, ?)");
            ps.setString(1, ad1);
            ps.setString(2, ad2);
            ps.setString(3, postal);
            ps.setString(4, ph);
            ps.setString(5, createDate);
            ps.setString(6, username);
            ps.setString(7, lastUpdate);
            ps.setString(8, username);
            ps.execute();
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            addressID++;
        }
        
        
        //Add new customer to the database
        try{
            System.out.println(addressID);
            ps = conn.prepareStatement("INSERT INTO customer VALUES(" + custID + ", ?, " + 
                addressID + ", " + isActive + ", ?, ?, '" + lastUpdate + "', ?)");
            ps.setString(1, name);
            ps.setString(2, createDate);
            ps.setString(3, username);
            ps.setString(4, username);
            ps.execute();
        }
        catch(SQLException ex){
            custID++;
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
       
        //Close the connection
            conn.close();

    }

//This method retrieves the name of the customer associated with the provided id number    
    public String getName(int id) throws SQLException{
        String query = "SELECT customerName FROM customer WHERE customerID = " + id;
        String result = runQuery(query);
        return result;
    }

//This method updates a customer name and logs user that made change to database and time of change    
    public void setName(String custName, String username, int id){
        String column = "customerName";
        String table = "customer";
        String idValue = "customerID";
        update(custName, column, table, idValue, username, id);
    }
    
//This method takes a customer id number as an argument and returns the customer's address     
    public String getAddress(int id)throws SQLException{
        String query;
//Get the address id from the customer table
        query = "SELECT addressId FROM customer WHERE customerId = " + id;
        int adID = getID(query);
            
//Retrieve the first line of the address from the address table
        query = "SELECT address FROM address WHERE addressID = " + adID;
        String ad1 = runQuery(query);

//Retrieve second line of address from the address table    
        query = "SELECT address2 FROM address WHERE addressID = " + adID;
        String ad2 = runQuery(query);
         
//Get the city id from the address table
        query = "SELECT cityID FROM address WHERE addressID = " + adID;
        int cID = getID(query);
 
//Retrieve city name from the city table  
        query = "SELECT city FROM city WHERE cityID = " + cID;
        String c = runQuery(query);
          
//Retrieve postal code from the address table
        query = "SELECT postalCode FROM address WHERE addressID = " + adID;
        String postal = runQuery(query);
         
//Retrieve country id from the city table
        query = "SELECT countryID FROM city WHERE cityID = " + cID;
        int ctryID = getID(query);
            
//Retrieve country name from the country table
        query = "SELECT country FROM country WHERE countryID = " + ctryID;
        String ctry = runQuery(query);

//Set and return the address variable
        String address = ad1 + " " + ad2 + " \n" + c + ", " + postal + " \n" + ctry;
        return address;
    }
    
//This method updates a user's address in the database
    public void setAddress(String ad1, String ad2, String c, String postal, int ctryID, String username, int id)throws SQLException{
        //Establish connection to the database
        Connection conn = DriverManager.getConnection(url, user, pass);
        
        //create timestamp
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String lastUpdate =  new String(String.valueOf(ts));
        String lastUpdatedBy = username;
        String createDate = lastUpdate;
            
    //Set value of cityID, ctryID, and adID
        String query = "SELECT addressID FROM customer WHERE customerID = " + id;
        int adID = getID(query);
            
        //Get cityID
        query = "SELECT cityId FROM city WHERE city = '" + c + "'";
        int cID = getID(query);
        
        //If the city is not found, create new city in database
        if(cID == 0){
            try{
                query = "SELECT COUNT(*) FROM city";
                
                Connection connection = DriverManager.getConnection(url, user, pass);
                PreparedStatement ps = connection.prepareStatement(query);
                
                ResultSet s = ps.executeQuery(query);
                ResultSetMetaData m = s.getMetaData();
                int col = m.getColumnCount();
                
                while(s.next()){
                    for(int i = 1; i <= col; i++){
                        cID = s.getInt(i) + 100;
                    }
                }
               
                ps = connection.prepareStatement("INSERT INTO city VALUES(?, ?, ?, ?, ?, ?, ?)");
                ps.setInt(1, cID);
                ps.setString(2, c);
                ps.setInt(3, ctryID);
                ps.setString(4, createDate);
                ps.setString(5, username);
                ps.setString(6, lastUpdate);
                ps.setString(7, username);
                ps.execute();
                
                connection.close();
            }
            catch(SQLException ex){
                
            }
        }
        
        //Update address in the database
        PreparedStatement ps = conn.prepareStatement("UPDATE address SET address = ?, address2 = ?, "
                + "cityID = " + cID + ", postalCode = ?, lastUpdate = ?, lastUpdateBy = ? WHERE addressID = " + adID);
            
        ps.setString(1, ad1);
        ps.setString(2, ad2);
        ps.setString(3, postal);
        ps.setString(4, lastUpdate);
        ps.setString(5, lastUpdatedBy);
        ps.execute();
            
        //close the connection
        conn.close();

    }
 
//This method retrieves the customer's phone number    
    public String getPhone(int id){  
        String ph = new String();
        try{
            String query = "SELECT addressID FROM customer WHERE customerID = " + id;
            int adID = getID(query);
            query = "SELECT phone FROM address WHERE addressId = " + adID;
            ph = runQuery(query);
            return ph;
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ph;
    }

//This method sets the user's phone number    
    public void setPhone(String phone, String username, int adID){
        String column = "phone";
        String table = "address";
        String idValue = "addressId";
        update(phone, column, table, idValue, username, adID);
    }
 
//This method updates a record based on the arguments provided
   private void update(String value, String column, String table, String idValue, String username, int id){
        try{
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            String lastUpdate =  new String(String.valueOf(ts));
        
            Connection conn = DriverManager.getConnection(url, user, pass);
            
            ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            
            PreparedStatement ps = conn.prepareStatement("UPDATE " + table + " SET " + column + " = ?, lastUpdate = ?, lastUpdateBy = ? WHERE " + idValue + " = " + id);
            ps.setString(1, value);
            ps.setString(2, lastUpdate);
            ps.setString(3, username);
            
            ps.execute();

            //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            //Close connection
            conn.close();
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 

//This method runs a query based on the String argument and returns a String value
    private String runQuery(String query)throws SQLException{
        String value = new String();
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
                    String result = set.getString(i);
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
    
   //This method takes a string argument and will be used to get ID numbers from database 
    public int getID(String query)throws SQLException{
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
  
    //This method returns the customer id. Will return 0 if customer object created with empty constructor
    public int getID(){
        return custID;
    }
 
//This method returns an array of existing ID numbers. Used to check for duplicates        
    public int[] getMultipleIDs(String table, String id) throws SQLException{
        //Establish the connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
            
//Find needed size of array
        ResultSet set = statement.executeQuery("SELECT COUNT(*) FROM " + table);
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
        int size = 0;
            
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                size = set.getInt(i);
            }
        }
            
//Create results array
        int[] results = new int[size];
            
//Populate the array
        set = statement.executeQuery("Select " + id + " FROM " + table);
        meta = set.getMetaData();
        columns = meta.getColumnCount();

        while(set.next()){
            for(int i = 1; i <= columns; i++){
                int result = set.getInt(i);
                results[i] = result;
            }
        }
            
        return results;
    }
   
//This method returns a string array containing the number of customers in each country
    public String[] getCustomersByCountry() throws SQLException{
        //Declare the arrays
        String[] customerCounts;
        String[] countries;
        String[] results;
        int size = 0;
        
        //Establish the connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        Statement statement = conn.createStatement();
        
        //Find needed size of arrays
        String query = "SELECT COUNT(*) FROM country";
        ResultSet set = statement.executeQuery(query);
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                size = set.getInt(i);
            }
        }
        
        //initialize the arrays
        countries = new String[size];
        customerCounts = new String[size];
        results = new String[size];
        
        //Populate the countries array
        query = "SELECT country FROM country";
        set = statement.executeQuery(query);
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        
        int count = 0;
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                countries[count] = set.getString(i);
                count++;
            }
        }
        
        //Populate the customerCounts array
        int x = 0;
        for(int i = 0; i < countries.length; i++){
            query = "SELECT COUNT(customerId) FROM customer WHERE addressId IN(SELECT addressId FROM address "
                + "WHERE cityId IN(SELECT cityId FROM city WHERE countryId IN(SELECT countryId FROM country WHERE country "
                + " = '" + countries[i] +  "')));";
            set = statement.executeQuery(query);
            meta = set.getMetaData();
            columns = meta.getColumnCount();
            
            while(set.next()){
                for(int y = 1; y <= columns; y++){
                    customerCounts[x] = set.getString(y);
                    x++;
                }
            }
        }
        
        //Populate the results array
        for(int i = 0; i < results.length; i++){
            String result = "Customers in " + countries[i] + ": \t\t" + customerCounts[i];
            results[i] = result;
        }
        
        return results;
    }
    
//This method returns an array containing the countries
    public String[] getCountries() throws SQLException{
        //Declare the array and needed variables
        String[]countries;
        int size = 0;
        String query;
        
        //Establish the connection
        Connection conn = DriverManager.getConnection(url, user, pass);
        
        //Create PreparedStatement and find needed size of the array
        query = "SELECT COUNT(*) FROM country";
        PreparedStatement ps = conn.prepareStatement(query);
        
        ResultSet set = ps.executeQuery(query);
        ResultSetMetaData meta = set.getMetaData();
        int columns = meta.getColumnCount();
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                size = set.getInt(i) + 1;
            }
        }
        
        //Initialize the countries array
        countries = new String[size];
        
        //Populate the countries array
        query = "SELECT country FROM country";
        set = ps.executeQuery(query);
        meta = set.getMetaData();
        columns = meta.getColumnCount();
        int x = 1;
        countries[0] = "Select Country";
        
        while(set.next()){
            for(int i = 1; i <= columns; i++){
                String value = set.getString(i);
                countries[x] = value;
                x++;
            }
        }
        x = 1;
        
        return countries;
    }

//Deletes a customer from the database    
    public void delete(int id) throws SQLException{ 
        
        try{
            //Establish connection
            Connection conn = DriverManager.getConnection(url, user, pass);
            
            //Delete the specified record
            PreparedStatement ps = conn.prepareStatement("DELETE FROM customer WHERE customerId = ?");
            
            ps.setInt(1, id);
            ps.execute();
            
            //Close the connection
            conn.close();
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}