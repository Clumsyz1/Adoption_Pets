import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class PetAdoptionGUI extends JFrame {
  private List<Pet> allPets; // เก็บข้อมูลใน RAM
  private JTextField petIdField, petNameField, petAgeField;
  private JComboBox<String> petTypeComboBox;
  private JTextArea displayArea;
  private JList<Pet> petList;

  public PetAdoptionGUI() {
    setTitle("ระบบรับเลี้ยงสัตว์เลี้ยง");
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    // 1. โหลดข้อมูลเก่าทันทีที่เปิด
    reloadData();

    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.add(createInputPanel(), BorderLayout.NORTH);

    displayArea = new JTextArea();
    displayArea.setEditable(false);
    displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
    mainPanel.add(new JScrollPane(displayArea), BorderLayout.CENTER);

    mainPanel.add(createButtonPanel(), BorderLayout.SOUTH);
    add(mainPanel);

    refreshDisplay();
  }

  private void reloadData() {
    allPets = DataManager.loadPets();
  }

  private JPanel createInputPanel() {
    JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
    panel.setBorder(BorderFactory.createTitledBorder("ลงทะเบียนสัตว์เลี้ยงใหม่"));

    panel.add(new JLabel("รหัสสัตว์ (ตัวเลข):"));
    petIdField = new JTextField();
    panel.add(petIdField);
    panel.add(new JLabel("ชื่อสัตว์:"));
    petNameField = new JTextField();
    panel.add(petNameField);
    panel.add(new JLabel("อายุสัตว์:"));
    petAgeField = new JTextField();
    panel.add(petAgeField);
    panel.add(new JLabel("ประเภทสัตว์:"));
    petTypeComboBox = new JComboBox<>(new String[] { "สุนัข", "แมว" });
    panel.add(petTypeComboBox);

    return panel;
  }

  private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout());

    JButton addBtn = new JButton("เพิ่มสัตว์");
    addBtn.addActionListener(e -> addPet());

    JButton refreshBtn = new JButton("รีเฟรชข้อมูล");
    refreshBtn.addActionListener(e -> {
      reloadData();
      refreshDisplay();
    });

    JButton adoptBtn = new JButton("รับเลี้ยงสัตว์");
    adoptBtn.setBackground(new Color(144, 238, 144));
    adoptBtn.addActionListener(e -> processAdoption());

    panel.add(addBtn);
    panel.add(refreshBtn);
    panel.add(adoptBtn);
    return panel;
  }

  private void addPet() {
    try {
      int id = Integer.parseInt(petIdField.getText());
      String name = petNameField.getText();
      int age = Integer.parseInt(petAgeField.getText());
      String type = (String) petTypeComboBox.getSelectedItem();

      if (allPets.stream().anyMatch(p -> p.getId() == id)) {
        JOptionPane.showMessageDialog(this, "รหัสสัตว์ซ้ำ!");
        return;
      }

      Pet newPet = type.equals("สุนัข") ? new Dog(id, name, age) : new Cat(id, name, age, false);
      DataManager.savePet(newPet);
      allPets.add(newPet);

      JOptionPane.showMessageDialog(this, "บันทึกสำเร็จ");
      petIdField.setText("");
      petNameField.setText("");
      petAgeField.setText("");
      refreshDisplay();

    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "กรุณากรอกตัวเลขให้ถูกต้อง", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void refreshDisplay() {
    displayArea.setText("=== รายชื่อสัตว์ที่ยังว่าง ===\n\n");
    List<Pet> available = allPets.stream()
        .filter(p -> p.getStatus().equals("ยังไม่มีเจ้าของ"))
        .collect(Collectors.toList());

    if (available.isEmpty())
      displayArea.append("ไม่มีสัตว์ว่าง");
    else
      available.forEach(p -> displayArea.append(p.toString() + "\n"));
  }

  private void processAdoption() {
    List<Pet> adoptable = allPets.stream().filter(p -> p.getStatus().equals("ยังไม่มีเจ้าของ"))
        .collect(Collectors.toList());
    if (adoptable.isEmpty()) {
      JOptionPane.showMessageDialog(this, "ไม่มีสัตว์ว่าง");
      return;
    }

    DefaultListModel<Pet> model = new DefaultListModel<>();
    adoptable.forEach(model::addElement);
    petList = new JList<>(model);

    if (JOptionPane.showConfirmDialog(this, new JScrollPane(petList), "เลือกสัตว์",
        JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
      Pet selectedPet = petList.getSelectedValue();
      if (selectedPet == null)
        return;

      JPanel form = new JPanel(new GridLayout(0, 2));
      JTextField nameF = new JTextField();
      JTextField salF = new JTextField();
      JTextField telF = new JTextField();
      JTextField donF = new JTextField();
      JComboBox<String> typeBox = new JComboBox<>(new String[] { "VIP", "Walk-In" });

      form.add(new JLabel("ประเภท:"));
      form.add(typeBox);
      form.add(new JLabel("ชื่อ:"));
      form.add(nameF);
      form.add(new JLabel("เงินเดือน:"));
      form.add(salF);
      form.add(new JLabel("เบอร์โทร:"));
      form.add(telF);
      form.add(new JLabel("บริจาค (ถ้า VIP):"));
      form.add(donF);

      if (JOptionPane.showConfirmDialog(this, form, "ข้อมูลลูกค้า",
          JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
        try {
          String type = (String) typeBox.getSelectedItem();
          Customer cust;
          if ("VIP".equals(type)) {
            double don = donF.getText().isEmpty() ? 0 : Double.parseDouble(donF.getText());
            cust = new VIPCustomer("C" + System.currentTimeMillis(), nameF.getText(),
                Double.parseDouble(salF.getText()), telF.getText(), don);
          } else {
            cust = new WalkInCustomer("C" + System.currentTimeMillis(), nameF.getText(),
                Double.parseDouble(salF.getText()), telF.getText());
          }

          AdoptionRequest req = cust.requestAdoption(selectedPet);
          if (req.validateAdoption()) {
            selectedPet.setStatus("มีเจ้าของแล้ว");
            req.updateStatus(AdoptionRequest.RequestStatus.APPROVED);

            DataManager.updatePetStatus(selectedPet.getId(), "มีเจ้าของแล้ว");
            DataManager.saveAdoptionRequest(req);

            JOptionPane.showMessageDialog(this, "รับเลี้ยงสำเร็จ!");
            refreshDisplay();
          } else {
            JOptionPane.showMessageDialog(this,
                "เงื่อนไขไม่ผ่าน (เช่น เงินเดือนไม่พอ หรือลูกค้า Walk-in รับเลี้ยงแมวไม่ได้)");
          }
        } catch (Exception ex) {
          JOptionPane.showMessageDialog(this, "ข้อมูลผิดพลาด");
        }
      }
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new PetAdoptionGUI().setVisible(true));
  }
}
