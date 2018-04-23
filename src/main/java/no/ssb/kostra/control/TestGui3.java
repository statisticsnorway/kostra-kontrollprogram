package no.ssb.kostra.control;

/*
import java.io.*;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JTextField;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import no.ssb.kostra.control.sensitiv.ControlProgram;
import oracle.jdeveloper.layout.XYLayout;
import oracle.jdeveloper.layout.XYConstraints;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
*/
public class TestGui3 //extends JFrame 
{
  /*  
  private JPanel jPanel1 = new JPanel();
  private JTextField jTextField1 = new JTextField();
  private JLabel jLabel1 = new JLabel();
  private JComboBox jComboBox1 = new JComboBox();
  private JLabel jLabel4 = new JLabel();
  private JButton jButton1 = new JButton();
  private JTextField jTextField2 = new JTextField();
  private JLabel jLabel2 = new JLabel();
  private JButton jButton2 = new JButton();
  private JTextField jTextField3 = new JTextField();
  private JLabel jLabel3 = new JLabel();
  private JPanel jPanel2 = new JPanel();
  private JButton jButton3 = new JButton();
  private JButton jButton4 = new JButton();
  private FlowLayout flowLayout1 = new FlowLayout();
  private JPanel jPanel3 = new JPanel();
  private JSplitPane jSplitPane1 = new JSplitPane();
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JScrollPane jScrollPane2 = new JScrollPane();
  private JTextArea jTextArea1 = new JTextArea();
  private JTextArea jTextArea2 = new JTextArea();
  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private JTextField txtKontorNr = new JTextField();
  private JLabel jLabel5 = new JLabel();
  private GridBagLayout gridBagLayout1 = new GridBagLayout();

  public TestGui3()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    //jTextField1.setText("100300");
    //jComboBox1.setSelectedItem("Sosialhjelp");
    //jTextField2.setText("Q:\\DOK\\KOSTRA\\KOSTRA-kommune og Fylkes-KOSTRA\\Arbeidsområde\\Filuttrekk\\Kravspec\\Kontrollprogram\\Sosialhjelp\\Testfil_sosialhjelp2007.dat");
    //jTextField3.setText("C:\\Documents and Settings\\pll\\Skrivebord\\test_ssb11.txt");
  }

  private void jbInit() throws Exception
  {
    this.getContentPane().setLayout(borderLayout2);
    this.setSize(new Dimension(750, 726));
    this.setTitle("Test av kontrollprogram for " + Constants.kostraYear + "-rapporteringen");
    this.setDefaultCloseOperation(3);
    jPanel1.setLayout(gridBagLayout1);
    jLabel1.setText("Region");
    jLabel4.setText("Type");
    jButton1.setText("Velg...");
    jButton1.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton1_actionPerformed(e);
        }
      });
    jLabel2.setText("Filuttrekk / kildefil");
    jButton2.setText("Velg...");
    jButton2.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton2_actionPerformed(e);
        }
      });
    jLabel3.setText("Rapportfil");
    jPanel2.setLayout(flowLayout1);
    jButton3.setText("Kjør kontroll");
    jButton3.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton3_actionPerformed(e);
        }
      });
    jButton4.setText("Avslutt");
    jButton4.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          jButton4_actionPerformed(e);
        }
      });
    jPanel3.setBackground(Color.green);
    jPanel3.setLayout(borderLayout1);
    jSplitPane1.setDividerLocation(310);
    jTextArea1.setTabSize(4);
    jTextArea1.setEditable(false);
    jTextArea2.setTabSize(4);
    jTextArea2.setEditable(false);
    jLabel5.setText("Kontornr.");
    jPanel1.add(jLabel5, new GridBagConstraints(4, 0, 1, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 45, 0, 0), 0, 0));
    jPanel1.add(txtKontorNr, new GridBagConstraints(5, 0, 1, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 59, 0));
    jPanel1.add(jLabel3, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 10, 0, 0), 15, -2));
    jPanel1.add(jTextField3, new GridBagConstraints(2, 5, 6, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(15, 15, 0, 0), 541, -1));
    jPanel1.add(jButton2, new GridBagConstraints(8, 5, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 20, 0, 0), 11, -2));
    jPanel1.add(jLabel2, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(28, 10, 0, 0), 16, -2));
    jPanel1.add(jTextField2, new GridBagConstraints(3, 2, 5, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(28, 0, 0, 0), 511, -1));
    jPanel1.add(jButton1, new GridBagConstraints(8, 2, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(28, 20, 0, 0), 11, -2));
    jPanel1.add(jLabel4, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 22, 0, 0), 8, -2));
    jComboBox1.addItem("Regnskap");
    jComboBox1.addItem("Sosialhjelp");
    jComboBox1.addItem("Introduksjonsstønad");
    jComboBox1.addItem("Barnevern");
    jComboBox1.addItem("Familievern");
    jPanel1.add(jComboBox1, new GridBagConstraints(7, 0, 1, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 121, -1));
    jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 1, 3));
    jPanel1.add(jTextField1, new GridBagConstraints(1, 0, 3, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 15, 0, 0), 81, -1));
    jPanel2.add(jButton3, null);
    jPanel2.add(jButton4, null);
    jScrollPane1.getViewport().add(jTextArea1, null);
    jSplitPane1.add(jScrollPane1, JSplitPane.BOTTOM);
    jScrollPane2.getViewport().add(jTextArea2, null);
    jSplitPane1.add(jScrollPane2, JSplitPane.TOP);
    jPanel3.add(jSplitPane1, BorderLayout.CENTER);
    this.getContentPane().add(jPanel3, BorderLayout.CENTER);
    this.getContentPane().add(jPanel1, BorderLayout.NORTH);
    this.getContentPane().add(jPanel2, BorderLayout.SOUTH);
  }

  public static void main(String[] args)
  {
    TestGui3 gui = new TestGui3();
    gui.setVisible(true);
  }

  private void jButton3_actionPerformed(ActionEvent e)
  {
    if (((String)jComboBox1.getSelectedItem()).equalsIgnoreCase("Regnskap"))
    {
      no.ssb.kostra.control.regnskap.ControlProgram controlProgram
          = new no.ssb.kostra.control.regnskap.ControlProgram 
              (new String[] {jTextField1.getText(), getInfile(),
                getOutfile()});
      controlProgram.start();
    }
    else if (((String)jComboBox1.getSelectedItem()).equalsIgnoreCase("Sosialhjelp"))
    {
      no.ssb.kostra.control.sensitiv.ControlProgram controlProgram
          = new no.ssb.kostra.control.sensitiv.ControlProgram 
              (new String[] {jTextField1.getText(), getInfile(),
                getOutfile(),Integer.toString(ControlProgram.SOSIALHJELP)});
      controlProgram.start();
    }
    else if (((String)jComboBox1.getSelectedItem()).equalsIgnoreCase("Introduksjonsstønad"))
    {
      no.ssb.kostra.control.sensitiv.ControlProgram controlProgram
          = new no.ssb.kostra.control.sensitiv.ControlProgram 
              (new String[] {jTextField1.getText(), getInfile(),
                getOutfile(),Integer.toString(ControlProgram.INTRODUKSJONSSTONAD)});
      controlProgram.start();
    }
    else if (((String)jComboBox1.getSelectedItem()).equalsIgnoreCase("Barnevern"))
    {
      no.ssb.kostra.control.sensitiv.ControlProgram controlProgram
          = new no.ssb.kostra.control.sensitiv.ControlProgram 
              (new String[] {jTextField1.getText(), getInfile(),
                getOutfile(),Integer.toString(ControlProgram.BARNEVERN)});
      controlProgram.start();
    }
    else if (((String)jComboBox1.getSelectedItem()).equalsIgnoreCase("Familievern"))
    {
      no.ssb.kostra.control.sensitiv.ControlProgram controlProgram
          = new no.ssb.kostra.control.sensitiv.ControlProgram 
              (new String[] {jTextField1.getText(), getInfile(),
                getOutfile(),Integer.toString(ControlProgram.FAMILIEVERN), txtKontorNr.getText()});
      controlProgram.start();
    }

    try {
      BufferedReader fileReader = new BufferedReader (new FileReader(getOutfile()));
      String line;
      String buffer = "";
      while ((line = fileReader.readLine()) != null) {
          buffer += line + "\n";
      }
      fileReader.close();
      jTextArea1.setText(buffer);
      jTextArea1.setCaretPosition(0);
  
      fileReader = new BufferedReader (new FileReader(getInfile()));
      buffer = "";
      int lineNo = 1;
      while ((line = fileReader.readLine()) != null) {
        buffer += lineNo++ + ":\t" + line + "\n";
      }
      fileReader.close();
      jTextArea2.setText(buffer);
      jTextArea2.setCaretPosition(0);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  private String getInfile() {
    File f = new File(jTextField2.getText()); 
    return f.getAbsolutePath(); 
  }
  
  private String getOutfile() {
    File f = new File(jTextField3.getText()); 
    return f.getAbsolutePath(); 
  }

  private void jButton4_actionPerformed(ActionEvent e)
  {
    System.exit (0);
  }

  private void jButton1_actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File("Q:/DOK/KOSTRA/KOSTRA-kommune og Fylkes-KOSTRA/Arbeidsområde/Filuttrekk/Kravspec/Kontrollprogram")); //C:/KODE/KOSTRA/Workspace1/Kontrollprogram05/testfiler"));
    fc.setDialogTitle("Velg kildefil (filuttrekk)");
    int returnValue = fc.showOpenDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION)
    {
      jTextField2.setText(fc.getSelectedFile().getAbsolutePath());
    }
  
  }

  private void jButton2_actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File("Q:/DOK/KOSTRA/KOSTRA-kommune og Fylkes-KOSTRA/Arbeidsområde/Filuttrekk/Kravspec/Kontrollprogram")); //C:/KODE/KOSTRA/Workspace1/Kontrollprogram05/testfiler"));
    fc.setDialogTitle("Velg rapportfil");
    int returnValue = fc.showSaveDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION)
    {
      jTextField3.setText(fc.getSelectedFile().getAbsolutePath());
    }
  
  }
*/  
}