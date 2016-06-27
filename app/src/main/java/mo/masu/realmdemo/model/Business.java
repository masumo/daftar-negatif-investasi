package mo.masu.realmdemo.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Business extends RealmObject {

    @PrimaryKey
    private int id;

    //  additionalReq

    private String name;

    private String status;

    private String otherReqs;

    private String kbli;

    private String foreignStock;

    private String sector;

    private int forSMB;

    private int forPartnership;

    // Standard getters & setters generated by your IDE…
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherReqs() {
        return otherReqs;
    }

    public void setOtherReqs(String otherReqs) {
        this.otherReqs = otherReqs;
    }

    public String getKbli() {
        return kbli;
    }

    public void setKbli(String kbli) {
        this.kbli = kbli;
    }

    public String getForeignStock() {
        return foreignStock;
    }

    public void setForeignStock(String foreignStock) {
        this.foreignStock = foreignStock;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public int getForSMB() {
        return forSMB;
    }

    public void setForSMB(int forSMB) {
        this.forSMB = forSMB;
    }

    public int getForPartnership() {
        return forPartnership;
    }

    public void setForPartnership(int forPartnership) {
        this.forPartnership = forPartnership;
    }

}