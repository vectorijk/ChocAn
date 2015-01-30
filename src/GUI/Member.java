package GUI;

import Format.Time;
import Format.formatNumber;
import database.SQLiteInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Member extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel IDLabel;
    private JLabel NameLabel;
    private JLabel FeeLabel;
    private JLabel LoginTimeLabel;
    private JComboBox comboBox1;
    private int id;

    private SQLiteInterface DB;

    public Member(int id) {
        //build up connection
        DB = new SQLiteInterface();
        Person.Member newMember = DB.retrieveMember(id);
        this.id = id;

        setContentPane(contentPane);
        setTitle("Welcome Member: " + newMember.name + "!");
        setLocationRelativeTo(null);
        setResizable(false);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        String LoginTime = Time.getCurrentTimestamp();

        IDLabel.setText(String.valueOf(id));

        NameLabel.setText(newMember.name);

        if (newMember.fee < 0) {
            FeeLabel.setForeground(Color.RED);
        } else {
            FeeLabel.setForeground(Color.BLUE);
        }
        FeeLabel.setText(String.valueOf(newMember.fee));

        LoginTimeLabel.setText(LoginTime);

        for (Person.Provider item : DB.retrieveProviderTable()) {
            comboBox1.addItem(formatNumber.ID(item.id) + "    " + item.name);
        }


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
        //update service instance
        //get service code
        int serviceCode = DB.updateServiceAndGetCode(id, DB.retrieveProviderTable().elementAt(comboBox1.getSelectedIndex()).id);
        //show service code
        //provider id and name
        String result2display = "Service code:  ";
        result2display += formatNumber.serviceCode(serviceCode);
        result2display += "\nProvider ID & name:  ";
        result2display += comboBox1.getSelectedItem();
        JOptionPane.showConfirmDialog(null, result2display, "Confirm", JOptionPane.CLOSED_OPTION);
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        Member dialog = new Member(10);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
