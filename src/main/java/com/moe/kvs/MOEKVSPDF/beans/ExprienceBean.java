package com.moe.kvs.MOEKVSPDF.beans;

import java.io.Serializable;
import java.util.Date;

public class ExprienceBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String workExperienceId;
	private String teacherId;
	private String udiseSchCode;
	private String schoolId;
	private Date workStartDate;
	private Date workEndDate;
	private String positionType;
	private Integer natureOfAppointment;
	private String appointedForSubject;
	private String udiseSchoolName;
	
	private Integer shiftType;
	private Integer verifyFlag;
	private Integer verifiedType;
	private String createdBy;
	private String groundForTransfer;
	private Integer currentlyActiveYn;
	private Integer shift_yn;
	private String createdTime;
	private String modifiedBy;
	private String modifiedTime;
	
	
	
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getWorkExperienceId() {
		return workExperienceId;
	}
	public void setWorkExperienceId(String workExperienceId) {
		this.workExperienceId = workExperienceId;
	}

	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getUdiseSchCode() {
		return udiseSchCode;
	}
	public void setUdiseSchCode(String udiseSchCode) {
		this.udiseSchCode = udiseSchCode;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Date getWorkStartDate() {
		return workStartDate;
	}
	public void setWorkStartDate(Date workStartDate) {
		this.workStartDate = workStartDate;
	}
	public Date getWorkEndDate() {
		return workEndDate;
	}
	public void setWorkEndDate(Date workEndDate) {
		this.workEndDate = workEndDate;
	}
	public String getPositionType() {
		return positionType;
	}
	public void setPositionType(String positionType) {
		this.positionType = positionType;
	}
	public Integer getNatureOfAppointment() {
		return natureOfAppointment;
	}
	public void setNatureOfAppointment(Integer natureOfAppointment) {
		this.natureOfAppointment = natureOfAppointment;
	}
	public String getAppointedForSubject() {
		return appointedForSubject;
	}
	public void setAppointedForSubject(String appointedForSubject) {
		this.appointedForSubject = appointedForSubject;
	}
	public String getUdiseSchoolName() {
		return udiseSchoolName;
	}
	public void setUdiseSchoolName(String udiseSchoolName) {
		this.udiseSchoolName = udiseSchoolName;
	}
	public Integer getShiftType() {
		return shiftType;
	}
	public void setShiftType(Integer shiftType) {
		this.shiftType = shiftType;
	}
	public Integer getVerifyFlag() {
		return verifyFlag;
	}
	public void setVerifyFlag(Integer verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	public Integer getVerifiedType() {
		return verifiedType;
	}
	public void setVerifiedType(Integer verifiedType) {
		this.verifiedType = verifiedType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getGroundForTransfer() {
		return groundForTransfer;
	}
	public void setGroundForTransfer(String groundForTransfer) {
		this.groundForTransfer = groundForTransfer;
	}
	public Integer getCurrentlyActiveYn() {
		return currentlyActiveYn;
	}
	public void setCurrentlyActiveYn(Integer currentlyActiveYn) {
		this.currentlyActiveYn = currentlyActiveYn;
	}
	public Integer getShift_yn() {
		return shift_yn;
	}
	public void setShift_yn(Integer shift_yn) {
		this.shift_yn = shift_yn;
	}
	
	
	

}
