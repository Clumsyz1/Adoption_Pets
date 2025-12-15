public class Cat extends Pet {
  private boolean isVIP;

  public Cat(int id, String name, int age, boolean isVIP) {
    super(id, name, "แมว", age);
    this.isVIP = isVIP;
  }

  public boolean checkVIP() {
    return isVIP;
  }
}
