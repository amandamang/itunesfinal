package com.example.amapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseResult implements Serializable, Parcelable {
    @JsonProperty("resultCount")
    public int resultCount;
    @JsonProperty("results")
    public List<Results> results = new ArrayList<>();

    public ResponseResult(){}

    public ResponseResult(int resultCount, List<Results> results){
        this.resultCount = resultCount;
        this.results = results;
    }

    public int getResultCount() {
        return resultCount;
    }

    public List<Results> getResults() {
        return results;
    }

    protected ResponseResult(Parcel in) {
        resultCount = in.readInt();
        results = in.createTypedArrayList(Results.CREATOR);
    }

    public static final Creator<ResponseResult> CREATOR = new Creator<ResponseResult>() {
        @Override
        public ResponseResult createFromParcel(Parcel in) {
            return new ResponseResult(in);
        }

        @Override
        public ResponseResult[] newArray(int size) {
            return new ResponseResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(resultCount);
        parcel.writeTypedList(results);
    }
}
