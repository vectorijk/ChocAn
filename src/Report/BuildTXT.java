package Report;

import Format.Time;
import Person.Member;

import javax.swing.*;
import java.io.*;
import java.util.Vector;

/**
 * Created by Kai Jiang on 12/5/2014.
 */
public class BuildTXT {
    private String fileName;

    public BuildTXT() {
        String fileNamePrefix = "";
        String fileNameMiddle = Time.getCurrentTimestampSimple();
        String fileNameSuffix = ".txt";
        this.fileName = fileNamePrefix +"@"+ fileNameMiddle + fileNameSuffix;
    }

    public BuildTXT(String name) {
        String fileNamePrefix = name;
        String fileNameMiddle = Time.getCurrentTimestampSimple();
        String fileNameSuffix = ".txt";
        this.fileName = fileNamePrefix +"@"+ fileNameMiddle + fileNameSuffix;
    }

    public BuildTXT(Member m) {
        String fileNamePrefix = m.name;
        String fileNameMiddle = Time.getCurrentTimestampSimple();
        String fileNameSuffix = ".txt";
        this.fileName = fileNamePrefix +"@"+ fileNameMiddle + fileNameSuffix;
        System.out.println(this.fileName);
        printFile(buildReport(m));
    }

    public void printFile(Vector<String> everyLine) {
        PrintWriter out = null;
        FileOutputStream fos;
        OutputStreamWriter osw;

        try {
            fos = new FileOutputStream(this.fileName, false);
            osw = new OutputStreamWriter(fos,"UTF-8");
            out = new PrintWriter(osw, false);	//false so we don't automatically flush

            for (String eachLine : everyLine) {
                out.println(eachLine);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "FileWriter Error: unable to write to file!", "I/O Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            if(out != null)
                out.close();
        }
    }

    public Vector<String> buildReport(Member m){
        Vector<String> content = new Vector<String>();
        content.add("================================================================");
        content.add("============   Build Time: " + Time.getCurrentTimestamp() + "   ===============");
        content.add("================================================================");
        content.add(" |  Member ID:  |      " + Format.formatNumber.ID(m.id));
        content.add(" |  Name:       |      " + m.name);
        content.add(" |  Address:    |      " + m.address);
        content.add(" |              |      " + m.city + " ," + m.state + m.zipCode);
        content.add(" |  Fee:        |      $" + m.fee);
        content.add("================================================================");
        content.add("=========================  END  ================================");
        content.add("================================================================");

        for(String i: content){
            System.out.println(i);
        }
        return content;
    }

    public static void main(String[] args){

        Member m1 = new Member();
        m1.id = 12;
        m1.name = "Kai";
        m1.address = "1234 er";
        m1.city = "Portland";
        m1.state = "OR";
        m1.zipCode = "97200";
        m1.fee = 10.23;

        BuildTXT rpt1 = new BuildTXT(m1);
    }
}
