package com.example.apoorv.conferenceattendancetracker.data;

import android.content.Context;

import com.example.apoorv.conferenceattendancetracker.data.model.Attendee;
import com.example.apoorv.conferenceattendancetracker.utils.NetworkHelper;

import java.util.List;

/**
 * The primary class for the presenters that extend
 * for fetching and storing data.
 * It is the middleman in front of all data sources such as
 ** them depending on conditions such as network availability, etc.
 */
public class DataRepository {

    private DataSource remoteDataSource;
    private DataSource localDataSource;
    private NetworkHelper networkHelper;

    private static DataRepository dataRepository;

    public DataRepository(DataSource remoteDataSource, DataSource localDataSource,
                          NetworkHelper networkHelper) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
        this.networkHelper = networkHelper;
    }

    public static synchronized DataRepository getInstance(DataSource remoteDataSource,
            DataSource localDataSource,
            NetworkHelper networkHelper) {
        if (dataRepository == null) {
            dataRepository = new DataRepository(remoteDataSource, localDataSource, networkHelper);
        }
        return dataRepository;
    }

    public void getAttendee(Context context, final DataSource.GetAttendeeCallback callback) {
        if (networkHelper.isNetworkAvailable(context)) {
            remoteDataSource.getAttendeeList(new DataSource.GetAttendeeCallback() {
                @Override
                public void onSuccess(List<Attendee> attendees) {
                    callback.onSuccess(attendees);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onNetworkFailure() {
                    callback.onNetworkFailure();
                }
            });
        } else {
          //  localDataSource.getAttendees(allback);
        }
    }

    public void destroyInstance() {
        dataRepository = null;
    }
}
