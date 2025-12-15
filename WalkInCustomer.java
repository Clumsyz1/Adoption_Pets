public class WalkInCustomer extends Customer {
  public WalkInCustomer(String id, String name, double salary, String phone) {
    super(id, name, salary, phone, CustomerType.WALKIN);
  }

  @Override
  public boolean canAdoptPet(Pet pet) {
    return pet instanceof Dog; // Walk-in รับได้แต่หมา
  }
}
