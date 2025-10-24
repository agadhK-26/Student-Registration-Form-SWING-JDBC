import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class StudentRegistrationFormJDBC extends JFrame {

    private JTextField tfName, tfAge, tfEmail, tfContact;
    private JComboBox<String> cbGender, cbDepartment;
    private JButton btnSubmit;

    // JDBC Variables
    private final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    private final String DB_USER = "root";
    private final String DB_PASS = "agadhk";

    public StudentRegistrationFormJDBC() {
        setTitle("Student Registration Form");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(240, 248, 255)); // Light blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Student Registration", JLabel.CENTER);
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 24));
        lblTitle.setForeground(new Color(25, 25, 112)); // Dark blue
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitle, gbc);

        gbc.gridwidth = 1;
        JLabel lblName = new JLabel("Full Name:");
        tfName = new JTextField(20);
        JLabel lblAge = new JLabel("Age:");
        tfAge = new JTextField(20);
        JLabel lblGender = new JLabel("Gender:");
        cbGender = new JComboBox<>(new String[]{"Select", "Male", "Female", "Other"});
        JLabel lblDepartment = new JLabel("Department:");
        cbDepartment = new JComboBox<>(new String[]{"Select", "Computer Science", "IT", "Mechanical", "Civil"});
        JLabel lblEmail = new JLabel("Email:");
        tfEmail = new JTextField(20);
        JLabel lblContact = new JLabel("Contact No:");
        tfContact = new JTextField(20);

        btnSubmit = new JButton("Register");
        btnSubmit.setBackground(new Color(65, 105, 225)); // Royal Blue
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setFont(new Font("Arial", Font.BOLD, 16));
        btnSubmit.addActionListener(e -> handleSubmit());

        // Adding components
        gbc.gridx = 0; gbc.gridy = 1; add(lblName, gbc);
        gbc.gridx = 1; add(tfName, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(lblAge, gbc);
        gbc.gridx = 1; add(tfAge, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(lblGender, gbc);
        gbc.gridx = 1; add(cbGender, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(lblDepartment, gbc);
        gbc.gridx = 1; add(cbDepartment, gbc);
        gbc.gridx = 0; gbc.gridy = 5; add(lblEmail, gbc);
        gbc.gridx = 1; add(tfEmail, gbc);
        gbc.gridx = 0; gbc.gridy = 6; add(lblContact, gbc);
        gbc.gridx = 1; add(tfContact, gbc);
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; add(btnSubmit, gbc);
    }

    private void handleSubmit() {
        String name = tfName.getText().trim();
        String ageStr = tfAge.getText().trim();
        String gender = (String) cbGender.getSelectedItem();
        String department = (String) cbDepartment.getSelectedItem();
        String email = tfEmail.getText().trim();
        String contact = tfContact.getText().trim();

        if (name.isEmpty() || ageStr.isEmpty() || email.isEmpty() || contact.isEmpty()
                || gender.equals("Select") || department.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid age!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // JDBC: Insert into MySQL
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            String sql = "INSERT INTO students(name, age, gender, department, email, contact) VALUES(?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, name);
            pst.setInt(2, age);
            pst.setString(3, gender);
            pst.setString(4, department);
            pst.setString(5, email);
            pst.setString(6, contact);
            pst.executeUpdate();
            pst.close();
            conn.close();

            JOptionPane.showMessageDialog(this, "Student Registered Successfully!");
            clearFields();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        tfName.setText("");
        tfAge.setText("");
        tfEmail.setText("");
        tfContact.setText("");
        cbGender.setSelectedIndex(0);
        cbDepartment.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentRegistrationFormJDBC().setVisible(true));
    }
}
