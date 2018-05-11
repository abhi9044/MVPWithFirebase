package com.example.apoorv.conferenceattendancetracker.ui.attendeeList;

import android.content.Context;

import com.example.apoorv.conferenceattendancetracker.data.model.Attendee;
import com.example.apoorv.conferenceattendancetracker.utils.mvp.IBasePresenter;
import com.example.apoorv.conferenceattendancetracker.utils.mvp.IBaseView;

import java.util.List;

interface AttendeeContract {

    interface View extends IBaseView {

        void showAttendeeList(List<Attendee> attendeeList);

        void shouldShowPlaceholderText();
    }

    interface Presenter extends IBasePresenter<View> {
        void getAttendeeList(Context context);
    }
}