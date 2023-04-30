import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Container;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.*;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

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

			  // Display the list of rows in a dialog box
			  JOptionPane.showMessageDialog(null, rows.toString());			  

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

	static void showRows(ArrayList<String> listOfRows, JFrame frame) {
		
		// Get the content pane of the frame
		Container contentPane = frame.getContentPane();

		// Remove any existing components from the content pane
		contentPane.removeAll();
		
		frame.setTitle("My First GUI - Cancel Bookings");

		// Create a table model and add columns
		DefaultTableModel tableModel = new DefaultTableModel();
		String[] headers = { "ID", "Name", "Email", "Phone", "Location", "Date", "" };
		tableModel.setColumnIdentifiers(headers);

		// Add rows to the table model
		for (String row : listOfRows) {
			String[] rowData = row.split(",");
			tableModel.addRow(rowData);
		}

		// Create a JTable with the table model
		JTable table = new JTable(tableModel);

		// Create a custom cell renderer for the last column
		TableColumn column = table.getColumnModel().getColumn(6);
		column.setCellRenderer(new ButtonRenderer());

		// Set the preferred size of the table and add it to a scroll pane
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		JScrollPane scrollPane = new JScrollPane(table);

		// Add the scroll pane to the content pane's CENTER position
		contentPane.add(scrollPane, BorderLayout.CENTER);

		// Set the content pane of the frame and make it visible
		frame.setContentPane(contentPane);
		frame.setVisible(true);
	}

	// Custom cell renderer for the last column
	static class ButtonRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
			setOpaque(true);
		}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			JButton button = new JButton("Cancel Booking");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Handle button click event
					System.out.println("View button clicked for row " + row);
				}
			});
			return button;
		}
	}
	
	public static void main(String args[]) {
		
		JFrame frame = new JFrame("My First GUI - Show Bookings");
		
		frame.setSize(1024, 768);
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
