package ua.gov.court.supreme.sevhelper.model;

public class SevUser {
    private long id;
    private String edrpou;
    private String shortName;
    private String fullName;
    private String isTerminated;
    private boolean isConnected;

    public SevUser(long id, String edrpou, String shortName, String fullName, String isTerminated, boolean isConnected) {
        this.id = id;
        this.edrpou = edrpou;
        this.shortName = shortName;
        this.fullName = fullName;
        this.isTerminated = isTerminated;
        this.isConnected = isConnected;
    }

    public long getId() {
        return id;
    }

    public String getEdrpou() {
        return edrpou;
    }

    public String getShortName() {
        return shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIsTerminated() {
        return isTerminated;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
