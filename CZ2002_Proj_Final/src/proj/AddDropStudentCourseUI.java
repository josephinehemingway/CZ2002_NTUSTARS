package proj;

import java.util.Scanner;

public class AddDropStudentCourseUI {
//	public static final Scanner scan = new Scanner(System.in);

	public static void addCourse(String username, CourseListCtrl courseListControl,
			CourseIndexListCtrl courseIndexListControl, StudentCourseListCtrl studentCourseListControl,
			StudentListCtrl studentListControl) {
		Scanner scan = new Scanner(System.in);

		courseListControl.printAllCourseDetails();

		// enter course id
		System.out.println("\nEnter choice of course (eg CZ2004): ");
		String course = courseListControl.chooseCourse();

		if (studentCourseListControl.checkIfRegistered(username, course) == false) {
			// print index in course
			System.out.println("\nCourse Indexes for Course " + course);
			courseIndexListControl.printIndexesUnderCourse(course);

			// enter course index
			System.out.println("\nEnter Course Index:");
			int courseIndex = courseIndexListControl.chooseCourseIndex(course);

			// print course index details (schedule)
			courseIndexListControl.printCourseIndexInfo(course, courseIndex);

			System.out.println("\nConfirm to add course? Enter your choice: ");
			System.out.println("1. Yes");
			System.out.println("2. No");
			int choice_confirm;

			while (true) {
				try {
					while (true) {
						choice_confirm = scan.nextInt();
						if (choice_confirm >= 1 && choice_confirm <= 2) {
							break;
						} else {
							System.out.println("Invalid choice, please enter integer 1 or 2. ");
						}
					}
					break;
				} catch (Exception e) {
					System.out.println("Invalid choice, please enter integer 1 or 2. ");
					scan = new Scanner(System.in);
				}
			}

			if (choice_confirm == 1) {
				for (int i = 0; i < courseIndexListControl.getCourseIndexSize(); i++) {
					if (courseIndexListControl.getCourseIndexList().get(i).getIndexID() == courseIndex) {
						CourseIndex c = courseIndexListControl.getCourseIndexList().get(i);
						if (studentCourseListControl.checkNewCourseClash(c, username) == false) {
							if (c.getCurrentVacancy() == 0) {
								System.out.println("\nCourse is currently fully registered----");

								for (int j = 0; j < studentListControl.getStudentListSize(); j++) {
									if (studentListControl.getStudentList().get(j).getUsername().equals(username)) {
										Student s = studentListControl.getStudentList().get(j);
										if (s.getAcadunits() > 20) {
											System.out.println("You have exceeded the Max of 21AU");
										} else {
											c.getWaitingList().add(s);
											System.out.println("You are added to the waitList for " + courseIndex);
										}
									}
								}
							}

							else {

								System.out.println("\nRegistering course " + course + "------------------");
								for (int k = 0; k < studentListControl.getStudentListSize(); k++) {
									if (studentListControl.getStudentList().get(k).getUsername().equals(username)) {
										Student s1 = studentListControl.getStudentList().get(k);
										if (s1.getAcadunits() > 20) {
											System.out.println("You have exceeded 21AUs");
											System.out.println(
													"Your current AU for this semester is : " + s1.getAcadunits());
										} else {
											c.getStudent().add(s1);
											SendMailSSL.SendRegisteredNoti(c.getCourseName(), c.getCourseID(),
													courseIndex, s1.getEmail());
											s1.addAcadunits();
											studentCourseListControl.addStudentCourse(username, course, courseIndex,
													courseIndexListControl);
											courseIndexListControl.subtractVacancy(course, courseIndex);
											System.out.println("You have successfully registered course " + course
													+ " under index " + courseIndex);
											System.out.println(
													"Your current AU for this semester is : " + s1.getAcadunits());
										}
									}

								}
							}
						}
					}
				}
			}

			else {
				System.out.println("\nCancelling addition of course ------------------");
				return;
			}
		} else
			return;
		courseIndexListControl.save();

	}

	public static void dropCourse(String username, CourseListCtrl courseListControl,
			CourseIndexListCtrl courseIndexListControl, StudentCourseListCtrl studentCourseListControl) {

		Scanner scan = new Scanner(System.in);

		// enter choice of course from list of registered courses
		System.out.println("--------------------------------------------------------");
		System.out.println("Enter choice of course: ");
		String course = studentCourseListControl.chooseCourse(username);
		int courseIndex = studentCourseListControl.getIndexOfCourse(username, course);

		System.out.println("\nConfirm to drop course? Enter your choice: ");
		System.out.println("1. Yes");
		System.out.println("2. No");
		int choice_confirm;

		while (true) {
			try {
				while (true) {
					choice_confirm = scan.nextInt();
					if (choice_confirm >= 1 && choice_confirm <= 2) {
						break;
					} else {
						System.out.println("Invalid choice, please enter integer 1 or 2. ");
					}
				}
				break;
			} catch (Exception e) {
				System.out.println("Invalid choice, please enter integer 1 or 2. ");
				scan = new Scanner(System.in);
			}
		}

		if (choice_confirm == 1) {
			System.out.println("\nDropping course " + course + " ------------------");
			for (int k = 0; k < studentCourseListControl.getStudentListCtrl().getStudentListSize(); k++) {
				if (studentCourseListControl.getStudentListCtrl().getStudentList().get(k).getUsername()
						.equals(username)) {
					Student s2 = studentCourseListControl.getStudentListCtrl().getStudent(k);
					studentCourseListControl.dropStudentCourse(username, course);
					s2.dropAcadunits();
					System.out.println("You have successfully dropped course " + course);
					System.out.println("Your current AU for this Semester is: " + s2.getAcadunits());
					courseIndexListControl.addToVacancy(course, courseIndex);

					///// auto registering Next-in-Line student in waiting list
					for (int l = 0; l < courseIndexListControl.getCourseIndexSize(); l++) {
						if (courseIndexListControl.getCourseIndexList().get(l).getIndexID() == courseIndex) {
							CourseIndex c = courseIndexListControl.getCourseIndexList().get(l);
							for (int t = 0; t < c.getStudent().size(); t++) {
								if (c.getStudent().get(t).getUsername().equals(username)) {
									Student removeStud = c.getStudent().get(t);
									c.getStudent().remove(removeStud); // not working
								}

							}

							if (c.getWaitingList().size() > 0) {
								Student nextStud = c.getWaitingList().get(0);
								studentCourseListControl.addStudentCourse(nextStud.getUsername(), course, courseIndex,
										courseIndexListControl);
								for (int m = 0; m < studentCourseListControl.getStudentListCtrl()
										.getStudentListSize(); m++) {
									if (studentCourseListControl.getStudentListCtrl().getStudentList().get(m)
											.getUsername().equals(nextStud.getUsername())) {
										Student s3 = studentCourseListControl.getStudentListCtrl().getStudentList()
												.get(k);

										System.out.println(s3.getAcadunits());
										s3.addAcadunits();
										SendMailSSL.SendRegisteredNoti(c.getCourseName(), c.getCourseID(), courseIndex,
												s3.getEmail());
										System.out.println(s3.getAcadunits());
										studentCourseListControl.getStudentListCtrl().save();
									}
								}
								c.getWaitingList().remove(0);

								courseIndexListControl.subtractVacancy(course, courseIndex);
								return;
							}

						}
					}

					// send email to next person in line for waitlist & auto add course
				}
			}
		} else {
			System.out.println("\nCancelling dropping of course ------------------");
			return;
		}

	}

	public static void changeCourseIndex(String username, CourseListCtrl courseListControl,
			CourseIndexListCtrl courseIndexListControl, StudentCourseListCtrl studentCourseListControl) {

		Scanner scan = new Scanner(System.in);

		// enter choice of course from list of registered courses
		System.out.println("--------------------------------------------------------");
		System.out.println("Enter choice of course: ");
		String course = studentCourseListControl.chooseCourse(username);
		int initialIndex = studentCourseListControl.getIndexOfCourse(username, course);

		System.out.println("\nYour current Course Index: " + initialIndex);
		System.out.println("Course Indexes for Course " + course + ": ");
		courseIndexListControl.printIndexesUnderCourse(course);

		System.out.println("Enter new Index No. of course: ");
		int courseIndex = courseIndexListControl.chooseCourseIndex(course);

		if (courseIndex != initialIndex) {

			// print course index details (schedule)
			courseIndexListControl.printCourseIndexInfo(course, courseIndex);

			boolean courseClash = false;
			CourseIndex newIndex = null, initial = null;

			for (int i = 0; i < courseIndexListControl.getCourseIndexSize(); i++) {
				if (courseIndexListControl.getCourseIndexList().get(i).getIndexID() == courseIndex) {
					newIndex = courseIndexListControl.getCourseIndexList().get(i);
					System.out.println(newIndex);
				}

				if (courseIndexListControl.getCourseIndexList().get(i).getIndexID() == initialIndex) {
					initial = courseIndexListControl.getCourseIndexList().get(i);
					System.out.println(initial);
				}
			}
			courseClash = studentCourseListControl.checkChangedCourseClash(initial, newIndex, username);

			System.out.println("\nConfirm to change Index No.? Enter your choice: ");
			System.out.println("1. Yes");
			System.out.println("2. No");
			int choice_confirm;

			while (true) {
				try {
					while (true) {
						choice_confirm = scan.nextInt();
						if (choice_confirm >= 1 && choice_confirm <= 2) {
							break;
						} else {
							System.out.println("Invalid choice, please enter integer 1 or 2. ");
						}
					}
					break;
				} catch (Exception e) {
					System.out.println("Invalid choice, please enter integer 1 or 2. ");
					scan = new Scanner(System.in);
				}
			}
			if (courseClash == false) {
				if (choice_confirm == 1) {
					System.out.println("\nChanging course Index No. of " + course + " ------------------");
					studentCourseListControl.changeStudentCourseIndex(username, course, courseIndex);
					System.out
							.println("Successfully changed Index No from " + initialIndex + " to " + courseIndex + ".");
				} else {
					System.out.println("\nCancelling changing of course index ------------------");
					return;
				}
			}
			else {
				System.out.println("\nCannot change course as there is a clash.");
			}
		} else {
			System.out.println("\nYou are already registered under this index.");
			System.out.println("Cancelling changing of course index ------------------");
		}

	}

	public static void swapCourseIndex(String username, CourseListCtrl courseListControl,
			CourseIndexListCtrl courseIndexListControl, StudentCourseListCtrl studentCourseListControl,
			StudentListCtrl studentListControl) {

		Scanner scan = new Scanner(System.in);

		// enter choice of course from list of registered courses
		System.out.println("--------------------------------------------------------");

		System.out.println("Enter choice of course: ");
		String course = studentCourseListControl.chooseCourse(username);
		int initialIndex = studentCourseListControl.getIndexOfCourse(username, course);

		System.out.println("Your Course Index No. for Course " + course + ": " + initialIndex);

		System.out.println("\nEntering details for Student #2  ------------------");
		String peerUsername, peerPassword, peerName;
		int initialIndex_peer;
		byte[][] SaltArray = PasswordHash.ReturnByteArray();
		String[] HashedPasswords = PasswordHash.ReturnHashedPasswordsArray();

		while (true) { // student login
			System.out.println("\nEnter your Peer's Username: ");
			peerUsername = scan.next();
			System.out.println("Enter your Peer's Password:  ");
			peerPassword = scan.next();

			// validation
			if (UserValidation.loginStudent(peerUsername, peerPassword, SaltArray, HashedPasswords) == false)
				System.out.println("Incorrect username or password! Please try again. ");
			else {
				System.out.println("Student account is verified. ");

				peerName = studentListControl.getName(peerUsername);
				initialIndex_peer = studentCourseListControl.getIndexOfCourse(peerUsername, course);
				System.out.println("Your Peer's Index No. for Course " + course + ": " + initialIndex_peer);
				break;
			}
		}
		
		if (initialIndex_peer != initialIndex) {
			// print course index details (schedule)
			System.out.println("\nLesson schedule for current index " + initialIndex);
			courseIndexListControl.printCourseIndexInfo(course, initialIndex);
			System.out.println("\nLesson schedule for Peer's index " + initialIndex_peer);
			courseIndexListControl.printCourseIndexInfo(course, initialIndex_peer);

			boolean courseClash = false;
			CourseIndex initial_peer = null, initial = null;

			for (int i = 0; i < courseIndexListControl.getCourseIndexSize(); i++) {
				if (courseIndexListControl.getCourseIndexList().get(i).getIndexID() == initialIndex_peer) {
					initial_peer = courseIndexListControl.getCourseIndexList().get(i);
				}

				if (courseIndexListControl.getCourseIndexList().get(i).getIndexID() == initialIndex) {
					initial = courseIndexListControl.getCourseIndexList().get(i);
					System.out.println(initial);
				}
			}
			courseClash = studentCourseListControl.checkChangedCourseClash(initial, initial_peer, username);
			
			System.out.println("\nConfirm to swap Index No.? Enter your choice: ");
			System.out.println("1. Yes");
			System.out.println("2. No");
			int choice_confirm;

			while (true) {
				try {
					while (true) {
						choice_confirm = scan.nextInt();
						if (choice_confirm >= 1 && choice_confirm <= 2) {
							break;
						} else {
							System.out.println("Invalid choice, please enter integer 1 or 2. ");
						}
					}
					break;
				} catch (Exception e) {
					System.out.println("Invalid choice, please enter integer 1 or 2. ");
					scan = new Scanner(System.in);
				}
			}
			
			if(courseClash == false) {
				if (choice_confirm == 1) {
					System.out.println("\nSwapping course Index No. of " + course + " ------------------");
					studentCourseListControl.changeStudentCourseIndex(username, course, initialIndex_peer);
					studentCourseListControl.changeStudentCourseIndex(peerUsername, course, initialIndex);
					System.out.println("Successfully changed Index No from " + initialIndex + " to " + initialIndex_peer
							+ " with " + peerName);
	
					for (int k = 0; k < studentListControl.getStudentListSize(); k++) {
						if (studentListControl.getStudent(k).getUsername().equals(username)) {
							Student curStud = studentListControl.getStudent(k);
							SendMailSSL.SendSwapNoti(course, initialIndex, initialIndex_peer, peerName, curStud.getEmail());
							// add student to peer's Index
							for (CourseIndex c2 : courseIndexListControl.getCourseIndexList()) {
								if (c2.getIndexID() == initialIndex_peer) {
									c2.getStudent().add(curStud);
									System.out.println(initialIndex_peer + ": " + c2.getStudent());
									courseIndexListControl.save();
								}
							}
						}
					}
	
					// remove student from initialIndex
					for (CourseIndex c : courseIndexListControl.getCourseIndexList()) {
						if (c.getIndexID() == initialIndex) {
							for (int i = 0; i < c.getStudent().size(); i++) {
								if (c.getStudent().get(i).getUsername().equals(username)) {
									System.out.println("im here");
									c.getStudent().remove(i);
									courseIndexListControl.save();
								}
							}
						}
					}
	
					// remove peer from peer's initialIndex
					for (CourseIndex c1 : courseIndexListControl.getCourseIndexList()) {
						if (c1.getIndexID() == initialIndex_peer) {
							for (int k1 = 0; k1 < c1.getStudent().size(); k1++) {
								if (c1.getStudent().get(k1).getUsername().equals(peerUsername)) {
									c1.getStudent().remove(k1);
									System.out.println("After:" + c1.getStudent());
									courseIndexListControl.save();
								}
							}
	
						}
					}
					// add peer to student's initialIndex
					for (int k2 = 0; k2 < studentListControl.getStudentListSize(); k2++) {
						if (studentListControl.getStudent(k2).getUsername().equals(peerUsername)) {
							Student peerStud = studentListControl.getStudent(k2);
							for (CourseIndex c3 : courseIndexListControl.getCourseIndexList()) {
								if (c3.getIndexID() == initialIndex) {
									c3.getStudent().add(peerStud);
									courseIndexListControl.save();
								}
	
							}
						}
					}
	
				}else {
					System.out.println("\nCannot change course as there is a clash.");
					return;
				}
			}

			else {
				System.out.println("\nCancelling changing of course index ------------------");
				return;

			}
		}
		else {
			System.out.println("You are registered under the same Index as your peer.");
			return;
		}
		

	}

}