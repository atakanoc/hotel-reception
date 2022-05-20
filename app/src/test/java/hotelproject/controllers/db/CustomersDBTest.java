package hotelproject.controllers.db;

import hotelproject.controllers.objects.Customer;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
public class CustomersDBTest {

    private final DatabaseManager dm = new DatabaseManager();
    private final Customer customer_1 = new Customer(72091719, "Sivliden 71", "Patrik Wikstrom", 761404361, "patrik.wikstrom@hotmail.com");
    private final Customer customer_2 = new Customer(11111111, "Street 1", "James Green", 12345678, "james.green@hotmail.com");
    private final Customer customer_3 = new Customer(22222222, "Street 2", "Lisa Green", 87654321, "lisa.green@hotmail.com");

    /**
     * Customer_1 should return true; customer_2 and customer_3 should return false.
     */
    @Test
    public void test_001_UserExists() {
        Assert.assertTrue(dm.cdb.customerExists(customer_1));
        Assert.assertFalse(dm.cdb.customerExists(customer_2));
        Assert.assertFalse(dm.cdb.customerExists(customer_3));
    }

    /**
     * After adding customer_2 in the database, the customerExists() method should return true.
     */
    @Test
    public void test_002_AddCustomer() {
        Assert.assertFalse(dm.cdb.customerExists(customer_2));
        dm.cdb.addCustomer(customer_2);
        Assert.assertTrue(dm.cdb.customerExists(customer_2));
    }

    /**
     * After updating the customer_2's information to customer_3, customer_2 should be replaced by customer_3's information.the customerExists() method should return true.
     */
    @Test
    public void test_003_UpdateCustomer() {
        dm.cdb.updateCustomer(customer_3, 11111111);
        Assert.assertTrue(dm.cdb.customerExists(customer_3));
    }

    /**
     * After deleting the customer_2 and customer_3, both records should be removed from database.
     */
    @Test
    public void test_004_DeleteCustomer() {
        dm.cdb.deleteCustomer(customer_2);
        Assert.assertFalse(dm.cdb.customerExists(customer_2));
    }

    @Test
    public void test_005_findAllCustomers() {
        ArrayList<Customer> customers = dm.cdb.findAllCustomers();
        Assert.assertTrue(customers.size() != 0);
    }


    @Test
    public void test_006_getCustomerName() {
        Integer c_ss_number = customer_1.getC_ss_number();
        String customerName = dm.cdb.getCustomerName(c_ss_number);
        Assert.assertEquals(customerName, customer_1.getC_full_name());
    }

}
