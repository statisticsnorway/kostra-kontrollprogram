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
import oracle.jdeveloper.layout.XYLayout;
import oracle.jdeveloper.layout.XYConstraints;
import javax.swing.JScrollPane;
*/
public class TestGui2 //extends JFrame 
{
  /*  
  private JScrollPane jScrollPane1 = new JScrollPane();
  private JTextArea jTextArea1 = new JTextArea();
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
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  private JPanel jPanel3 = new JPanel();
  private JScrollPane jScrollPane2 = new JScrollPane();
  private JTextArea jTextArea2 = new JTextArea();
  private GridLayout gridLayout1 = new GridLayout();
  private BorderLayout borderLayout1 = new BorderLayout();

  public TestGui2()
  {
    try
    {
      jbInit();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }

  }

  private void jbInit() throws Exception
  {
    this.getContentPane().setLayout(borderLayout1);
    this.setSize(new Dimension(750, 726));
    this.setTitle("Test av kontrollprogram for 2006-rapporteringen 2.");
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
    jPanel3.setLayout(gridLayout1);
    jTextArea2.setTabSize(4);
    gridLayout1.setColumns(2);
    jPanel1.add(jLabel3, new GridBagConstraints(0, 5, 2, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(15, 10, 0, 0), 15, -2));
    jPanel1.add(jTextField3, new GridBagConstraints(2, 5, 4, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(15, 15, 0, 0), 391, -1));
    jPanel1.add(jButton2, new GridBagConstraints(7, 5, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(15, 20, 45, 0), 11, -2));
    jPanel1.add(jLabel2, new GridBagConstraints(0, 2, 3, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(25, 10, 0, 0), 16, -2));
    jPanel1.add(jTextField2, new GridBagConstraints(3, 2, 3, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(25, 0, 0, 0), 361, -1));
    jPanel1.add(jButton1, new GridBagConstraints(7, 2, 1, 3, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(25, 15, 0, 0), 11, -2));
    jPanel1.add(jLabel4, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 45, 0, 0), 8, -2));
    jComboBox1.addItem("Regnskap");
    jPanel1.add(jComboBox1, new GridBagConstraints(5, 0, 1, 2, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 0, 0), 65, -1));
    jPanel1.add(jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 0, 0), 1, 3));
    jPanel1.add(jTextField1, new GridBagConstraints(1, 0, 3, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 15, 0, 0), 141, -1));
    jPanel2.add(jButton3, null);
    jPanel2.add(jButton4, null);
    jScrollPane2.getViewport().add(jTextArea2, null);
    jPanel3.add(jScrollPane2, null);
    jScrollPane1.getViewport().add(jTextArea1, null);
    jPanel3.add(jScrollPane1, null);
    this.getContentPane().add(jPanel3, BorderLayout.CENTER);
    this.getContentPane().add(jPanel1, BorderLayout.NORTH);
    this.getContentPane().add(jPanel2, BorderLayout.SOUTH);
  }

  public static void main(String[] args)
  {
    TestGui2 gui = new TestGui2();
    gui.setVisible(true);
  }

  private void jButton3_actionPerformed(ActionEvent e)
  {
    if (((String)jComboBox1.getSelectedItem()).equalsIgnoreCase("Regnskap"))
    {
      no.ssb.kostra.control.regnskap.ControlProgram controlProgram
          = new no.ssb.kostra.control.regnskap.ControlProgram 
              (new String[] {jTextField1.getText(), jTextField2.getText(),
                jTextField3.getText()});
      controlProgram.start();
    }

    try
    {
      BufferedReader fileReader 
          = new BufferedReader (new FileReader (jTextField3.getText()));
      String line;
    String buffer = "";
      while ((line = fileReader.readLine()) != null)
      {
        buffer += line + "\n";
      }
      fileReader.close();
      jTextArea1.setText(buffer);

      fileReader 
          = new BufferedReader (new FileReader (jTextField2.getText()));
    buffer = "";
    int lineNo = 1;
      while ((line = fileReader.readLine()) != null)
      {
        buffer += lineNo++ + ":\t" + line + "\n";
      }
      fileReader.close();
      jTextArea2.setText(buffer);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void jButton4_actionPerformed(ActionEvent e)
  {
    System.exit (0);
  }

  private void jButton1_actionPerformed(ActionEvent e)
  {
    JFileChooser fc = new JFileChooser();
    fc.setCurrentDirectory(new File("C:/PC/Skrivebord/q-dok/Regnskap"));
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
    fc.setCurrentDirectory(new File("C:/PC/Skrivebord/q-dok/Regnskap"));
    fc.setDialogTitle("Velg rapportfil");
    int returnValue = fc.showSaveDialog(this);
    if (returnValue == JFileChooser.APPROVE_OPTION)
    {
      jTextField3.setText(fc.getSelectedFile().getAbsolutePath());
    }
  
  }
 */ 
}