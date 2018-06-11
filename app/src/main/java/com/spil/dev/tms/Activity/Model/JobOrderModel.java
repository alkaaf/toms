package com.spil.dev.tms.Activity.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by andresual on 2/1/2018.
 */

public class JobOrderModel implements Parcelable {

    private Integer PortOrderId;
    private String consignee_id;
    private Integer id;
    private Integer container_Cargo_ID;
    private String container_Cargo_Name;
    private String container_isfull;
    private String container_name;
    private String container_no;
    private String container_ownership;
    private Integer container_sizeID;
    private String customer_id;
    private String fleet_driver_email;
    private Integer fleet_driver_id;
    private String fleet_driver_name;
    private Integer fleet_driver_score;
    private Integer fleet_id;
    private String fleet_nopol;
    private String fleet_ownership;
    private String job_accept_time;
    private String job_blast;
    private String job_created;
    private String job_deliver_address;
    private String job_deliver_address_id;
    private String job_deliver_cancelnote;
    private String job_deliver_finishtime;
    private double job_deliver_latitude;
    private double job_deliver_longitude;
    private String job_deliver_starttime;
    private String job_deliver_status;
    private String job_description;
    private String job_pickup_address;
    private String job_pickup_address_id;
    private String job_pickup_datetime;
    private double job_pickup_latitude;
    private double job_pickup_longitude;
    private String job_pickup_name;
    private String job_pickup_reject_note;
    private String job_pickup_status;
    private Integer job_type;
    private String job_type_name;
    private String message;
    private String order_id;
    private Integer status;
    private String time_zone;
    private ArrayList<JobOrderModel> jobOrderModelArrayList;

    public ArrayList<JobOrderModel> getJobOrderModelArrayList() {
        return jobOrderModelArrayList;
    }

    public void setJobOrderModelArrayList(ArrayList<JobOrderModel> jobOrderModelArrayList) {
        this.jobOrderModelArrayList = jobOrderModelArrayList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPortOrderId() {
        return PortOrderId;
    }

    public void setPortOrderId(Integer portOrderId) {
        PortOrderId = portOrderId;
    }

    public String getConsignee_id() {
        return consignee_id;
    }

    public void setConsignee_id(String consignee_id) {
        this.consignee_id = consignee_id;
    }

    public Integer getContainer_Cargo_ID() {
        return container_Cargo_ID;
    }

    public void setContainer_Cargo_ID(Integer container_Cargo_ID) {
        this.container_Cargo_ID = container_Cargo_ID;
    }

    public String getContainer_Cargo_Name() {
        return container_Cargo_Name;
    }

    public void setContainer_Cargo_Name(String container_Cargo_Name) {
        this.container_Cargo_Name = container_Cargo_Name;
    }

    public String getContainer_isfull() {
        return container_isfull;
    }

    public void setContainer_isfull(String container_isfull) {
        this.container_isfull = container_isfull;
    }

    public String getContainer_name() {
        return container_name;
    }

    public void setContainer_name(String container_name) {
        this.container_name = container_name;
    }

    public String getContainer_no() {
        return container_no;
    }

    public void setContainer_no(String container_no) {
        this.container_no = container_no;
    }

    public String getContainer_ownership() {
        return container_ownership;
    }

    public void setContainer_ownership(String container_ownership) {
        this.container_ownership = container_ownership;
    }

    public Integer getContainer_sizeID() {
        return container_sizeID;
    }

    public void setContainer_sizeID(Integer container_sizeID) {
        this.container_sizeID = container_sizeID;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getFleet_driver_email() {
        return fleet_driver_email;
    }

    public void setFleet_driver_email(String fleet_driver_email) {
        this.fleet_driver_email = fleet_driver_email;
    }

    public Integer getFleet_driver_id() {
        return fleet_driver_id;
    }

    public void setFleet_driver_id(Integer fleet_driver_id) {
        this.fleet_driver_id = fleet_driver_id;
    }

    public String getFleet_driver_name() {
        return fleet_driver_name;
    }

    public void setFleet_driver_name(String fleet_driver_name) {
        this.fleet_driver_name = fleet_driver_name;
    }

    public Integer getFleet_driver_score() {
        return fleet_driver_score;
    }

    public void setFleet_driver_score(Integer fleet_driver_score) {
        this.fleet_driver_score = fleet_driver_score;
    }

    public Integer getFleet_id() {
        return fleet_id;
    }

    public void setFleet_id(Integer fleet_id) {
        this.fleet_id = fleet_id;
    }

    public String getFleet_nopol() {
        return fleet_nopol;
    }

    public void setFleet_nopol(String fleet_nopol) {
        this.fleet_nopol = fleet_nopol;
    }

    public String getFleet_ownership() {
        return fleet_ownership;
    }

    public void setFleet_ownership(String fleet_ownership) {
        this.fleet_ownership = fleet_ownership;
    }

    public String getJob_accept_time() {
        return job_accept_time;
    }

    public void setJob_accept_time(String job_accept_time) {
        this.job_accept_time = job_accept_time;
    }

    public String getJob_blast() {
        return job_blast;
    }

    public void setJob_blast(String job_blast) {
        this.job_blast = job_blast;
    }

    public String getJob_created() {
        return job_created;
    }

    public void setJob_created(String job_created) {
        this.job_created = job_created;
    }

    public String getJob_deliver_address() {
        return job_deliver_address;
    }

    public void setJob_deliver_address(String job_deliver_address) {
        this.job_deliver_address = job_deliver_address;
    }

    public String getJob_deliver_address_id() {
        return job_deliver_address_id;
    }

    public void setJob_deliver_address_id(String job_deliver_address_id) {
        this.job_deliver_address_id = job_deliver_address_id;
    }

    public String getJob_deliver_cancelnote() {
        return job_deliver_cancelnote;
    }

    public void setJob_deliver_cancelnote(String job_deliver_cancelnote) {
        this.job_deliver_cancelnote = job_deliver_cancelnote;
    }

    public String getJob_deliver_finishtime() {
        return job_deliver_finishtime;
    }

    public void setJob_deliver_finishtime(String job_deliver_finishtime) {
        this.job_deliver_finishtime = job_deliver_finishtime;
    }

    public double getJob_deliver_latitude() {
        return job_deliver_latitude;
    }

    public void setJob_deliver_latitude(double job_deliver_latitude) {
        this.job_deliver_latitude = job_deliver_latitude;
    }

    public double getJob_deliver_longitude() {
        return job_deliver_longitude;
    }

    public void setJob_deliver_longitude(double job_deliver_longitude) {
        this.job_deliver_longitude = job_deliver_longitude;
    }

    public String getJob_deliver_starttime() {
        return job_deliver_starttime;
    }

    public void setJob_deliver_starttime(String job_deliver_starttime) {
        this.job_deliver_starttime = job_deliver_starttime;
    }

    public String getJob_deliver_status() {
        return job_deliver_status;
    }

    public void setJob_deliver_status(String job_deliver_status) {
        this.job_deliver_status = job_deliver_status;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public String getJob_pickup_address() {
        return job_pickup_address;
    }

    public void setJob_pickup_address(String job_pickup_address) {
        this.job_pickup_address = job_pickup_address;
    }

    public String getJob_pickup_address_id() {
        return job_pickup_address_id;
    }

    public void setJob_pickup_address_id(String job_pickup_address_id) {
        this.job_pickup_address_id = job_pickup_address_id;
    }

    public String getJob_pickup_datetime() {
        return job_pickup_datetime;
    }

    public void setJob_pickup_datetime(String job_pickup_datetime) {
        this.job_pickup_datetime = job_pickup_datetime;
    }

    public double getJob_pickup_latitude() {
        return job_pickup_latitude;
    }

    public void setJob_pickup_latitude(double job_pickup_latitude) {
        this.job_pickup_latitude = job_pickup_latitude;
    }

    public double getJob_pickup_longitude() {
        return job_pickup_longitude;
    }

    public void setJob_pickup_longitude(double job_pickup_longitude) {
        this.job_pickup_longitude = job_pickup_longitude;
    }

    public String getJob_pickup_name() {
        return job_pickup_name;
    }

    public void setJob_pickup_name(String job_pickup_name) {
        this.job_pickup_name = job_pickup_name;
    }

    public String getJob_pickup_reject_note() {
        return job_pickup_reject_note;
    }

    public void setJob_pickup_reject_note(String job_pickup_reject_note) {
        this.job_pickup_reject_note = job_pickup_reject_note;
    }

    public String getJob_pickup_status() {
        return job_pickup_status;
    }

    public void setJob_pickup_status(String job_pickup_status) {
        this.job_pickup_status = job_pickup_status;
    }

    public Integer getJob_type() {
        return job_type;
    }

    public void setJob_type(Integer job_type) {
        this.job_type = job_type;
    }

    public String getJob_type_name() {
        return job_type_name;
    }

    public void setJob_type_name(String job_type_name) {
        this.job_type_name = job_type_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.PortOrderId);
        dest.writeString(this.consignee_id);
        dest.writeValue(this.id);
        dest.writeValue(this.container_Cargo_ID);
        dest.writeString(this.container_Cargo_Name);
        dest.writeString(this.container_isfull);
        dest.writeString(this.container_name);
        dest.writeString(this.container_no);
        dest.writeString(this.container_ownership);
        dest.writeValue(this.container_sizeID);
        dest.writeString(this.customer_id);
        dest.writeString(this.fleet_driver_email);
        dest.writeValue(this.fleet_driver_id);
        dest.writeString(this.fleet_driver_name);
        dest.writeValue(this.fleet_driver_score);
        dest.writeValue(this.fleet_id);
        dest.writeString(this.fleet_nopol);
        dest.writeString(this.fleet_ownership);
        dest.writeString(this.job_accept_time);
        dest.writeString(this.job_blast);
        dest.writeString(this.job_created);
        dest.writeString(this.job_deliver_address);
        dest.writeString(this.job_deliver_address_id);
        dest.writeString(this.job_deliver_cancelnote);
        dest.writeString(this.job_deliver_finishtime);
        dest.writeDouble(this.job_deliver_latitude);
        dest.writeDouble(this.job_deliver_longitude);
        dest.writeString(this.job_deliver_starttime);
        dest.writeString(this.job_deliver_status);
        dest.writeString(this.job_description);
        dest.writeString(this.job_pickup_address);
        dest.writeString(this.job_pickup_address_id);
        dest.writeString(this.job_pickup_datetime);
        dest.writeDouble(this.job_pickup_latitude);
        dest.writeDouble(this.job_pickup_longitude);
        dest.writeString(this.job_pickup_name);
        dest.writeString(this.job_pickup_reject_note);
        dest.writeString(this.job_pickup_status);
        dest.writeValue(this.job_type);
        dest.writeString(this.job_type_name);
        dest.writeString(this.message);
        dest.writeString(this.order_id);
        dest.writeValue(this.status);
        dest.writeString(this.time_zone);
    }

    public JobOrderModel() {
    }

    protected JobOrderModel(Parcel in) {
        this.PortOrderId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.consignee_id = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.container_Cargo_ID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.container_Cargo_Name = in.readString();
        this.container_isfull = in.readString();
        this.container_name = in.readString();
        this.container_no = in.readString();
        this.container_ownership = in.readString();
        this.container_sizeID = (Integer) in.readValue(Integer.class.getClassLoader());
        this.customer_id = in.readString();
        this.fleet_driver_email = in.readString();
        this.fleet_driver_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleet_driver_name = in.readString();
        this.fleet_driver_score = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleet_id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleet_nopol = in.readString();
        this.fleet_ownership = in.readString();
        this.job_accept_time = in.readString();
        this.job_blast = in.readString();
        this.job_created = in.readString();
        this.job_deliver_address = in.readString();
        this.job_deliver_address_id = in.readString();
        this.job_deliver_cancelnote = in.readString();
        this.job_deliver_finishtime = in.readString();
        this.job_deliver_latitude = in.readDouble();
        this.job_deliver_longitude = in.readDouble();
        this.job_deliver_starttime = in.readString();
        this.job_deliver_status = in.readString();
        this.job_description = in.readString();
        this.job_pickup_address = in.readString();
        this.job_pickup_address_id = in.readString();
        this.job_pickup_datetime = in.readString();
        this.job_pickup_latitude = in.readDouble();
        this.job_pickup_longitude = in.readDouble();
        this.job_pickup_name = in.readString();
        this.job_pickup_reject_note = in.readString();
        this.job_pickup_status = in.readString();
        this.job_type = (Integer) in.readValue(Integer.class.getClassLoader());
        this.job_type_name = in.readString();
        this.message = in.readString();
        this.order_id = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.time_zone = in.readString();
    }

    public static final Parcelable.Creator<JobOrderModel> CREATOR = new Parcelable.Creator<JobOrderModel>() {
        @Override
        public JobOrderModel createFromParcel(Parcel source) {
            return new JobOrderModel(source);
        }

        @Override
        public JobOrderModel[] newArray(int size) {
            return new JobOrderModel[size];
        }
    };
}