package com.example.apoorv.conferenceattendancetracker.data.model;

/**
 * Model Class of Attendee
 */
public class Attendee {

    public String id;
    public String name;
    public String company;
    public String type;
    public boolean checkedIn;

    //Empty Constructor
    public Attendee() {
    }

    /**
     * Parameterised Constructor Used to make Attendee Object
     *
     * @param attendanceId    refers to the attendee's id
     * @param attendeeName    refers to the attendee's name
     * @param attendeeCompany refers to the attendee's company
     * @param attendeeType    refers to the occupation/type of the attendee
     * @param checkedInState  refers to the checked in state of the attendee in conference
     */
    public Attendee(String attendanceId, String attendeeName, String attendeeCompany, String attendeeType, boolean checkedInState) {
        this.id = attendanceId;
        this.name = attendeeName;
        this.company = attendeeCompany;
        this.type = attendeeType;
        this.checkedIn = checkedInState;
    }

    //Getter -- @return id
    public String getId() {
        return id;
    }

    //Setter -- @param attendanceId
    public void setId(String attendanceId) {
        this.id = attendanceId;
    }

    //Getter -- @return name
    public String getName() {
        return name;
    }

    //Setter -- @param name
    public void setName(String name) {
        this.name = name;
    }

    //Getter -- @return company
    public String getCompany() {
        return company;
    }

    //Setter -- @param company
    public void setCompany(String company) {
        this.company = company;
    }

    //Getter -- @return type
    public String getType() {
        return type;
    }

    //Setter -- @param type
    public void setType(String type) {
        this.type = type;
    }

    //Getter -- @return checkedIn
    public boolean getCheckedIn() {
        return checkedIn;
    }

    //Setter -- @param checkedIn
    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}
