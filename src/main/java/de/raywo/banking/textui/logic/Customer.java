package de.raywo.shellui.logic;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
public class Customer {
  private Long customerID;
  private String name;
  private Date dayOfBirth;


  public Customer(Long customerID, String name) {
    this.customerID = customerID;
    this.name = name;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Customer customer = (Customer) o;
    return getCustomerID().equals(customer.getCustomerID());
  }


  @Override
  public int hashCode() {
    return Objects.hash(getCustomerID());
  }


  @Override
  public String toString() {
    return String.format("%s (geb. %s; KundenNr: %d)",
        name, dayOfBirth, customerID);
  }
}
