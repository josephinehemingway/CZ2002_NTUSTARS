package proj;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Schedule implements Serializable {
	/**
	 * Venue for different type of lesson
	 */
	private String venuelec, venuetut, venuelab;
	private String indexID;
	private Date startlab;
	private Date startlect;
	private Date starttut;
	private Date endlab;
	private Date endlect;
	private Date endtut;
	private Lesson lab, tut, lect;
	// 1 for courses with lab, lec and tut, 2 for courses with lec and tut, 3 for
	// courses with lec.
	private int coursetype;
	// Day of the week of the lesson
	private int labday, lectday, tutday;

	private static int venuetutcounter = 10;
	private static int venuelabcounter = 15;
	private static int venuelectcounter = 20;

	public Date getStartlab() {
		return startlab;
	}

	public Date getStartlect() {
		return startlect;
	}

	public Date getStarttut() {
		return starttut;
	}

	// Constructor to add schedule to an index
	public Schedule(int labday, int lectday, int tutday, Date _lab, Date _lect, Date _tut, int indexID) {
		this.labday = labday;
		this.lectday = lectday;
		this.tutday = tutday;
		this.startlab = _lab;
		this.startlect = _lect;
		this.starttut = _tut;
		this.coursetype = 1;

		endlab = new Date(startlab.getTime() + 7200000);
		endlect = new Date(startlect.getTime() + 3600000);
		endtut = new Date(starttut.getTime() + 3600000);

		venuetut = "TR" + venuetutcounter++ + "";
		venuelab = "LAB" + venuelabcounter++;
		venuelec = "LT" + venuelectcounter++ + "";

		lab = new Lesson(labday, startlab, endlab, venuelab, indexID);
		tut = new Lesson(tutday, starttut, endtut, venuetut, indexID);
		lect = new Lesson(lectday, startlect, endlect, venuelec, indexID);
	}

	public Schedule(int lectday, int tutday, Date _lect, Date _tut, int indexID) {
		this.lectday = lectday;
		this.tutday = tutday;
		this.startlect = _lect;
		this.starttut = _tut;
		this.coursetype = 2;

		endlect = new Date(startlect.getTime() + 3600000);
		endtut = new Date(starttut.getTime() + 3600000);

		venuetut = "TR" + venuetutcounter++ + "";
		venuelec = "LT" + venuelectcounter++ + "";

		tut = new Lesson(tutday, starttut, endtut, venuetut, indexID);
		lect = new Lesson(lectday, startlect, endlect, venuelec, indexID);
	}

	public Schedule(int lectday, Date _lect, int indexID) {
		this.lectday = lectday;
		this.startlect = _lect;
		this.coursetype = 3;

		endlect = new Date(startlect.getTime() + 3600000);
		venuelec = "LT" + venuelectcounter++ + "";
		lect = new Lesson(lectday, startlect, endlect, venuelec, indexID);
	}
	
	public Lesson getLab() {
		return lab;
	}

	public void setLab(Lesson lab) {
		this.lab = lab;
	}

	public Lesson getTut() {
		return tut;
	}

	public void setTut(Lesson tut) {
		this.tut = tut;
	}

	public Lesson getLect() {
		return lect;
	}

	public void setLect(Lesson lect) {
		this.lect = lect;
	}

	public String getIndexID() {
		return indexID;
	}

	public String getVenueTut() {
		return this.venuetut;
	}

	public String getVenueLect() {
		return this.venuelec;
	}

	public String getVenueLab() {
		return this.venuelab;
	}
	
	public int getCoursetype() {
		return coursetype;
	}

	public void setCoursetype(int coursetype) {
		this.coursetype = coursetype;
	}

	@Override
	public String toString() {
		if (this.coursetype == 1) {
			return printInfoLabTutLec();
		}
		else if(this.coursetype == 2) {
			return printInfoTutLec();
		}
		else if(this.coursetype == 3) {
			return printInfoLec();
		}
		return "Invalid Schedule";
	}

	public boolean checkClash(Schedule otherSch) {
		// try to find a combination of clashes
		
		// check lab clash with lec of this schedule
		if (timeClash(otherSch.labday, lectday, otherSch.startlab, otherSch.endlab, startlect, endlect) == true) {
			System.out.println("Lab timeslot of this index clashes with lecture of existing schedule");
			return true;
		}

		// check lec clash with lec of this schedule
		if (timeClash(otherSch.lectday, lectday, otherSch.startlect, otherSch.endlect, startlect, endlect) == true) {
			System.out.println("Lecture timeslot of this index clashes with lecture of existing schedule");
			return true;
		}

		// check tut clash with lec of this schedule
		if (timeClash(otherSch.tutday, lectday, otherSch.starttut, otherSch.endtut, startlect, endlect) == true) {
			System.out.println("Tutorial timeslot of this index clashes with lecture of existing schedule");
			return true;
		}
		if (this.coursetype == 2) {
			// check lab clash with tut of this schedule
			if (timeClash(otherSch.labday, tutday, otherSch.startlab, otherSch.endlab, starttut, endtut) == true) {
				System.out.println("Lab timeslot of this index clashes with tutorial of existing schedule");
				return true;
			}
			
			// check lec clash with tut of this schedule
			if (timeClash(otherSch.lectday, tutday, otherSch.startlect, otherSch.endlect, starttut, endtut) == true) {
				System.out.println("Lecture timeslot of this index clashes with tutorial of existing schedule");
				return true;
			}
			
			// check tut clash with tut of this schedule
			if (timeClash(otherSch.tutday, tutday, otherSch.starttut, otherSch.endtut, starttut, endtut) == true) {
				System.out.println("Tutorial timeslot of this index clashes with tutorial of existing schedule");
				return true;
			}
			if (this.coursetype == 1) {
				// check lab clash with lab of this schedule
				if (timeClash(otherSch.labday, labday, otherSch.startlab, otherSch.endlab, startlab, endlab) == true) {
					System.out.println("Lab timeslot of this index clashes with lab of existing schedule");
					return true;
				}
				// check lec clash with lab of this schedule
				if (timeClash(otherSch.lectday, labday, otherSch.startlect, otherSch.endlect, startlab, endlab) == true) {
					System.out.println("Lecture timeslot of this index clashes with lab of existing schedule");
					return true;
				}
				// check tut clash with lab of this schedule
				if (timeClash(otherSch.tutday, labday, otherSch.starttut, otherSch.endtut, startlab, endlab) == true) {
					System.out.println("Tutorial timeslot of this index clashes with lab of existing schedule");
					return true;
				}
			}
			
		}
		return false;

	}

//CHECK IF THE TIMING CONFLCITS
	public boolean timeClash(int checkDay, int existDay, Date start1, Date end1, Date start2, Date end2) {

		if (checkDay != existDay) {
			return false;
		} else {
			if (start1.getTime() < end2.getTime() && start2.getTime() < end1.getTime()) {
				return true;
			} else
				return false;
		}

	}

	public String printInfoLabTutLec() {
		DateFormat df = new SimpleDateFormat("HH:mm");
		String strlabday = "";
		String strtutday = "";
		String strlectday = "";
		switch (labday) {
		case 1:
			strlabday = "MON";
			break;
		case 2:
			strlabday = "TUE";
			break;
		case 3:
			strlabday = "WED";
			break;
		case 4:
			strlabday = "THU";
			break;
		case 5:
			strlabday = "FRI";
			break;
		default:
			break;
		}

		switch (tutday) {
		case 1:
			strtutday = "MON";
			break;
		case 2:
			strtutday = "TUE";
			break;
		case 3:
			strtutday = "WED";
			break;
		case 4:
			strtutday = "THU";
			break;
		case 5:
			strtutday = "FRI";
			break;
		default:
			break;
		}

		switch (lectday) {
		case 1:
			strlectday = "MON";
			break;
		case 2:
			strlectday = "TUE";
			break;
		case 3:
			strlectday = "WED";
			break;
		case 4:
			strlectday = "THU";
			break;
		case 5:
			strlectday = "FRI";
			break;
		default:
			break;
		}

		return "====================================================\n" + "  ClassType	Day   	   Time	 	Venue   "
				+ "\n====================================================" + "\n     Lab	" + strlabday + "	"
				+ df.format(startlab) + "-" + df.format(endlab) + "	" + venuelab + "\n" + "\n     Lec	" + strlectday
				+ "	" + df.format(startlect) + "-" + df.format(endlect) + "	" + venuelec + "\n" + "\n     Tut	"
				+ strtutday + "	" + df.format(starttut) + "-" + df.format(endtut) + "	" + venuetut + "\n"
				+ "====================================================";
	}

	public String printInfoTutLec() {
		DateFormat df = new SimpleDateFormat("HH:mm");
		String strtutday = "";
		String strlectday = "";

		switch (tutday) {
		case 1:
			strtutday = "MON";
			break;
		case 2:
			strtutday = "TUE";
			break;
		case 3:
			strtutday = "WED";
			break;
		case 4:
			strtutday = "THU";
			break;
		case 5:
			strtutday = "FRI";
			break;
		default:
			break;
		}

		switch (lectday) {
		case 1:
			strlectday = "MON";
			break;
		case 2:
			strlectday = "TUE";
			break;
		case 3:
			strlectday = "WED";
			break;
		case 4:
			strlectday = "THU";
			break;
		case 5:
			strlectday = "FRI";
			break;
		default:
			break;
		}

		return "====================================================\n" + "  ClassType	Day   	   Time	 	Venue   "
				+ "\n====================================================" + "\n     Lec	" + strlectday + "	"
				+ df.format(startlect) + "-" + df.format(endlect) + "	" + venuelec + "\n" + "\n     Tut	"
				+ strtutday + "	" + df.format(starttut) + "-" + df.format(endtut) + "	" + venuetut + "\n"
				+ "====================================================";
	}

	public String printInfoLec() {
		DateFormat df = new SimpleDateFormat("HH:mm");
		String strlectday = "";

		switch (lectday) {
		case 1:
			strlectday = "MON";
			break;
		case 2:
			strlectday = "TUE";
			break;
		case 3:
			strlectday = "WED";
			break;
		case 4:
			strlectday = "THU";
			break;
		case 5:
			strlectday = "FRI";
			break;
		default:
			break;
		}

		return "====================================================\n" + "  ClassType	Day   	   Time	 	Venue   "
				+ "\n====================================================" + "\n     Lec	" + strlectday + "	"
				+ df.format(startlect) + "-" + df.format(endlect) + "	" + venuelec + "\n"
				+ "====================================================";
	}

}