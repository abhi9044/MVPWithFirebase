package com.example.apoorv.conferenceattendancetracker.data;

import com.example.apoorv.conferenceattendancetracker.data.model.Attendee;

import java.util.List;

/**
 * The interface that exposes fetching and storing data through helper methods. This is to be
 * implemented by all data sources such as
 */
public abstract class DataSource {


    public interface GetAttendeeCallback {

        void onSuccess(List<Attendee> attendees);

        void onFailure(Throwable throwable);

        void onNetworkFailure();

    }

    public abstract void getAttendeeList( GetAttendeeCallback callback);
}
