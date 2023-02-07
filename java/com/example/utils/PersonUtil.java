package com.example.utils;

import com.example.model.NhanKhauModel;
import com.example.service.INhanKhauService;
import com.example.service.impl.NhanKhauService;

import java.util.ArrayList;
import java.util.List;

public class PersonUtil {
    private static PersonUtil personUtil = null;
    private INhanKhauService nhanKhauService = new NhanKhauService();
    private List<NhanKhauModel> models = nhanKhauService.findAll();
    private NhanKhauModel monitor = null;
    private NhanKhauModel member = null;
    private NhanKhauModel minhchung = null;
    private List<NhanKhauModel> tempModel = new ArrayList<>();

    public static PersonUtil getInstance() {
        if (personUtil == null) personUtil = new PersonUtil();

        return personUtil;
    }

    public void removeData() {
        personUtil = null;
    }
    public List<NhanKhauModel> getModels() {
        return models;
    }

    public void add(NhanKhauModel model) {
        models.add(model);
    }

    public NhanKhauModel getMonitor() {
        return monitor;
    }

    public void setMonitor(NhanKhauModel monitor) {
        this.monitor = monitor;
    }

    public NhanKhauModel getMember() {
        return member;
    }
    public void setMember(NhanKhauModel member) {
        this.member = member;
    }

    public List<NhanKhauModel> getTempModel() {
        return tempModel;
    }

    public void setTempModel(List<NhanKhauModel> tempModel) {
        this.tempModel = tempModel;
    }

    public NhanKhauModel getMinhchung() {
        return minhchung;
    }

    public void setMinhChung(NhanKhauModel personModel){
        minhchung = personModel;

    }
}
