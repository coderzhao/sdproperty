package com.quadrant.fr;

import java.util.List;
import java.util.Map;

public class NTechIdentifyResponse
{
    private Map<String,List<NTechFaceMatch>> results;

    public Map<String, List<NTechFaceMatch>> getResults()
    {
        return results;
    }

    public void setResults(Map<String, List<NTechFaceMatch>> results)
    {
        this.results = results;
    }

}
