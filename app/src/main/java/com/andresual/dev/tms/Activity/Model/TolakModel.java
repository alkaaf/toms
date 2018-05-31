package com.andresual.dev.tms.Activity.Model;

/**
 * Created by andresual on 1/31/2018.
 */

public class TolakModel {

    String email;
    String idJob;
    String rejectNote;

    public String getRejectNote() {
        return rejectNote;
    }

    public void setRejectNote(String rejectNote) {
        this.rejectNote = rejectNote;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdJob() {
        return idJob;
    }

    public void setIdJob(String idJob) {
        this.idJob = idJob;
    }
}
