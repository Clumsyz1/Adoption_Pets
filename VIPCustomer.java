public class VIPCustomer extends Customer {
  private double monthlyDonation;

  public VIPCustomer(String id, String name, double salary, String phone, double donation) {
    super(id, name, salary, phone, CustomerType.VIP);
    this.monthlyDonation = donation;
  }

  public double getMonthlyDonation() {
    return monthlyDonation;
  }

  @Override
  public boolean canAdoptPet(Pet pet) {
    return monthlyDonation >= 500; // ต้องบริจาคเกิน 500
  }
}
