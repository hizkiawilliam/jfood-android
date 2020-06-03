package hizkia.william.jfood_android;

    /**
     * Class Promo is class that stores information for every promo object
     *
     * @author Hizkia William Eben
     */

    public class Promo
    {
        private int id;
        private String code;
        private int discount;
        private int minPrice;
        private boolean active;

        /**
         * Constructor for objects of class Promo
         * @param id variable for identifying promo
         * @param code variable that stores code promo
         * @param discount variable to store information about discount amount
         * @param minPrice variable to store the minimum price of food needed to activate promo
         * @param active variable to store information of promo status
         */
        public Promo(int id, String code, int discount, int minPrice, boolean active)
        {
            this.id = id;
            this.code = code;
            this.discount = discount;
            this.minPrice = minPrice;
            this.active = active;
        }

        /**
         * Method as accessor to get id of the promo
         * @return id variable that stores id of the promo
         */
        public int getId()
        {
            return id;
        }

        /**
         * Method as accessor to get code of the promo
         * @return code variable that stores code of the promo
         */
        public String getCode()
        {
            return code;
        }

        /**
         * Method as accessor to get discount of the promo
         * @return discount variable that stores discount of promo
         */
        public int getDiscount()
        {
            return discount;
        }

        /**
         * Method as accessor to get minimum price of the promo
         * @return minPrice variable that stores minimum price of the promo
         */
        public int getMinPrice()
        {
            return minPrice;
        }

        /**
         * Method as accessor to get status of the the promo
         * @return active variable that stores status of the promo
         */
        public boolean getActive()
        {
            return active;
        }

        /**
         * Method as setter or mutator to set or change id of the promo
         * @param id variable that stores information about id of the promo
         */
        public void setId(int id)
        {
            this.id = id;
        }

        /**
         * Method as setter or mutator to set or change code of the promo
         * @param code variable that stores information about code of the promo
         */
        public void setCode(String code)
        {
            this.code = code;
        }

        /**
         * Method as setter or mutator to set or change discount of the promo
         * @param discount variable that stores information about discount amount of the promo
         */
        public void setDiscount(int discount)
        {
            this.discount = discount;
        }

        /**
         * Method as setter or mutator to set or change minimum price of the promo
         * @param minPrice variable that stores information about minimum price of the promo
         */
        public void setMinPrice(int minPrice)
        {
            this.minPrice = minPrice;
        }

        /**
         * Method as setter or mutator to set or change status of the promo
         * @param active variable that stores information about status of the promo
         */
        public void setActive(boolean active)
        {
            this.active = active;
        }
}


