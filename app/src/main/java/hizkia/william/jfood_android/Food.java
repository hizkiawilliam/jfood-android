package hizkia.william.jfood_android;

public class Food {

    private int id;
    private String name;
    private int price;
    private Seller seller;
    private String category;


    /**
     * Constructor for objects of class Food
     */
    public Food(int id, String name, Seller seller, int price, String category)
    {
        this.id = id;
        this.name = name;
        this.seller = seller;
        this.price = price;
        this.category = category;
    }

    /**
     * Method as accessor to get id of the food
     * @return id variable that stores id of food
     */
    public int getId()
    {
        return id;
    }

    /**
     * Method as accessor to get name of the food
     * @return name variable that stores name of food
     */
    public String getName()
    {
        return name;
    }

    /**
     * Method as accessor to get price of the food
     * @return price variable that stores price of food
     */
    public int getPrice()
    {
        return price;
    }

    /**
     * Method as accessor to get category of the food
     * @return category variable that stores category of food
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * Method as accessor to get seller of the food
     * @return seller variable that stores seller of food
     */
    public Seller getSeller()
    {
        return seller;
    }

    /**
     * Method as setter or mutator to set or change id of food
     * @param id variable that stores information about id of the food
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Method as setter or mutator to set or change name of food
     * @param name variable that stores information about name of the food
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Method as setter or mutator to set or change price of food
     * @param price variable that stores information about price of the food
     */
    public void setPrice(int price)
    {
        this.price = price;
    }

    /**
     * Method as setter or mutator to set or change category of food
     * @param Category variable that stores information about category of the food
     */
    public void setCategory(String Category)
    {
        this.category = category;
    }

    /**
     * Method as setter or mutator to set or change seller of food
     * @param seller variable that stores information about seller of the food
     */
    public void setSeller(Seller seller)
    {
        this.seller = seller;
    }

    /**
     * Method to print data of the customer
     */
    public String toString()
    {
        return "==========FOOD==========" +
                "\nId = " + getId() +
                "\nName = " + getName() +
                "\nSeller = " + getSeller().getName() +
                "\nCity = " + seller.getLocation().getCity() +
                "\nPrice = " + getPrice() +
                "\nCategory = " + getCategory().toString() + "\n";
    }
}
