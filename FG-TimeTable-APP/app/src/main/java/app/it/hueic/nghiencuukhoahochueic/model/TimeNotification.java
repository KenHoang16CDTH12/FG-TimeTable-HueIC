package app.it.hueic.nghiencuukhoahochueic.model;

/**
 * Created by kenhoang on 1/2/18.
 */

public class TimeNotification {
	private int ID;
	private String TITLE;
	private String CONTENT;
	private int STATUS;

	public TimeNotification() {
	}

	public TimeNotification(String TITLE, String CONTENT, int STATUS) {
		this.TITLE = TITLE;
		this.CONTENT = CONTENT;
		this.STATUS = STATUS;
	}

	public int getID() {
		return ID;
	}

	public void setID(int ID) {
		this.ID = ID;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String TITLE) {
		this.TITLE = TITLE;
	}

	public String getCONTENT() {
		return CONTENT;
	}

	public void setCONTENT(String CONTENT) {
		this.CONTENT = CONTENT;
	}

	public int getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(int STATUS) {
		this.STATUS = STATUS;
	}
}
