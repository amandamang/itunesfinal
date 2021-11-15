package com.example.amapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Results implements Parcelable, Serializable {
    @JsonProperty("wrapperType")
    private final String wrapperType;
    @JsonProperty("artistType")
    private final String artistType;
    @JsonProperty("artistName")
    private final String artistName;
    @JsonProperty("artistLinkUrl")
    private final String artistLinkUrl;
    @JsonProperty("artistId")
    private final int artistId;
    @JsonProperty("primaryGenreName")
    private final String primaryGenreName;
    @JsonProperty("primaryGenreId")
    private final int primaryGenreId;
    @JsonProperty("results")
    private List<Results> resultsList;

    @JsonCreator
    public Results(@JsonProperty("wrapperType") String wrapperType, @JsonProperty("artistType") String artistType, @JsonProperty("artistName") String artistName, @JsonProperty("artistLinkUrl") String artistLinkUrl, @JsonProperty("artistId") int artistId, @JsonProperty("primaryGenreName") String primaryGenreName, @JsonProperty("primaryGenreId") int primaryGenreId) {
        this.wrapperType = wrapperType;
        this.artistType = artistType;
        this.artistName = artistName;
        this.artistLinkUrl = artistLinkUrl;
        this.artistId = artistId;
        this.primaryGenreName = primaryGenreName;
        this.primaryGenreId = primaryGenreId;
    }

    protected Results(Parcel in) {
        wrapperType = in.readString();
        artistType = in.readString();
        artistName = in.readString();
        artistLinkUrl = in.readString();
        artistId = in.readInt();
        primaryGenreName = in.readString();
        primaryGenreId = in.readInt();
    }

    public static final Creator<Results> CREATOR = new Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel in) {
            return new Results(in);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };

    public List<Results> getResultsList() {
        return resultsList;
    }

    public String getWrapperType() {
        return wrapperType;
    }

    public String getArtistType() {
        return artistType;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistLinkUrl() {
        return artistLinkUrl;
    }

    public int getArtistId() {
        return artistId;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public int getPrimaryGenreId() {
        return primaryGenreId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(wrapperType);
        parcel.writeString(artistType);
        parcel.writeString(artistName);
        parcel.writeString(artistLinkUrl);
        parcel.writeInt(artistId);
        parcel.writeString(primaryGenreName);
        parcel.writeInt(primaryGenreId);
    }
}
