package com.spil.dev.tms.Activity.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Report {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("iddriver")
    @Expose
    private Integer iddriver;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("idkendaraan")
    @Expose
    private Integer idkendaraan;
    @SerializedName("kode")
    @Expose
    private String kode;
    @SerializedName("nopol")
    @Expose
    private String nopol;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("reportdate")
    @Expose
    private String reportdate;
    @SerializedName("lokasi")
    @Expose
    private String lokasi;
    @SerializedName("catatan")
    @Expose
    private String catatan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIddriver() {
        return iddriver;
    }

    public void setIddriver(Integer iddriver) {
        this.iddriver = iddriver;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public Integer getIdkendaraan() {
        return idkendaraan;
    }

    public void setIdkendaraan(Integer idkendaraan) {
        this.idkendaraan = idkendaraan;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNopol() {
        return nopol;
    }

    public void setNopol(String nopol) {
        this.nopol = nopol;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getReportdate() {
        return reportdate;
    }

    public String getParsedReportData(){
        SimpleDateFormat parser = new SimpleDateFormat("M/d/yyyy hh:mm:ss a");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date d = parser.parse(reportdate);
            return formatter.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Unable to parse";
        }
    }

    public void setReportdate(String reportdate) {
        this.reportdate = reportdate;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

}