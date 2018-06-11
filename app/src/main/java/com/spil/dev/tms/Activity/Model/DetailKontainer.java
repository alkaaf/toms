package com.spil.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailKontainer implements Parcelable {
    @SerializedName("iddetail")
    @Expose
    private String iddetail;
    @SerializedName("container_no")
    @Expose
    private String containerNo;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("container_owner")
    @Expose
    private String containerOwner;
    @SerializedName("container_status")
    @Expose
    private String containerStatus;
    @SerializedName("Container_ID")
    @Expose
    private String containerID;
    @SerializedName("container_name")
    @Expose
    private String containerName;
    @SerializedName("pickup_location_name")
    @Expose
    private String pickupLocationName;
    @SerializedName("pickup_lat")
    @Expose
    private String pickupLat;
    @SerializedName("pickup_lng")
    @Expose
    private String pickupLng;
    @SerializedName("destination_name")
    @Expose
    private String destinationName;
    @SerializedName("destination_lat")
    @Expose
    private double destinationLat;
    @SerializedName("destination_lng")
    @Expose
    private double destinationLng;
    @SerializedName("job_status")
    int jobStatus;

    public int getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getIddetail() {
        return iddetail;
    }

    public void setIddetail(String iddetail) {
        this.iddetail = iddetail;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContainerOwner() {
        return containerOwner;
    }

    public void setContainerOwner(String containerOwner) {
        this.containerOwner = containerOwner;
    }

    public String getContainerStatus() {
        return containerStatus;
    }

    public void setContainerStatus(String containerStatus) {
        this.containerStatus = containerStatus;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getPickupLocationName() {
        return pickupLocationName;
    }

    public void setPickupLocationName(String pickupLocationName) {
        this.pickupLocationName = pickupLocationName;
    }

    public String getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(String pickupLat) {
        this.pickupLat = pickupLat;
    }

    public String getPickupLng() {
        return pickupLng;
    }

    public void setPickupLng(String pickupLng) {
        this.pickupLng = pickupLng;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }

    public DetailKontainer() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.iddetail);
        dest.writeString(this.containerNo);
        dest.writeString(this.customerName);
        dest.writeString(this.containerOwner);
        dest.writeString(this.containerStatus);
        dest.writeString(this.containerID);
        dest.writeString(this.containerName);
        dest.writeString(this.pickupLocationName);
        dest.writeString(this.pickupLat);
        dest.writeString(this.pickupLng);
        dest.writeString(this.destinationName);
        dest.writeDouble(this.destinationLat);
        dest.writeDouble(this.destinationLng);
        dest.writeInt(this.jobStatus);
    }

    protected DetailKontainer(Parcel in) {
        this.iddetail = in.readString();
        this.containerNo = in.readString();
        this.customerName = in.readString();
        this.containerOwner = in.readString();
        this.containerStatus = in.readString();
        this.containerID = in.readString();
        this.containerName = in.readString();
        this.pickupLocationName = in.readString();
        this.pickupLat = in.readString();
        this.pickupLng = in.readString();
        this.destinationName = in.readString();
        this.destinationLat = in.readDouble();
        this.destinationLng = in.readDouble();
        this.jobStatus = in.readInt();
    }

    public static final Creator<DetailKontainer> CREATOR = new Creator<DetailKontainer>() {
        @Override
        public DetailKontainer createFromParcel(Parcel source) {
            return new DetailKontainer(source);
        }

        @Override
        public DetailKontainer[] newArray(int size) {
            return new DetailKontainer[size];
        }
    };
}
