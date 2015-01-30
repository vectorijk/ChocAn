package GUI;

import database.SQLiteInterface;

import javax.swing.*;
import java.awt.event.*;

public class Log_in extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField IDtextField;
    private JPasswordField passwordField1;
    private JRadioButton memberRadioButton;
    private JRadioButton providerRadioButton;
    private JRadioButton managerRadioButton;
    private ButtonGroup Role;

    public Log_in() {
        setContentPane(contentPane);
        setTitle("Welcome!");
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //mark name on button
        memberRadioButton.setActionCommand("member");
        providerRadioButton.setActionCommand("provider");
        managerRadioButton.setActionCommand("manager");

        //set member as default role
        memberRadioButton.setSelected(true);

        //add three buttons to group
        Role = new ButtonGroup();
        Role.add(memberRadioButton);
        Role.add(providerRadioButton);
        Role.add(managerRadioButton);


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

        System.out.println("click ok");

        String IDstring = IDtextField.getText();
        int id = Integer.valueOf(IDstring);
        System.out.println(id);

        System.out.println("Password:" + id);
        char[] passwordCharArray = passwordField1.getPassword();
        String passwd = String.valueOf(passwordCharArray);
        System.out.println(passwd);

        String roleName = Role.getSelection().getActionCommand();
        System.out.println(roleName + "\n");

        SQLiteInterface DB = new SQLiteInterface();
        System.out.println("Password fetch back:" + DB.checkPassword(id));
        if (!DB.checkPassword(id).equals("") && DB.checkPassword(id).equals(passwd) && DB.checkRole(id).equals(roleName)) {
            // correct
            // new window according to different Role
            switch (roleName.toLowerCase()) {
                case "manager":
                    Manager dialogManager = new Manager(id);
                    dialogManager.pack();
                    dialogManager.setVisible(true);
                    break;
                case "member":
                    Member dialogMember = new Member(id);
                    dialogMember.pack();
                    dialogMember.setVisible(true);
                    break;
                case "provider":
                    Provider dialogProvider = new Provider(id);
                    dialogProvider.pack();
                    dialogProvider.setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Wrong with selecting role", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // incorrect
            if (!DB.checkRole(id).equals(roleName)) {
                JOptionPane.showMessageDialog(null, "Wrong with selecting role", "Warning", JOptionPane.ERROR_MESSAGE);
            } else
                JOptionPane.showMessageDialog(null, "Wrong Password with ID: " + id + " OR not exists such ID.", "Invalid!", JOptionPane.ERROR_MESSAGE);
        }

        // dispose();
    }

    private void onCancel() {
        System.out.println("click cancel");
        dispose();
    }

    public static void main(String[] args) {
        Log_in dialog = new Log_in();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
