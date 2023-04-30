import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Container;
import java.awt.Color;

import java.awt.event.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

import javax.swing.table.*;

class gui {
	
	static ArrayList<String> retrieveRows(JFrame frame){

		// Use BorderLayout for the content pane
		JPanel contentPane = new JPanel(new BorderLayout());

		// Create a panel with FlowLayout and add the button to it
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton button1 = new JButton("Show Bookings");
		button1.setPreferredSize(new Dimension(150, 50));
		buttonPanel.add(button1);
		
		// Create empty list of rows
		ArrayList<String> rows = new ArrayList<String>();

		// Add a listener to the button
		button1.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			try {
			  Class.forName("com.mysql.cj.jdbc.Driver");
			  // Establish a connection to the database
			  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/restaurantbooking", "cccc", "edcrfv456");
			  Statement stmt = conn.createStatement();

			  // Execute a query to retrieve the rows from the booking table
			  ResultSet rs = stmt.executeQuery("SELECT * FROM booking WHERE theDate >= CURDATE() ORDER BY theDate ASC");
			  
			  // Iterate over the result set to retrieve the rows and store them in a list or array
			  while (rs.next()) {
				  
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");	
				String phone = rs.getString("phone");	
				String location = rs.getString("location");	
				String theDate = rs.getString("theDate");	
				
				String row = id + "," + name + "," + email + "," + phone + "," + location + "," + theDate;
				rows.add(row);
			  }
			  
			  if(rows.isEmpty()){
				rows.add("No Data" + "," + "No Data" + "," + "No Data" + "," + "No Data" + "," + "No Data" + "," + "No Data");  
			  }		  

			  // Close the database connection
			  rs.close();
			  stmt.close();
			  conn.close();
			  
			  showRows(rows,frame);	
			  
			} catch (ClassNotFoundException ex) {
			  ex.printStackTrace();
			} catch (SQLException ex) {
			  ex.printStackTrace();
			}
		  }
		});

		// Add the button panel to the content pane's SOUTH position
		contentPane.add(buttonPanel, BorderLayout.SOUTH);

		frame.setContentPane(contentPane);
		frame.setVisible(true);

		return rows;
		
	}
	

	static void showRows(ArrayList<String> rows, JFrame frame){
		
		// Clear the frame's content pane
		frame.getContentPane().removeAll();
		frame.repaint();
		
		// Create a panel with BorderLayout and add it to the frame's content pane
		JPanel panel = new JPanel(new BorderLayout());
		frame.getContentPane().add(panel);

		// Create a JTable with the number of rows equal to the size of the rows arraylist and six columns
		String[] columnNames = {"ID", "Name", "Email", "Phone", "Location", "Date"};
		Object[][] rowData = new Object[rows.size()][6];
		for (int i = 0; i < rows.size(); i++) {
			String[] row = rows.get(i).split(",");
			for (int j = 0; j < row.length; j++) {
				rowData[i][j] = row[j];
			}
		}
		JTable table = new JTable(rowData, columnNames);

		// Set the table's column widths
		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(30);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(150);
		columnModel.getColumn(3).setPreferredWidth(100);
		columnModel.getColumn(4).setPreferredWidth(100);
		columnModel.getColumn(5).setPreferredWidth(100);

		// Add the table to a JScrollPane and add the JScrollPane to the panel
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);

		// Add a cancel button to the panel
		JButton cancelButton = new JButton("Cancel Booking");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					String bookingId = (String) table.getValueAt(selectedRow, 0);
					cancelBooking(bookingId,rows,frame);
					rows.remove(selectedRow);
					((AbstractTableModel) table.getModel()).fireTableDataChanged();
				} else {
					JOptionPane.showMessageDialog(frame, "Please select a row to cancel.");
				}
			}
		});
		
		// Set the preferred size of the cancel button
		cancelButton.setPreferredSize(new Dimension(150, 50));
		
		// Add a cancel button to a separate JPanel and add this panel to the SOUTH position of the main panel
		JPanel buttonPanel = new JPanel(new FlowLayout());
		buttonPanel.add(cancelButton);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		// Set the frame's size and make it visible
		frame.setPreferredSize(new Dimension(1024, 600));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	
	public static void cancelBooking(String bookingId,ArrayList<String> rows, JFrame frame) {
		// Perform the logic to cancel the booking with the given ID
		// For example, you might remove the booking from a database or mark it as cancelled
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			// Establish a connection to the database
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/restaurantbooking", "dddd", "uhbygv123");
			Statement stmt = conn.createStatement();

			// Execute a query to delete the row with the given ID
			stmt.executeUpdate("DELETE FROM booking WHERE id = " + bookingId);
			stmt.close();
			conn.close();
			
			// Show a message to the user to indicate that the booking has been cancelled
			JOptionPane.showMessageDialog(null, "Booking " + bookingId + " has been cancelled.");

			} catch (ClassNotFoundException ex) {
				
			// Show a message to the user to indicate that the booking has been cancelled
			JOptionPane.showMessageDialog(null, "Booking " + bookingId + " CANNOT cancelled.");	
				
			ex.printStackTrace();
			
			} catch (SQLException ex) {
				
			// Show a message to the user to indicate that the booking has been cancelled
			JOptionPane.showMessageDialog(null, "Booking " + bookingId + " CANNOT cancelled.");		
				
			ex.printStackTrace();
		}		
		
		ArrayList<String> newRows = new ArrayList<String>();
		
		try {
		  Class.forName("com.mysql.cj.jdbc.Driver");
		  // Establish a connection to the database
		  Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/restaurantbooking", "cccc", "edcrfv456");
		  Statement stmt = conn.createStatement();

		  // Execute a query to retrieve the rows from the booking table
		  ResultSet rs = stmt.executeQuery("SELECT * FROM booking WHERE theDate >= CURDATE() ORDER BY theDate ASC");
		  
		  // Iterate over the result set to retrieve the rows and store them in a list or array
		  while (rs.next()) {
			  
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String email = rs.getString("email");	
			String phone = rs.getString("phone");	
			String location = rs.getString("location");	
			String theDate = rs.getString("theDate");	
			
			String row = id + "," + name + "," + email + "," + phone + "," + location + "," + theDate;
			newRows.add(row);
		  }
		  
		  if(newRows.isEmpty()){
			newRows.add("No Data" + "," + "No Data" + "," + "No Data" + "," + "No Data" + "," + "No Data" + "," + "No Data");  
		  }		  

		  // Close the database connection
		  rs.close();
		  stmt.close();
		  conn.close();
		  
		  showRows(newRows,frame);	
		  
		} catch (ClassNotFoundException ex) {
		  ex.printStackTrace();
		} catch (SQLException ex) {
		  ex.printStackTrace();
		}
	}
	
	public static void main(String args[]) {
		
		JFrame frame = new JFrame("My First GUI - Show Bookings");
		
		frame.setSize(1024, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Load rows
		ArrayList<String> listOfRows = new ArrayList<String>();
		
		listOfRows = retrieveRows(frame);
		
		//if(!listOfRows.isEmpty()){
		//	//Display the rows on the frame
		//	showRows(listOfRows,frame);			
		//}
			
		
	}
}