package Controllers;

public class Details {

    private String nameOrId;
    private String bloodGroup;
    private String city;
    private String contactNumber;
    private String lastDateOfDonation;

    public Details(String nameOrId, String bloodGroup, String city, String contactNumber) {
        this.nameOrId = nameOrId;
        this.bloodGroup = bloodGroup;
        this.city = city;
        this.contactNumber = contactNumber;
    }

    public Details(String nameOrId, String bloodGroup, String lastDateOfDonation, String city, String contactNumber) {
        this.nameOrId = nameOrId;
        this.bloodGroup = bloodGroup;
        this.lastDateOfDonation = lastDateOfDonation;
        this.city = city;
        this.contactNumber = contactNumber;

    }

    public Details(String bloodBankName, String contactNo) {
        this.nameOrId = bloodBankName;
        this.contactNumber = contactNo;
    }

    public String getNameOrId() {
        return nameOrId;
    }

    public void setNameOrId(String nameOrId) {
        this.nameOrId = nameOrId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getLastDateOfDonation() {
        return lastDateOfDonation;
    }

    public void setLastDateOfDonation(String lastDateOfDonation) {
        this.lastDateOfDonation = lastDateOfDonation;
    }
}
