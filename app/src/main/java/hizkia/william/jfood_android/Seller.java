package hizkia.william.jfood_android;

public class Seller {

    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private Location location;

    /**
     * Constructor for objects of class Customer
     * @param id variable for identifying seller
     * @param name variable to store name of seller
     * @param email variable to store email information about seller's email
     * @param phoneNumber variable to store information about phone number of seller
     * @param location variable to store information about location as object
     */
    public Seller(int id, String name, String email, String phoneNumber, Location location)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    /**
     * Method as accessor to get id of the seller
     * @return id of the seller
     */
    public int getId()
    {
        return id;
    }

    /**
     * Method as accessor to get name of the seller
     * @return name of the seller
     */
    public String getName()
    {
        return name;
    }

    /**
     * Method as accessor to get email of the seller
     * @return email of the seller
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Method as accessor to get phone number of the seller
     * @return phoneNumber of the seller
     */
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    /**
     * Method as accessor to get location of the seller
     * @return location of the seller
     */
    public Location getLocation()
    {
        return location;
    }

    /**
     * Method as setter to set id of the seller
     * @param id variable that stores information of seller id
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Method as setter to set name of the seller
     * @param name variable that stores information of seller name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Method as setter to set email of the seller
     * @param email variable that stores information of seller email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * Method as setter to set phone number of the seller
     * @param phoneNumber variable that stores information of seller phone number
     */
    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Method as accessor to set location of the seller
     * @param location variable that stores information of seller location
     */
    public void setLocation(Location location)
    {
        this.location = location;
    }

    /**
     * Method to return all variables Seller as String
     */
    public String toString()
    {
        return "Id = " + getId() +
                "Name = " + getName() +
                "Phone Number = " + getPhoneNumber() +
                "Location =" + getLocation().getCity();
    }
}
