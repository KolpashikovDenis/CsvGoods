package psb.kdv;

import common.FileNames;
import jdk.internal.util.xml.impl.Input;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        Properties ini = new Properties();
        ini.load(new FileInputStream((new File("settings.ini"))));

        String baseDir = ini.getProperty("BaseFolder");
        String mwb = ini.getProperty("MyWarehouseReports");
        String barcodes = ini.getProperty("GoodsBarcodeReports");
        String counts = ini.getProperty("GoodsCountReports");
        String finals = ini.getProperty("ConsolidatedReports");

        Path mwbPath = Paths.get(baseDir, mwb);
        Path barcodesPath = Paths.get(baseDir, barcodes);
        Path countsPath = Paths.get(baseDir, counts);
        Path finalsDir = Paths.get(baseDir, finals);

        List<String> mwbList = new ArrayList<String> ();
        File[] fArray = new File(String.valueOf(mwbPath)).listFiles();
        for(File file: fArray){
            mwbList.add(file.toString());
        }
        Collections.reverse(mwbList);

        List<String> barList = new ArrayList<String> ();
        fArray = new File(String.valueOf(barcodesPath)).listFiles();
        for(File file: fArray){
            barList.add(file.toString());
        }
        Collections.reverse(barList);

        List<String> countsList = new ArrayList<String> ();
        fArray = new File(String.valueOf(countsPath)).listFiles();
        for(File file: fArray){
            countsList.add(file.toString());
        }
        Collections.reverse(countsList);

        System.out.println(mwbList.get(0));
//        String header = "Articul;Name;Leftover;CostPrice;Brand;Barcode;ArticulWB;LeftoverWBMH;CostAmount\n";
        String header1= "Articul;Name;Leftover;CostPrice;CostAmount;Brand;ArticulWB;LeftoverWBWH;Date\n";

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String strDate = dateFormat.format(date);
        StringBuilder stringBuilder = new StringBuilder(header1);
        StringBuilder sbb = new StringBuilder();
        StringBuilder sbbb = new StringBuilder();
        boolean b = false;
        boolean bb = false;

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(mwbList.get(0))), "windows-1251"));
        String line = br.readLine();
        while((line = br.readLine()) != null){
            String s[] = line.split(";");
            String Articul = s[0];
            String Name = s[1].trim().length() > 0 ? s[1].trim() : "Нет данных";
            double Leftover = Double.parseDouble(s[2].trim());
            double CostPrice = Double.parseDouble(s[3].trim());
            double CostAmount = Double.parseDouble(s[4].trim());
            BufferedReader brr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(barList.get(0))), "windows-1251"));
            String lline = brr.readLine();
            while((lline = brr.readLine()) != null){
                String ss[] = lline.split(";");
                String Brand = ss[0].trim();
                String ArticulWB = ss[1].trim();
                String NomenclatureWBMW = ss[2].trim();
                String Barcode = ss[3].trim().length() > 0 ? ss[3].trim() : "Нет данных";
                if(Articul.equals(NomenclatureWBMW)){
                    b = true;
                    sbb.append(Articul).append(";").append(Name).append(";").append(Leftover).append(";").append(CostPrice).append(";");
                    BufferedReader brrr = new BufferedReader(new InputStreamReader(new FileInputStream(new File(countsList.get(0))), "windows-1251"));
                    String llline = brrr.readLine();
                    while ((llline = brrr.readLine()) != null) {
                        String sss[] = llline.split(";");
                        String BBrand = sss[0].trim();
                        String AArticulWB = sss[1].trim();
                        double LeftoverWBWH = Double.parseDouble(sss[2].trim());
                        if (ArticulWB.equals(AArticulWB) && Brand.equals(BBrand)) {
                            bb = true;
                            CostAmount = (Leftover + LeftoverWBWH) * CostPrice;
                            sbbb.append(CostAmount).append(";").append(BBrand).append(";").append(AArticulWB).append(";")
                                    .append(LeftoverWBWH).append(strDate).append("\n");
//
//                            stringBuilder.append(Articul).append(";").append(Name).append(";").append(Leftover).append(";")
//                                    .append(CostPrice).append(";").append(CostAmount).append(";").append(BBrand).append(";")
//                                    .append(AArticulWB).append(";").append(LeftoverWBWH).append(";").append(strDate).append("\n");
                            bb = true;
                        }
                    }
                    if(!bb){
                        sbbb.append("Нет данных;Нет данных;Нет данных;Нет данных;").append(strDate).append("\n");
                    }
                    sbb.append(sbbb);
                    sbbb.setLength(0);
                    bb = false;
                }
            }
            if(!b){
                sbb.append(Articul).append(";Нет данных;Нет данных;Нет данных;Нет данных;Нет данных;Нет данных;Нет данных;")
                        .append(strDate).append("\n");
            }
            stringBuilder.append(sbb);
            sbb.setLength(0);
            b = false;
        }
        System.out.println(stringBuilder.toString());
        date = Calendar.getInstance().getTime();
        dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        strDate = dateFormat.format(date);
        File fout = new File(String.valueOf(finalsDir)+"\\Consolidated"+strDate+".csv");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout), "windows-1251"));
        bw.write(stringBuilder.toString());
        bw.flush();
        bw.close();
    }
}
