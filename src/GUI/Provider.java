package GUI;

import Format.formatNumber;
import Person.Service;
import database.SQLiteInterface;

import javax.swing.*;
import java.awt.event.*;
import java.util.Map;
import java.util.Vector;

public class Provider extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox comboBoxMember;
    private JComboBox comboBoxService;
    private JLabel IDLabel;
    private JLabel NameLabel;
    private JTextArea textAreaComment;

    private int id;
    private SQLiteInterface DB;
    private Vector<Integer> ServiceID;

    public Provider(int id) {
        //initialize database connection
        DB = new SQLiteInterface();
        Person.Provider newProvider = DB.retrieveProvider(id);
        this.id = id;

        //set swing window configuration
        setContentPane(contentPane);
        setTitle("Welcome Provider: " + newProvider.name + "!");
        setLocationRelativeTo(null);
        setResizable(true);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        IDLabel.setText(formatNumber.ID(newProvider.id));
        NameLabel.setText(newProvider.name);

        //list unVisitMembers
        ServiceID = new Vector<Integer>();
        ServiceID.clear();
        for (Map.Entry<Integer, Integer> entry : DB.unVisitMembers(id).entrySet()) {
            int serviceCode = entry.getKey();
            Person.Member display = DB.retrieveMember(entry.getValue());
            comboBoxMember.addItem(formatNumber.serviceCode(serviceCode) + "   " +
                    display.name + "   " + formatNumber.ID(display.id));
            ServiceID.add(serviceCode);
        }

        for (Service add : DB.retrieveServieTable()) {
            comboBoxService.addItem(String.valueOf(add.fee) + "     " + add.service_name);
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
        double updateFee = DB.retrieveServieTable().get(comboBoxService.getSelectedIndex()).fee;
        int serviceID = ServiceID.get(comboBoxMember.getSelectedIndex());
        String comments = textAreaComment.getText();
        //update comment to service instance
        //set visit bit in serviceinstance table as 1
        DB.setVisitedAndComments(serviceID, comments);

        //output result
        System.out.println(formatNumber.serviceCode(ServiceID.get(comboBoxMember.getSelectedIndex())) + "  Comments: " + comments + "\n");

        //update member fee
        DB.updateMemberFee(serviceID, updateFee);


        //print test ServiceID
        for (int i = 0; i < ServiceID.size(); i++) {
            System.out.println(formatNumber.serviceCode(ServiceID.get(i)));
        }

        String result = "Service Code:  " + formatNumber.serviceCode(serviceID) + "\n";
        result += "Service:       " + comboBoxService.getSelectedItem() + "\n";
        result += "Comment:       " + comments + "\n";
        JOptionPane.showConfirmDialog(null, result, "Confirm", JOptionPane.CLOSED_OPTION);
        dispose();
    }

    private void onCancel() {

        dispose();
    }

    public static void main(String[] args) {
        Provider dialog = new Provider(4);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
