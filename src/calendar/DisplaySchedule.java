/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendar;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author April
 */
public class DisplaySchedule extends JFrame{
    public DisplaySchedule(int userID) throws SQLException{
        super("Schedule");
    
        User u = new User();
        String user = u.getUserName(userID);
        String[][][] schedule = u.getSchedule(userID);
        
        int half = schedule.length / 2;
        int size = 0;
        int x = 0;
            
    //Get needed array size
        for(int i = 0; i < schedule.length; i++){
            for(int col = 0; col < schedule[i].length; col ++){
                size = schedule[i][col].length * 2;
            }
        }
            
    //Declare and initialize the arrays
        String[] locDesc = new String[size];
        String[] times = new String[size];
            
        size = size / 2;
        JLabel[] locations = new JLabel[size];
        JLabel[] titles = new JLabel[size];
        JLabel[] starts = new JLabel[size];
        JLabel[] ends = new JLabel[size];
            
            
    //Populate the array that holds titles and locations
        for(int i = 0; i < half; i++){
            for(int row = 0; row < schedule[i].length; row++){
                for(int column = 0; column < schedule[i][row].length; column++){
                    locDesc[x] = schedule[i][row][column];
                    x++;
                }
            }
        }
            
    //Populate the array that holds the times
        x = 0;
        for(int i = half; i < half * 2; i++){
            for(int row = 0; row < schedule[i].length; row++){
                for(int column = 0; column < schedule[i][row].length; column++){
                    times[x] = schedule[i][row][column];
                    x++;
                }
            }
        }
            
    //Split the locDesc array into two arrays
        half = locDesc.length / 2;
        for(int i = 0; i < half; i++){
            String value = locDesc[i];
            titles[i] = new JLabel();
            titles[i].setText(value);
        }
            
        x = 0;
        for(int i = half; i < half * 2; i++){
            String value = locDesc[i];
            locations[x] = new JLabel();
            locations[x].setText(value);
            x++;
        }
            
    //Split the times array into two separate arrays
        half = times.length / 2;
        for(int i = 0; i < half; i++){
            String value = times[i];

            //Convert back to local time
            ZonedDateTime gmtTime = LocalDateTime.parse(value, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
            LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            value = localTime.toString();
            
            starts[i] = new JLabel();
            starts[i].setText(value);
        }
            
        x = 0;
        for(int i = half; i < half * 2; i++){
            String value = times[i];
            
            //Convert back to local time
            ZonedDateTime gmtTime = LocalDateTime.parse(value, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.of("GMT"));
            LocalDateTime localTime = gmtTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
            value = localTime.toString();
            
            ends[x] = new JLabel();
            ends[x].setText(value);
            x++;
        }
 
        //Create Labels
        JLabel headingLbl = new JLabel(user);
        headingLbl.setFont(new Font("Verdana", Font.BOLD, 18));
        
        JLabel titleLbl = new JLabel("Title");
        titleLbl.setFont(new Font("Verdana", Font.BOLD, 14));
        
        JLabel locationLbl = new JLabel("Location");
        locationLbl.setFont(new Font("Verdana", Font.BOLD, 14));
        
        JLabel startLbl = new JLabel("Start Time");
        startLbl.setFont(new Font("Verdana", Font.BOLD, 14));
        
        JLabel endLbl = new JLabel("End Time");
        endLbl.setFont(new Font("Verdana", Font.BOLD, 14));
        
        //Create text field
        JTextField searchField = new JTextField();
        searchField.setColumns(10);
        
        //Create Buttons
        JButton searchBtn = new JButton("Search");
        
        //Create frame and panels
        this.setSize(950, 575);
        this.setLocationRelativeTo(null);
        
        JPanel namePnl = new JPanel(new GridLayout(2, 1));
        namePnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel labelPnl = new JPanel(new GridLayout(1, 4));
        labelPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel titlePnl = new JPanel(new GridLayout(size, 1));
        titlePnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel locationPnl = new JPanel(new GridLayout(size, 1));
        locationPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel startPnl = new JPanel(new GridLayout(size, 1));
        startPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel endPnl = new JPanel(new GridLayout(size, 1));
        endPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel schedulePnl = new JPanel(new GridLayout(1, 4));
        schedulePnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel searchPnl = new JPanel(new FlowLayout());
        searchPnl.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        //Add components to the panels
        labelPnl.add(titleLbl);
        labelPnl.add(locationLbl);
        labelPnl.add(startLbl);
        labelPnl.add(endLbl);
        
        namePnl.add(headingLbl);
        namePnl.add(labelPnl);

        
       //Add the schedule info to the appropriate panels
        for(int i = 0; i < size; i++){
            titlePnl.add(titles[i]);
            locationPnl.add(locations[i]);
            startPnl.add(starts[i]);
            endPnl.add(ends[i]);
        }
        

        schedulePnl.add(titlePnl);
        schedulePnl.add(locationPnl);
        schedulePnl.add(startPnl);
        schedulePnl.add(endPnl);

        searchPnl.add(searchField);
        searchPnl.add(searchBtn);
        
        searchBtn.addActionListener((ActionEvent e) -> { /*Lambda used to improve ActionListener efficiency*/
            try{
                int id = Integer.parseInt(searchField.getText());
                this.dispose();
                DisplaySchedule display = new DisplaySchedule(id);
            }
            catch(Exception ex){
                Logger.getLogger(Calendar.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //Create main panel and add the other panels to it
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(namePnl, BorderLayout.NORTH);
        mainPanel.add(schedulePnl, BorderLayout.CENTER);
        mainPanel.add(searchPnl, BorderLayout.SOUTH);
          
        //Create the scroll pane
        JScrollPane sp = new JScrollPane(mainPanel);

        //Display the schedule
        this.add(sp);
        this.setVisible(true);
    }
    
}