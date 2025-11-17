package com.ifrs.movimentaif.movimentaifapi.model;

import java.util.Date;

public class AcademyInfo {
    private String academyId;
    private Date startDate;
    private Date endDate;
    private String openHour;
    private String closeHour;
    private String additionalInfo;

    public AcademyInfo() {
        this.academyId = "academy-info-singleton";
    }

    public AcademyInfo(Date startDate, Date endDate, String openHour, String closeHour, String additionalInfo) {
        this.academyId = "academy-info-singleton";
        this.startDate = startDate;
        this.endDate = endDate;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.additionalInfo = additionalInfo;
    }

    public String getAcademyId() {
        return academyId;
    }

    public void setAcademyId(String academyId) {
        this.academyId = academyId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOpenHour() {
        return openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
