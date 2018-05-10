package com.example.apoorv.conferenceattendancetracker.utils.mvp;


public interface IBasePresenter<ViewT> {

    void onViewActive(ViewT view);

    void onViewInactive();
}
