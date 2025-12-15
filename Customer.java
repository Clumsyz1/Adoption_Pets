public abstract class Customer {
  private String id;
  private String name;
  private double salary;
  private String phone;
  private CustomerType type;

  public enum CustomerType {
    VIP, WALKIN
  }

  public Customer(String id, String name, double salary, String phone, CustomerType type) {
    this.id = id;
    this.name = name;
    this.salary = salary;
    this.phone = phone;
    this.type = type;
  }

  public boolean checkSalary() {
    return salary >= 30000;
  }

  public AdoptionRequest requestAdoption(Pet pet) {
    return new AdoptionRequest(this, pet);
  }

  public abstract boolean canAdoptPet(Pet pet);

  public String getName() {
    return name;
  }

  public double getSalary() {
    return salary;
  }

  public CustomerType getType() {
    return type;
  }

  public String getPhone() {
    return phone;
  }
}
