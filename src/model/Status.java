package model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public enum Status {
    @SerializedName("NEW")
    NEW,
    @SerializedName("IN_PROGRESS")
    IN_PROGRESS,
    @SerializedName("DONE")
    DONE;


}
