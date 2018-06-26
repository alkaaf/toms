package com.spil.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RealJob implements Parcelable {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fleet_driver_email")
    @Expose
    private String fleetDriverEmail;
    @SerializedName("job_type")
    @Expose
    private Integer jobType;
    @SerializedName("job_type_name")
    @Expose
    private String jobTypeName;
    @SerializedName("job_description")
    @Expose
    private String jobDescription;
    @SerializedName("job_created")
    @Expose
    private String jobCreated;
    @SerializedName("job_blast")
    @Expose
    private String jobBlast;
    @SerializedName("job_pickup_name")
    @Expose
    private String jobPickupName;
    @SerializedName("job_pickup_address_id")
    @Expose
    private String jobPickupAddressId;
    @SerializedName("job_pickup_address")
    @Expose
    private String jobPickupAddress;
    @SerializedName("job_pickup_latitude")
    @Expose
    private String jobPickupLatitude;
    @SerializedName("job_pickup_longitude")
    @Expose
    private String jobPickupLongitude;
    @SerializedName("job_pickup_datetime")
    @Expose
    private String jobPickupDatetime;
    @SerializedName("job_accept_time")
    @Expose
    private String jobAcceptTime;
    @SerializedName("job_deliver_address_id")
    @Expose
    private String jobDeliverAddressId;
    @SerializedName("job_deliver_address")
    @Expose
    private String jobDeliverAddress;
    @SerializedName("job_deliver_latitude")
    @Expose
    private String jobDeliverLatitude;
    @SerializedName("job_deliver_longitude")
    @Expose
    private String jobDeliverLongitude;
    @SerializedName("job_deliver_starttime")
    @Expose
    private String jobDeliverStarttime;
    @SerializedName("job_deliver_finishtime")
    @Expose
    private String jobDeliverFinishtime;
    @SerializedName("job_deliver_status")
    @Expose
    private Integer jobDeliverStatus;
    @SerializedName("job_deliver_namastatus")
    @Expose
    private String jobDeliverNamastatus;
    @SerializedName("job_deliver_distance")
    @Expose
    private Integer jobDeliverDistance;
    @SerializedName("job_deliver_distancetext")
    @Expose
    private String jobDeliverDistancetext;
    @SerializedName("job_deliver_estimatetime")
    @Expose
    private Integer jobDeliverEstimatetime;
    @SerializedName("job_deliver_estimatetimetext")
    @Expose
    private String jobDeliverEstimatetimetext;
    @SerializedName("job_deliver_estimatefinish")
    @Expose
    private String jobDeliverEstimatefinish;
    @SerializedName("job_balik_address_id")
    @Expose
    private String jobBalikAddressId;
    @SerializedName("job_balik_address")
    @Expose
    private String jobBalikAddress;
    @SerializedName("job_balik_latitude")
    @Expose
    private String jobBalikLatitude;
    @SerializedName("job_balik_longitude")
    @Expose
    private String jobBalikLongitude;
    @SerializedName("job_baliktipe")
    @Expose
    private String jobBaliktipe;
    @SerializedName("fleet_id")
    @Expose
    private Integer fleetId;
    @SerializedName("fleet_nopol")
    @Expose
    private String fleetNopol;
    @SerializedName("fleet_kode")
    @Expose
    private String fleetKode;
    @SerializedName("fleet_driver_id")
    @Expose
    private Integer fleetDriverId;
    @SerializedName("fleet_driver_name")
    @Expose
    private String fleetDriverName;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;
    @SerializedName("portorderid")
    @Expose
    private Integer portorderid;
    @SerializedName("jumlahbox")
    @Expose
    private String jumlahbox;
    @SerializedName("detailkontainer")
    @Expose
    private List<DetailKontainer> detailkontainer = null;
    @SerializedName("jumlahtujuan")
    int jumlahtujuan;
    @SerializedName("jumlahterikrim")
    int jumlahterkirim;

    List<Fence> geofence_asal;
    List<Fence> geofence_tujuan;
    List<Fence> geofence_balik;


    public List<LatLng> getGeofence_asal() {
        if (geofence_asal != null && !geofence_asal.isEmpty()) {
            return geofence_asal.get(0).getDecodedPath();
        }
        return null;
    }

    public void setGeofence_asal(List<Fence> geofence_asal) {
        this.geofence_asal = geofence_asal;
    }

    public List<LatLng> getGeofence_tujuan() {
        if (geofence_tujuan != null && !geofence_tujuan.isEmpty()) {
            return geofence_tujuan.get(0).getDecodedPath();
        }
        return null;
    }

    public void setGeofence_tujuan(List<Fence> geofence_tujuan) {
        this.geofence_tujuan = geofence_tujuan;
    }

    public List<LatLng> getGeofence_balik() {
        if (geofence_balik != null && !geofence_balik.isEmpty()) {
            return geofence_balik.get(0).getDecodedPath();
        }
        return null;
    }

    public void setGeofence_balik(List<Fence> geofence_balik) {
        this.geofence_balik = geofence_balik;
    }

    public String getButtonLabel() {
        SparseArray<SparseArray<String>> sp = new SparseArray<>();
        // 1
        SparseArray<String> s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI LINI 1");
        s.put(5, "MULAI MUAT");
        s.put(6, "SELESAI MUAT");
        s.put(7, "KIRIM KE DEPO");
        s.put(8, "SAMPAI DI DEPO");
        s.put(9, "MULAI BONGKAR");
        s.put(10, "SELESAI ORDER");
        if (statusKontainer() == 2 || jumlahtujuan == 1) {
            s.put(10, "SELESAI ORDER");
            s.put(14, "UNGGAH FOTO");
        } else {
            s.put(10, "ORDER PERTAMA SELESAI");
            s.put(14, "KIRIM SELANJUTNYA");
        }
        sp.put(1, s);


        // 2
        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI DEPO");
        s.put(5, "MULAI MUAT");
        s.put(6, "SELESAI MUAT");
        s.put(7, "KIRIM KE LINI 1");
        s.put(8, "SAMPAI DI LINI 1");
        s.put(9, "MULAI BONGKAR");
        s.put(10, "SELESAI ORDER");
        if (statusKontainer() == 2 || jumlahtujuan == 1) {
            s.put(10, "SELESAI ORDER");
            s.put(14, "UNGGAH FOTO");
        } else {
            s.put(10, "ORDER PERTAMA SELESAI");
            s.put(14, "KIRIM SELANJUTNYA");
        }
        sp.put(2, s);


        // 3
        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI DEPO");
        s.put(5, "MULAI MUAT");
        s.put(6, "SELESAI MUAT");
        s.put(7, "KIRIM KE DEPO");
        s.put(8, "SAMPAI DI DEPO");
        s.put(9, "MULAI BONGKAR");
        s.put(10, "SELESAI ORDER");
        if (statusKontainer() == 2 || jumlahtujuan == 1) {
            s.put(10, "SELESAI ORDER");
            s.put(14, "UNGGAH FOTO");
        } else {
            s.put(10, "ORDER PERTAMA SELESAI");
            s.put(14, "KIRIM SELANJUTNYA");
        }
        sp.put(3, s);
        

        // 4
        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI DEPO");
        s.put(5, "MULAI MUAT");
        s.put(6, "SELESAI MUAT");
        s.put(7, "KIRIM KE PABRIK");
        s.put(8, "SAMPAI DI PABRIK");
        s.put(9, "MULAI BONGKAR");
        s.put(10, "SELESAI ORDER");
        if (statusKontainer() == 2) {
            s.put(10, "SELESAI ORDER");
            s.put(14, "UNGGAH FOTO");
        } else {
            s.put(10, "ORDER PERTAMA SELESAI");
            s.put(14, "KIRIM SELANJUTNYA");
        }
        sp.put(4, s);
        

        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI PABRIK");
        s.put(5, "MULAI ISI");
        s.put(6, "SELESAI ISI");
        s.put(7, "KIRIM KE DEPO");
        s.put(8, "SAMPAI DI DEPO");
        s.put(9, "SELESAI MUAT");
        s.put(10, "SELESAI ORDER");
        if (statusKontainer() == 2) {
            s.put(10, "SELESAI ORDER");
            s.put(14, "UNGGAH FOTO");
        } else {
            s.put(10, "ORDER PERTAMA SELESAI");
            s.put(14, "KIRIM SELANJUTNYA");
        }
        sp.put(5, s);
        

        // 6
        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI PABRIK");
        s.put(5, "MULAI ISI");
        s.put(6, "SELESAI ISI");
        s.put(7, "KIRIM KE LINI 1");
        s.put(8, "SAMPAI DI LINI 1");
        s.put(9, "SELESAI BONGKAR");
        s.put(10, "SELESAI ORDER");
        if (statusKontainer() == 2) {
            s.put(10, "SELESAI ORDER");
            s.put(14, "UNGGAH FOTO");
        } else {
            s.put(10, "ORDER PERTAMA SELESAI");
            s.put(14, "KIRIM SELANJUTNYA");
        }
        sp.put(6, s);
        
        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI LINI 1");
        s.put(5, "MULAI MUAT");
        s.put(6, "SELESAI MUAT");
        s.put(7, "KIRIM DI PABRIK");
        s.put(8, "SAMPAI DI PABRIK");
        s.put(9, "SELESAI BONGKAR");
        if (statusKontainer() == 2) {
            s.put(10, "SELESAI ORDER");
            s.put(14, "UNGGAH FOTO");
        } else {
            s.put(10, "ORDER PERTAMA SELESAI");
            s.put(14, "KIRIM SELANJUTNYA");
        }
        sp.put(7, s);


        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI DEPO");
        s.put(5, "MULAI MUAT");
        s.put(6, "SELESAI MUAT");
        s.put(7, "KIRIM KE PABRIK");
        s.put(8, "SAMPAI DI PABRIK");
        s.put(9, "MULAI ISI");
        s.put(10, "SELESAI ISI");
        s.put(11, "KIRIM KE LINI 1");
        s.put(12, "SAMPAI DI LINI 1");
        s.put(13, "SELESAI BONGKAR");
//        if (statusKontainer() == 2) {
        s.put(14, "UNGGAH FOTO");
//        } else {
//            s.put(14, "KIRIM SELANJUTNYA");
//        }
        sp.put(8, s);


        s = new SparseArray<>();
        s.put(3, "ANGKUT");
        s.put(4, "SAMPAI DI LINI 1");
        s.put(5, "MULAI MUAT");
        s.put(6, "SELESAI MUAT");
        s.put(7, "KIRIM KE PABRIK");
        s.put(8, "SAMPAI DI PABRIK");
        s.put(9, "MULAI PENGELUARAN");
        s.put(10, "SELESAI MENGELUARKAN");
        s.put(11, "KIRIM KE DEPO");
        s.put(12, "SAMPAI DI DEPO");
        s.put(13, "SELESAI BONGKAR");
//        if (statusKontainer() == 2) {
        s.put(14, "UNGGAH FOTO");
//        } else {
//            s.put(14, "KIRIM JOB SELANJUTNYA");
//        }
        sp.put(9, s);
        return sp.get(getJobType()).get(getJobDeliverStatus(), "-");
    }

    public boolean isContainerValid(){
        return detailkontainer!= null && !detailkontainer.isEmpty() && isNomorContainerAvailable();
    }

    public boolean isNomorContainerAvailable(){
        if(detailkontainer == null || detailkontainer.isEmpty()){
            return false;
        }

        for (int i = 0; i < detailkontainer.size(); i++) {
            if(TextUtils.isEmpty(detailkontainer.get(i).getContainerNo())){
                return false;
            }
        }
        return true;
    }
    public String getStringJobTypeName() {
        SparseArray<String> s = new SparseArray<>();
        s.put(1, "Lini 1 ke Depo");
        s.put(2, "Depo ke Lini 1");
        s.put(3, "Depo ke Depo");
        s.put(4, "Depo ke Pabrik");
        s.put(5, "Pabrik ke Depo");
        s.put(6, "Pabrik ke Lini 1");
        s.put(7, "Lini 1 ke Pabrik");
        s.put(8, "Depo - Pabrik - Lini 1");
        s.put(9, "Lini 1 - Pabrik - Depo");
        return s.get(getJobType(), "-");
    }

    public int getJumlahterkirim() {
        return jumlahterkirim;
    }

    public void setJumlahterkirim(int jumlahterkirim) {
        this.jumlahterkirim = jumlahterkirim;
    }

    public int getJumlahtujuan() {
        return jumlahtujuan;
    }

    public void setJumlahtujuan(int jumlahtujuan) {
        this.jumlahtujuan = jumlahtujuan;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFleetDriverEmail() {
        return fleetDriverEmail;
    }

    public void setFleetDriverEmail(String fleetDriverEmail) {
        this.fleetDriverEmail = fleetDriverEmail;
    }

    public Integer getJobType() {
        return jobType;
    }

    public void setJobType(Integer jobType) {
        this.jobType = jobType;
    }

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobCreated() {
        return jobCreated;
    }

    public void setJobCreated(String jobCreated) {
        this.jobCreated = jobCreated;
    }

    public String getJobBlast() {
        return jobBlast;
    }

    public void setJobBlast(String jobBlast) {
        this.jobBlast = jobBlast;
    }

    public String getJobPickupName() {
        return jobPickupName;
    }

    public void setJobPickupName(String jobPickupName) {
        this.jobPickupName = jobPickupName;
    }

    public String getJobPickupAddressId() {
        return jobPickupAddressId;
    }

    public void setJobPickupAddressId(String jobPickupAddressId) {
        this.jobPickupAddressId = jobPickupAddressId;
    }

    public String getJobPickupAddress() {
        return jobPickupAddress;
    }

    public void setJobPickupAddress(String jobPickupAddress) {
        this.jobPickupAddress = jobPickupAddress;
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

    public String getJobPickupDatetime() {
        return jobPickupDatetime;
    }

    public void setJobPickupDatetime(String jobPickupDatetime) {
        this.jobPickupDatetime = jobPickupDatetime;
    }

    public String getJobAcceptTime() {
        return jobAcceptTime;
    }

    public void setJobAcceptTime(String jobAcceptTime) {
        this.jobAcceptTime = jobAcceptTime;
    }

    public String getJobDeliverAddressId() {
        return jobDeliverAddressId;
    }

    public void setJobDeliverAddressId(String jobDeliverAddressId) {
        this.jobDeliverAddressId = jobDeliverAddressId;
    }

    public String getJobDeliverAddress() {
        return jobDeliverAddress;
    }

    public void setJobDeliverAddress(String jobDeliverAddress) {
        this.jobDeliverAddress = jobDeliverAddress;
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

    public String getJobDeliverStarttime() {
        return jobDeliverStarttime;
    }

    public void setJobDeliverStarttime(String jobDeliverStarttime) {
        this.jobDeliverStarttime = jobDeliverStarttime;
    }

    public String getJobDeliverFinishtime() {
        return jobDeliverFinishtime;
    }

    public void setJobDeliverFinishtime(String jobDeliverFinishtime) {
        this.jobDeliverFinishtime = jobDeliverFinishtime;
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

    public Integer getJobDeliverStatus() {
        return jobDeliverStatus;
    }

    public void setJobDeliverStatus(Integer jobDeliverStatus) {
        this.jobDeliverStatus = jobDeliverStatus;
    }

    public String getJobDeliverNamastatus() {
        return jobDeliverNamastatus;
    }

    public void setJobDeliverNamastatus(String jobDeliverNamastatus) {
        this.jobDeliverNamastatus = jobDeliverNamastatus;
    }

    public Integer getJobDeliverDistance() {
        return jobDeliverDistance;
    }

    public void setJobDeliverDistance(Integer jobDeliverDistance) {
        this.jobDeliverDistance = jobDeliverDistance;
    }

    public String getJobDeliverDistancetext() {
        return jobDeliverDistancetext;
    }

    public void setJobDeliverDistancetext(String jobDeliverDistancetext) {
        this.jobDeliverDistancetext = jobDeliverDistancetext;
    }

    public Integer getJobDeliverEstimatetime() {
        return jobDeliverEstimatetime;
    }

    public void setJobDeliverEstimatetime(Integer jobDeliverEstimatetime) {
        this.jobDeliverEstimatetime = jobDeliverEstimatetime;
    }

    public String getJobDeliverEstimatetimetext() {
        return jobDeliverEstimatetimetext;
    }

    public void setJobDeliverEstimatetimetext(String jobDeliverEstimatetimetext) {
        this.jobDeliverEstimatetimetext = jobDeliverEstimatetimetext;
    }

    public String getJobDeliverEstimatefinish() {
        return jobDeliverEstimatefinish;
    }

    public void setJobDeliverEstimatefinish(String jobDeliverEstimatefinish) {
        this.jobDeliverEstimatefinish = jobDeliverEstimatefinish;
    }

    public String getJobBalikAddressId() {
        return jobBalikAddressId;
    }

    public void setJobBalikAddressId(String jobBalikAddressId) {
        this.jobBalikAddressId = jobBalikAddressId;
    }

    public String getJobBalikAddress() {
        return jobBalikAddress;
    }

    public void setJobBalikAddress(String jobBalikAddress) {
        this.jobBalikAddress = jobBalikAddress;
    }

    public String getJobBalikLatitude() {
        return jobBalikLatitude;
    }

    public void setJobBalikLatitude(String jobBalikLatitude) {
        this.jobBalikLatitude = jobBalikLatitude;
    }

    public String getJobBalikLongitude() {
        return jobBalikLongitude;
    }

    public void setJobBalikLongitude(String jobBalikLongitude) {
        this.jobBalikLongitude = jobBalikLongitude;
    }

    public String getJobBaliktipe() {
        return jobBaliktipe;
    }

    public void setJobBaliktipe(String jobBaliktipe) {
        this.jobBaliktipe = jobBaliktipe;
    }

    public Integer getFleetId() {
        return fleetId;
    }

    public void setFleetId(Integer fleetId) {
        this.fleetId = fleetId;
    }

    public String getFleetNopol() {
        return fleetNopol;
    }

    public void setFleetNopol(String fleetNopol) {
        this.fleetNopol = fleetNopol;
    }

    public String getFleetKode() {
        return fleetKode;
    }

    public void setFleetKode(String fleetKode) {
        this.fleetKode = fleetKode;
    }

    public Integer getFleetDriverId() {
        return fleetDriverId;
    }

    public void setFleetDriverId(Integer fleetDriverId) {
        this.fleetDriverId = fleetDriverId;
    }

    public String getFleetDriverName() {
        return fleetDriverName;
    }

    public void setFleetDriverName(String fleetDriverName) {
        this.fleetDriverName = fleetDriverName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getPortorderid() {
        return portorderid;
    }

    public void setPortorderid(Integer portorderid) {
        this.portorderid = portorderid;
    }

    public String getJumlahbox() {
        return jumlahbox;
    }

    public void setJumlahbox(String jumlahbox) {
        this.jumlahbox = jumlahbox;
    }

    public List<DetailKontainer> getDetailkontainer() {
        return detailkontainer;
    }

    public void setDetailkontainer(List<DetailKontainer> detailkontainer) {
        this.detailkontainer = detailkontainer;
    }

    public String parsedPickupDate() {
//        6/1/2018 10:03:12 AM
        SimpleDateFormat parser = new SimpleDateFormat("M/d/yyyy hh:mm:ss a");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            Date d = parser.parse(getJobPickupDatetime());
            return formatter.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Unable to parse";
        }
    }



    public RealJob() {
    }

    public int statusKontainer() {
        final boolean isJob89 = getJobType() == 8 || getJobType() == 9;
        final boolean isJobTujuanMoreThanOne = getJumlahtujuan() > 1;
        final boolean isSingleBox = Integer.parseInt(getJumlahbox()) == 1;

        if (!isJob89 && isJobTujuanMoreThanOne && !isSingleBox) {
            // jika belum diantar semua
            if (getDetailkontainer().get(0).getJobStatus() < 14 && getDetailkontainer().get(1).getJobStatus() < 14)
                return 0;

                // jika sudah diantar mek 1
            else if (getDetailkontainer().get(0).getJobStatus() == 14 && getDetailkontainer().get(1).getJobStatus() < 14)
                return 1;

                // jika sudah diantar keduanya
            else if (getDetailkontainer().get(0).getJobStatus() == 14 && getDetailkontainer().get(1).getJobStatus() == 14)
                return 2;
        }
        return -1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.message);
        dest.writeValue(this.status);
        dest.writeString(this.orderId);
        dest.writeValue(this.id);
        dest.writeString(this.fleetDriverEmail);
        dest.writeValue(this.jobType);
        dest.writeString(this.jobTypeName);
        dest.writeString(this.jobDescription);
        dest.writeString(this.jobCreated);
        dest.writeString(this.jobBlast);
        dest.writeString(this.jobPickupName);
        dest.writeString(this.jobPickupAddressId);
        dest.writeString(this.jobPickupAddress);
        dest.writeString(this.jobPickupLatitude);
        dest.writeString(this.jobPickupLongitude);
        dest.writeString(this.jobPickupDatetime);
        dest.writeString(this.jobAcceptTime);
        dest.writeString(this.jobDeliverAddressId);
        dest.writeString(this.jobDeliverAddress);
        dest.writeString(this.jobDeliverLatitude);
        dest.writeString(this.jobDeliverLongitude);
        dest.writeString(this.jobDeliverStarttime);
        dest.writeString(this.jobDeliverFinishtime);
        dest.writeValue(this.jobDeliverStatus);
        dest.writeString(this.jobDeliverNamastatus);
        dest.writeValue(this.jobDeliverDistance);
        dest.writeString(this.jobDeliverDistancetext);
        dest.writeValue(this.jobDeliverEstimatetime);
        dest.writeString(this.jobDeliverEstimatetimetext);
        dest.writeString(this.jobDeliverEstimatefinish);
        dest.writeString(this.jobBalikAddressId);
        dest.writeString(this.jobBalikAddress);
        dest.writeString(this.jobBalikLatitude);
        dest.writeString(this.jobBalikLongitude);
        dest.writeString(this.jobBaliktipe);
        dest.writeValue(this.fleetId);
        dest.writeString(this.fleetNopol);
        dest.writeString(this.fleetKode);
        dest.writeValue(this.fleetDriverId);
        dest.writeString(this.fleetDriverName);
        dest.writeString(this.timeZone);
        dest.writeValue(this.portorderid);
        dest.writeString(this.jumlahbox);
        dest.writeTypedList(this.detailkontainer);
        dest.writeInt(this.jumlahtujuan);
        dest.writeInt(this.jumlahterkirim);
        dest.writeTypedList(this.geofence_asal);
        dest.writeTypedList(this.geofence_tujuan);
        dest.writeTypedList(this.geofence_balik);
    }

    protected RealJob(Parcel in) {
        this.message = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.orderId = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleetDriverEmail = in.readString();
        this.jobType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobTypeName = in.readString();
        this.jobDescription = in.readString();
        this.jobCreated = in.readString();
        this.jobBlast = in.readString();
        this.jobPickupName = in.readString();
        this.jobPickupAddressId = in.readString();
        this.jobPickupAddress = in.readString();
        this.jobPickupLatitude = in.readString();
        this.jobPickupLongitude = in.readString();
        this.jobPickupDatetime = in.readString();
        this.jobAcceptTime = in.readString();
        this.jobDeliverAddressId = in.readString();
        this.jobDeliverAddress = in.readString();
        this.jobDeliverLatitude = in.readString();
        this.jobDeliverLongitude = in.readString();
        this.jobDeliverStarttime = in.readString();
        this.jobDeliverFinishtime = in.readString();
        this.jobDeliverStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobDeliverNamastatus = in.readString();
        this.jobDeliverDistance = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobDeliverDistancetext = in.readString();
        this.jobDeliverEstimatetime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jobDeliverEstimatetimetext = in.readString();
        this.jobDeliverEstimatefinish = in.readString();
        this.jobBalikAddressId = in.readString();
        this.jobBalikAddress = in.readString();
        this.jobBalikLatitude = in.readString();
        this.jobBalikLongitude = in.readString();
        this.jobBaliktipe = in.readString();
        this.fleetId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleetNopol = in.readString();
        this.fleetKode = in.readString();
        this.fleetDriverId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.fleetDriverName = in.readString();
        this.timeZone = in.readString();
        this.portorderid = (Integer) in.readValue(Integer.class.getClassLoader());
        this.jumlahbox = in.readString();
        this.detailkontainer = in.createTypedArrayList(DetailKontainer.CREATOR);
        this.jumlahtujuan = in.readInt();
        this.jumlahterkirim = in.readInt();
        this.geofence_asal = in.createTypedArrayList(Fence.CREATOR);
        this.geofence_tujuan = in.createTypedArrayList(Fence.CREATOR);
        this.geofence_balik = in.createTypedArrayList(Fence.CREATOR);
    }

    public static final Creator<RealJob> CREATOR = new Creator<RealJob>() {
        @Override
        public RealJob createFromParcel(Parcel source) {
            return new RealJob(source);
        }

        @Override
        public RealJob[] newArray(int size) {
            return new RealJob[size];
        }
    };
}
