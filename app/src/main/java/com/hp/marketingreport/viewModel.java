package com.hp.marketingreport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;

public class viewModel extends ViewModel {
    private final MutableLiveData<Integer> totalRoutes = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> totalVisits = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> todayVisits = new MutableLiveData<Integer>();
    private final MutableLiveData<Integer> totalSalesman = new MutableLiveData<Integer>();
    private final MutableLiveData<HashMap<String, Float>> monthlyDataCount = new MutableLiveData<HashMap<String, Float>>();

    public void setTotalRoutes(Integer item) {
        totalRoutes.setValue(item);
    }

    public LiveData<Integer> getTotalRoutes() {
        return totalRoutes;
    }

    public void setTotalVisits(Integer item) {
        totalVisits.setValue(item);
    }

    public LiveData<Integer> getTotalVisits() {
        return totalVisits;
    }

    public void setTodayVisits(Integer item) {
        todayVisits.setValue(item);
    }

    public LiveData<Integer> getTodayVisits() {
        return todayVisits;
    }

    public void setTotalSalesman(Integer item) {
        totalSalesman.setValue(item);
    }

    public LiveData<Integer> getTotalSalesman() {
        return totalSalesman;
    }

    public void setMonthlyDataCount(HashMap<String, Float> item) {
        monthlyDataCount.setValue(item);
    }

    public LiveData<HashMap<String, Float>> getMonthlyDataCount() {
        return monthlyDataCount;
    }
}
