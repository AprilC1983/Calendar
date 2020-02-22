/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 *
 * @author April May
 */
public class Calendar {
    int id;
    static LocalDate d;
    static String user;
    static String pass;
    static String day;
    static String month;
    static String year;
    static String startHour;
    static String endHour;
    static String startMin;
    static String endMin;
    static String column = "title";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        login();
    }
    
    public static void login(){
        
        //Locale.setDefault(Locale.GERMAN);
        Locale spanish = new Locale("sp");
        //Locale.setDefault(spanish);
        
         //Create the labels and textboxes and set their properties
        JLabel nameLbl = new JLabel("Username");
        JLabel passLbl = new JLabel("Password");
        
        TextField nameTxt = new TextField();
        
        JPasswordField passTxt = new JPasswordField();
        passTxt.setEchoChar('*');
        
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(150, 160, 100, 30);
                
        //Create the JPanels and the JFrame
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JPanel panel2 = new JPanel();
        javax.swing.border.Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(border);
        
        JFrame loginFrame = new JFrame();
        loginFrame.setSize(300, 150);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setTitle("Welcome");
        loginFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        if(Locale.getDefault().equals(Locale.GERMAN)){
            nameLbl.setText("Benutzername");
            passLbl.setText("Passwort");
            loginBtn.setText("Anmeldung");
            loginFrame.setTitle("Wilkommen");
        }
        if(Locale.getDefault().equals(spanish)){
            nameLbl.setText("Usuario");
            passLbl.setText("Contrasena");
            loginBtn.setText("Iniciar Secion");
            loginFrame.setTitle("Bienvenido");
        }
        
        //Add the labels and text fields to the panels
        panel.add(nameLbl);
        panel.add(nameTxt);
        panel.add(passLbl);
        panel.add(passTxt);
        panel2.add(loginBtn);
        
        //Add the panels to frame and set visible to true
        loginFrame.add(panel, BorderLayout.CENTER);
        loginFrame.add(panel2, BorderLayout.SOUTH);
        loginFrame.setResizable(false);
        loginFrame.setVisible(true);
        
        //Adds functionality to the login button
        loginBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency */
            try{
                
                //Retrieve username and password
                String username = nameTxt.getText();
                String password = passTxt.getText();
                
                user = username;
                pass = password;
                
                //Determine the user's locale
                Locale locale = Locale.getDefault();
                
                //Launch the main application and close login window if username and password are correct
                User u = new User();
                if(!u.validate(username, password)){
                    //Create timestamp
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    String lastLogin = new String(String.valueOf(ts));
                    
                    //Create list containing username and last login
                    List<String> data = Arrays.asList(username, lastLogin);
                    
                    //Create file
                    File logins = new File("logins.txt");
                    
                    //Create the BufferedWriter and append new login data
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(logins, true))){
                        for(String s : data){
                            writer.write(s);
                            writer.newLine();
                        }
                    }
                    catch(IOException ex){
                        Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    launch();
                    loginFrame.dispose();
                }
                else{
                    throw new LoginException("Incorrect username or password");
                }    
            }
            catch(LoginException ex){
                errorMessage();
            }
            catch(SQLException ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    //Gernerates an error message if username and/or password is incorrect
    public static void errorMessage(){
        Locale spanish = new Locale("sp");
        
        //Create JLabel and set message based on the default locale
        JLabel message = new JLabel("User name or password is incorrect");
        
        if(Locale.getDefault().equals(Locale.GERMAN))
            message.setText("Benutzername oder Passwort ist falsch");
        if(Locale.getDefault().equals(spanish))
            message.setText("Usuario o contrasena incorrectos");
        
        Font font = new Font("Verdana", Font.BOLD, 14);
        message.setFont(font);
        
        //'Ok' is the same in German and English and so will only be altered if the default is Spanish
        JButton okBtn = new JButton("OK");
        if(Locale.getDefault().equals(spanish))
            okBtn.setText("De acuerdo");
                
        //Create the JPanels and the JFrame
        JPanel panel = new JPanel(new GridLayout(3, 1));
        JPanel panel2 = new JPanel();
        javax.swing.border.Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        panel.setBorder(border);
        
        JFrame errorFrame = new JFrame();
        errorFrame.setLocationRelativeTo(null);
        
        //'Error' is the same in English, German, and Spanish and so will not be altered
        errorFrame.setTitle("Error");        
        
        //Add the labels and text fields to the panel
        panel.add(message);
        panel2.add(okBtn);
        
        //Add the panels to frame and set visible to true
        errorFrame.add(panel, BorderLayout.CENTER);
        errorFrame.add(panel2, BorderLayout.SOUTH);
        errorFrame.setSize(350, 150);
        errorFrame.setResizable(false);
        errorFrame.setVisible(true);
        
        okBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            errorFrame.dispose();
        });
    }
     
    public static void mainScreen(){
        Boolean upcoming = false;
        String meetingWith = new String();
        
    //Create Spansih locale
        Locale spanish = new Locale("sp");
        
    //Create days of the week array    
        String[] daysOfWeek = new String[]{
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
        };
           
    //Set the value of the current month and the current year
        LocalDate date = LocalDate.now();  
        String currentMonth = date.getMonth().toString();
        int currentYear = date.getYear();  
            
    //Create JFrame
        JFrame calFrame = new JFrame();
        calFrame.setSize(950, 750);
        calFrame.setLocationRelativeTo(null);
        calFrame.setTitle("Calendar");
        calFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            
    //Create JLabel, will have its text set based on the current selected month
        JLabel monthLbl = new JLabel(currentMonth + " " + currentYear);
        monthLbl.setFont(new Font("Verdana", Font.BOLD, 19));
          
    //Create the panels
        JPanel btnPanel = new JPanel();
        javax.swing.border.Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        btnPanel.setBorder(border);
            
        JPanel monthPanel = new JPanel();
        monthPanel.setBorder(border);
            
        JPanel calPanel = new JPanel(new GridLayout(6, 7, 5, 5));
            
        JPanel backPnl = new JPanel(new FlowLayout());
        JPanel fwdPnl = new JPanel(new FlowLayout());
            
    //Create the radio buttons and button group
        JRadioButton monthBtn = new JRadioButton("Month");
        monthBtn.setSelected(true);
            
        JRadioButton weekBtn = new JRadioButton("Week");
            
        ButtonGroup group = new ButtonGroup();
        group.add(monthBtn);
        group.add(weekBtn);
        
        //Create navigation buttons
        JButton backBtn = new JButton("<<");
        backBtn.setEnabled(false);
        JButton fwdBtn = new JButton(">>");
            
    //Create the selection menu
        JMenuBar menuBar = new JMenuBar();
            
        JMenu custMenu = new JMenu("Customer");
        JMenu updateCustomer = new JMenu("Update");
            
        JMenu apptMenu = new JMenu("Appointment");
        
        JMenu reportsMenu = new JMenu("Reports");
            
        JMenuItem searchCustItem = new JMenuItem("Search");
        searchCustItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency */
            try{
                searchCustomer();
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem addCustItem = new JMenuItem("Add New");
        addCustItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency */
            try{
                createCustomer();
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem updateNameItem = new JMenuItem("Update Name");
        updateNameItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                String value = "Name";
                updateCustomer(value);
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem updateAddressItem = new JMenuItem("Update Address");
        updateAddressItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                String value = "Address";
                updateCustomer(value);
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem updatePhoneItem = new JMenuItem("Update Phone");
        updatePhoneItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                String value = "Phone";
                updateCustomer(value);
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem delCustItem = new JMenuItem("Delete");
        delCustItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                delete("Customer");
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem addApptItem = new JMenuItem("Add New");
        addApptItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                createAppointment();
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem updateApptItem = new JMenuItem("Update");
        updateApptItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                updateAppointment();
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
            
        JMenuItem delApptItem = new JMenuItem("Delete");
        delApptItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                delete("Appointment");
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        JMenuItem typesItem = new JMenuItem("Types by Month");
        typesItem.addActionListener((ActionEvent e) -> { /*Lambda used to improve efficiency*/
            try{
                d = LocalDate.now();
                displayTypes(d);
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        JMenuItem scheduleItem = new JMenuItem("Consultant Schedules");
        scheduleItem.addActionListener((ActionEvent e)->{ /*Lambda used to improve efficiency*/
            try{
                displaySchedule();
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        JMenuItem customerItem = new JMenuItem("Customers by Country");
        customerItem.addActionListener((ActionEvent e)->{ /*Lambda used to improve efficiency of ActionListener*/
            try{
                displayCustomers();
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        
        //Put together the menu
        updateCustomer.add(updateNameItem);
        updateCustomer.add(updateAddressItem);
        updateCustomer.add(updatePhoneItem);
            
        custMenu.add(searchCustItem);
        custMenu.add(addCustItem);
        custMenu.add(updateCustomer);
        custMenu.add(delCustItem);
            
        apptMenu.add(addApptItem);
        apptMenu.add(updateApptItem);
        apptMenu.add(delApptItem);
        
        reportsMenu.add(typesItem);
        reportsMenu.add(scheduleItem);
        reportsMenu.add(customerItem);
            
        menuBar.add(custMenu);
        menuBar.add(apptMenu);
        menuBar.add(reportsMenu);
            
        calFrame.setJMenuBar(menuBar);
            
    //Add objects to the panels
        btnPanel.add(monthBtn);
        btnPanel.add(weekBtn);
            
        monthPanel.add(monthLbl);

//Set the year and month values and determine which day of the week the month started on
        int selectedMonth = date.getMonthValue();
        int year = date.getYear();
        LocalDate first = LocalDate.of(year, selectedMonth, 1);
        DayOfWeek dow = first.getDayOfWeek();
        int dayOfWeek = dow.ordinal() + 1;
        int dayCount = date.lengthOfMonth();

//Obtain appointments scheduled for the chosen month        
        try{
            User u = new User();
            int userID = u.getID(user, pass);

            Appointment appt = new Appointment();
            StringBuilder[] appointments = appt.getAppointments(selectedMonth, year, dayCount, userID);
   
   //Display the labels marking the days of the week
            for (String daysOfWeek1 : daysOfWeek) {
                calPanel.add(new JLabel(daysOfWeek1));
            }
            
    //Display empty labels preceding the first day of the month
            for(int i = 0; i < dayOfWeek; i++){
                calPanel.add(new JLabel(" "));
            }
            
   //Display the days of the month
            for(int i = 1; i <= dayCount; i++){
                String display = Integer.toString(i) + "\n";
                
                for(int s = 0; s < appointments.length; s++){
                    StringBuilder scheduled = appointments[s];
                    //String startTime = scheduled.substring(11, 16);
                    String st = scheduled.substring(0, 16);
                    
                    //Convert back to local time
                    ZonedDateTime gmtTime = LocalDateTime.parse(st, 
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).atZone(ZoneId.of("GMT"));
                    LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                    String startTime = localTime.toString();
                    startTime = startTime.substring(11, 16);
                    
                    
                    String name = scheduled.substring(40);
                    String dayString = scheduled.substring(8, 10);
                    int dayNum = Integer.parseInt(dayString);
                    
                    if(dayNum == i){
                        display = display + name + "\n" + startTime + "\n";
                        
                //Display notification if user has an appointment within 15 minutes of login
                        int startHour = Integer.parseInt(startTime.substring(0, 2));
                        int startMin = Integer.parseInt(startTime.substring(3));
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime start = LocalDateTime.of(year, selectedMonth, dayNum, startHour, startMin);
                        long difference = now.until(start, ChronoUnit.MINUTES);
                        long minutes = 15;
                        
                        if(difference <= minutes && difference >= 0){
                            upcoming = true;
                            meetingWith = name;
                        }
                    }
                }
                
                JTextArea area = new JTextArea(display);
                area.setEditable(false);
                calPanel.add(area);
            }
            
            //backPnl.add(backBtn);
            //fwdPnl.add(fwdBtn);
         
 //Add functionality to weekBtn to display only the week
        weekBtn.addActionListener((ActionEvent e)->{ /*Lambda used to improve ActionListener efficiency */
            calFrame.setSize(1500, 350);
            calFrame.setLocationRelativeTo(null);
            calPanel.removeAll();
            calPanel.setLayout(new GridLayout(1, 7, 5, 5));
        
            int currentDay = date.getDayOfMonth();
            int week = currentDay;
            int count = currentDay;
            int d = 1;
            
            for(int i = 0; i < 6 && count < dayCount; i++){
                week += 1;
                count++;
                d++;
            }
            
//Display the days of the week
            for(int i = currentDay; i <= week; i++){
                String display = Integer.toString(i) + "\n";
            
                for(int s = 0; s < appointments.length; s++){
                    StringBuilder scheduled = appointments[s];
                    String time = scheduled.substring(11, 16);
                    String name = scheduled.substring(19);
                    String dayString = scheduled.substring(8, 10);
                    int dayNum = Integer.parseInt(dayString);
                
                    if(dayNum == i){
                        display = display + name + " " + time + "\n";
                    }
                }
            
                JTextArea area = new JTextArea(display);
                area.setEditable(false);
                calPanel.add(area);
            }
            
            String numDays = "Current " + d + " Days";
            monthLbl.setText(numDays);
            calPanel.repaint();
        });
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }            
        
//Add functionality to the monthBtn
    monthBtn.addActionListener((ActionEvent e)->{ /*Lambda used to provide greater efficiency to ActionListener */
        calFrame.dispose();
        mainScreen();
    });
 
//Add the panels to the frame and display the frame
        calFrame.add(btnPanel, BorderLayout.SOUTH);
        calFrame.add(monthPanel, BorderLayout.NORTH);
        calFrame.add(backPnl, BorderLayout.WEST);
        calFrame.add(fwdPnl, BorderLayout.EAST);
        calFrame.add(calPanel, BorderLayout.CENTER);
        calFrame.setResizable(false);
        calFrame.setVisible(true);
        
        if(upcoming){
            String message = "You have an upcoming appointment with " + meetingWith;
            notification(message, 475, 150);
        }

    }
    
    public static void notification(String message, int w, int h){
        JLabel notifyLbl = new JLabel(message);
        notifyLbl.setFont(new Font("Verdana", Font.BOLD, 14));
        
        JButton okBtn = new JButton("Ok");
        
        JFrame frame = new JFrame("Notification");
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        okBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            frame.dispose();
        });
        
        panel.add(notifyLbl);
        panel.add(okBtn);
        
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
    
    public static void createCustomer() throws SQLException{
 //Create Labels
        JLabel headingLbl = new JLabel("Create Customer");
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        
        JLabel nameLbl = new JLabel("Name");
        JLabel address1Lbl = new JLabel("Address 1");
        JLabel address2Lbl = new JLabel("Address 2");  
        JLabel cityLbl = new JLabel("City");        
        JLabel postalLbl = new JLabel("Postal Code");
        JLabel countryLbl = new JLabel("Country");
        JLabel phoneLbl = new JLabel("Phone");
        
        //Create text fields
        JTextField nameField = new JTextField("");
        JTextField address1Field = new JTextField("");
        JTextField address2Field = new JTextField("");
        JTextField cityField = new JTextField("");
        JTextField postalField = new JTextField("");
        JTextField phoneField = new JTextField("");
        
        //Create JComboBox
        Customer customer = new Customer();
        String[] countries = customer.getCountries();
     
        JComboBox<String> countriesCB = new JComboBox<>(countries);
        countriesCB.setVisible(true);
        
        //Create Buttons
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        //Create frame and panels
        JFrame frame = new JFrame("Customer");
        frame.setSize(500, 450);
        frame.setLocationRelativeTo(null);
        
        JPanel p1 = new JPanel(new FlowLayout());
        p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel p2 = new JPanel(new GridLayout(8, 2, 5, 5));
        p2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel p3 = new JPanel(new FlowLayout());
        p3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //Add components to the panels
        p1.add(headingLbl);
        
        p2.add(nameLbl);
        nameLbl.setLabelFor(nameField);
        p2.add(nameField);
        
        p2.add(address1Lbl);
        address1Lbl.setLabelFor(address1Field);
        p2.add(address1Field);
        
        p2.add(address2Lbl);
        address2Lbl.setLabelFor(address2Field);
        p2.add(address2Field);
        
        p2.add(cityLbl);
        cityLbl.setLabelFor(cityField);
        p2.add(cityField);
        
        p2.add(postalLbl);
        postalLbl.setLabelFor(postalField);
        p2.add(postalField);
        
        p2.add(countryLbl);
        countryLbl.setLabelFor(countriesCB);
        p2.add(countriesCB);
        
        p2.add(phoneLbl);
        phoneLbl.setLabelFor(phoneField);
        p2.add(phoneField);
        
        p3.add(cancelBtn);
        p3.add(saveBtn);
        
        saveBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try {
                String name = nameField.getText();
                String address1 = address1Field.getText();
                String address2 = address2Field.getText();
                String city = cityField.getText();
                String postal = postalField.getText();
                String phone = phoneField.getText();
                String username = user;
                String[] fields = new String[]{name, address1, city, postal, phone};
                //Test to make sure all required values have been entered
                for(int i = 0; i < fields.length; i++){
                    if(fields[i].equals("")){
                        throw new BlankFieldException("Field left blank");
                    }
                }
                //Verify that a country is selected
                int countryID = countriesCB.getSelectedIndex();
                if(countryID == 0){
                    throw new BlankFieldException("Country not Selected");
                }
                else{
                    countryID += 99;
                }
                
                //Verify that a valid phone number has been entered
                String phoneRegex = "^[0-9\\-]*$";
                Boolean validPhone = true;
                if(phone.matches(phoneRegex))
                    validPhone = true;
                else
                    validPhone = false;
                if(!validPhone)
                    throw new TelephoneFormatException("Invalid phone number");
                //Verify that a valid postal code has been entered
                String postalRegex = "^[0-9]*$";
                Boolean validPostal = true;
                if(postal.matches(postalRegex))
                    validPostal = true;
                else
                    validPostal = false;
                if(!validPostal)
                    throw new InvalidPostalCodeException("Invalid postal code entered");
                //Attempt to create new customer in the database
                Customer cust = new Customer(name, address1, address2, city, postal, countryID, phone, username);
                int id1 = cust.getID();
                notification("Customer " + id1 + " saved successfully to the database", 450, 150);
                frame.dispose();
            }catch(InvalidPostalCodeException ex){
                notification("Please enter a valid postal code using only numerals", 450, 150);
            }catch(TelephoneFormatException ex){
                notification("Please enter a valid phone number using only numerals and dashes", 550, 150);
            }catch(BlankFieldException ex){
                notification("Please complete all required fields", 350, 150);
            }catch(SQLException ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        cancelBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                frame.dispose();
            }
            catch(Exception ex){
                
            }
        });
        
        //Add panels to the frame
        frame.add(p1, BorderLayout.NORTH);
        frame.add(p2, BorderLayout.CENTER);
        frame.add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);

    }
    
    //This method will search for an existing customer and call the updateCustomer method if found
    public static void searchCustomer(){
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 25));
        
        JLabel searchLbl = new JLabel("Enter Customer ID");
        JButton searchBtn = new JButton("Search");
        
        JFrame frame = new JFrame("Search");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        searchPanel.add(searchLbl);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        frame.add(searchPanel, BorderLayout.CENTER);
        frame.setVisible(true);
       
        //displays specified customer
        searchBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try {
                int id1 = Integer.parseInt(searchField.getText());
                displayCustomer(id1);
                frame.dispose();
            }catch(Exception ex){
                
            }
        });
    }

//Updates customer value based on String argument
    public static void updateCustomer(String value) throws SQLException{
        //Create Labels
        JLabel headingLbl = new JLabel("Update Customer " + value);
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        
        JLabel idLbl = new JLabel("Customer ID");
        JLabel newValueLbl = new JLabel("New " + value);
        
        //These labels will be used if the customer address is being updated
        JLabel ad1Lbl = new JLabel("Address 1");
        JLabel ad2Lbl = new JLabel("Address 2");
        JLabel cityLbl = new JLabel("City");
        JLabel postalLbl = new JLabel("Postal Code");
        JLabel countryLbl = new JLabel("Country");
        
        //Create text fields
        JTextField idField = new JTextField("");
        JTextField newValueField = new JTextField("");
        
        //These text fields will be used if the address is being updated
        JTextField ad1Field = new JTextField("");
        JTextField ad2Field = new JTextField("");
        JTextField cityField = new JTextField("");
        JTextField postalField = new JTextField("");
        
        //Create JComboBox
        Customer customer = new Customer();
        String[] countries = customer.getCountries();
        
        JComboBox<String> countriesCB = new JComboBox<>(countries);
        countriesCB.setVisible(true);
        
        //Create Buttons
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        
        //Create frame and panels
        JFrame frame = new JFrame("Update");
        if(value.equals("Address")){
            frame.setSize(400, 450);
        }
        else{
            frame.setSize(400, 300);
        }
        frame.setLocationRelativeTo(null);
        
        JPanel p1 = new JPanel(new FlowLayout());
        p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel p2 = new JPanel();
        if(value.equals("Address")){
            p2.setLayout(new GridLayout(8, 2, 5, 5));
        }
        else{
            p2.setLayout(new GridLayout(3, 2, 5, 5));
        }
        p2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel p3 = new JPanel(new FlowLayout());
        p3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //Add components to the panels
        p1.add(headingLbl);
        
        p2.add(idLbl);
        idLbl.setLabelFor(idField);
        p2.add(idField);
        
        //Add these labels and fields if address is being updated
        if(value.equals("Address")){
            p2.add(ad1Lbl);
            ad1Lbl.setLabelFor(ad1Field);
            p2.add(ad1Field);
            
            p2.add(ad2Lbl);
            ad2Lbl.setLabelFor(ad2Field);
            p2.add(ad2Field);
            
            p2.add(cityLbl);
            cityLbl.setLabelFor(cityField);
            p2.add(cityField);
            
            p2.add(postalLbl);
            postalLbl.setLabelFor(postalField);
            p2.add(postalField);
            
            p2.add(countryLbl);
            countryLbl.setLabelFor(countriesCB);
            p2.add(countriesCB);
        }
        else{
            p2.add(newValueLbl);
            newValueLbl.setLabelFor(newValueField);
            p2.add(newValueField);
        }
        
        p3.add(cancelBtn);
        p3.add(saveBtn);
        
        saveBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                String newValue = newValueField.getText();
                String username = user;
                int cusID = Integer.parseInt(idField.getText());
                
                if(value.equals("Name")){
                    if(newValue.equals("")){
                        throw new BlankFieldException("Field left blank");
                    }
                    
                    customer.setName(newValue, username, cusID);
                    notification("Update successful. New " + value.toLowerCase() + " is " + customer.getName(cusID) + ".", 450, 150);
                }
                else if(value.equals("Phone")){
                    if(newValue.equals("")){
                        throw new BlankFieldException("Field left blank");
                    }
                    
                    //Verify that a valid phone number has been entered
                    String phoneRegex = "^[0-9\\-]*$";
                    Boolean validPhone = true;
                    if(newValue.matches(phoneRegex))
                        validPhone = true;
                    else
                        validPhone = false;
                    
                    if(!validPhone)
                        throw new TelephoneFormatException("Invalid phone number");
                    
                    customer.setPhone(newValue, username, cusID);
                    notification("Update successful. New " + value.toLowerCase() + " is " + customer.getPhone(cusID) + ".", 450, 150);
                }
                else if(value.equals("Address")){
                    String ad1 = ad1Field.getText();
                    String ad2 = ad2Field.getText();
                    String c = cityField.getText();
                    String postal = postalField.getText();
                    int countryID = countriesCB.getSelectedIndex();
                    
                    //Verify that a country has been selected
                    if(countryID == 0){
                        throw new BlankFieldException("Country not selected");
                    }
                    else{
                        countryID += 99;
                    }
                    
                    //Verify that all required fields are completes
                    String [] verify = new String[]{
                        ad1, c, postal
                    };
                    
                    for(int i = 0; i < verify.length; i++){
                        if(verify[i].equals("")){
                            throw new BlankFieldException("Field left blank");
                        }
                    }
                    
                    String postalRegex = "^[0-9]*$";
                    Boolean validPostal = true;
                    if(postal.matches(postalRegex))
                        validPostal = true;
                    else
                        validPostal = false;
                    
                    if(!validPostal)
                        throw new InvalidPostalCodeException("Invalid postal code entered");
                    
                    customer.setAddress(ad1, ad2, c, postal, countryID, username, cusID);
                    notification("Update successful. New " + value.toLowerCase() + " is " + customer.getAddress(cusID) + ".", 800, 150);
                }
                frame.dispose();
            }
            catch(SQLException ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(BlankFieldException ex){
                notification("Please complete all required fields", 350, 150);
            }
            catch(NumberFormatException ex){
                notification("Please enter a valid ID number", 350, 150);
            }
            catch(InvalidPostalCodeException ex){
                notification("Please enter a valid postal code using only numerals", 450, 150);
            }
            catch(TelephoneFormatException ex){
                notification("Please enter a valid phone number using only numerals and dashes", 550, 150);
            }
        });
        
        cancelBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                frame.dispose();
            }
            catch(Exception ex){
                
            }
        });
        
        //Add panels to the frame
        frame.add(p1, BorderLayout.NORTH);
        frame.add(p2, BorderLayout.CENTER);
        frame.add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);

    }
    
    //Deletes a customer record from the database
    public static void delete(String entry){
        //Allow user to enter customer ID for deletion
        JTextField searchField = new JTextField();
        searchField.setPreferredSize(new Dimension(200, 25));
        
        JLabel searchLbl = new JLabel("Enter " + entry + " ID");
        JButton searchBtn = new JButton("Search");
        
        JFrame frame = new JFrame("Delete");
        frame.setSize(350, 200);
        frame.setLocationRelativeTo(null);
        
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        searchPanel.add(searchLbl);
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        
        frame.add(searchPanel, BorderLayout.CENTER);
        frame.setVisible(true);
        
        searchBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int idNum = Integer.parseInt(searchField.getText());
            JLabel confirmLbl = new JLabel("Delete " + entry + " " + idNum + "?");
            confirmLbl.setFont(new Font("Verdana", Font.BOLD, 14));
            JButton okBtn = new JButton("Ok");
            JButton cancelBtn = new JButton("Cancel");
            JFrame frame1 = new JFrame("Confirm");
            frame1.setSize(250, 200);
            frame1.setLocationRelativeTo(null);
            JPanel panel = new JPanel(new FlowLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
            okBtn.addActionListener((ActionEvent e1) -> { /*Lambda used to improve ActionListener efficiency*/
                try {
                    if(entry.equals("Customer")){
                        Customer customer = new Customer();
                        customer.delete(idNum);
                    }
                    else if(entry.equals("Appointment")){
                        Appointment appointment = new Appointment();
                        appointment.delete(idNum);
                    }
                    notification(entry + " " + idNum + " successfully deleted", 350, 150);
                    frame1.dispose();
                }catch (SQLException ex) {
                    Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            cancelBtn.addActionListener((ActionEvent e1) -> { /*Lambda used to improve ActionListener efficiency*/
                frame1.dispose();
            });
            panel.add(confirmLbl);
            panel.add(okBtn);
            panel.add(cancelBtn);
            frame1.add(panel, BorderLayout.CENTER);
            frame1.setVisible(true);
        });
        
    }
    
    public static void displayCustomer(int id) throws SQLException{
        //Create customer object
        Customer cust = new Customer();
        
        //Create Labels
        JLabel headingLbl = new JLabel("Customer " + id);
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        
        JTextArea nameArea = new JTextArea();
        nameArea.setEditable(false);
        nameArea.setFont(new Font("Verdana", Font.BOLD, 14));
        
        JTextArea addressArea = new JTextArea();
        addressArea.setEditable(false);
        addressArea.setFont(new Font("Verdana", Font.BOLD, 14));
        
        JTextArea phoneArea = new JTextArea();
        phoneArea.setEditable(false);
        phoneArea.setFont(new Font("Verdana", Font.BOLD, 14));
        
        
        try{
        //Set the values of the labels
            String name = cust.getName(id);
            //Throws an exception if customer name is not found
            if(name.equals("")){
                throw new NotFoundException("Customer with specified id number not found");
            }
                
            nameArea.setText(name);
            addressArea.setText(cust.getAddress(id));
            phoneArea.setText(cust.getPhone(id));
       
        //Create Buttons
            JButton apptBtn = new JButton("New Appointment");
            JButton exitBtn = new JButton("Exit");
        
        //Create frame and panels
            JFrame frame = new JFrame("Customer");
            frame.setSize(450, 375);
            frame.setLocationRelativeTo(null);
        
            JPanel p1 = new JPanel(new FlowLayout());
            p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
            JPanel p2 = new JPanel(new GridLayout(3, 1));
            p2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
            JPanel p3 = new JPanel(new FlowLayout());
            p3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //Add components to the panels
            p1.add(headingLbl);
        
            p2.add(nameArea, 0, 0);
            p2.add(addressArea, 0, 1);
            p2.add(phoneArea, 0, 2);
        
            p3.add(exitBtn);
            p3.add(apptBtn);
        
            apptBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
                try{
                    createAppointment();
                    frame.dispose();
                }
                catch(Exception ex){
                    Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        
            exitBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
                try{
                    frame.dispose();
                }
                catch(Exception ex){
                    Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        
        //Add panels to the frame
            frame.add(p1, BorderLayout.NORTH);
            frame.add(p2, BorderLayout.CENTER);
            frame.add(p3, BorderLayout.SOUTH);
            frame.setVisible(true);
        }
        catch(SQLException ex){
            Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(NotFoundException ex){
            notification("Customer with id number " + id + " not found", 450, 150);
        }
    }

//This method creates a new appointment in the database    
    public static void createAppointment() throws OutsideBusinessHoursException{
          //Create Labels
        JLabel headingLbl = new JLabel("Create Appointment");
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
  
        JLabel customerLbl = new JLabel("Customer ID");
        JLabel titleLbl = new JLabel("Title");
        JLabel descLbl = new JLabel("Description");
        JLabel locLbl = new JLabel("Location");
        JLabel contactLbl = new JLabel("Contact");
        JLabel urlLbl = new JLabel("URL");
        JLabel dateLbl = new JLabel("Appointment Date");
        JLabel startLbl = new JLabel("Start");
        JLabel endLbl = new JLabel("End");
  
  //Create text fields
        JTextField customerField = new JTextField("");
        JTextField titleField = new JTextField("");
        JTextField descField = new JTextField("");
        JTextField locField = new JTextField("");
        JTextField contactField = new JTextField("");
        JTextField urlField = new JTextField("");
        JTextField dateField = new JTextField(""); 
  
  //Create combo boxes
        String[] hours = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] minutes = {"00", "15", "30", "45"};
  
        JComboBox<String> startHourCB = new JComboBox<>(hours);
        startHourCB.setVisible(true);
        JComboBox<String> startMinCB = new JComboBox<>(minutes);
        startMinCB.setVisible(true);
  
        JComboBox<String> endHourCB = new JComboBox<>(hours);
        endHourCB.setVisible(true);
        JComboBox<String> endMinCB = new JComboBox<>(minutes);
        endMinCB.setVisible(true);
  
  //create radio buttons and groups
        JRadioButton amBtn = new JRadioButton("AM");
        amBtn.setSelected(true);
  
        JRadioButton pmBtn = new JRadioButton("PM");
  
        JRadioButton am2Btn = new JRadioButton("AM");
        am2Btn.setSelected(true);
  
        JRadioButton pm2Btn = new JRadioButton("PM");
  
        ButtonGroup group = new ButtonGroup();
        group.add(amBtn);
        group.add(pmBtn);
  
        ButtonGroup group2 = new ButtonGroup();
        group2.add(am2Btn);
        group2.add(pm2Btn);
  
  //Create Buttons
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
  
  //Create frame and panels
        JFrame frame = new JFrame("Appointment");
        frame.setSize(450, 525);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
  
        JPanel p1 = new JPanel(new FlowLayout());
        p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
  
        JPanel p2 = new JPanel(new GridLayout(10, 2, 5, 5));
        p2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
  
        JPanel p3 = new JPanel(new FlowLayout());
        p3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
  
        JPanel rbPanel = new JPanel(new FlowLayout());
        JPanel rb2Panel = new JPanel(new FlowLayout());
  
        rbPanel.add(startHourCB);
        rbPanel.add(startMinCB);
        rbPanel.add(amBtn);
        rbPanel.add(pmBtn);
  
        rb2Panel.add(endHourCB);
        rb2Panel.add(endMinCB);
        rb2Panel.add(am2Btn);
        rb2Panel.add(pm2Btn);
  
  //Add components to the panels
        p1.add(headingLbl);
  
        p2.add(customerLbl);
        customerLbl.setLabelFor(customerField);
        p2.add(customerField);
  
        p2.add(titleLbl);
        titleLbl.setLabelFor(titleField);
        p2.add(titleField);
  
        p2.add(descLbl);
        descLbl.setLabelFor(descField);
        p2.add(descField);
  
        p2.add(locLbl);
        locLbl.setLabelFor(locField);
        p2.add(locField);
  
        p2.add(contactLbl);
        contactLbl.setLabelFor(contactField);
        p2.add(contactField);
  
        p2.add(urlLbl);
        urlLbl.setLabelFor(urlField);
        p2.add(urlField);
  
        p2.add(dateLbl);
        dateLbl.setLabelFor(dateField);
        p2.add(dateField);
  
        p2.add(startLbl);
        startLbl.setLabelFor(rbPanel);
        p2.add(rbPanel);
  
        p2.add(endLbl);
        endLbl.setLabelFor(rb2Panel);
        p2.add(rb2Panel);
  
        p3.add(cancelBtn);
        p3.add(saveBtn);

  //Set time variables
        startHour = (String)startHourCB.getSelectedItem();
        startMin = (String)startMinCB.getSelectedItem();
        endHour = (String)endHourCB.getSelectedItem();
        endMin = (String)endMinCB.getSelectedItem();
        
 //Add ActionListeners to comboboxes
        startHourCB.addActionListener((ActionEvent e)-> { /*Lambda used to improve ActionListener efficiency*/
            startHour = (String)startHourCB.getSelectedItem();
        });
        
        startMinCB.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            startMin = (String)startMinCB.getSelectedItem();
        });
        
        endHourCB.addActionListener((ActionEvent e)-> { /*Lambda used to improve ActionListener efficiency*/
            endHour = (String)endHourCB.getSelectedItem();
        });
        
        endMinCB.addActionListener((ActionEvent e)-> { /*Lambda used to improve ActionListener efficiency*/
            endMin = (String)endMinCB.getSelectedItem();
        });
 
  
//Add functionality to the radio buttons
        amBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int start = Integer.parseInt(startHour) - 12;
            startHour = Integer.toString(start);
                });

        pmBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int start = Integer.parseInt(startHour) + 12;
            startHour = Integer.toString(start);
                });

        am2Btn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int end = Integer.parseInt(endHour) - 12;
            endHour = Integer.toString(end);
                });

        pm2Btn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int end = Integer.parseInt(endHour) + 12;
            endHour = Integer.toString(end);
                });
  
  //Add functionality to the save button
        saveBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try { 
                //Set variable values
                String sec = ":00";
                String date = dateField.getText();
                String title = titleField.getText();
                String description = descField.getText();
                String location = locField.getText();
                String contact = contactField.getText();
                String url = urlField.getText();
                String start = date + " " + startHour + ":" + startMin + sec;
                String end = date + " " + endHour + ":" + endMin + sec;
                String cust = customerField.getText();
                //Throws an exception if a field is left blank
                String[] fields = new String[]{title, description, location, contact, url, start, end, cust};
                for(int i = 0; i < fields.length; i++){
                    if(fields[i].equals(""))
                        throw new BlankFieldException("Field Left Blank");
                }
                int customerID = Integer.parseInt(customerField.getText());
                int hour1 = Integer.parseInt(startHour);
                int minute1 = Integer.parseInt(startMin);
                int hour2 = Integer.parseInt(endHour);
                int minute2 = Integer.parseInt(endMin);
                int month1 = Integer.parseInt(date.substring(5, 7));
                int year1 = Integer.parseInt(date.substring(0, 4));
                int day1 = Integer.parseInt(date.substring(8));
                LocalDateTime desiredStart = LocalDateTime.of(year1, month1, day1, hour1, minute1);
                LocalDateTime desiredEnd = LocalDateTime.of(year1, month1, day1, hour2, minute2);
                LocalTime beginBusiness = LocalTime.of(9, 0);
                LocalTime endBusiness = LocalTime.of(17, 0);
                LocalTime apptStart = LocalTime.of(hour1, minute1);
                LocalTime apptEnd = LocalTime.of(hour2, minute2);
                //Throw an exception if invalid customerID is provided
                Customer customer = new Customer();
                String query = "SELECT customerID FROM customer WHERE customerID = " + customerID;
                int testID = customer.getID(query);
                if(testID == 0){
                    throw new InvalidIDNumberException("Customer not found");
                }
                //Throws an exception if appointment is outside business hours
                if(apptStart.isBefore(beginBusiness)|| apptStart.isAfter(endBusiness))
                    throw new OutsideBusinessHoursException("Selected time is outside business hours");
                if(apptEnd.isBefore(beginBusiness) || apptEnd.isAfter(endBusiness))
                    throw new OutsideBusinessHoursException("Selected time is outside business hours");
                
                //Set dayCount variable to match number of days in selected month
                LocalDate d = LocalDate.of(year1, month1, day1);
                int dayCount = d.lengthOfMonth();
                
                //Retrieve list of existing appointments
                Appointment temp = new Appointment();
                User u = new User();
                int uID = u.getID(user, pass);
                StringBuilder[] existingAppointments = temp.getAppointments(month1, year1, dayCount, uID);
                //Check for conflicting times  and throw exception if found
                for(int i = 0; i < existingAppointments.length; i++){
                    StringBuilder startTime = new StringBuilder(existingAppointments[i].substring(0, 19));
                    StringBuilder endTime = new StringBuilder(existingAppointments[i].substring(20, 39));
                    
                    ZonedDateTime gmtBeginning = LocalDateTime.parse(startTime, 
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
                    ZonedDateTime gmtEnding = LocalDateTime.parse(endTime, 
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
                    
                    LocalDateTime beginning = gmtBeginning.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                    LocalDateTime ending = gmtEnding.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                    
                    
                    ZoneId zone = ZoneId.of("GMT");
                    ZonedDateTime zonedStart = ZonedDateTime.of(desiredStart, zone);
                    ZonedDateTime zonedEnd = ZonedDateTime.of(desiredEnd, zone);
                    
                    
                    if(desiredStart.equals(beginning)){
                        throw new ScheduleConflictException("Conflicts with existing appointment");
                    }
                    
                    if(desiredStart.isAfter(beginning) && desiredStart.isBefore(ending)){
                        throw new ScheduleConflictException("Conflicts with existing appointment");
                    }
                    
                    if(desiredEnd.isAfter(beginning) && desiredEnd.isBefore(ending)){
                        throw new ScheduleConflictException("Conflicts with existing appointment");
                    }
                    if(desiredStart.isAfter(desiredEnd)){
                        throw new TimeTravelException("Attempt to schedule beginning after end");
                    }
                }
                
                //Convert to GMT
                ZoneId zone = ZoneId.of(TimeZone.getDefault().getID());
                ZonedDateTime zonedStart = ZonedDateTime.of(desiredStart, zone);
                ZonedDateTime zonedEnd = ZonedDateTime.of(desiredEnd, zone);
                Instant startInstant = zonedStart.toInstant();
                Instant endInstant = zonedEnd.toInstant();
                
                start = String.valueOf(startInstant);
                start = start.substring(0, 19);
                
                end = String.valueOf(endInstant);
                end = end.substring(0, 19);
                
                //Save appointment to the database
                Appointment appointment = new Appointment(customerID, uID, title, description, location, contact, url, start, end);
                
                //Display message to verify successful creation of appointment
                int id1 = appointment.getID();
                notification("Appointment " + id1 + " saved successfully to the database", 450, 150);
                
                //Close the frame
                frame.dispose();
                
            }catch(SQLException ex){
                notification("An error has occured", 400, 150);
            }catch(OutsideBusinessHoursException ex){
                notification("Appointment must be during business hours", 400, 150);
            }catch(ScheduleConflictException ex){
                notification("The chosen time conflicts with an existing appointment", 500, 150);
            }catch(NumberFormatException ex){
                notification("Please ensure that date and customer ID are correct", 450, 150);
            }catch(StringIndexOutOfBoundsException ex){
                notification("You must enter a valid date (i.e. YYYY-MM-DD)", 450, 150);
            }catch(DateTimeException ex){
                notification("You must enter a valid date (i.e. YYYY-MM-DD)", 450, 150);
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }catch(BlankFieldException ex){
                notification("Please ensure that all fields are completed", 450, 150);
            }catch(InvalidIDNumberException ex){
                notification("That customer could not be found", 350, 150);
            }catch(TimeTravelException ex){
                notification("The start cannot be scheduled after the end", 350, 150);
            }
        });
  
  //Add functionality to the cancel button
        cancelBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                frame.dispose();
            }
            catch(Exception ex){
                
            }
        });

  
  //Add panels to the frame
        frame.add(p1, BorderLayout.NORTH);
        frame.add(p2, BorderLayout.CENTER);
        frame.add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
  
//This method allows a user to update a previously created appointment
    public static void updateAppointment(){
    //Create Labels
        JLabel headingLbl = new JLabel("Update Appointment");
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
  
        JLabel appointmentLbl = new JLabel("Appointment ID");
        JLabel selectedLbl = new JLabel("Title");
        JLabel startLbl = new JLabel("Start Time");
        JLabel endLbl = new JLabel("End Time");
  
  //Create text fields
        JTextField appointmentField = new JTextField("");
        JTextField selectedField = new JTextField("");
  
  //Create combo boxes
        String[] fields = {"Title", "Description", "Location", "Contact", "URL", "Time"};
        String[] hours = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        String[] minutes = {"00", "15", "30", "45"};
  
        JComboBox<String> fieldCB = new JComboBox<>(fields);
        fieldCB.setVisible(true);
  
        JComboBox<String> startHourCB = new JComboBox<>(hours);
        startHourCB.setVisible(true);
        JComboBox<String> startMinCB = new JComboBox<>(minutes);
        startMinCB.setVisible(true);
  
        JComboBox<String> endHourCB = new JComboBox<>(hours);
        endHourCB.setVisible(true);
        JComboBox<String> endMinCB = new JComboBox<>(minutes);
        endMinCB.setVisible(true);
  
  //create radio buttons and groups
        JRadioButton amBtn = new JRadioButton("AM");
        amBtn.setSelected(true);
  
        JRadioButton pmBtn = new JRadioButton("PM");
  
        JRadioButton am2Btn = new JRadioButton("AM");
        am2Btn.setSelected(true);
  
        JRadioButton pm2Btn = new JRadioButton("PM");
  
        ButtonGroup group = new ButtonGroup();
        group.add(amBtn);
        group.add(pmBtn);
  
        ButtonGroup group2 = new ButtonGroup();
        group2.add(am2Btn);
        group2.add(pm2Btn);
  
  //Create Buttons
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
  
  //Create frame and panels
        JFrame frame = new JFrame("Update Appointment");
        frame.setSize(450, 300);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
  
        JPanel p1 = new JPanel(new FlowLayout());
        p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
  
        JPanel p2 = new JPanel(new GridLayout(3, 2, 5, 5));
        p2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
  
        JPanel p3 = new JPanel(new FlowLayout());
        p3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
  
        JPanel startPanel = new JPanel(new FlowLayout());
        JPanel endPanel = new JPanel(new FlowLayout());
        JPanel selectPanel = new JPanel(new FlowLayout());
  
        selectPanel.add(fieldCB);
        startPanel.add(startHourCB);
        startPanel.add(startMinCB);
        startPanel.add(amBtn);
        startPanel.add(pmBtn);
  
        endPanel.add(endHourCB);
        endPanel.add(endMinCB);
        endPanel.add(am2Btn);
        endPanel.add(pm2Btn);
  
  //Add components to the panels
        p1.add(headingLbl);
        p1.add(selectPanel);
  
        p2.add(appointmentLbl);
        appointmentLbl.setLabelFor(appointmentField);
        p2.add(appointmentField);

        p2.add(selectedLbl);
        selectedLbl.setLabelFor(selectedField);
        p2.add(selectedField);
  
        p3.add(cancelBtn);
        p3.add(saveBtn);

  //Set time variables
        startHour = (String)startHourCB.getSelectedItem();
        startMin = (String)startMinCB.getSelectedItem();
        endHour = (String)endHourCB.getSelectedItem();
        endMin = (String)endMinCB.getSelectedItem();
  
//Add functionality to the radio buttons
        amBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int start = Integer.parseInt(startHour) - 12;
            startHour = Integer.toString(start);
                });

        pmBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int start = Integer.parseInt(startHour) + 12;
            startHour = Integer.toString(start);
                });

        am2Btn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int end = Integer.parseInt(endHour) - 12;
            endHour = Integer.toString(end);
                });

        pm2Btn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            int end = Integer.parseInt(endHour) + 12;
            endHour = Integer.toString(end);
                });
  
//Add ActionListeners to start time and end time combo boxes
        startHourCB.addActionListener((ActionEvent e) ->{ /*Lambda used to improve ActionListener efficiency*/
            startHour = (String)startHourCB.getSelectedItem();
        });
        startMinCB.addActionListener((ActionEvent e)->{ /*Lambda used to improve ActionListener efficiency*/
            startMin = (String)startMinCB.getSelectedItem();
        });
        endHourCB.addActionListener((ActionEvent e)->{ /*Lambda used to improve ActionListener efficiency*/
            endHour = (String)endHourCB.getSelectedItem();
        });
        endMinCB.addActionListener((ActionEvent e)->{ /*Lambda used to improve ActionListener efficiency*/
            endMin = (String)endMinCB.getSelectedItem();
        });

//Alter the display based on which field the user chooses from the combobox
        fieldCB.addActionListener((ActionEvent e) ->{ /*Lambda used to improve ActionListener efficiency*/
            int selected = fieldCB.getSelectedIndex();
    
            switch(selected){
                case 0: 
                    column = "title";
                    selectedLbl.setText("Title");
                    selectedField.setText("");
                    appointmentField.setText("");
                    
                    frame.setSize(450, 300);
                    p2.setLayout(new GridLayout(3, 2, 5, 5));
                    
                    p2.removeAll();
             
                    p2.add(appointmentLbl);
                    appointmentLbl.setLabelFor(appointmentField);
                    p2.add(appointmentField);

                    p2.add(selectedLbl);
                    selectedLbl.setLabelFor(selectedField);
                    p2.add(selectedField);
            
                    p2.repaint();
                    break;
                case 1:
                    column = "description";
                    selectedLbl.setText("Descpription");
                    selectedField.setText("");
                    appointmentField.setText("");
                    p2.removeAll();
                    
                    frame.setSize(450, 300);
                    p2.setLayout(new GridLayout(3, 2, 5, 5));
             
                    p2.add(appointmentLbl);
                    appointmentLbl.setLabelFor(appointmentField);
                    p2.add(appointmentField);

                    p2.add(selectedLbl);
                    selectedLbl.setLabelFor(selectedField);
                    p2.add(selectedField);
            
                    p2.repaint();
                    break;
                case 2:
                    column = "location";
                    selectedLbl.setText("Location");
                    selectedField.setText("");
                    appointmentField.setText("");
                    p2.removeAll();
                    
                    frame.setSize(450, 300);
                    p2.setLayout(new GridLayout(3, 2, 5, 5));
             
                    p2.add(appointmentLbl);
                    appointmentLbl.setLabelFor(appointmentField);
                    p2.add(appointmentField);

                    p2.add(selectedLbl);
                    selectedLbl.setLabelFor(selectedField);
                    p2.add(selectedField);
            
                    p2.repaint();
                    break;
                case 3:
                    column = "contact";
                    selectedLbl.setText("Contact");
                    selectedField.setText("");
                    appointmentField.setText("");
                    p2.removeAll();
                    
                    frame.setSize(450, 300);
                    p2.setLayout(new GridLayout(3, 2, 5, 5));
             
                    p2.add(appointmentLbl);
                    appointmentLbl.setLabelFor(appointmentField);
                    p2.add(appointmentField);

                    p2.add(selectedLbl);
                    selectedLbl.setLabelFor(selectedField);
                    p2.add(selectedField);
            
                    p2.repaint();
                    break;
                case 4:
                    column = "url";
                    selectedLbl.setText("URL");
                    selectedField.setText("");
                    appointmentField.setText("");
                    p2.removeAll();
                    
                    frame.setSize(450, 300);
                    p2.setLayout(new GridLayout(3, 2, 5, 5));
             
                    p2.add(appointmentLbl);
                    appointmentLbl.setLabelFor(appointmentField);
                    p2.add(appointmentField);

                    p2.add(selectedLbl);
                    selectedLbl.setLabelFor(selectedField);
                    p2.add(selectedField);
            
                    p2.repaint();
                    break;
                case 5:
                    column = "time";
                    selectedLbl.setText("Start Date");
                    selectedField.setText("");
                    appointmentField.setText("");
                    
                    frame.setSize(450, 350);
                    p2.setLayout(new GridLayout(4, 2, 5, 5));
                    
                    p2.removeAll();
             
                    p2.add(appointmentLbl);
                    appointmentLbl.setLabelFor(appointmentField);
                    p2.add(appointmentField);
                    
                    p2.add(selectedLbl);
                    selectedLbl.setLabelFor(selectedField);
                    p2.add(selectedField);

                    p2.add(startLbl);
                    startLbl.setLabelFor(startPanel);
                    p2.add(startPanel);
                    
                    p2.add(endLbl);
                    endLbl.setLabelFor(endPanel);
                    p2.add(endPanel);
            
                    p2.repaint();
            
                    break;
                default:
                    column = "title";
            }
        });

  //Add functionality to the save button
        saveBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
      //Set variable values
                String sec = ":00";
                String value = selectedField.getText();
                String appt = appointmentField.getText();
                String startTime = startHour + ":" + startMin + sec;
                String endTime = endHour + ":" + endMin + sec;
                
                LocalTime beginBusiness = LocalTime.of(9, 0);
                LocalTime endBusiness = LocalTime.of(17, 0);
                
                //Throw exception for blank fields
                if(value.equals("") || appt.equals("")){
                    throw new BlankFieldException("Field left blank");
                }
                
                //Throw exception if appointment not found
                int apptID = Integer.parseInt(appointmentField.getText());
                String query = "SELECT appointmentID FROM appointment WHERE appointmentID = " + apptID;
      
                Appointment appointment = new Appointment();
   
                int testID = appointment.getID(query);
                if(testID == 0){
                    throw new InvalidIDNumberException("Appointment not found");
                }
                
                //Get userID for update method
                User u = new User();
                int uID = u.getID(user, pass);
 
                //Set value of new time and test it for conflicts 
                if(column.equals("time")){
                //Trim value to ensure date is not too long
                    String date = value.substring(0, 10);
                    
                    String startColumn = "start";
                    String endColumn = "end";
                    String s = date + 'T' + startTime;
                    String en = date + 'T' + endTime;

                    int month = Integer.parseInt(date.substring(5, 7));
                    int year = Integer.parseInt(date.substring(0, 4));
                    int day = Integer.parseInt(date.substring(8, 10));
                    int strtHr = Integer.parseInt(startHour);
                    int strtMin = Integer.parseInt(startMin);
                    int endHr = Integer.parseInt(endHour);
                    int eMin = Integer.parseInt(endMin);
                    
                    LocalTime desiredStartTime = LocalTime.of(strtHr, strtMin);
                    LocalTime desiredEndTime = LocalTime.of(endHr, eMin);
                    LocalDateTime desiredStart = LocalDateTime.of(year, month, day, strtHr, strtMin);
                    LocalDateTime desiredEnd = LocalDateTime.of(year, month, day, endHr, eMin);
                    LocalDate d = LocalDate.of(year, month, day);
                    
                    int dayCount = d.lengthOfMonth();
                     
                    Appointment temp = new Appointment();
                    StringBuilder[] existingAppointments = temp.getAppointments(month, year, dayCount, uID);

                    for(int i = 0; i < existingAppointments.length; i++){
                        StringBuilder start = new StringBuilder(existingAppointments[i].substring(0, 19));
                        StringBuilder end = new StringBuilder(existingAppointments[i].substring(20, 39));
                        
                        
                        ZonedDateTime gmtBeginning = LocalDateTime.parse(start, 
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
                        ZonedDateTime gmtEnding = LocalDateTime.parse(end, 
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
                    
                        LocalDateTime beginning = gmtBeginning.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                        LocalDateTime ending = gmtEnding.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
                        
                        
                        if(desiredStart.equals(beginning)){
                            throw new ScheduleConflictException("Conflicts with existing appointment");
                        }
                        if(desiredStart.isAfter(beginning) && desiredStart.isBefore(ending)){
                            throw new ScheduleConflictException("Conflicts with existing appointment");
                        }
                        if(desiredEnd.isAfter(beginning) && desiredEnd.isBefore(ending)){
                            throw new ScheduleConflictException("Conflicts with existing appointment");
                        }
                        if(desiredStartTime.isBefore(beginBusiness) || desiredStartTime.isAfter(endBusiness)){
                            throw new OutsideBusinessHoursException("Selected start time is outside business hours");
                        }
                        if(desiredEndTime.isBefore(beginBusiness) || desiredEndTime.isAfter(endBusiness)){
                            throw new OutsideBusinessHoursException("Selected end time is outside business hours");
                        }
                        if(desiredStart.isAfter(desiredEnd)){
                            throw new TimeTravelException("Attempted to schedule start after end");
                        }
                    }
                    
                    //Convert to GMT
                    ZoneId zone = ZoneId.of(TimeZone.getDefault().getID());
                    ZonedDateTime zonedStart = ZonedDateTime.of(desiredStart, zone);
                    ZonedDateTime zonedEnd = ZonedDateTime.of(desiredEnd, zone);
                    Instant startInstant = zonedStart.toInstant();
                    Instant endInstant = zonedEnd.toInstant();
                
                    s = String.valueOf(startInstant);
                    s = s.substring(0, 19);
                
                    en = String.valueOf(endInstant);
                    en = en.substring(0, 19);
                    
                    //Update appointment time
                    appointment.update(s, startColumn, uID, apptID);
                    appointment.update(en, endColumn, uID, apptID);
                }
                else{
                    //Update specified appointment value
                    appointment.update(value, column,  uID, apptID);
                }
                notification("Appointment successfully updated", 450, 150);
                frame.dispose();
            }
            catch(SQLException ex){
                notification("Please enter a valid date (i.e. YYYY-MM-DD)", 400, 150);
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(BlankFieldException ex){
                notification("Fields may not be left blank", 325, 150);
            }
            catch(InvalidIDNumberException ex){
                notification("Sorry, that appointment could not be found", 400, 150);
            }
            catch(ScheduleConflictException ex){
                notification("Sorry, that conflicts with an existing appointment", 450, 150);
            }
            catch(NumberFormatException ex){
                notification("Please ensure that appointment ID and date are entered correctly", 550, 150);
            }
            catch(TimeTravelException ex){
                notification("The start cannot be scheduled for a later time than the end", 500, 150);
            }
            catch(OutsideBusinessHoursException ex){
                notification("The selected time is outside business hours", 400, 150);
            }
            catch(StringIndexOutOfBoundsException ex){
                //notification("Please enter a valid date (i.e. YYYY-MM-DD)", 400, 150);
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch(DateTimeException ex){
                notification("Please enter a valid date (i.e. YYYY-MM-DD)", 400, 150);
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
                });
  
  //Add functionality to the cancel button
        cancelBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
          frame.dispose();
        });

  //Add panels to the frame
        frame.add(p1, BorderLayout.NORTH);
        frame.add(p2, BorderLayout.CENTER);
        frame.add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    
//This method generates and displays a report containing the number of appointment types by month
    public static void displayTypes(LocalDate date) throws SQLException{
        Month mnth = date.getMonth();
        String month = String.valueOf(mnth);
        int y = date.getYear();
        int monthInt = mnth.getValue();
        String[] strings;
        String[] nums;
        JLabel[] stringLbls;
        JLabel[] numLbls;
        

        Appointment appt = new Appointment();
        String[][] types = appt.getTypes(monthInt, y);

        int size = types[0].length;
        strings = new String[size];
        nums = new String[size];
        numLbls = new JLabel[size];
        stringLbls = new JLabel[size];

        for(int i = 0; i < types[0].length; i++){
            String value = types[0][i];
            nums[i] = value;
            numLbls[i] = new JLabel();
            numLbls[i].setText(nums[i]);
        }
            
        for(int i = 0; i < types[1].length; i++){
            String value = types[1][i];
            strings[i] = value;
            stringLbls[i] = new JLabel();
            stringLbls[i].setText(strings[i]);
        }
        
        //Create Labels
        JLabel headingLbl = new JLabel(month);
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        
        //Create Buttons
        JButton forwardBtn = new JButton(">>");
        JButton backBtn = new JButton("<<");
        
        //Create frame and panels
        JFrame frame = new JFrame("Report");
        frame.setSize(450, 375);
        frame.setLocationRelativeTo(null);
        
        JPanel p1 = new JPanel(new FlowLayout());
        p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel p2 = new JPanel(new GridLayout(size, 2));
        p2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel p3 = new JPanel(new FlowLayout());
        p3.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //Add components to the panels
        p1.add(headingLbl);
        
        for(int i = 0; i < size; i++){
            p2.add(numLbls[i]);
            numLbls[i].setLabelFor(stringLbls[i]);
            p2.add(stringLbls[i]);
        }
        
        p3.add(backBtn);
        p3.add(forwardBtn);
        
        forwardBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                long add = 1;
                frame.dispose();
                d = d.plusMonths(add);
                displayTypes(d);
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        backBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                long subtract = 1;
                frame.dispose();
                d = d.minusMonths(subtract);
                displayTypes(d);
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //Add panels to the frame
        frame.add(p1, BorderLayout.NORTH);
        frame.add(p2, BorderLayout.CENTER);
        frame.add(p3, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

//This method creates and displays a report showing specified consultant schedule
    public static void displaySchedule() throws SQLException{
        User u = new User();
        int id = u.getID(user, pass);
        DisplaySchedule display = new DisplaySchedule(id);
    }
    
//This method generates and displays a report containing the number of customers per country
    public static void displayCustomers() throws SQLException{
        
        //Create customer object and arrays
        Customer cust = new Customer();
        String[] results = cust.getCustomersByCountry();
        int size = results.length;
        JLabel[] resultLabels = new JLabel[size];
        
        //Populate the resultLabels array
        for(int i = 0; i < resultLabels.length; i++){
            String result = results[i];
            resultLabels[i] = new JLabel(result);
        }
        
        //Create Labels
        JLabel headingLbl = new JLabel("Customers per Country");
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        
        //Create frame and panels
        JFrame frame = new JFrame("Report");
        frame.setSize(450, 375);
        frame.setLocationRelativeTo(null);
        
        JPanel p1 = new JPanel(new FlowLayout());
        p1.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel p2 = new JPanel(new GridLayout(size, 2));
        p2.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel mainPnl = new JPanel(new BorderLayout());
        
        //Add components to the panels
        p1.add(headingLbl);
        
        for(int i = 0; i < size; i++){
            p2.add(resultLabels[i]);
        }
        
        mainPnl.add(p1, BorderLayout.NORTH);
        mainPnl.add(p2, BorderLayout.CENTER);
        
        //Create scrollpane
        JScrollPane sp = new JScrollPane(mainPnl);
        
        //Add scrollpane to the frame
        frame.add(sp);
        frame.setVisible(true);
    }
    
    //This method will launch the main application
    public static void launch(){
        mainScreen();
    }
}