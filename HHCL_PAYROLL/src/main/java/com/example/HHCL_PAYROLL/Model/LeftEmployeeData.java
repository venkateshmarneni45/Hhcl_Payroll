package com.example.HHCL_PAYROLL.Model;

public class LeftEmployeeData {
	private int employeeId;
	private int documentTypeId;
	private int documentNo; 
	private String documentDate;
	private int actionBy;
	private String actionDate;
	private String notes;
	private String lastWorkingDate;
	private int docStatus; 
	private int logId;
	private String lupDate;

	public LeftEmployeeData(int employeeId, int documentTypeId, int documentNo, String documentDate, int actionBy,
			String actionDate, String notes, String lastWorkingDate, int docStatus, int logId, String lupDate) {
		super();
		this.employeeId = employeeId;
		this.documentTypeId = documentTypeId;
		this.documentNo = documentNo;
		this.documentDate = documentDate;
		this.actionBy = actionBy;
		this.actionDate = actionDate;
		this.notes = notes;
		this.lastWorkingDate = lastWorkingDate;
		this.docStatus = docStatus;
		this.logId = logId;
		this.lupDate = lupDate;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public int getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(int documentNo) {
		this.documentNo = documentNo;
	}

	public String getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}

	public int getActionBy() {
		return actionBy;
	}

	public void setActionBy(int actionBy) {
		this.actionBy = actionBy;
	}

	public String getActionDate() {
		return actionDate;
	}

	public void setActionDate(String actionDate) {
		this.actionDate = actionDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getLastWorkingDate() {
		return lastWorkingDate;
	}

	public void setLastWorkingDate(String lastWorkingDate) {
		this.lastWorkingDate = lastWorkingDate;
	}

	public int getDocStatus() {
		return docStatus;
	}

	public void setDocStatus(int docStatus) {
		this.docStatus = docStatus;
	}

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public String getLupDate() {
		return lupDate;
	}

	public void setLupDate(String lupDate) {
		this.lupDate = lupDate;
	}

	@Override
	public String toString() {
		return "LeftEmployeeUpdation [employeeId=" + employeeId + ", documentTypeId=" + documentTypeId + ", documentNo="
				+ documentNo + ", documentDate=" + documentDate + ", actionBy=" + actionBy + ", actionDate="
				+ actionDate + ", notes=" + notes + ", lastWorkingDate=" + lastWorkingDate + ", docStatus=" + docStatus
				+ ", logId=" + logId + ", lupDate=" + lupDate + "]";
	}

}
