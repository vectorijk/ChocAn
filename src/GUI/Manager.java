package GUI;

import database.SQLiteInterface;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class Manager extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField addressTextField;
    private JTextField cityTextField;
    private JTextField stateTextField;
    private JTextField zipcodeTextField;
    private JRadioButton addMemberRadioButton;
    private JRadioButton addProviderRadioButton;
    private JRadioButton addManagerRadioButton;
    private JTextField nameTextField;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JButton buttonReport;
    private ButtonGroup Role;

    private SQLiteInterface DB;
    private int id;

    public Manager(int id) {
        //initialize database connection
        DB = new SQLiteInterface();
        Person.Manager newManager = DB.retrieveManager(id);
        this.id = id;

        setContentPane(contentPane);
        setTitle("Welcome " + newManager.name + "!");
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        Role = new ButtonGroup();
        Role.add(addManagerRadioButton);
        Role.add(addMemberRadioButton);
        Role.add(addProviderRadioButton);
        addMemberRadioButton.setActionCommand("member");
        addManagerRadioButton.setActionCommand("manager");
        addProviderRadioButton.setActionCommand("provider");
        addMemberRadioButton.setSelected(true);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        buttonReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                memberReport dialog = new memberReport();
                dialog.pack();
                dialog.setVisible(true);
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        char[] passwordCharArray = passwordField1.getPassword();
        String passwd = String.valueOf(passwordCharArray);
        switch (Role.getSelection().getActionCommand()) {
            case "member":
                if (judgeTwoPassword()) {
                    Person.Member newMember = new Person.Member();
                    newMember.name = nameTextField.getText();
                    newMember.address = addressTextField.getText();
                    newMember.city = cityTextField.getText();
                    newMember.state = stateTextField.getText();
                    newMember.zipCode = zipcodeTextField.getText();
                    newMember.fee = 0.0;

                    int member_id = DB.insertRoleToAccountAndRoleTable(newMember, passwd);


                } else {
                    JOptionPane.showConfirmDialog(null, "Different Passwords Input", "Wrong", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "manager":
                if (judgeTwoPassword()) {
                    Person.Manager newManager = new Person.Manager();
                    newManager.name = nameTextField.getText();
                    newManager.address = addressTextField.getText();
                    newManager.city = cityTextField.getText();
                    newManager.state = stateTextField.getText();
                    newManager.zipCode = zipcodeTextField.getText();

                    int member_id = DB.insertRoleToAccountAndRoleTable(newManager, passwd);
                } else {
                    JOptionPane.showConfirmDialog(null, "Different Passwords Input", "Wrong", JOptionPane.ERROR_MESSAGE);
                }
                break;
            case "provider":
                if (judgeTwoPassword()) {
                    Person.Provider newProvider = new Person.Provider();
                    newProvider.name = nameTextField.getText();
                    newProvider.address = addressTextField.getText();
                    newProvider.city = cityTextField.getText();
                    newProvider.state = stateTextField.getText();
                    newProvider.zipCode = zipcodeTextField.getText();

                    int member_id = DB.insertRoleToAccountAndRoleTable(newProvider, passwd);
                } else {
                    JOptionPane.showConfirmDialog(null, "Different Passwords Input", "Wrong", JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
        }

    }

    private void clearTextField() {
        addressTextField.setText("");
        cityTextField.setText("");
        stateTextField.setText("");
        zipcodeTextField.setText("");
        nameTextField.setText("");
        passwordField1.setText("");
        passwordField2.setText("");
    }

    private void onCancel() {
        dispose();
    }

    private boolean judgeTwoPassword() {
        return Arrays.equals(passwordField1.getPassword(), passwordField2.getPassword());
    }

    public static void main(String[] args) {
        Manager dialog = new Manager(7);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
