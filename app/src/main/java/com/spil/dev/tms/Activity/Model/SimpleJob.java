package com.spil.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andresual on 2/15/2018.
 */

public class SimpleJob implements Parcelable {

    @SerializedName("job_pickup_name")
    private String jobName;
    @SerializedName("job_pickup_address")
    private String origin;
    @SerializedName("job_deliver_address")
    private String destination;
    @SerializedName("order_id")
    private String orderNo;
    @SerializedName("pelanggan")
    private String pelanggan;
    @SerializedName("job_blast")
    private String tanggal;
    @SerializedName("container_name")
    private String containerName;
    @SerializedName("container_no")
    private String containerNo;
    @SerializedName("container_cargo_name")
    private String comodity;
    @SerializedName("job_deliver_status")
    private Integer jobDeliverStatus;
    @SerializedName("id")
    private Integer jobId;
    @SerializedName("job_pickup_status")
    private Integer jobPickupStatus;
    @SerializedName("job_pickup_latitude")
    private String jobPickupLatitude;
    @SerializedName("job_pickup_longitude")
    private String jobPickupLongitude;
    @SerializedName("job_deliver_latitude")
    private String jobDeliverLatitude;
    @SerializedName("job_deliver_longitude")
    private String jobDeliverLongitude;
    @SerializedName("job_type")
    private Integer jobType;
    @SerializedName("fleet_driver_id")
    @Expose
    private Integer fleetDriverId;
    @SerializedName("fleet_nopol")
    @Expose
    private String fleetNopol;

    public String getFleetNopol() {
        return fleetNopol;
    }

    public void setFleetNopol(String fleetNopol) {
        this.fleetNopol = fleetNopol;
    }

    public Integer getFleetDriverId() {
        return fleetDriverId;
    }

    public void setFleetDriverId(Integer fleetDriverId) {
        this.fleetDriverId = fleetDriverId;
    }

    public String getStringDeliverStatus() {
        SparseArray<String> a = new SparseArray<>();
        a.put(1, "Menunggu");
        a.put(2, "Ditugaskan");
        a.put(3, "Diterima");
        a.put(4, "Menuju lokasi awal");
        a.put(5, "Siap Muat/Bongkar");
        a.put(6, "Mulai Muat/Bongkar");
        a.put(7, "Selesai Muat/Bongkar");
        a.put(8, "Kirim");
        a.put(9, "Sampai Tujuan");
        a.put(10, "Mulai Muat/Bongkar");
        a.put(11, "Selesai Muat/Bongkar");
        a.put(12, "Bawa Kosong ke Depo");
        a.put(13, "Turunkan Kosong di Depo");
        a.put(14, "Order Selesai");
        a.put(15, "Batal");
        a.put(16, "Finish job");
        a.put(17, "Cancel pickup");
        a.put(18, "Cancel job");
        a.put(19, "Reject job");
        return a.get(jobDeliverStatus);
    }

    public int getStatusColor() {
        SparseArray<Integer> a = new SparseArray<>();
        a.put(1, 0xFFe64a19);
        a.put(2, 0xFFf57c00);
        a.put(3, 0xFFffa000);
        a.put(4, 0xFFc2185b);
        a.put(5, 0xFF7b1fa2);
        a.put(6, 0xFF512da8);
        a.put(7, 0xFF303f9f);
        a.put(8, 0xFF455ede);
        a.put(9, 0xFF0288d1);
        a.put(10, 0xFF0097a7);
        a.put(11, 0xFF00796b);
        a.put(12, 0xFF0a7e07);
        a.put(13, 0xFF689f38);
        a.put(14, 0xFFafb42b);
        a.put(15, 0xFF000000);
        a.put(16, 0xFF000000);
        a.put(17, 0xFF000000);
        a.put(18, 0xFF000000);
        a.put(19, 0xFF000000);
        return a.get(jobDeliverStatus);
    }



    public Integer getJobPickupStatus() {
        return jobPickupStatus;
    }

    public void setJobPickupStatus(Integer jobPickupStatus) {
        this.jobPickupStatus = jobPickupStatus;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobDeliverLatitude() {
        return jobDeliverLatitude;
    }

    public void setJobDeliverLatitude(String jobDeliverLatitude) {
        this.jobDeliverLatitude = jobDeliverLatitude;
    }

    public String getJobDeliverLongitude() {
        return jobDeliverLongitude;
    }

    public void setJobDeliverLongitude(String jobDeliverLongitude) {
        this.jobDeliverLongitude = jobDeliverLongitude;
    }

    public String getJobPickupLatitude() {
        return jobPickupLatitude;
    }

    public void setJobPickupLatitude(String jobPickupLatitude) {
        this.jobPickupLatitude = jobPickupLatitude;
    }

    public String getJobPickupLongitude() {
        return jobPickupLongitude;
    }

    public void setJobPickupLongitude(String jobPickupLongitude) {
        this.jobPickupLongitude = jobPickupLongitude;
    }

    public Integer getJobDeliverStatus() {
        return jobDeliverStatus;
    }

    public void setJobDeliverStatus(Integer jobDeliverStatus) {
        this.jobDeliverStatus = jobDeliverStatus;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(String pelanggan) {
        this.pelanggan = pelanggan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getComodity() {
        return comodity;
    }

    public void setComodity(String comodity) {
        this.comodity = comodity;
    }

    public SimpleJob() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobName);
        dest.writeString(this.origin);
        dest.writeString(this.destination);
        dest.writeString(this.orderNo);
        dest.writeString(this.pelanggan);
        dest.writeString(this.tanggal);
        dest.writeString(this.containerName);
        dest.writeString(this.containerNo);
        dest.writeString(this.comodity);
        dest.writeValue(this.jobDeliverStatus);
        dest.writeValue(this.jobId);
        dest.writeValue(this.jobPickupStatus);
        dest.writeString(this.jobPickupLatitude);
        dest.writeString(this.jobPickupLongitude);
        dest.writeString(this.jobDeliverLatitude);
        dest.writeString(this.jobDeliverLongitude);
        dest.writeValue(this.jobType);
        dest.writeValue(this.fleetDriverId);
        dest.writeString(this.fleetNopol);
    }

    protected SimpleJob(Parcel in) {
        this.jobName = in.readString();
        this.origin = in.readString();
        this.destination = in.readString();
        this.orderNo = in.readString();
        this.pelanggan = in.readString();
        this.tanggal = in.readString();
        this.containerName = in.readString();
        this.containerNo = in.readString();
        this.comodity = in.readString();
        this.jobDeliverStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobPickupStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobPickupLatitude = in.readString();
        this.jobPickupLongitude = in.readString();
        this.jobDeliverLatitude = in.readString();
        this.jobDeliverLongitude = in.readString();
        this.jobType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleetDriverId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleetNopol = in.readString();
    }

    public static final Creator<SimpleJob> CREATOR = new Creator<SimpleJob>() {
        @Override
        public SimpleJob createFromParcel(Parcel source) {
            return new SimpleJob(source);
        }

        @Override
        public SimpleJob[] newArray(int size) {
            return new SimpleJob[size];
        }
    };
}
