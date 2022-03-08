package de.raywo.banking.textui.persistence;

import de.raywo.banking.textui.logic.Customer;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepository {
  private final Map<Long, Customer> customers = new HashMap<>();


  public void add(Customer customer) {
    this.customers.put(customer.getCustomerID(), customer);
  }


  public Customer get(Long customerId) {
    return this.customers.get(customerId);
  }


  public Map<Long, Customer> allCustomers() {
    return customers;
  }


  public Long nextId() {
    return customers
        .keySet()
        .stream()
        .max(Comparator.naturalOrder())
        .orElse(0L)
        + 1L;
  }


  public int count() {
    return customers.size();
  }
}
