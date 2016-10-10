package de.odinoxin.aiddesk;

public class Human {

    private int id;
    private String name;
    private String forename;
    private String code;
    private String languageCode;
    private int addressId;

    public Human(int id) {
        this.id = id;
    }

    public Human(int id, String name, String forename, String code, String languageCode, int addressId) {
        this(id);
        this.name = name;
        this.forename = forename;
        this.code = code;
        this.languageCode = languageCode;
        this.addressId = addressId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getForename() {
        return forename;
    }

    public String getCode() {
        return code;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public int getAddressId() {
        return addressId;
    }
}
