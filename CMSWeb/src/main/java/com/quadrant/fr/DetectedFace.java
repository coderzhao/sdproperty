
package com.quadrant.fr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class DetectedFace {

    @SerializedName("faces")
    @Expose
    private List<NTechFace> results = new ArrayList<NTechFace>();

    public List<NTechFace> getResults() {
        return results;
    }

    public void setResults(List<NTechFace> results) {
        this.results = results;
    }

}
