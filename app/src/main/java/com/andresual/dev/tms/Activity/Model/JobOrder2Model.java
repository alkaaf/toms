package com.andresual.dev.tms.Activity.Model;

import java.util.ServiceConfigurationError;

import javax.xml.transform.sax.SAXResult;

/**
 * Created by andresual on 2/15/2018.
 */

public class JobOrder2Model {

    private String jobName;
    private String origin;
    private String destination;
    private String orderNo;
    private String pelanggan;
    private String tanggal;
    private String containerName;
    private String containerNo;
    private String comodity;
    private Integer jobDeliverStatus;
    private Integer jobId;
    private Integer jobPickupStatus;
    private String jobPickupLatitude;
    private String jobPickupLongitude;
    private String jobDeliverLatitude;
    private String jobDeliverLongitude;
    private Integer jobType;

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
}
