package psb.kdv;

import common.FileNames;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here
        Properties ini = new Properties();
        ini.load(new FileInputStream((new File("settings.ini"))));

        String baseDir = ini.getProperty("BaseFolder");
        String mwb = ini.getProperty("MyWarehosReports");
        String barcodes = ini.getProperty("GoodsBarcodeReports");
        String counts = ini.getProperty("GoodsCountReports");
        String finals = ini.getProperty("ConsolidatedReports");

        Path mwbPath = Paths.get(baseDir, mwb);
        Path barcodesPath = Paths.get(baseDir, barcodes);
        Path countsPath = Paths.get(baseDir, counts);
        Path finalsDir = Paths.get(baseDir, finals);

        File fmwb = new File(String.valueOf(mwbPath.toAbsolutePath()));
        List<String> mwbList = new ArrayList<String>();
        for(File file: new File(String.valueOf(mwbPath.toAbsolutePath())).listFiles()){
            mwbList.add(file.toString());
        }
        Collections.reverse(mwbList);
        System.out.println(mwbList.get(0));

        List<String> barList = Arrays.asList(new File(String.valueOf(barcodesPath.toAbsolutePath())).list());
        Collections.reverse(barList);
        System.out.println(barList.get(0));
        List<String> countsList = Arrays.asList(new File(String.valueOf(countsPath.toAbsolutePath())).list());
        Collections.reverse(countsList);
        System.out.println(countsList.get(0));


    }
}
