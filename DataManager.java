import java.io.*;
import java.util.*;

public class DataManager {
    private static final String PET_FILE = "Pets.txt";
    private static final String ADOPTION_FILE = "adoption_requests.txt";

    // โหลดข้อมูลสัตว์จากไฟล์เข้าสู่โปรแกรม
    public static List<Pet> loadPets() {
        List<Pet> pets = new ArrayList<>();
        File file = new File(PET_FILE);
        if (!file.exists()) return pets;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int id = 0;
            String name = "";
            String type = "";
            int age = 0;
            String status = "";
            boolean isDataBlock = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("รหัสสัตว์:")) {
                    id = Integer.parseInt(line.substring(10).trim());
                    isDataBlock = true;
                } else if (line.startsWith("ชื่อสัตว์:")) {
                    name = line.substring(10).trim();
                } else if (line.startsWith("ประเภทสัตว์:")) {
                    type = line.substring(13).trim();
                } else if (line.startsWith("อายุสัตว์:")) {
                    age = Integer.parseInt(line.substring(10).trim());
                } else if (line.startsWith("สถานะ:")) {
                    status = line.substring(6).trim();

                    if (isDataBlock) {
                        Pet p;
                        if (type.equals("สุนัข")) {
                            p = new Dog(id, name, age);
                        } else {
                            p = new Cat(id, name, age, false);
                        }
                        p.setStatus(status);
                        pets.add(p);
                        isDataBlock = false;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading pets: " + e.getMessage());
        }
        return pets;
    }

    // บันทึกสัตว์ใหม่
    public static void savePet(Pet pet) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PET_FILE, true))) {
            writer.write("รหัสสัตว์: " + pet.getId()); writer.newLine();
            writer.write("ชื่อสัตว์: " + pet.getName()); writer.newLine();
            writer.write("ประเภทสัตว์: " + pet.getType()); writer.newLine();
            writer.write("อายุสัตว์: " + pet.getAge()); writer.newLine();
            writer.write("สถานะ: " + pet.getStatus()); writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // อัปเดตสถานะสัตว์ (เขียนทับไฟล์เดิม)
    public static void updatePetStatus(int petId, String newStatus) {
        List<String> lines = new ArrayList<>();
        boolean targetFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(PET_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("รหัสสัตว์: " + petId)) targetFound = true;

                if (targetFound && line.startsWith("สถานะ:")) {
                    lines.add("สถานะ: " + newStatus);
                    targetFound = false;
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PET_FILE))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // บันทึกประวัติการรับเลี้ยง
    public static void saveAdoptionRequest(AdoptionRequest request) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ADOPTION_FILE, true))) {
            writer.write("--- Adoption Record ---"); writer.newLine();
            writer.write("วันที่: " + request.getRequestDate()); writer.newLine();
            writer.write("ลูกค้า: " + request.getCustomer().getName() + " (" + request.getCustomer().getType() + ")"); writer.newLine();
            writer.write("สัตว์เลี้ยง: " + request.getPet().getName()); writer.newLine();
            writer.write("สถานะ: " + request.getStatus()); writer.newLine();
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
