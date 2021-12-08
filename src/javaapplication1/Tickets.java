package javaapplication1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class Tickets extends JFrame implements ActionListener {

	// class level member objects
	Dao dao = new Dao(); // for CRUD operations
	Boolean chkIfAdmin = null;

	// Main menu object items
	private JMenu mnuFile = new JMenu("File");
	private JMenu mnuAdmin = new JMenu("Admin");
	private JMenu mnuTickets = new JMenu("Tickets");

	// Sub menu item objects for all Main menu item objects
	JMenuItem mnuItemExit;
	JMenuItem mnuItemUpdate;
	JMenuItem mnuItemDelete;
	JMenuItem mnuItemClose;
	JMenuItem mnuItemOpenTicket;
	JMenuItem mnuItemViewTicket;

	String uname;
	DefaultTableModel model;
	JTable jt;
	JScrollPane sp;

//	public Tickets(Boolean isAdmin) {
//
//		chkIfAdmin = isAdmin;
//		createMenu();
//		prepareGUI();
//
//	}

	public Tickets(Boolean isAdmin, String uname) throws SQLException {

		chkIfAdmin = isAdmin;
		createMenu();
		prepareGUI();
		this.uname = uname;
		this.model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));
		this.jt = new JTable(model);
		this.sp = new JScrollPane(jt);

	}

	private void createMenu() {

		/* Initialize sub menu items **************************************/

		// initialize sub menu item for File main menu
		mnuItemExit = new JMenuItem("Exit");
		// add to File main menu item
		mnuFile.add(mnuItemExit);

		// initialize first sub menu items for Admin main menu
		mnuItemUpdate = new JMenuItem("Update Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemUpdate);

		// initialize second sub menu items for Admin main menu
		mnuItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mnuAdmin.add(mnuItemDelete);

		// initialize first sub menu item for Tickets main menu
		mnuItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemOpenTicket);

		// initialize first sub menu items for Tickets main menu
		mnuItemClose = new JMenuItem("Close Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemClose);

		// initialize second sub menu item for Tickets main menu
		mnuItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mnuTickets.add(mnuItemViewTicket);

		// initialize any more desired sub menu items below

		/* Add action listeners for each desired menu item *************/
		mnuItemExit.addActionListener(this);
		mnuItemUpdate.addActionListener(this);
		mnuItemClose.addActionListener(this);
		mnuItemDelete.addActionListener(this);
		mnuItemOpenTicket.addActionListener(this);
		mnuItemViewTicket.addActionListener(this);

		/*
		 * continue implementing any other desired sub menu items (like for update and
		 * delete sub menus for example) with similar syntax & logic as shown above*
		 */

	}

	private void prepareGUI() {

		// create JMenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mnuFile); // add main menu items in order, to JMenuBar
		System.out.println("chkIfAdmin: " + chkIfAdmin);

		// bar.add(mnuAdmin);
		if (chkIfAdmin == true) {
			bar.add(mnuAdmin);
		}

		bar.add(mnuTickets);
		// add menu bar components to frame
		setJMenuBar(bar);

		addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		setSize(400, 400);
		getContentPane().setBackground(Color.LIGHT_GRAY);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
		if (e.getSource() == mnuItemExit) {
			System.exit(0);
		} else if (e.getSource() == mnuItemOpenTicket) {
			try {
				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

				// get ticket information
				String ticketName = uname; // JOptionPane.showInputDialog(null, "Enter your name");
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");

				// insert ticket information to database
				System.out.println("TName: " + ticketName);
				int id = dao.insertRecords(ticketName, ticketDesc);

				// display results if successful or not to console / dialog box
				if (id != 0) {
					System.out.println("Ticket ID : " + id + " created successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket id: " + id + " created");
				} else
					System.out.println("Ticket cannot be created!!!");

				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		else if (e.getSource() == mnuItemViewTicket) {

			// retrieve all tickets details for viewing in JTable
			try {
				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				// JTable jt = new
				// JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname)));

				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);

				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == mnuItemUpdate) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				// jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin,
				// uname)));
				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

				// get ticket information
				String ticketID = JOptionPane.showInputDialog(null, "Enter the ticket id:");
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter the new ticket description: ");
				int ticket_ID = 0;
				try {
					ticket_ID = Integer.parseInt(ticketID);

					// update ticket information to database
					int id = dao.updateRecords(ticket_ID, ticketDesc);
					// display results if successful or not to console / dialog box
					if (id != 0) {
						System.out.println("Ticket ID : " + id + " updated successfully!!!");
						JOptionPane.showMessageDialog(null, "Ticket id: " + id + " updated");
					} else
						System.out.println("Ticket cannot be updated!!!");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Invalid ticket ID. Please start over.");
				}

				// jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin,
				// uname)));
				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));
				model.fireTableStructureChanged();
				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == mnuItemClose) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				// jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin,
				// uname)));
				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

				// get ticket information
				String ticketID = JOptionPane.showInputDialog(null, "Enter the ticket id:");
				int ticket_ID = 0;
				try {
					ticket_ID = Integer.parseInt(ticketID);
					int id = 0;
					int reply = JOptionPane.showConfirmDialog(null, "Close ticket " + ticket_ID, "Close Ticket",
							JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						// update ticket information to close
						id = dao.closeTicket(ticket_ID);
						// display results if successful or not to console / dialog box
						if (id != 0) {
							System.out.println("Ticket ID : " + id + " closed successfully!!!");
							JOptionPane.showMessageDialog(null, "Ticket id: " + id + " closed");
						}
					} else
						System.out.println("Ticket cannot be closed!!!");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Invalid ticket ID. Please start over.");
				}
				// jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin,
				// uname)));
				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == mnuItemDelete) {

			// retrieve all tickets details for viewing in JTable
			try {

				// Use JTable built in functionality to build a table model and
				// display the table model off your result set!!!
				// jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin,
				// uname)));
				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				// getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

				// get ticket information
				String ticketID = JOptionPane.showInputDialog(null, "Enter the ticket id:");
				int ticket_ID = 0;
				try {
					ticket_ID = Integer.parseInt(ticketID);
					int id = 0;
					int reply = JOptionPane.showConfirmDialog(null, "Delete ticket " + ticket_ID, "Delete Ticket",
							JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						// update ticket information to close
						id = dao.deleteTicket(ticket_ID);
						// display results if successful or not to console / dialog box
						if (id != 0) {
							System.out.println("Ticket ID : " + id + " deleted successfully!!!");
							JOptionPane.showMessageDialog(null, "Ticket id: " + id + " deleted");
						}
					} else
						System.out.println("Ticket cannot be deleted!!!");
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Invalid ticket ID. Please start over.");
				}
				// jt = new JTable(ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin,
				// uname)));
				model = ticketsJTable.buildTableModel(dao.readRecords(chkIfAdmin, uname));

				jt = new JTable(model);
				// jt.setModel(model);
				jt.setBounds(30, 40, 200, 400);
				sp = new JScrollPane(jt);

				getContentPane().add(sp);
				setVisible(true); // refreshes or repaints frame on screen

			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}

	}

}
