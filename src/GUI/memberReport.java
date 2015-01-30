package GUI;

import Report.BuildTXT;
import database.SQLiteInterface;
import Person.Member;
import Format.*;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


public class memberReport extends JDialog {
    private JPanel ContentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList MembersList;
    private SQLiteInterface DB;

//    private String[] namesArray;
    private Vector<Member> Members;

    public memberReport() {
        setContentPane(ContentPane);
        setTitle("Report System");
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        //connect to database
        DB = new SQLiteInterface();
        Members = DB.retrieveMemberTable();

//        List<String> names = new ArrayList<String>();
//        namesArray = names.toArray(new String[names.size()]);

        DefaultListModel mb = new DefaultListModel();
        for(Member m: Members){

            mb.addElement( Format.formatNumber.ID(m.id) + "   " + m.name );

//          names.add( Format.formatNumber.ID(m.id) + "   " + m.name );
        }

        MembersList.setModel(mb);

//        for(String p: names){
//            System.out.println(p);
//        }

//        MembersList = new JList <String> (namesArray);

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
        ContentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        BuildTXT Res;
        for(int i: MembersList.getSelectedIndices()){
            Res = new BuildTXT(Members.get(i));
        }
        JOptionPane.showConfirmDialog(null, MembersList.getSelectedIndices().length + " Reports were built.", "Confirm", JOptionPane.CLOSED_OPTION);
        dispose();
    }

    private void onCancel() {

        dispose();
    }

    public static void main(String[] args) {
        memberReport dialog = new memberReport();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here

    }
}
