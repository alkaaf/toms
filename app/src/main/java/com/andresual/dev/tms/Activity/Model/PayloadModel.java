package com.andresual.dev.tms.Activity.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class PayloadModel implements Parcelable {
    int id;
    String status;
    String prompt;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.status);
        dest.writeString(this.prompt);
    }

    public PayloadModel() {
    }

    protected PayloadModel(Parcel in) {
        this.id = in.readInt();
        this.status = in.readString();
        this.prompt = in.readString();
    }

    public static final Parcelable.Creator<PayloadModel> CREATOR = new Parcelable.Creator<PayloadModel>() {
        @Override
        public PayloadModel createFromParcel(Parcel source) {
            return new PayloadModel(source);
        }

        @Override
        public PayloadModel[] newArray(int size) {
            return new PayloadModel[size];
        }
    };
}
