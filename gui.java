import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;

class gui {
  public static void main(String args[]) {
    JFrame frame = new JFrame("My First GUI");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1024, 768);
    JButton button1 = new JButton("Show Bookings");
	button1.setPreferredSize(new Dimension(150, 50));

    // Add a listener to the button
    button1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        try {
		  Class.forName("com.mysql.cj.jdbc.Driver");
          // Establish a connection to the database
          Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/restaurantbooking", "cccc", "edcrfv456");
          Statement stmt = conn.createStatement();

          // Execute a query to retrieve the dates from the booking table
          ResultSet rs = stmt.executeQuery("SELECT theDate FROM booking");

          // Iterate over the result set to retrieve the dates and store them in a list or array
          ArrayList<String> dates = new ArrayList<String>();
          while (rs.next()) {
            String date = rs.getString("theDate");
            dates.add(date);
          }

          // Display the list of dates in a dialog box
          JOptionPane.showMessageDialog(null, dates.toString());

          // Close the database connection
          rs.close();
          stmt.close();
          conn.close();
		  } catch (ClassNotFoundException ex) {
			  ex.printStackTrace();
		  } catch (SQLException ex) {
			  ex.printStackTrace();
		  }
      }
    });

    frame.getContentPane().add(button1);
    frame.setVisible(true);
  }
}
