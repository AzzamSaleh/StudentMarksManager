import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;


public class Frame1 extends JFrame {


    private GridLayout GridLayout;
    private JLabel Label;
    private JButton None1,None2;//spaces
    private JButton AddStudent, View, searchByName, searchByMark, exit;//main button
    private JTextField NameOfStudent,MarkOfStudent;
    private JTextArea TextArea;
    private JList subjectsist;


    private final String[] subjects = {"الـبـرمـجـة الـمـتـقـدمـة", "الأنـظـمـة الـمـضـمـنـة", "الـذكـاء الإصـطـنـاعـي "};

    //for database
    private final String[] tables = {"advanced_programming", "embedded_systems", "ai"};


    public Frame1() {
        super("تـسـجـيـل الـعـلامـات");
        GridLayout = new GridLayout(4, 3, 7, 80);
      //  Container = getContentPane();
        setLayout(GridLayout);




        //set up the frame
        None1 = new JButton("");
        None1.setVisible(false);
        add(None1);

        Icon Balqa = new ImageIcon(getClass().getResource("Balqa.jpg"));
        Label = new JLabel("نظام العلامات", Balqa, SwingConstants.CENTER);

        Label.setHorizontalTextPosition(SwingConstants.CENTER);
        Label.setVerticalTextPosition(SwingConstants.BOTTOM);
        add(Label);
        None2 = new JButton("  ");
        None2.setVisible(false);
        add(None2);

        MarkOfStudent = new JTextField("عـلامـة الـطـالـب من100");

        MarkOfStudent.setHorizontalAlignment(JTextField.RIGHT);
        add(MarkOfStudent);
        NameOfStudent = new JTextField("إسـم الـطـالـب");
        NameOfStudent.setHorizontalAlignment(JTextField.RIGHT);
        add(NameOfStudent);

        AddStudent = new JButton("أضـف الـطـالـب");
        AddStudent.setVisible(true);
        add(AddStudent);

        searchByName = new JButton("بحث حسب الاسم");
        searchByName.setVisible(true);
        add(searchByName);

        searchByMark = new JButton("بحث حسب العلامه");

        searchByMark.setVisible(true);
        add(searchByMark);

        View = new JButton("عـرض");
        View.setVisible(true);
        add(View);

        exit = new JButton("خروج");
        exit.setVisible(true);
        add(exit);

        TextArea = new JTextArea(50, 50);
        TextArea.setEditable(false);
        add(new JScrollPane(TextArea));

        subjectsist = new JList(subjects);
        subjectsist.setVisibleRowCount(3);
        subjectsist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(subjectsist);



        ButtonHandler handler = new ButtonHandler();
        AddStudent.addActionListener(handler);
        View.addActionListener(handler);
        searchByName.addActionListener(handler);
        searchByMark.addActionListener(handler);
        exit.addActionListener(e -> System.exit(0));

    }

    private class ButtonHandler implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source == AddStudent) {
                addStudent();
            } else if (source == View) {
                viewStudents();
            } else if (source == searchByName) {
                searchStudentByName();
            } else if (source == searchByMark) {
                searchStudentByMark();
            }
        }

        //data base
        private Connection connect() throws SQLException {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/finalproject", "root", "azzam2237");
        }

        //index
        private String getSelectedTable() {
            int index = subjectsist.getSelectedIndex();
            return index >= 0 ? tables[index] : null;
        }

        private void addStudent() {
            String table = getSelectedTable();
            if (table == null) {
                JOptionPane.showMessageDialog(Frame1.this, "يرجى اختيار المادة أولاً");
                return;
            }

            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("INSERT INTO " + table + " (name, mark) VALUES (?, ?)")) {
                stmt.setString(1, NameOfStudent.getText());
                stmt.setFloat(2, Float.parseFloat(MarkOfStudent.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(Frame1.this, "تمت إضافة الطالب.");
                MarkOfStudent.setText("");
                NameOfStudent.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Frame1.this, "خطأ: " + ex.getMessage());
            }
        }

        private void viewStudents() {
            String table = getSelectedTable();
            if (table == null) {
                JOptionPane.showMessageDialog(Frame1.this, "يرجى اختيار المادة أولاً");
                return;
            }

            TextArea.setText("");
            try (Connection conn = connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name, mark FROM " + table)) {

                int count = 0;
                while (rs.next()) {
                    TextArea.append((count++) + ") " + rs.getString("name") + " : " + rs.getFloat("mark") + "\n");
                }
                if (count == 0) {
                    TextArea.setText("لا يوجد بيانات.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Frame1.this, "خطأ: " + ex.getMessage());
            }
        }

        private void searchStudentByName() {
            String table = getSelectedTable();
            if (table == null) {
                JOptionPane.showMessageDialog(Frame1.this, "يرجى اختيار المادة أولاً");
                return;
            }

            String name = JOptionPane.showInputDialog(Frame1.this, "أدخل اسم الطالب:");
            if (name == null || name.isEmpty()) return;

            TextArea.setText("");
            try (Connection conn = connect();
                 PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table + " WHERE name = ?")) {
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();

                int count = 0;
                while (rs.next()) {
                    TextArea.append(rs.getString("name") + " : " + rs.getFloat("mark") + "\n");
                    count++;
                }

                if (count == 0) {
                    TextArea.setText("لا يوجد طالب بهذا الاسم.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Frame1.this, "خطأ: " + ex.getMessage());
            }
        }

        private void searchStudentByMark() {
            String table = getSelectedTable();
            if (table == null) {
                JOptionPane.showMessageDialog(Frame1.this, "يرجى اختيار المادة أولاً");
                return;
            }

            String markStr = JOptionPane.showInputDialog(Frame1.this, "أدخل العلامة:");
            if (markStr == null || markStr.isEmpty()) return;

            try {
                float mark = Float.parseFloat(markStr);
                TextArea.setText("");
                try (Connection conn = connect();
                     PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + table + " WHERE mark = ?")) {
                    stmt.setFloat(1, mark);
                    ResultSet rs = stmt.executeQuery();

                    int count = 0;
                    while (rs.next()) {
                        TextArea.append(rs.getString("name") + " : " + rs.getFloat("mark") + "\n");
                        count++;
                    }

                    if (count == 0) {
                        TextArea.setText("لا يوجد طالب بهذه العلامة.");
                    }

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(Frame1.this, "يرجى إدخال علامة صحيحة (رقم).");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Frame1.this, "خطأ: " + ex.getMessage());
            }
        }
    }


}





