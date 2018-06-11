package com.spil.dev.tms.Activity.Model;

/**
 * Created by andresual on 2/5/2018.
 */

public class CityModel {

    String id;
    String namaKota;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaKota() {
        return namaKota;
    }

    public void setNamaKota(String namaKota) {
        this.namaKota = namaKota;
    }

    @Override
    public String toString() {
        return namaKota;
    }
}
