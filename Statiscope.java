/////////////////////////////////////////////////////////////////////
//
// Statiscope
//
/**
 *    Statiscope v1.0.9 by M.O.B. as Java SE applet and application. 
 *    Copyright (C) 1996-2012 by Mikael O. Bonnier, Lund, Sweden.
 *    License for all methods except those marked "Proprietary": GNU GPL v3 or later, 
 *    http://www.gnu.org/licenses/gpl.txt
 *    A plan is to replace these proprietary methods (tStats, tDistr, chi2Stats, 
 *    chi2Distr) using functions from Octave or R or some
 *    other statistics package under a FOSS licence.
 *    Donations are welcome to PayPal mikael.bonnier@gmail.com.
 *    The source code is at http://www.df.lth.se.orbin.se/~mikaelb/statiscope/
 *
 *    @version        1.0.9
 *    @author         Mikael O. Bonnier
 */
//
// It was developed using JDK-1.0.2 on Windows 95.
//
// Revision history:
// 2012-Jan: 1.0.9  "private protected" replaced with  "private /* protected */".
//                   This change made it work in OpenJDK/IcedTea.
// 1996-Oct: 1.0bX  Beta versions.
//
// Suggestions, improvements, and bug-reports
// are always welcome to:
//                  Mikael O. Bonnier
//                  Osten Undens gata 88
//                  SE-227 62  LUND
//                  SWEDEN
//
// Or use my internet addresses:
//                  mikael.bonnier@gmail.com
//                  http://www.df.lth.se.orbin.se/~mikaelb/
//              _____
//             /   / \
// ***********/   /   \***********
//           /   /     \
// *********/   /       \*********
//         /   /   / \   \
// *******/   /   /   \   \*******
//       /   /   / \   \   \
// *****/   /   /***\   \   \*****
//     /   /__ /_____\   \   \
// ***/               \   \   \***
//   /_________________\   \   \
// **\                      \  /**
//    \______________________\/
//
// Mikael O. Bonnier
/////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.io.*;
import java.net.*;
import java.applet.Applet;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Hashtable;

public class Statiscope extends Applet implements Runnable
{
   public int Copyright__C__1996_Mikael_O_Bonnier__Lund__Sweden;
   private /* protected */ StatCanvas canvas;
   protected Checkbox chkRel;
   protected Checkbox chkTheo;
   private /* protected */ Button btnLess;
   private /* protected */ Button btnGreater;
   protected Label lblXMin;
   protected Label lblXMax;
   protected Label lblYMin;
   protected Label lblYMax;
   private /* protected */ TextArea txtStemLeaf;
   protected TextField txtData;
   protected List lstData;
   private /* protected */ CheckboxGroup grpDiagStyle;
   private /* protected */ Checkbox chkCDF;
   private /* protected */ Checkbox chkPMF;
   private /* protected */ Checkbox chkDF;
   private /* protected */ Checkbox chkBP;
   private /* protected */ Checkbox chkSL;
   private /* protected */ Checkbox chkData;

   private /* protected */ Label lblData;
   private /* protected */ Label lblNumber;
   private /* protected */ Label lblSum;
   private /* protected */ Label lblMean;
   private /* protected */ Label lblSamStdDev;
   private /* protected */ Label lblPopStdDev;
   private /* protected */ Label lblMin;
   private /* protected */ Label lblQ1;
   private /* protected */ Label lblMed;
   private /* protected */ Label lblQ3;
   private /* protected */ Label lblMax;
   private /* protected */ Label lblSimNo;
   private /* protected */ Label lblSimMean;
   private /* protected */ Label lblSimStdDev;
   private /* protected */ Label lblConf;
   private /* protected */ Label lblConfMean;
   private /* protected */ Label lblConfStdDev;
   private /* protected */ Label lblHyp;
   private /* protected */ Label lblPValue;

   private /* protected */ TextField txtNumber;
   private /* protected */ TextField txtSum;
   private /* protected */ TextField txtMean;
   private /* protected */ TextField txtSamStdDev;
   private /* protected */ TextField txtPopStdDev;
   private /* protected */ TextField txtMin;
   private /* protected */ TextField txtQ1;
   private /* protected */ TextField txtMed;
   private /* protected */ TextField txtQ3;
   private /* protected */ TextField txtMax;
   private /* protected */ Choice choConf;
   private /* protected */ CheckboxGroup grpRange;
   private /* protected */ Checkbox chkLower;
   private /* protected */ Checkbox chkMiddle;
   private /* protected */ Checkbox chkUpper;
   private /* protected */ TextField txtConfMean;
   private /* protected */ TextField txtHypMean;
   private /* protected */ TextField txtPValueMean;
   private /* protected */ TextField txtConfStdDev;
   private /* protected */ TextField txtHypStdDev;
   private /* protected */ TextField txtPValueStdDev;
   private /* protected */ TextField txtSimNo;
   private /* protected */ TextField txtSimMean;
   private /* protected */ TextField txtSimStdDev;
   private /* protected */ Button btnSimulate;
   private /* protected */ Button btnStop;
   private /* protected */ Button btnClearData;
   private /* protected */ Button btnSort;

   protected double[] madData;
   protected double mdMin;
   protected double mdQ1;
   protected double mdMed;
   protected double mdQ3;
   protected double mdMax;
   protected double mdMean, mdSamStdDev;
   private /* protected */ boolean mbSite = false;
   private /* protected */ boolean bURLCGI = true;
   protected boolean mbApp = false;
   protected Hashtable parameters;
   protected Thread dataThread;
   protected int mnRunAction;

   public String getAppletInfo() 
   {
      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
      System.gc();

      return "Statiscope 1.0beta8\n" +
             "Copyright © 1996-2012 Mikael O. Bonnier, Lund, Sweden\n" +
             "All rights reserved\n" +
             "Internet e-mail: mikael.bonnier@gmail.com\n" +
             "WWW home page: http://www.df.lth.se.orbin.se/~mikaelb/\n\n" +
             "Operating system architecture: " + System.getProperty("os.arch") + "\n" +
             "Operating system name: " + System.getProperty("os.name") + "\n" +
             "Java vendor: " + System.getProperty("java.vendor") + "\n" +
             "Java version number: " + System.getProperty("java.version") + "\n" +
             "Free memory: " + Runtime.getRuntime().freeMemory()/1024 + " KB\n" +
             "Screen size: " + dim.width + "×" + dim.height + "\n" +
             "Directory: " + System.getProperty("user.dir") + "\n\n" +
             "DISCLAIMER\n" +
             "THIS PROGRAM IS USED AT YOUR OWN RISK. IT\n" +
             "SHOULD SPECIFICALLY NOT BE USED IN HIGH RISK\n" +
             "ACTIVITY SUCH AS DIAGNOSIS AND TREATMENT OF\n" +
             "PATIENTS, OR THE OPERATION OF NUCLEAR\n" +
             "FACILITIES OR WEAPONS SYSTEMS.";
   }

   public String[][] getParameterInfo()
   {
      String pinfo[][] = {
         {"bgcolor", "#RRGGBB", "Background color"},
         {"fgcolor", "#RRGGBB", "Foreground color"},
         {"cgiurl", "boolean", "Load URL via a CGI"},
         {"url", "String", "Data URL"}
      };
      return pinfo;
   }

   public String getParameter(String name)
   {
      if(mbApp)
      {
         return (String)parameters.get(name.toLowerCase());
      }
      else
         return super.getParameter(name);
   }

   public void paint(Graphics g)
   {
      if(mbSite)
         g.drawString("Statiscope: Contact Mikael O. Bonnier, mikael.bonnier@gmail.com,"
            + " http://www.df.lth.se.orbin.se/~mikaelb/ for licensing info.", 0, 40);
   }

   public void init() 
   {
      setFont(new Font("Dialog", Font.PLAIN, 12));

      setLayout(null);

      int nBGColor, nFGColor;
      try
      {
         nBGColor = Integer.parseInt(getParameter("bgcolor").substring(1), 16); // Skip #
         nFGColor = Integer.parseInt(getParameter("fgcolor").substring(1), 16); // Skip #
      }
      catch(RuntimeException e)
      {
         nBGColor = Color.lightGray.getRGB();
         nFGColor = Color.black.getRGB();
      }
      setBackground(new Color(nBGColor));
      setForeground(new Color(nFGColor));

      try
      {
         bURLCGI = Boolean.valueOf(getParameter("urlcgi")).booleanValue();
      }
      catch(RuntimeException e)
      {
      }


      /*
      if(!mbApp && !getDocumentBase().toString().substring(0, 30).equals(  // <---
         "http://www.df.lth.se.orbin.se/~mikaelb/")) // <---
      {
         mbSite = true;
         return;
      }
      */

      int nCW = 95;
      int nCH = 63;
      int nLW = 48;
      int nLH = 10;
      int nRH = 29;
      int nNW = 73;
      int nTW = 90;
      int nPW = 7;
      int nBH = 16;

      lblData = new Label("Data/Location:");
      add(lblData);
      lblData.reshape(nLW+nCW+nPW+nTW+nNW+nPW, 0, nTW, nRH);

      txtData = new TextField(80);
      txtData.setBackground(Color.white);
      add(txtData);
      txtData.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, 0, nNW, nRH);

      lstData = new List();
      lstData.setBackground(Color.white);
      add(lstData);
      lstData.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, nRH, nNW, 6*nRH);
      lstData.addItem("");
      lstData.select(0);
      lstData.makeVisible(0);
      txtData.setText("");
      txtData.selectAll();

      grpDiagStyle = new CheckboxGroup();

      chkCDF = new Checkbox("Distribution", grpDiagStyle, true);
      add(chkCDF);
      chkCDF.reshape(0, 4*nRH, nLW+nCW, nRH);

      chkPMF = new Checkbox("Probability mass", grpDiagStyle, false);
      add(chkPMF);
      chkPMF.reshape(0, 5*nRH, nLW+nCW, nRH);

      chkDF = new Checkbox("Density", grpDiagStyle, false);
      add(chkDF);
      chkDF.reshape(0, 6*nRH, nLW+nCW, nRH);

      chkBP = new Checkbox("Box plot", grpDiagStyle, false);
      add(chkBP);
      chkBP.reshape(0, 7*nRH, nLW+nCW, nRH);

      chkSL = new Checkbox("Stem and leaf", grpDiagStyle, false);
      add(chkSL);
      chkSL.reshape(0, 8*nRH, nLW+nCW, nRH);

      chkData = new Checkbox("Data listing", grpDiagStyle, false);
      add(chkData);
      chkData.reshape(0, 9*nRH, nLW+nCW, nRH);

      lblNumber = new Label("Number");
      add(lblNumber);
      lblNumber.reshape(nLW+nCW+nPW, 0, nTW, nRH);

      txtNumber = new TextField(80);
      txtNumber.setEditable(false);
      add(txtNumber);
      txtNumber.reshape(nLW+nCW+nPW+nTW, 0, nNW, nRH);

      lblSum = new Label("Sum");
      add(lblSum);
      lblSum.reshape(nLW+nCW+nPW, nRH, nTW, nRH);

      txtSum = new TextField(80);
      txtSum.setEditable(false);
      add(txtSum);
      txtSum.reshape(nLW+nCW+nPW+nTW, nRH, nNW, nRH);    

      lblMean = new Label("Mean");
      add(lblMean);
      lblMean.reshape(nLW+nCW+nPW, 2*nRH, nTW, nRH);

      txtMean = new TextField(80);
      txtMean.setEditable(false);
      add(txtMean);
      txtMean.reshape(nLW+nCW+nPW+nTW, 2*nRH, nNW, nRH);    

      lblSamStdDev = new Label("Sam. std. dev.");
      add(lblSamStdDev);
      lblSamStdDev.reshape(nLW+nCW+nPW, 3*nRH, nTW, nRH);

      txtSamStdDev = new TextField(80);
      txtSamStdDev.setEditable(false);
      add(txtSamStdDev);
      txtSamStdDev.reshape(nLW+nCW+nPW+nTW, 3*nRH, nNW, nRH);

      lblPopStdDev = new Label("Pop. std. dev.");
      add(lblPopStdDev);
      lblPopStdDev.reshape(nLW+nCW+nPW, 4*nRH, nTW, nRH);

      txtPopStdDev = new TextField(80);
      txtPopStdDev.setEditable(false);
      add(txtPopStdDev);
      txtPopStdDev.reshape(nLW+nCW+nPW+nTW, 4*nRH, nNW, nRH);

      lblMin = new Label("Minimum");
      add(lblMin);
      lblMin.reshape(nLW+nCW+nPW, 5*nRH, nTW, nRH);

      txtMin = new TextField(80);
      txtMin.setEditable(false);
      add(txtMin);
      txtMin.reshape(nLW+nCW+nPW+nTW, 5*nRH, nNW, nRH);

      lblQ1 = new Label("Quartile 1");
      add(lblQ1);
      lblQ1.reshape(nLW+nCW+nPW, 6*nRH, nTW, nRH);

      txtQ1 = new TextField(80);
      txtQ1.setEditable(false);
      add(txtQ1);
      txtQ1.reshape(nLW+nCW+nPW+nTW, 6*nRH, nNW, nRH);

      lblMed = new Label("Median");
      add(lblMed);
      lblMed.reshape(nLW+nCW+nPW, 7*nRH, nTW, nRH);

      txtMed = new TextField(80);
      txtMed.setEditable(false);
      add(txtMed);
      txtMed.reshape(nLW+nCW+nPW+nTW, 7*nRH, nNW, nRH);

      lblQ3 = new Label("Quartile 3");
      add(lblQ3);
      lblQ3.reshape(nLW+nCW+nPW, 8*nRH, nTW, nRH);

      txtQ3 = new TextField(80);
      txtQ3.setEditable(false);
      add(txtQ3);
      txtQ3.reshape(nLW+nCW+nPW+nTW, 8*nRH, nNW, nRH);

      lblMax = new Label("Maximum");
      add(lblMax);
      lblMax.reshape(nLW+nCW+nPW, 9*nRH, nTW, nRH);

      txtMax = new TextField(80);
      txtMax.setEditable(false);
      add(txtMax);
      txtMax.reshape(nLW+nCW+nPW+nTW, 9*nRH, nNW, nRH);

      lblConf = new Label("Confidence");
      add(lblConf);
      lblConf.reshape(nLW+nCW+nPW, 11*nRH, nTW, nRH);

      choConf = new Choice();
      choConf.setBackground(Color.white);
      choConf.addItem("90%");
      choConf.addItem("95%");
      choConf.addItem("97.5%");
      choConf.addItem("99%");
      choConf.addItem("99.5%");
      choConf.select(1);
      add(choConf);
      choConf.reshape(nLW+nCW+nPW+nTW, 11*nRH, nNW, nRH);

      lblConfMean = new Label("Mean");
      add(lblConfMean);
      lblConfMean.reshape(nLW+nCW-nTW+nPW, 12*nRH, nTW, nRH);

      txtConfMean = new TextField(80);
      txtConfMean.setEditable(false);
      add(txtConfMean);
      txtConfMean.reshape(nLW+nCW+nPW, 12*nRH, nTW+nNW, nRH);

      lblConfStdDev = new Label("Std. dev.");
      add(lblConfStdDev);
      lblConfStdDev.reshape(nLW+nCW-nTW+nPW, 13*nRH, nTW, nRH);

      txtConfStdDev = new TextField(80);
      txtConfStdDev.setEditable(false);
      add(txtConfStdDev);
      txtConfStdDev.reshape(nLW+nCW+nPW, 13*nRH, nTW+nNW, nRH);

      lblPValue = new Label("P-value");
      add(lblPValue);
      lblPValue.reshape(nLW+nCW+nPW+nTW+nTW, 11*nRH, nNW, nRH);

      lblHyp = new Label("Hypothesis");
      add(lblHyp);
      lblHyp.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, 11*nRH, nTW, nRH);

      txtPValueMean = new TextField(80);
      txtPValueMean.setEditable(false);
      add(txtPValueMean);
      txtPValueMean.reshape(nLW+nCW+nPW+nTW+nTW, 12*nRH, nNW, nRH);

      txtHypMean = new TextField(80);
      txtHypMean.setBackground(Color.white);
      add(txtHypMean);
      txtHypMean.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, 12*nRH, nNW, nRH);

      txtPValueStdDev = new TextField(80);
      txtPValueStdDev.setEditable(false);
      add(txtPValueStdDev);
      txtPValueStdDev.reshape(nLW+nCW+nPW+nTW+nTW, 13*nRH, nNW, nRH);

      txtHypStdDev = new TextField(80);
      txtHypStdDev.setBackground(Color.white);
      add(txtHypStdDev);
      txtHypStdDev.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, 13*nRH, nNW, nRH);

      grpRange = new CheckboxGroup();

      chkLower = new Checkbox("Lower", grpRange, false);
      add(chkLower);
      chkLower.reshape(nLW+nCW+nPW, 14*nRH, (2*nTW+nNW)/3, nRH);

      chkMiddle = new Checkbox("Middle", grpRange, true);
      add(chkMiddle);
      chkMiddle.reshape(nLW+nCW+nPW+(2*nTW+nNW)/3, 14*nRH, (2*nTW+nNW)/3, nRH);

      chkUpper = new Checkbox("Upper", grpRange, false);
      add(chkUpper);
      chkUpper.reshape(nLW+nCW+nPW+2*(2*nTW+nNW)/3, 14*nRH, (2*nTW+nNW)/3, nRH);
      
      lblSimNo = new Label("Number");
      add(lblSimNo);
      lblSimNo.reshape(nLW+nCW+nPW+nTW+nNW+nPW, 7*nRH, nTW, nRH);

      txtSimNo = new TextField(80);
      txtSimNo.setBackground(Color.white);
      add(txtSimNo);
      txtSimNo.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, 7*nRH, nNW, nRH);

      lblSimMean = new Label("Mean");
      add(lblSimMean);
      lblSimMean.reshape(nLW+nCW+nPW+nTW+nNW+nPW, 8*nRH, nTW, nRH);

      txtSimMean = new TextField(80);
      txtSimMean.setBackground(Color.white);
      add(txtSimMean);
      txtSimMean.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, 8*nRH, nNW, nRH);

      lblSimStdDev = new Label("Std. dev.");
      add(lblSimStdDev);
      lblSimStdDev.reshape(nLW+nCW+nPW+nTW+nNW+nPW, 9*nRH, nTW, nRH);

      txtSimStdDev = new TextField(80);
      txtSimStdDev.setBackground(Color.white);
      add(txtSimStdDev);
      txtSimStdDev.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW, 9*nRH, nNW, nRH);

      btnSimulate = new Button("Simulate");
      add(btnSimulate);
      btnSimulate.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW+nNW+2*nPW+6, 7*nRH+1, nNW-2, nRH-2);

      btnStop = new Button("Stop");
      btnStop.disable();
      add(btnStop);
      btnStop.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW+nNW+2*nPW+6, 1, nNW-2, nRH-2);

      btnClearData = new Button("Clear data");
      add(btnClearData);
      btnClearData.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW+nNW+2*nPW+6, nRH+1, nNW-2, nRH-2);

      btnSort = new Button("Sort");
      add(btnSort);
      btnSort.reshape(nLW+nCW+nPW+nTW+nNW+nPW+nTW+nNW+2*nPW+6, 2*nRH+1, nNW-2, nRH-2);

      calcStatistics();

      Font fntLabel = new Font("Helvetica", Font.PLAIN, 9);

      lblXMin = new Label("", Label.LEFT);
      lblXMin.setFont(fntLabel);
      add(lblXMin);
      lblXMin.reshape(nLW, nCH, nLW, nLH);

      lblXMax = new Label("", Label.RIGHT);
      lblXMax.setFont(fntLabel);
      add(lblXMax);
      lblXMax.reshape(nCW, nCH, nLW, nLH);

      lblYMin = new Label("", Label.RIGHT);
      lblYMin.setFont(fntLabel);
      add(lblYMin);
      lblYMin.reshape(0, nCH - nLH, nLW, nLH);

      lblYMax = new Label("", Label.RIGHT);
      lblYMax.setFont(fntLabel);
      add(lblYMax);
      lblYMax.reshape(0, 0, nLW, nLH);

      chkRel = new Checkbox("Rel.");
      chkRel.setFont(fntLabel);
      chkRel.disable();
      add(chkRel);
      chkRel.reshape(0, nCH/2 - nBH, nLW, nBH);

      chkTheo = new Checkbox("Theo.");
      chkTheo.setFont(fntLabel);
      chkTheo.enable();
      add(chkTheo);
      chkTheo.reshape(0, nCH/2, nLW, nBH);

      btnLess = new Button("<");
      btnLess.setFont(fntLabel);
      btnLess.disable();
      add(btnLess);
      btnLess.reshape(nLW+nCW/2 - nBH, nCH+nLH, nBH, nBH);

      btnGreater = new Button(">");
      btnGreater.setFont(fntLabel);
      btnGreater.disable();
      add(btnGreater);
      btnGreater.reshape(nLW+nCW/2, nCH+nLH, nBH, nBH);

      txtStemLeaf = new TextArea();
      txtStemLeaf.setEditable(false);
      txtStemLeaf.setBackground(Color.green);
      txtStemLeaf.setFont(new Font("Courier", Font.PLAIN, 10));
      txtStemLeaf.hide();
      add(txtStemLeaf);
      txtStemLeaf.reshape(nLW, 0, nCW, nCH);

      canvas = new StatCanvas();
      add(canvas);
      canvas.reshape(nLW, 0, nCW, nCH);

      validate();
//      txtData.requestFocus();
      try
      {
         txtData.setText(getParameter("url"));
         postEvent(new Event(txtData, Event.ACTION_EVENT, new Object()));
      }
      catch(RuntimeException e)
      {
      }
   }

   public boolean handleEvent(Event evt) 
   {
// System.out.println(evt);
      switch(evt.id)
      {
         case Event.ACTION_EVENT:
            if(evt.target == chkCDF) 
            {
               canvas.mnDiagStyle = 0;
               refreshScope();
            }
            else if(evt.target == chkPMF) 
            {
               canvas.mnDiagStyle = 1;
               refreshScope();
            }
            else if(evt.target == chkDF) 
            {
               canvas.mnDiagStyle = 2;
               refreshScope();
            }
            else if(evt.target == chkBP)
            {
               canvas.mnDiagStyle = 3;
               refreshScope();
            }
            else if(evt.target == chkSL)
            {
               refreshScope();
            }
            else if(evt.target == chkData)
            {
               refreshScope();
            }
            else if(evt.target == chkRel)
            {
               refreshScope();
            }
            else if(evt.target == chkTheo)
            {
               refreshScope();
            }
            else if(evt.target == btnLess)
            {
               --canvas.mnXScl;
               refreshScope();
            }
            else if(evt.target == btnGreater)
            {
               ++canvas.mnXScl;
               refreshScope();
            }
            else if(evt.target == txtData)
            {
               StringTokenizer st = new StringTokenizer(txtData.getText(), ",; \t\n\r");
               String sNumber = "";
               try 
               {
                  sNumber = st.nextToken();
                  Double.valueOf(sNumber);
                  int nSel = lstData.getSelectedIndex();
                  if(nSel == lstData.countItems() - 1)
                     lstData.addItem("");
                  lstData.replaceItem(sNumber, nSel);
                  while(st.hasMoreTokens()) 
                  { 
                     ++nSel;
                     sNumber = st.nextToken();
                     Double.valueOf(sNumber);
                     lstData.addItem(sNumber, nSel); 
                  }
                  lstData.select(nSel + 1);
                  lstData.makeVisible(nSel + 1);
                  txtData.setText(lstData.getSelectedItem());
                  txtData.selectAll();
                  calcStatistics();
                  refreshScope();
               }
               catch(java.util.NoSuchElementException e)
               {
               }
               catch(NumberFormatException e)
               {
                  if(dataThread == null) 
                  {
                     dataThread = new Thread(this, "Data");
                     mnRunAction = 1;
                     dataThread.start();
                  }
               }
            }
            else if(evt.target == choConf)
            {
               calcConfMean();
               calcConfStdDev();
            }
            else if(evt.target == chkLower || evt.target == chkMiddle
               || evt.target == chkUpper)
            {
               calcConfMean();
               testHypMean();
               calcConfStdDev();
               testHypStdDev();
            }
            else if(evt.target == txtHypMean)
               testHypMean();
            else if(evt.target == txtHypStdDev)
               testHypStdDev();
            else if(evt.target == btnSimulate || evt.target == txtSimNo
               || evt.target == txtSimMean || evt.target == txtSimStdDev)
            {
               if(dataThread == null) 
               {
                  if(txtSimNo.getText().length() != 0 &&
                     txtSimMean.getText().length() != 0 &&
                     txtSimStdDev.getText().length() != 0)
                  {
                     dataThread = new Thread(this, "Data");
//                     dataThread.setPriority(Thread.MIN_PRIORITY);
                     mnRunAction = 0;
                     dataThread.start();
                  }
               }
            }
            else if(evt.target == btnClearData)
            {
               clearData();
            }
            else if(evt.target == btnSort)
            {
               lstData.clear();
               for(int i = 0; i < madData.length; ++i)
                  lstData.addItem(Double.toString(madData[i]));
               lstData.addItem("");
               lstData.select(lstData.countItems()-1);
               lstData.makeVisible(lstData.countItems()-1);
               if(chkData.getState())
                  printData();
            }
            else if(evt.target == btnStop)
            {
               if(dataThread != null)
               {
//                  dataThread.stop();
// System.out.println("Stop " + dataThread);
                  dataThread = null;
               }
//               btnStop.disable();
            }
            break;
         case Event.LIST_SELECT:
            if(evt.target == lstData)
            {
               txtData.setText(lstData.getSelectedItem());
               txtData.selectAll();              
            }
            break;
         case Event.KEY_PRESS:
            if(evt.target == lstData)
            {
               switch(evt.key)
               {
                  case 127:  // Delete
                  case '\b': // Backspace
                     int nSel = lstData.getSelectedIndex();
                     if(nSel != lstData.countItems() - 1)
                     {
                        lstData.delItem(nSel);
                        lstData.select(nSel);
                        lstData.makeVisible(nSel);
                        txtData.setText(lstData.getSelectedItem());
                        txtData.selectAll();
                        calcStatistics();
                        refreshScope();
                     }
                     break;
                  case 'i':  // 105
                  case 'I':  // 73
                     nSel = lstData.getSelectedIndex();
                     lstData.addItem("0", nSel);
                     lstData.select(nSel);
                     lstData.makeVisible(nSel);
                     txtData.setText(lstData.getSelectedItem());
                     txtData.requestFocus();
                     txtData.selectAll();
                     calcStatistics();
                     refreshScope();
                     break;
               }
            }
         case Event.KEY_ACTION:
            if(evt.target == txtData)
            {
               int nSel;
               switch(evt.key)
               {
                  case Event.DOWN:
                     nSel = lstData.getSelectedIndex();
                     ++nSel;
                     lstData.select(nSel);
                     lstData.makeVisible(nSel);
                     txtData.setText(lstData.getSelectedItem());
                     txtData.selectAll();
                     return true;
                     // break;
                  case Event.UP:
                     nSel = lstData.getSelectedIndex();
                     --nSel;
                     lstData.select(nSel);
                     lstData.makeVisible(nSel);
                     txtData.setText(lstData.getSelectedItem());
                     txtData.selectAll();
                     return true;
                     // break;
               }
            }
            break;
      }
      return super.handleEvent(evt);
   }

   protected void clearData()
   {
      txtData.setText("");
      lstData.clear();
      lstData.addItem("");
      lstData.select(lstData.countItems()-1);
      lstData.makeVisible(lstData.countItems()-1);
      calcStatistics();
      canvas.mnXScl = 0;
      refreshScope();
      lblXMin.setText("");
      lblXMax.setText("");
      lblYMin.setText("");
      lblYMax.setText("");
   }

   public void stop()
   {
      if(dataThread != null)
      {
         dataThread.stop();
         dataThread = null;
      }
   }

   private /* protected */ void refreshScope()
   {
      boolean bText = chkSL.getState() || chkData.getState();
      if(dataThread == null)
      {
         chkRel.enable(chkPMF.getState() || chkDF.getState());
         chkTheo.enable(chkCDF.getState() || chkDF.getState());
         btnLess.enable(!bText && madData.length >= 2);
         btnGreater.enable(!bText && madData.length >= 2);
      }
      if(bText)
      {
         if(canvas.isVisible())
         {
            canvas.hide();
            lblXMin.hide();
            lblXMax.hide();
            lblYMin.hide();
            lblYMax.hide();
            txtStemLeaf.show();
         }
         if(chkSL.getState())
            printStemLeaf();
         else
            printData();
      }
      else
      {
         if(!chkBP.getState())
         {
            lblYMin.show();
            lblYMax.show();
         }
         else
         {
            lblYMin.hide();
            lblYMax.hide();
         }
         if(canvas.isVisible())
            canvas.repaint();
         else
         {
            txtStemLeaf.hide();
            lblXMin.show();
            lblXMax.show();
            canvas.repaint();
            canvas.show();
         }
      }
   }
   
   private /* protected */ void calcStatistics()
   {
      double dSum, dSumSquare, dSamVar, dPopStdDev;
      double dTmp, dXTmp;

      int nNoOfData = Math.max(lstData.countItems() - 1, 0);
      madData = new double[nNoOfData];
      if(nNoOfData == 0)
      {
         txtMean.setText("");
         txtSum.setText("");
         txtSamStdDev.setText("");
         txtPopStdDev.setText("");
         txtNumber.setText("");
         txtMin.setText("");
         txtQ1.setText("");
         txtMed.setText("");
         txtQ3.setText("");
         txtMax.setText("");
         calcConfMean();
         testHypMean();
         calcConfStdDev();
         testHypStdDev();
         return;
      }
      for(int i = 0; i < nNoOfData; ++i)
         madData[i] = Double.valueOf(lstData.getItem(i)).doubleValue();
      quick(madData);

      dXTmp = madData[0];
      dSum = mdMean = dXTmp;
      dSumSquare = dXTmp*dXTmp; 
      dSamVar = 0;
      for(int i = 1; i < nNoOfData; ++i)
      {
         dXTmp = madData[i];
         dSum = dSum + dXTmp;
         dSumSquare = dSumSquare + dXTmp*dXTmp;
         dTmp = mdMean;
         mdMean = mdMean + (dXTmp - mdMean)/(i + 1);
         dSamVar = (1 - 1.0/i)*dSamVar + (i + 1)*(mdMean - dTmp)*(mdMean - dTmp);
      }
      mdSamStdDev = Math.sqrt(dSamVar);
      dPopStdDev = Math.sqrt((nNoOfData-1)*dSamVar/nNoOfData);
      mdMin = madData[0];
      mdMax = madData[nNoOfData - 1];

      if(nNoOfData % 2 == 0)
      {
         mdMed = (madData[nNoOfData/2 - 1] + madData[nNoOfData/2])/2;
         if(nNoOfData % 4 == 0)
         {
            mdQ1 = (madData[nNoOfData/4 - 1] + madData[nNoOfData/4])/2;
            mdQ3 = (madData[3*nNoOfData/4 - 1] + madData[3*nNoOfData/4])/2;
         }
         else
         {
            mdQ1 = madData[nNoOfData/4];
            mdQ3 = madData[nNoOfData - nNoOfData/4 - 1];
         }
      }
      else
      {
         mdMed = madData[nNoOfData/2];
         int nBase = (nNoOfData - 1)/2 + 1;
         if(nBase % 2 == 0)
         {
            mdQ1 = (madData[nBase/2 - 1] + madData[nBase/2])/2;
            mdQ3 = (madData[nNoOfData - nBase/2 - 1] + madData[nNoOfData - nBase/2])/2;
         }
         else
         {
            mdQ1 = madData[nBase/2];
            mdQ3 = madData[nNoOfData - nBase/2 - 1];
         }
      }

      txtMean.setText(Double.toString(mdMean));
      txtSum.setText(Double.toString(dSum));
      txtSamStdDev.setText(Double.toString(mdSamStdDev));
      txtPopStdDev.setText(Double.toString(dPopStdDev));
      txtNumber.setText(Integer.toString(nNoOfData));
      txtMin.setText(Double.toString(mdMin));
      txtQ1.setText(Double.toString(mdQ1));
      txtMed.setText(Double.toString(mdMed));
      txtQ3.setText(Double.toString(mdQ3));
      txtMax.setText(Double.toString(mdMax));
      calcConfMean();
      testHypMean();
      calcConfStdDev();
      testHypStdDev();
   }

   private /* protected */ void calcConfMean()
   {
      if(madData.length < 2)
      {
         txtConfMean.setText("");
         return;
      }
      String sConf = choConf.getSelectedItem();
      sConf = sConf.substring(0, sConf.length()-1);
      double dAlpha = (100 - Double.valueOf(sConf).doubleValue())/100;
      double dTmp;
      if(chkLower.getState())
      {
         dTmp = tStats(dAlpha, madData.length - 1)*mdSamStdDev/Math.sqrt(madData.length);
         txtConfMean.setText("(-INF, " + Double.toString(mdMean + dTmp) + ")");
      }
      else if(chkMiddle.getState())
      {
         dTmp = tStats(dAlpha/2, madData.length - 1)*mdSamStdDev/Math.sqrt(madData.length);
         txtConfMean.setText("(" + Double.toString(mdMean - dTmp) + ", " + Double.toString(mdMean + dTmp) + ")");
      }
      else if(chkUpper.getState())
      {
         dTmp = tStats(dAlpha, madData.length - 1)*mdSamStdDev/Math.sqrt(madData.length);
         txtConfMean.setText("(" + Double.toString(mdMean - dTmp) + ", +INF)");
      }
   }

   private /* protected */ void testHypMean()
   {
      if(madData.length < 2 || txtHypMean.getText().length() == 0)
      {
         txtPValueMean.setText("");
         return;
      }

      double dMu0 = Double.valueOf(txtHypMean.getText()).doubleValue();
      double dT = Math.sqrt(madData.length)*(mdMean - dMu0)/mdSamStdDev;
      double dPValue = 0.0;
      if(chkLower.getState())
         dPValue = tDistr(madData.length - 1, -dT);
      else if(chkMiddle.getState())
         dPValue = 2*(1 - tDistr(madData.length - 1, Math.abs(dT)));
      else if(chkUpper.getState())
         dPValue = tDistr(madData.length - 1, dT);
      txtPValueMean.setText(Double.toString(dPValue));     
   }

   private /* protected */ void calcConfStdDev()
   {
      if(madData.length < 2)
      {
         txtConfStdDev.setText("");
         return;
      }
      String sConf = choConf.getSelectedItem();
      sConf = sConf.substring(0, sConf.length()-1);
      double dAlpha = (100 - Double.valueOf(sConf).doubleValue())/100;
      double dTmp = (madData.length - 1)*mdSamStdDev*mdSamStdDev;
      double dLower;
      double dUpper;
      if(chkLower.getState())
      {
            dUpper = Math.sqrt(dTmp/chi2Stats(1 - dAlpha, madData.length - 1));
            txtConfStdDev.setText("(0, " + Double.toString(dUpper) + ")");
      }
      else if(chkMiddle.getState())
      {
            dLower = Math.sqrt(dTmp/chi2Stats(dAlpha/2, madData.length - 1));
            dUpper = Math.sqrt(dTmp/chi2Stats(1 - dAlpha/2, madData.length - 1));
            txtConfStdDev.setText("(" + Double.toString(dLower) + ", " + Double.toString(dUpper) + ")");
      }
      else if(chkUpper.getState())
      {
            dLower = Math.sqrt(dTmp/chi2Stats(dAlpha, madData.length - 1));
            txtConfStdDev.setText("(" + Double.toString(dLower) + ", +INF)");
      }
   }

   private /* protected */ void testHypStdDev()
   {
      if(madData.length < 2 || txtHypStdDev.getText().length() == 0)
      {
         txtPValueStdDev.setText("");
         return;
      }

      double dS0 = Double.valueOf(txtHypStdDev.getText()).doubleValue();
      double dC = (madData.length - 1)*mdSamStdDev*mdSamStdDev/(dS0*dS0);
      double dTmp = chi2Distr(madData.length - 1, dC);
      double dPValue = 0.0;
      if(chkLower.getState())
         dPValue = 1 - dTmp;
      else if(chkMiddle.getState())
         dPValue = 2*Math.min(dTmp, 1 - dTmp);
      else if(chkUpper.getState())
         dPValue = dTmp;
      txtPValueStdDev.setText(Double.toString(dPValue));     
   }

   public void run()
   {
// System.out.println("Start " + dataThread);
      int nCount = countComponents();
      Component[] components = getComponents();
      for(int i = 0; i < nCount; ++i)
      {
         components[i].disable();
      }
      btnStop.enable();
      switch(mnRunAction)
      {
         case 0:
            simulate();
            break;
         case 1:
            readFromURL();
            break;
         case 2:
            printData2();
            break;
         case 3:
            printStemLeaf2();
            break;
      }
      for(int i = 0; i < nCount; ++i)
      {
         components[i].enable();
      }
      btnStop.disable();
      boolean bText = chkSL.getState() || chkData.getState();
      chkRel.enable(chkPMF.getState() || chkDF.getState());
      chkTheo.enable(chkCDF.getState() || chkDF.getState());
      btnLess.enable(!bText && madData.length >= 2);
      btnGreater.enable(!bText && madData.length >= 2);
      if(mnRunAction == 0 || mnRunAction == 1)
         dataThread = null;
   }

   private /* protected */ void simulate()
   {
      double dZ, dX;
      int nNoOfData = Integer.valueOf(txtSimNo.getText()).intValue();
      double dMean = Double.valueOf(txtSimMean.getText()).doubleValue();
      double dStdDev = Double.valueOf(txtSimStdDev.getText()).doubleValue();

      lstData.clear();
      calcStatistics();
      refreshScope();
      Random r = new Random();
      for(int i = 1; i <= nNoOfData && dataThread != null; ++i)
      {
         dZ = r.nextGaussian();
         dX = dMean + dStdDev*dZ;
         lstData.addItem(Double.toString(dX));
         if(i%100 == 0)
         {
            txtNumber.setText(Integer.toString(i));
            dataThread.yield();
         }
      }
      lstData.addItem("");
      lstData.select(lstData.countItems()-1);
      lstData.makeVisible(lstData.countItems()-1);
      calcStatistics();
      refreshScope();
   }

   protected void readFromURL()
   {
      URL urlData;
      int nCol;
      DataInputStream disData;
      StringTokenizer st = new StringTokenizer(txtData.getText(), ",; \t\n\r");
      String sLocation = "";
      try 
      {
         sLocation = st.nextToken();
         try {
            nCol = Integer.parseInt(st.nextToken());
         } catch(Exception exc) {
            nCol = 1;
         }
         if(mbApp || !bURLCGI)
            urlData = new URL(sLocation);
         else
            urlData = new URL(getCodeBase() + "cgi-bin/nph-rover.cgi?URL="
               + sLocation);
         disData = new DataInputStream(urlData.openStream());
         lstData.clear();
         calcStatistics();
         refreshScope();
         String sLine = "";
         boolean bMoreLines = true;
         try {
            sLine = disData.readLine();
         } catch(IOException exc) {
            bMoreLines = false;
         }
         int nNoOfData = 0;
         String sNumber = "";
         while(bMoreLines && dataThread != null)
         {
            try {
               StringTokenizer stCols = new StringTokenizer(sLine, " \t");
               for(int i = 1; i <= nCol; ++i)
                  sNumber = stCols.nextToken();
               Double.valueOf(sNumber);
               lstData.addItem(sNumber);
               ++nNoOfData;
               if(nNoOfData % 10 == 0)
               {
                  txtNumber.setText(Integer.toString(nNoOfData));
                  dataThread.yield();
               }
               sLine = disData.readLine();
            } catch(Exception exc) {
               bMoreLines = false;
            }
         }
         disData.close();
      } catch(MalformedURLException ex) {
         System.err.println("MalformedURLException: " + ex);
      } catch(IOException ex) {
         System.err.println("IOException: " + ex);
      }
      lstData.addItem("");
      lstData.select(lstData.countItems()-1);
      lstData.makeVisible(lstData.countItems()-1);
      calcStatistics();
      refreshScope();
   }

   private /* protected */ void printData()
   {
      if(dataThread == null) 
      {
         dataThread = new Thread(this, "printData");
         mnRunAction = 2;
         dataThread.start();
      }
      else
         printData2();
   }

   private /* protected */ void printData2()
   {
// System.out.println("printData2 " + dataThread);
      if(madData.length < 1)
      {
         txtStemLeaf.setText("");
         return;
      }
      txtStemLeaf.setText(lstData.getItem(0));
      for(int i = 1; i < lstData.countItems()-1 && dataThread != null; ++i) 
         txtStemLeaf.appendText("\n" + lstData.getItem(i));
      if(mnRunAction == 2)
         dataThread = null;
   }

   private /* protected */ void printStemLeaf()
   {
      if(dataThread == null) 
      {
         dataThread = new Thread(this, "Data");
         mnRunAction = 3;
         dataThread.start();
      }
      else
         printStemLeaf2();
   }

   private /* protected */ void printStemLeaf2()
   {
      if(madData.length < 1)
      {
         txtStemLeaf.setText("");
         return;
      }
      int iSlot = (int)mdMin/10;
      int nLen = Integer.toString((int)mdMax/10).length();
      StringBuffer sStemLeaf = new StringBuffer(alignRight(iSlot, nLen) + ' ');
      txtStemLeaf.setText("");
      for(int i = 0; i < madData.length && dataThread != null;) 
      {
         if((int)madData[i]/10 == iSlot)
         {
            sStemLeaf.append((int)madData[i] % 10);
            ++i;
         }
         else
         {
            txtStemLeaf.appendText(sStemLeaf.toString());
            sStemLeaf.setLength(0);
            ++iSlot;
            sStemLeaf.append('\n').append(alignRight(iSlot, nLen)).append(' ');
         }
      }
      txtStemLeaf.appendText(sStemLeaf.toString());
      if(mnRunAction == 3)
         dataThread = null;
   }

   static private /* protected */ String alignRight(int nNum, int nLen)
   {
      StringBuffer s = new StringBuffer(nLen);
      String sNum = new String(Integer.toString(nNum));
      for(int i = 0; i < nLen - sNum.length(); ++i)
         s.append(" ");
      s.append(sNum);
      return s.toString();
   }

// Proprietary since translated from a BASIC-program in a book.
/** The Inverse of the t-Distribution Function */
   private /* protected */ double tStats(double dA, double dN)
   {
      double dC = 2.515517;
      double dD = .802853;
      double dE = .010328;
      double dF = 1.432788;
      double dG = .189269;
      double dH = .001308;
      double dT, dX, dW;
      
      dT = Math.sqrt(-2*Math.log(dA));
      dX = dT - (dC + (dD + dE*dT)*dT)/(1 + (dF + (dG + dH*dT)*dT)*dT);
      dW = dX + (dX + dX*dX*dX)/(4*dN) + (5*Math.pow(dX,5) + 16*dX*dX*dX + 3*dX)/(96*dN*dN) + (3*Math.pow(dX,7) + 19*Math.pow(dX,5) + 17*dX*dX*dX - 15*dX)/(384*dN*dN*dN);

      return dW;
   }

// Start of translated BASIC.
// Proprietary since translated from a BASIC-program in a book.
/** The t-Distribution Function */
   private /* protected */ double tDistr(double dN, double dX)
   {
      double dA, dB, dU, dY, dI;

      dA = dN - 2.0/3.0 + 1.0/(10.0*dN);
      dB = Math.log(1.0 + dX*dX/dN)/(dN - 5.0/6.0);
      if(dX > 0)
         dX = dA*Math.sqrt(dB);
      else
         dX = -dA*Math.sqrt(dB);
      dU = Math.abs(dX);
      if(dU > 4.0)
         if(dX < .0)
            return .0;
         else
            return 1.0;
      dY = dU*dU;
      dI = dU;
      for(double i = 1.0; i <= 40.0; ++i)
      {
         dU = -dU*dY*(2.0*i - 1.0)/(2.0*i*(2.0*i + 1));
         dI = dI + dU;
      }
      dI = dI/Math.sqrt(2.0*Math.PI);
      if(dU > dX)
         return .5 - dI;
      else
         return .5 + dI;
   }

// Proprietary since translated from a BASIC-program in a book.
/** The Inverse of the Chi-Square Distribution Function */
   private /* protected */ double chi2Stats(double A, double N)
   {
      double T = Math.sqrt(-2 * Math.log(A));
      double C = 2.515517;
      double D = .802853;
      double E = .010328;
      double F = 1.432788;
      double G = .189269;
      double H = .001308;
      double X = T - (C + D * T + E * T*T) / (1.0 + F * T + G * T*T + H * T*T*T);
      double W = N * Math.pow(X * Math.sqrt(2.0 / (9.0 * N)) + 1.0 - 2.0 / (9.0 * N), 3);
      double B = 1.0;
      double K = N / 2.0;
      if(K == Math.floor(K))
      {
         for(int i = 1; i <= K - 1; ++i)
            B = B * i;
      }
      else
      {
         for(int i = 1; i <= K - .5; ++i)
            B = B * (K - i);
         B = B * Math.sqrt(Math.PI);
      }
      double M = W / 2;
      double S = (N - 1) / 2;
      D = M - N / 2 + 1.0 / 3.0;
      D = D - .04 / N;
      if(N == 1.0)
         X = D * Math.sqrt(2.0 / M);
      else if(S == M)
         X = D / Math.sqrt(M);
      else
      {
         H = S / M;
         X = (1 - H * H + 2 * H * Math.log(H)) / Math.pow((1 - H), 2);
         X = D * Math.sqrt((1 + X) / M);
      }
      double U = Math.abs(X);
      double R;
      if(U > 4)
         if(X < 0)
            R = .0001;
         else
            R = .9999;
      else
      {
         double Y = U*U;
         double I = U;
         for(int i = 1; i <= 40; ++i)
         {
            U = -U * Y * (2.0 * i - 1.0) / (2.0 * i * (2.0 * i + 1.0));
            I = I + U;
         }
         I = I / Math.sqrt(2 * Math.PI);
         if(X < 0)
            R = .5 - I;
         else
            R = .5 + I;
      }
      R = 1 - A - R;
      B = Math.pow(2, (-N / 2)) * (1/B) * Math.exp(-W / 2) * Math.pow(W, (N / 2 - 1));
      W = W + R / B;
      return W;
   }

// Proprietary since translated from a BASIC-program in a book.
/** The Chi-Square Distribution Function */
   private /* protected */ double chi2Distr(double dN, double dX)
   {
      double dS = (dN - 1)/2;
      double dM = dX/2;
      double dD = dX/2 - dN/2 + 1.0/3.0;
      dD = dD - .04/dN;
      if(dN == 1)
         dX = dD*Math.sqrt(2.0/dM);
      else if(dS == dM)
         dX = dD/Math.sqrt(dM);
      else
      {
         double dH = dS/dM;
         dX = (1 - dH*dH + 2*dH*Math.log(dH))/Math.pow((1 - dH), 2);
         dX = dD*Math.sqrt((1 + dX)/dM);
      }
      double dU = Math.abs(dX);
      if(dU > 4)
         if(dX < 0)
            return 0.0;
         else
            return 1.0;
      double dY = dU*dU;
      double dI = dU;
      for(int i = 1; i <= 40; ++i)
      {
         dU = -dU*dY*(2.0*i - 1.0)/(2.0*i*(2.0*i + 1.0));
         dI = dI + dU;
      }
      dI = dI/Math.sqrt(2.0*Math.PI);
      if(dX < 0)
         return .5 - dI;
      else
         return .5 + dI;
   }
// End of translated BASIC.
   
/** Quicksort setup function. */
   private /* protected */ void quick(double[] item)
   {
      if(item.length <= 1)
         return;
      qs(item, 0, item.length-1);
   }

/** The Quicksort. */
   void qs(double[] item, int left, int right)
   {
      int i, j;
      double x, y;

      i = left; j = right;
      x = item[(left+right)/2];

      do {
         while(item[i]<x && i<right) i++;
         while(x<item[j] && j>left) j--; 

         if(i<=j) {
            y = item[i];
            item[i] = item[j];
            item[j] = y;
            i++; j--;
         }
      } while(i<=j);

      if(left<j) qs(item, left, j);
      if(i<right) qs(item, i, right);
   }
}

class StatCanvas extends Canvas
{
   public int Copyright__C__1996_Mikael_O_Bonnier__Lund__Sweden;
   private /* protected */ Image offImage;
   private /* protected */ Graphics offGraphics;
   private /* protected */ boolean mbTestedSite = false;
   private /* protected */ boolean mbSite = false;
   private /* protected */ double mdXMin, mdXMax, mdDeltaX, mdXScl, mdDeltaY;
   private /* protected */ int mnXTicks;
   protected int mnDiagStyle = 0;
   protected int mnXScl = 0;
   private /* protected */ boolean mbNew = true;

   StatCanvas()
   {
   }

   public void repaint()
   {
      mbNew = true;
      super.repaint();
   }

   public void paint(Graphics g)
   {
      update(g);
   }

   public void update(Graphics g)
   {
      if(mbNew)
      {
         run();
         mbNew = false;
      }
      g.drawImage(offImage, 0, 0, this);
   }

   private void run()
   {

      if(!mbTestedSite)
      {
         Statiscope a = (Statiscope)getParent();

         /*
         if(!a.mbApp && !a.getDocumentBase().toString().substring(0, 30).equals( // <---
            "http://www.df.lth.se.orbin.se/~mikaelb/")) // <---
            mbSite = true;
            */

         mbTestedSite = true;
      }
      if(mbSite)
      {
         return;
      }

      if(offGraphics == null)
      {
         Dimension dim = size();
         offImage = createImage(dim.width, dim.height);
         offGraphics = offImage.getGraphics();
      }
      fillArea();
   }

   private /* protected */ void fillArea()
   {
      Dimension dim = size();

      offGraphics.setColor(Color.green);
      offGraphics.fillRect(0, 0, dim.width, dim.height);
      offGraphics.setColor(Color.black);

      switch(mnDiagStyle)
      {
         case 0:
            cumulDistr();            
            break;
         case 1:
            probMass();
            break;
         case 2:
            density();
            break;
         case 3:
            boxPlot();
            break;
      }
   }

/** Cumulative distribution function */
   private /* protected */ void cumulDistr()
   {
      Statiscope a = (Statiscope)getParent();
      if(a.madData.length < 2)
         return;

      drawScales(a.mdMin, a.mdMax, 1.0);
      Dimension dim = size();

      int x1 = 0, y1 = dim.height - 2;
      int x2, y2;
      for(int i = 0; i < a.madData.length; ++i)
      {
         x2 = (int)((a.madData[i] - mdXMin)*mdDeltaX + 1.5);
         y2 = (int)(dim.height-2 - mdDeltaY/a.madData.length*i + .5);
         offGraphics.drawLine(x1, y1, x2, y2);
         x1 = x2; y1 = y2;
         y2 = (int)(dim.height-2 - mdDeltaY/a.madData.length*(i + 1) + .5);
         offGraphics.drawLine(x1, y1, x2, y2);
         y1 = y2;
      }
      x2 = dim.width; y2 = 0;
      offGraphics.drawLine(x1, y1, x2, y2);
      if(a.chkTheo.getState())
      {
         // offGraphics.setColor(Color.gray);
         double dX, dY;
         x1 = 0;
         dX = -1.0/mdDeltaX + mdXMin;
         dY = normalDistr((dX - a.mdMean)/a.mdSamStdDev);
         y1 = (int)(dim.height-2 - mdDeltaY*dY + .5);
         for(x2 = 1; x2 <= dim.width; ++x2)
         {
            dX = (x2 - 1)/mdDeltaX + mdXMin;
            dY = normalDistr((dX - a.mdMean)/a.mdSamStdDev);
            y2 = (int)(dim.height-2 - mdDeltaY*dY + .5);
            offGraphics.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
         }
      }
   }

   private /* protected */ double normalDistr(double dZ)
   {
      double dY;
      if(dZ >= 0)
         dY = 1.0/(1.0 + Math.exp(-dZ*(1.5976 + 0.070566*dZ*dZ)));
      else
         dY = 1 - 1.0/(1.0 + Math.exp(dZ*(1.5976 + 0.070566*dZ*dZ)));
      return dY;
   }

/** Probability mass function */
   private /* protected */ void probMass()
   {
      Statiscope a = (Statiscope)getParent();
      if(a.madData.length < 2)
         return;

      int nMaxFreq;
      int anFreq[];
      int iFreq;
      nMaxFreq = 1;
      double adUnique[] = new double[a.madData.length];
      anFreq = new int[a.madData.length];
      iFreq = 0;
      adUnique[0] = a.madData[0];
      anFreq[0] = 1;
      for(int i = 1; i < a.madData.length; ++i)
      {
         if(a.madData[i] != adUnique[iFreq])
         {
            if(anFreq[iFreq] > nMaxFreq)
               nMaxFreq = anFreq[iFreq];
            adUnique[++iFreq] = a.madData[i];
            anFreq[iFreq] = 1;
         }
         else
         {
            ++anFreq[iFreq];
         }
      }
      if(anFreq[iFreq] > nMaxFreq)
         nMaxFreq = anFreq[iFreq];
      double dYMax, dYFac;
      if(a.chkRel.getState())
      {
         dYMax = (double)nMaxFreq/a.madData.length;
         dYFac = 1.0/a.madData.length;
      }
      else
      {
         dYMax = nMaxFreq;
         dYFac = 1.0;
      }
      drawScales(a.mdMin, a.mdMax, dYMax);
      Dimension dim = size();
      for(int i = 0; i <= iFreq; ++i)
      {
         offGraphics.fillRect((int)((adUnique[i] - mdXMin)*mdDeltaX + 1.5), (int)(dim.height-2 - mdDeltaY*dYFac*anFreq[i] + .5), 1, (int)(mdDeltaY*dYFac*anFreq[i] + 1.49));
      }
   }

/** Density function */
   private /* protected */ void density()
   {
      Statiscope a = (Statiscope)getParent();
      if(a.madData.length < 2)
         return;
      Dimension dim = size();
      int nMaxFreq = 1;
      int anFreq[];
      int iFreq = 0;
      int nSum = 0;
      drawXScale(a.mdMin, a.mdMax*1.001);
      anFreq = new int[mnXTicks - 1];
      for(int i = 0; i < a.madData.length; ++i)
      {
         while(!(mdXMin + iFreq*mdXScl <= a.madData[i] && a.madData[i] < mdXMin + (iFreq + 1)*mdXScl))
         {
            ++iFreq;
         }
         try {
            ++anFreq[iFreq];
            ++nSum;
            if(anFreq[iFreq] > nMaxFreq)
               nMaxFreq = anFreq[iFreq];
         } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
         }
      }
      double dYMax, dYFac;
      if(a.chkRel.getState())
      {

         dYMax = (double)nMaxFreq/(nSum*mdXScl);
         dYFac = 1.0/(nSum*mdXScl);
      }
      else
      {
         dYMax = nMaxFreq;
         dYFac = 1.0;
      }
      drawYScale(dYMax);
      for(int i = 0; i < mnXTicks-1; ++i)
      {
         offGraphics.fillRect((int)(i*mdXScl*mdDeltaX + 1.5), (int)(dim.height-2 - dYFac*anFreq[i]*mdDeltaY + .5), (int)(mdXScl*mdDeltaX + 1.5), (int)(dYFac*anFreq[i]*mdDeltaY + 1.49));
      }

      if(a.chkTheo.getState())
      {
         offGraphics.setColor(Color.gray);
         double dX, dY;
         int x1, y1, x2, y2;
         if(a.chkRel.getState())
            dYFac = 1.0;
         else
            dYFac = nSum*mdXScl;
         x1 = 0;
         dX = -1.0/mdDeltaX + mdXMin;
         dY = dYFac*normalDensity((dX - a.mdMean)/a.mdSamStdDev)/a.mdSamStdDev;
         y1 = (int)(dim.height-2 - mdDeltaY*dY + .5);
         for(x2 = 1; x2 <= dim.width; ++x2)
         {
            dX = (x2 - 1)/mdDeltaX + mdXMin;
            dY = dYFac*normalDensity((dX - a.mdMean)/a.mdSamStdDev)/a.mdSamStdDev;
            y2 = (int)(dim.height-2 - mdDeltaY*dY + .5);
            offGraphics.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;
         }
      }
   }

   private /* protected */ double normalDensity(double dZ)
   {
       return 1.0/Math.sqrt(2.0*Math.PI)*Math.exp(-dZ*dZ/2.0);
   }

/** Box plot */
   private /* protected */ void boxPlot()
   {
      Statiscope a = (Statiscope)getParent();
      if(a.madData.length < 2)
         return;

      drawXScale(a.mdMin, a.mdMax);
      Dimension dim = size();
      int nMin, nQ1, nMed, nQ3, nMax;
      int y0, y1, y2, y3, y4;
      nMin = (int)((a.mdMin - mdXMin)*mdDeltaX + 1.5);
      nQ1 = (int)((a.mdQ1 - mdXMin)*mdDeltaX + 1.5);
      nMed = (int)((a.mdMed - mdXMin)*mdDeltaX + 1.5);
      nQ3 = (int)((a.mdQ3 - mdXMin)*mdDeltaX + 1.5);
      nMax = (int)((a.mdMax - mdXMin)*mdDeltaX + 1.5);
      y0 = dim.height/2;
      y1 = dim.height/2 - 2;
      y2 = dim.height/2 + 2;
      y3 = dim.height/2 - 10;
      y4 = dim.height/2 + 10;
      offGraphics.drawLine(nMin, y1, nMin, y2);
      offGraphics.drawLine(nMin, y0, nQ1, y0);
      offGraphics.drawLine(nQ1, y3, nQ1, y4);
      offGraphics.drawLine(nQ1, y3, nQ3, y3);
      offGraphics.drawLine(nQ1, y4, nQ3, y4);
      offGraphics.drawLine(nMed, y3, nMed, y4);
      offGraphics.drawLine(nQ3, y3, nQ3, y4);
      offGraphics.drawLine(nQ3, y0, nMax, y0);
      offGraphics.drawLine(nMax, y1, nMax, y2);
   }

   private /* protected */ void drawScales(double dXDataMin, double dXDataMax, double dYDataMax)
   {
      drawXScale(dXDataMin, dXDataMax);
      drawYScale(dYDataMax);
   }

   private /* protected */ void drawXScale(double dXDataMin, double dXDataMax)
   {
      Statiscope a = (Statiscope)getParent();
      Dimension dim = size();
      double dXPix = 10*(dXDataMax - dXDataMin)/(dim.width - 3);
      int n = (int)Math.floor(Math.log(dXPix)/Math.log(10));
      int nXMant;
      if(Math.pow(10, n) > dXPix)
         nXMant = 1;
      else if(2*Math.pow(10, n) > dXPix)
         nXMant = 2;
      else if(5*Math.pow(10, n) > dXPix)
         nXMant = 5;
      else
         nXMant = 10;
      if(mnXScl > 0)
          for(int i = 0; i < mnXScl; ++i)
             switch(nXMant)
             {
                case 1:
                   nXMant = 2;
                   break;
                case 2:
                   nXMant = 5;
                   break;
                case 5:
                   nXMant = 1;
                   ++n;
                   break;
                default:
                   nXMant = 2;
                   ++n;
                   break;
              }
      else if(mnXScl < 0)
          for(int i = 0; i < -mnXScl; ++i)
             switch(nXMant)
             {
                case 1:
                   nXMant = 5;
                   --n;
                   break;
                case 2:
                   nXMant = 1;
                   break;
                case 5:
                   nXMant = 2;
                   break;
                default:
                   nXMant = 5;
                   break;
              }
      mdXScl = nXMant*Math.pow(10, n);
      mdXMin = Math.floor(dXDataMin/mdXScl)*mdXScl;
      mdXMax = Math.ceil(dXDataMax/mdXScl)*mdXScl;
      mdDeltaX = (dim.width - 3)/(mdXMax - mdXMin);
      mnXTicks = (int)((mdXMax - mdXMin)/mdXScl) + 1;
      for(int i = 0; i < mnXTicks; ++i)
      {
         offGraphics.fillRect((int)(mdXScl*i*mdDeltaX + 1.5), dim.height - 1, 1, 1);
      }
      offGraphics.fillRect(0, dim.height - 2, dim.width, 1);
      a.lblXMin.setText(Double.toString(mdXMin));
      a.lblXMax.setText(Double.toString(mdXMax));
   }

   private /* protected */ void drawYScale(double dYDataMax)
   {
      Statiscope a = (Statiscope)getParent();
      Dimension dim = size();
      double dYPix = 5*(double)dYDataMax/(dim.height - 2);
      int n = (int)Math.floor(Math.log(dYPix)/Math.log(10));
      double dYScl;
      if(Math.pow(10, n) > dYPix)
         dYScl = Math.pow(10, n);
      else if(2*Math.pow(10, n) > dYPix)
         dYScl = 2*Math.pow(10, n);
      else if(5*Math.pow(10, n) > dYPix)
         dYScl = 5*Math.pow(10, n);
      else
         dYScl = 10*Math.pow(10, n);
      double dYMax = Math.ceil(dYDataMax/dYScl)*dYScl;
      mdDeltaY = (dim.height - 2)/dYMax;
      int nYTicks = (int)(dYMax/dYScl + .5) + 1;
      int nLeft;
      if(mdXMin <= 0  && 0 <= mdXMax)
      {
         nLeft = (int)(-mdXMin*mdDeltaX + .5);
         offGraphics.fillRect(nLeft + 1, 0, 1, dim.height);
      }
      else if(mdXMin > 0)
         nLeft = 0;
      else
         nLeft = dim.width - 1;
      for(int i = 0; i < nYTicks; ++i)
      {
         offGraphics.fillRect(nLeft, (int)(dYScl*i*mdDeltaY + .5), 1, 1);
      }
      a.lblYMin.setText("0");
      a.lblYMax.setText(Double.toString(dYMax));
   }
}

class StatiscopeApp extends Frame
{
   public int Copyright__C__1997_Mikael_O_Bonnier__Lund__Sweden;
   private /* protected */ Statiscope pnlStatiscope;
   private /* protected */ MenuItem mnuNew;
   private /* protected */ MenuItem mnuOpen;
   private /* protected */ MenuItem mnuSave;
   private /* protected */ MenuItem mnuSaveAs;
   private /* protected */ MenuItem mnuExit;
   private /* protected */ MenuItem mnuHelp;
   private /* protected */ MenuItem mnuAbout;
   private /* protected */ Button btnAboutOK;
   private /* protected */ String sFilename = new String();

   public static void main(String args[]) 
   {
      Hashtable parameters = new Hashtable();
      int i = 0;
      String arg;
      while(i < args.length && args[i].startsWith("-"))
      {
         arg = args[i++];
         if(i < args.length)
            parameters.put(arg.substring(1).toLowerCase(), args[i++]);
         else
         {
            System.err.println(arg + " requires a value");
            return;
         }
      }
      StatiscopeApp frm = new StatiscopeApp("Statiscope");
      frm.pnlStatiscope = new Statiscope();
      frm.add("Center", frm.pnlStatiscope);
      frm.pack();
      frm.pnlStatiscope.parameters = parameters;
      frm.pnlStatiscope.mbApp = true;
      frm.pnlStatiscope.init();
      frm.show();
      Insets ins = frm.insets();
      frm.resize(575 + ins.left + ins.right, 435 + ins.top + ins.top + ins.bottom);
   }

   StatiscopeApp(String s)
   {
      super(s);
      MenuBar mnuBar = new MenuBar();
      Menu mnu = new Menu("File");
      mnuNew = new MenuItem("New");
      mnu.add(mnuNew);
      mnuOpen = new MenuItem("Open...");
      mnu.add(mnuOpen);
      mnuSave = new MenuItem("Save");
      mnu.add(mnuSave);
      mnuSaveAs = new MenuItem("Save As...");
      mnu.add(mnuSaveAs);
      mnu.addSeparator();
      mnuExit = new MenuItem("Exit");
      mnu.add(mnuExit);
      mnuBar.add(mnu);
      mnu = new Menu("Edit");
      mnu.disable();
      mnuBar.add(mnu);
      mnu = new Menu("Help");
      mnuBar.setHelpMenu(mnu);
      mnuHelp = new MenuItem("Help");
      mnu.add(mnuHelp);
      mnu.addSeparator();
      mnuAbout = new MenuItem("About Statiscope");
      mnu.add(mnuAbout);
      setMenuBar(mnuBar);
   }

   public boolean handleEvent(Event evt) 
   {
      switch(evt.id)
      {
         case Event.WINDOW_DESTROY:
            if(evt.target == this)
            {
               System.exit(0);
               return true;
            }
            else
               ((Window)evt.target).dispose();
            break;
         case Event.ACTION_EVENT:
            if(evt.target == btnAboutOK)
               ((Window)btnAboutOK.getParent().getParent()).dispose();
            else if(evt.target instanceof MenuItem)
            {
               if(evt.target == mnuNew)
               {
                  setTitle("Statiscope");
                  sFilename = "";
                  pnlStatiscope.clearData();
               }
               else if(evt.target == mnuOpen)
               {
                  FileDialog dlg = new FileDialog(this, "Open", FileDialog.LOAD);
                  dlg.show();
                  if(dlg.getFile() != null)
                  {
                     sFilename = dlg.getDirectory() + dlg.getFile();
                     if(sFilename.substring(sFilename.length()-4).equals(".*.*"))
                        sFilename = sFilename.substring(0, sFilename.length()-4);
                     pnlStatiscope.txtData.setText("file://"
                        + sFilename.replace(System.getProperty("file.separator").charAt(0),
                        '/').replace(':', '|'));
                     if(pnlStatiscope.dataThread == null) 
                     {
                        pnlStatiscope.dataThread = new Thread(pnlStatiscope, "Data");
                        pnlStatiscope.mnRunAction = 1;
                        pnlStatiscope.dataThread.start();
                     }
                     setTitle(sFilename.substring(sFilename.lastIndexOf(System.getProperty("file.separator"))+1) + " - Statiscope");
                  }
               }
               else if(evt.target == mnuSaveAs || evt.target == mnuSave && sFilename.length() == 0)
               {
                  FileDialog dlg = new FileDialog(this, "Save As", FileDialog.SAVE);
                  dlg.show();
                  if(dlg.getFile() != null)
                  {
                     sFilename = dlg.getDirectory() + dlg.getFile();
                     if(sFilename.substring(sFilename.length()-4).equals(".*.*"))
                        sFilename = sFilename.substring(0, sFilename.length()-4);
                     save(sFilename);
                  }
               }
               else if(evt.target == mnuSave && sFilename.length() != 0)
               {
                  save(sFilename);
               }
               else if(evt.target == mnuExit)
               {
                  System.exit(0);
                  return true;
               }
               else if(evt.target == mnuHelp)
               {
                  try
                  {
                     Runtime.getRuntime().exec(System.getProperty("user.dir") + System.getProperty("file.separator") + "help.bat");
                  } catch(IOException ex) 
                  {
                     System.err.println("IOException: " + ex);
                  }
               }
               else if(evt.target == mnuAbout)
               {
                  Dialog dlg = new Dialog(this, "About Statiscope", true);
                  dlg.setResizable(false);
                  Panel pnl = new Panel();
                  dlg.add("Center", pnl);
                  dlg.pack();
                  pnl.setBackground(pnlStatiscope.getBackground());
                  pnl.setForeground(pnlStatiscope.getForeground());
                  pnl.setLayout(null);
                  TextArea txt = new TextArea(pnlStatiscope.getAppletInfo());
                  txt.setEditable(false);
                  pnl.add(txt);
                  txt.reshape(0, 0, 300, 180);
                  btnAboutOK = new Button("OK");
                  pnl.add(btnAboutOK);
                  btnAboutOK.reshape(260, 181, 40, 20);
                  dlg.show();
                  Insets ins = dlg.insets();
                  dlg.resize(300 + ins.left + ins.right, 201 + ins.top + ins.bottom);
                  dlg.validate();
               }
            }
            break;
      }
      return super.handleEvent(evt);
   }

   private /* protected */ void save(String sFilename)
   {
      try
      {
         FileOutputStream fOStream = new FileOutputStream(sFilename);
         PrintStream pOStream = new PrintStream(fOStream);
         if(pnlStatiscope.madData.length >= 1)
         {
            pOStream.print(pnlStatiscope.lstData.getItem(0));
            String sNL = System.getProperty("line.separator");
            for(int i = 1; i < pnlStatiscope.lstData.countItems()-1; ++i) 
               pOStream.print(sNL + pnlStatiscope.lstData.getItem(i));
         }
         pOStream.flush();
         pOStream.close();
         fOStream.close();
         setTitle(sFilename.substring(sFilename.lastIndexOf(System.getProperty("file.separator"))+1) + " - Statiscope");
      } catch(IOException ex) 
      {
         System.err.println("IOException: " + ex);
      }
   }
}
