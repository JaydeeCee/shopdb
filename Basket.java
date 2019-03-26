package shop;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.HashMap;


public class Basket {

    Map items;
    ShopDB db;
    
    //price in two decimal places
    private static DecimalFormat df2 = new DecimalFormat(".##");
    
    public static void main(String[] args) {
        Basket b = new Basket();
        b.addItem("art1");
        System.out.println( b.getTotalString() );
        b.clearBasket();
        System.out.println( b.getTotalString() );
        // check that adding a null String causes no problems
        String pid = null;
        b.addItem( pid );
        System.out.println( b.getTotalString() );
    }

    public Basket() {
        db = ShopDB.getSingleton();
        items = new HashMap();
    }

    /**
     *
     * @return Collection of Product items that are stored in the basket
     *
     * Each item is a product object - need to be clear about that...
     *
     * When we come to list the Basket contents, it will be much more
     * convenient to have all the product details as items in this way
     * in order to calculate that product totals etc.
     *
     */
    public Collection<Product> getItems() {
        return items.values();
    }

    /**
     * empty the basket - the basket should contain no items after calling this method
     */
    public void clearBasket() {
        items.clear();
    }

    /**
     *
     *  Adds an item specified by its product code to the shopping basket
     *
     * @param pid - the product code
     */
    public void addItem(String pid) {
        // need to look the product name up in the
        // database to allow this kind of item adding...
        System.out.println(pid);
        if(!items.containsKey(pid)) {
            addItem( db.getProduct( pid ) );
        } else {
             Product product = this.getProductFromBasket(pid);
             addItem(product);
        }
    }

    public void addItem(Product p) {
        // ensure that we don't add any nulls to the item list
        System.out.println(p);
        if (p != null ) {
            
            if(!items.containsKey(p.PID)) {
                System.out.println("Do not increase the quantity");
            } else {
               p.quantity++;
            }
            items.put(p.PID, p);
        } 
    }
    
    
    // remove an item from basket
    public void removeItem(String pid) {
        if(items.containsKey(pid)) {
            items.remove(pid);
        }
    }
    
    // add to the quanitity of an item ordered
    public void addQuantity(String pid) {
        Product product = this.getProductFromBasket(pid);
        product.quantity++;
        items.put(pid, product);
    }
    
    // subtract quantity of item added
    public void reduceQuantity(String pid) {
        Product product = this.getProductFromBasket(pid);
        product.quantity--;
        items.put(pid, product);
    }

    /**
     *
     * @return the total value of items in the basket in pence
     */
    public int getTotal() {
        // iterate over the set of products...
              int total = 0;
                Collection<Product> totalOrder = items.values();
                for(Product p : totalOrder) {
                    total += p.price* p.quantity;
                }
              // System.out.println(total); 
        // return the total
        return total;
    }

    /**
     *
     * @return the total value of items in the basket as
     * a pounds and pence String with two decimal places - hence
     * suitable for inclusion as a total in a web page
     */
    public String getTotalString() {
            int total = getTotal();
            double pTotal = total / 100;
            return String.valueOf(df2.format(pTotal));
    }
    
    private Product getProductFromBasket(String pid) {
         Collection<Product> products = items.values();
             for(Product pr : products) {
                 if(pr.PID.equals(pid)) {
                   return pr; 
                 }
             }
        return null;
    }
    
    
    
    
    
}
