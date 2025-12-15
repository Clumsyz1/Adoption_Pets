import java.util.Date;

public class AdoptionRequest {
  private Date requestDate;
  private RequestStatus status;
  private Customer customer;
  private Pet pet;

  public enum RequestStatus {
    PENDING, APPROVED, REJECTED
  }

  public AdoptionRequest(Customer customer, Pet pet) {
    this.requestDate = new Date();
    this.customer = customer;
    this.pet = pet;
    this.status = RequestStatus.PENDING;
  }

  public boolean validateAdoption() {
    return customer.checkSalary() && customer.canAdoptPet(pet);
  }

  public void updateStatus(RequestStatus newStatus) {
    this.status = newStatus;
  }

  public RequestStatus getStatus() {
    return status;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Pet getPet() {
    return pet;
  }

  public Date getRequestDate() {
    return requestDate;
  }
}
