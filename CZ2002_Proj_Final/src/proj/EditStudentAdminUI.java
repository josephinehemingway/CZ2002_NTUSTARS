package proj;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Admin sub console that contains methods to edit student
 * 
 * @author DSAI/1 Group 5
 * @version 1.0
 * @since 2020-11-19
 */

public class EditStudentAdminUI {
	/**
	 * Contains the student name
	 */
	private static String studentName;
	/**
	 * Contains the student username
	 */
	private static String studentUsername;
	/**
	 * Choice of admin
	 */
	private static int choice;
	/**
	 * Scanner to read admin user input
	 */
	public static final Scanner sc = new Scanner(System.in);

	/**
	 * Method that allows the admin to add a new student
	 * 
	 * @param studentListControl studentList control object that holds the student
	 *                           list and their details.
	 */
	public static void addStudent(StudentListCtrl studentListControl) {

		Scanner sc = new Scanner(System.in);
		// Add Student Name
		System.out.println("Enter Student Name (enter '0' to exit):");
		String newStudent;
		while (true) {
			try {
				newStudent = sc.nextLine();
				if (newStudent.equals("0")) {
					System.out.println("Operation cancelled.\nExiting back to Main..");
					return;
				}
				break;
			} catch (InputMismatchException e) {
				System.out.print("Please re-enter a valid name: ");
				sc.nextLine();
			}
		}

		// Add Student Username
		String username;
		while (true) {
			System.out.println("Enter Student Username:");
			username = sc.nextLine().toUpperCase();
			for (int i = 0; i < studentListControl.getStudentList().size(); i++) {
				if (studentListControl.getStudentList().get(i).getUsername().equals(username)) {
					System.out.println("Username already exist! Please re-enter valid Username");
					username = null;
					System.out.println("");
					break;
				}
			}
			if (username != null) {
				break;
			}
		}

		// Add Student Password
		System.out.println("Enter Student Password:");
		String password;
		while (true) {
			try {
				password = sc.nextLine();
				break;
			} catch (InputMismatchException e) {
				System.out.print("Please re-enter a valid password: ");
				sc.nextLine();
			}
		}

		// Add Student MatricID
		String matricID;
		while (true) {
			System.out.println("Enter Student MatricID:");
			matricID = sc.nextLine().toUpperCase();
			for (int i = 0; i < studentListControl.getStudentList().size(); i++) {
				if (studentListControl.getStudentList().get(i).getMatricID().equals(matricID)) {
					System.out.println("Username already exist! Please re-enter valid Username");
					matricID = null;
					System.out.println("");
					break;
				}
			}
			if (matricID != null) {
				break;
			}
		}

		// Add Student Email
		String email = username + "@e.ntu.edu.sg";

		// Add Student Gender
		char gender;
		while (true) {
			System.out.println("Enter Student Gender (F/M):");
			gender = sc.nextLine().toUpperCase().charAt(0);
			if (gender == 'F' || gender == 'M') {
				break;
			}
			System.out.println("Please input a valid character!");
			System.out.println("");
		}

		// Add Student Nationality
		System.out.println("Enter Student Nationality");
		String nationality = sc.nextLine();

		// Add Student School
		String schoolID = null;
		SchoolListCtrl schoolListControl = new SchoolListCtrl();
		int count = 1;
		while (true) {
			System.out.println("Enter Student School:");
			schoolID = sc.nextLine().toUpperCase();
			for (int i = 0; i < schoolListControl.getSchoolList().size(); i++) {
				if (schoolListControl.getSchoolList().get(i).getSchoolID().equals(schoolID)) {
					count = 0;
					break;
				}
			}
			if (count == 1) {
				System.out.println("Invalid SchoolID! Please re-enter valid SchoolID");
				System.out.println("");
			} else if (count == 0) {
				break;
			}
		}

		studentListControl.addStudent(newStudent, username, password, gender, nationality, matricID, email, schoolID);
		studentListControl.printAllStudentDetails();

	}

	/**
	 * Method that allows admin to edit an existing student
	 * 
	 * @param studentListControl StudentList control object that holds the student
	 *                           list and their details.
	 * @param userValidation     Checks the validation of student
	 */
	public static void editStudent(StudentListCtrl studentListControl, UserValidation userValidation) {
		Scanner sc = new Scanner(System.in);
		ArrayList<Student> studList = studentListControl.getStudentList();
		int count = 0;
		while (true) {
			System.out.println("Enter student username to edit (enter '0' to exit): ");
			studentUsername = sc.nextLine().toUpperCase();

			if (studentUsername.equals("0")) {
				System.out.println("Operation cancelled.\nExiting back to Main..");
				return;
			}

			if (userValidation.checkValidUsername(studentUsername, studList) == false) {

				System.out.println("Invalid username. Please re-enter valid username: ");
				count += 1;
				if (count == 3) {
					System.out.println("You have entered incorrect username 3 times. Exiting...");
					return;
				}
			} else {
				System.out.println("Current particulars: ");
				studentListControl.printStudentListByUsername(studentUsername);
				break;
			}
		}

		System.out.println("\nEnter your choice: ");
		System.out.println("1. Edit Name");
		System.out.println("2. Edit Username");
		System.out.println("3. Edit Password");
		System.out.println("4. Edit MatricID");
		System.out.println("5. Edit Email");
		System.out.println("6. Edit Gender");
		System.out.println("7. Edit Nationality");

		System.out.println("8. Exit");

		while (true) {
			try {
				choice = sc.nextInt();
				break;
			} catch (InputMismatchException e) {
				System.out.println("Please re-enter a valid choice");
			}
		}
		studentListControl.editStudent(studentUsername, choice);

	}

	/**
	 * Method that allows the admin to delete an existing student
	 * 
	 * @param studentListControl StudentList control object that holds the student
	 *                           list and their details.
	 */
	public static void deleteStudent(StudentListCtrl studentListControl) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter username of student to be deleted (enter '0' to exit): ");
		String student = sc.nextLine().toUpperCase();

		if (student.equals("0")) {
			System.out.println("Operation cancelled.\nExiting back to Main..");
			return;
		}

		studentListControl.deleteStudent(student);
		studentListControl.printAllStudentDetails();
	}
}
