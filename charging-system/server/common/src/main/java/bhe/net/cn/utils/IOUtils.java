package bhe.net.cn.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum IOUtils {
    ;
    static Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    public static void main(String[] args) throws Exception {
        System.out.printf("%s%n", getClassPath(IOUtils.class, 13));
    }

    public static String getClassPath(Class<?> clazz, int cutTheChild) {
        String path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath().toString();
        path = path.replace("\\", "/");
        path += clazz.getPackage().getName().replace(".", "/");
        path = path.substring(path.indexOf("/") + 1);
        for (int i = 0; i < cutTheChild; i++) {
            int first = path.indexOf("/");
            int last = path.lastIndexOf("/");
            if (last > first) {
                path = path.substring(0, last);
            } else {
                break;
            }
        }
        return path;
    }

    public static String readerRead(String path) throws Exception {
        StringBuilder result = new StringBuilder();
        FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String temp;
        while ((temp = br.readLine()) != null) {
            result.append(temp + "\n");
        }
        br.close();
        fr.close();
        return result.toString();
    }

    public static String streamRead(String path) throws Exception {
        StringBuilder result = new StringBuilder();
        FileInputStream fis = new FileInputStream(path);
        byte[] buffer = new byte[fis.available()];
        while (fis.read(buffer) > 0) {
            result.append(new String(buffer));
        }
        fis.close();
        return result.toString();
    }

    public static void iterFolder(String path) {
        recursion(new File(path).listFiles());
    }

    private static void recursion(File[] files) {
        for (File file : files) {
            System.out.printf("%-10s%n", file.getName());
            if (file.isDirectory()) {
                recursion(file.listFiles());
            }
        }
    }

    public static void writerWrite(String path, String content) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
        writer.append(content);
        writer.close();
    }

    public static void printWrite(String path, String content) throws Exception {
        FileWriter fileWriter = new FileWriter(path, true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(content);
        printWriter.close();
    }

    public static void streamWrite(String path, String content) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(path, true);
        outputStream.write(content.getBytes());
        outputStream.close();
    }

    public static Map<String, String> propToMap(String pathName) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream(pathName));
        @SuppressWarnings({ "unchecked", "rawtypes" })
        Map<String, String> map = new HashMap(prop);
        return map;
    }

    public static void writeExcel(String pathName, String fileType, String sheetName, List<List<String>> data) throws Exception {
        Workbook wb;
        if (fileType.equals("xls")) {
            wb = new HSSFWorkbook();
        } else if (fileType.equals("xlsx")) {
            wb = new XSSFWorkbook();
        } else {
            throw new Exception("wrong file format");
        }
        Sheet sheet = wb.createSheet(sheetName);
        int rownum = 0;
        for (List<String> data_row : data) {
            Row row = sheet.createRow(rownum);
            int colnum = 0;
            for (String data_cell : data_row) {
                row.createCell(colnum).setCellValue(data_cell);
                colnum++;
            }
            rownum++;
        }
        try (OutputStream fileOut = new FileOutputStream(pathName)) {
            wb.write(fileOut);
        }
        wb.close();
    }

    public static List<List<String>> readExcel(String pathName, String sheetName) throws EncryptedDocumentException, InvalidFormatException, IOException {
        Workbook wb = WorkbookFactory.create(new File(pathName));
        Sheet sheet = wb.getSheet(sheetName);
        List<List<String>> data = new ArrayList<List<String>>();
        for (Row row : sheet) {
            List<String> data_row = new ArrayList<String>();
            for (Cell data_cell : row) {
                switch (data_cell.getCellTypeEnum()) {
                case STRING:
                    data_row.add(data_cell.getRichStringCellValue().getString());
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(data_cell)) {
                        data_row.add(data_cell.getDateCellValue().toString());
                    } else {
                        data_row.add(String.valueOf(data_cell.getNumericCellValue()));
                    }
                    break;
                case BOOLEAN:
                    data_row.add(String.valueOf(data_cell.getBooleanCellValue()));
                    break;
                case FORMULA:
                    data_row.add(data_cell.getCellFormula());
                    break;
                case BLANK:
                    data_row.add("");
                    break;
                default:
                    data_row.add("");
                }
            }
            data.add(data_row);
        }
        return data;
    }

}
