package pck.dbms.be.administrativeDivision;

import java.util.HashMap;

public class AdministrativeDivision {
    private static AdministrativeDivision instance;

    private HashMap<String, Province> provinceList;
    private HashMap<String, District> districtList;
    private HashMap<String, Ward> wardList;

    private AdministrativeDivision(){
        provinceList = new HashMap<>();
        districtList = new HashMap<>();
        wardList = new HashMap<>();
    }

    public static AdministrativeDivision getInstance(){
        if (instance == null){
            instance = new AdministrativeDivision();
        }
        return instance;
    }

    public HashMap<String, Province> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(HashMap<String, Province> provinceList) {
        this.provinceList = provinceList;
    }

    public HashMap<String, District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(HashMap<String, District> districtList) {
        this.districtList = districtList;
    }

    public HashMap<String, Ward> getWardList() {
        return wardList;
    }

    public void setWardList(HashMap<String, Ward> wardList) {
        this.wardList = wardList;
    }
}
