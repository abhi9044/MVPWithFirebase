package com.example.apoorv.conferenceattendancetracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apoorv.conferenceattendancetracker.R;
import com.example.apoorv.conferenceattendancetracker.model.Attendee;

import java.util.ArrayList;
import java.util.List;

//Adapter class
public class AttendeeDisplayAdapter extends RecyclerView.Adapter<AttendeeDisplayAdapter.AttendeeViewHolder> {

    private List<Attendee> attendeeArrayList = new ArrayList<>();
    private Context context;
    private Attendee attendeeObj;

    public AttendeeDisplayAdapter(List<Attendee> attendeeArrayList, Context context) {
        this.attendeeArrayList = attendeeArrayList;
        this.context = context;
    }

    @Override
    public AttendeeDisplayAdapter.AttendeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendee_single_list_item, parent, false);
        AttendeeViewHolder attendeeViewHolder = new AttendeeViewHolder(view, context, attendeeArrayList);
        return attendeeViewHolder;
    }

    /*
    BindView Holder to bind the holder and set the textviews with fetched attendee arraylist position
    */
    @Override
    public void onBindViewHolder(final AttendeeViewHolder holder, final int position) {
        attendeeObj = attendeeArrayList.get(position);

        holder.tvAttendeeName.setText(attendeeObj.getName());
        holder.tvAttendeeCompanyAndProfile.setText(attendeeObj.getCompany() + " | " + attendeeObj.getType());

        if (attendeeObj.getCheckedIn() == false) {
            holder.tvAttendeeName.setTextColor(context.getResources().getColor(R.color.inactive_dots));
        } else
            holder.tvAttendeeName.setTextColor(context.getResources().getColor(R.color.active_dots));

    }

    //get array list count
    @Override
    public int getItemCount() {
        return attendeeArrayList.size();
    }

    //View Holder Class to bind individual item view
    public static class AttendeeViewHolder extends RecyclerView.ViewHolder {

        TextView tvAttendeeName;
        TextView tvAttendeeCompanyAndProfile;
        List<Attendee> attendeeList;
        Context context;

        public AttendeeViewHolder(View itemView, Context context, List<Attendee> attendeeList) {
            super(itemView);
            tvAttendeeName = itemView.findViewById(R.id.attendee_name);
            this.attendeeList = attendeeList;
            this.context = context;
            tvAttendeeCompanyAndProfile = itemView.findViewById(R.id.attendee_company_and_type);
        }

    }
}
