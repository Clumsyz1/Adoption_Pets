public abstract class Pet {
  private int id;
  private String name;
  private String type;
  private int age;
  private String status;

  public Pet(int id, String name, String type, int age) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.age = age;
    this.status = "ยังไม่มีเจ้าของ";
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public int getAge() {
    return age;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s (%s) อายุ %d ปี - %s", id, name, type, age, status);
  }
}
