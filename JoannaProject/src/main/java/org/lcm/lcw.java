package org.lcm;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class lcw {

    private static final boolean ASC = true;
    private static final boolean DESC = false;

    public static void main(String[] args) throws Exception {
        Scanner fileText = new Scanner(System.in);

        System.out.println("Podaj pelna sciezke do pliku:");
        String openedFile = fileText.nextLine();

        FileReader fr = null;
        try {
            fr = new FileReader(openedFile);
        } catch (FileNotFoundException e) {
            System.out.println("Niestety najwidoczniej pliku nie ma w domu");
            return;
        }

        double fileSizeInBytes = openedFile.length();
        double fileSizeInKB = fileSizeInBytes / 1024;
        double fileSizeInMB = fileSizeInKB / 1024;

        if (fileSizeInMB <= 5) {
            System.out.println(fr);
        } else {
            System.out.println("Za gruby, nie przejdzie przez wrota");
        }

        BufferedReader br = null;

        try {
            String line;

            br = new BufferedReader(fr);
            Map<String, Integer> howManyWords = new HashMap<>();

            String word = null;
            while ((line = br.readLine()) != null) {

                Scanner sc = new Scanner(line);
                while (sc.hasNext()) {
                    word = sc.next();
                    if (!howManyWords.containsKey(word)) {
                        howManyWords.put(word, 1);
                    } else {
                        howManyWords.put(word, howManyWords.get(word) + 1);
                    }
                }
            }

            System.out.println("Przed posortowaniem......");
            printMap(howManyWords);

            System.out.println("Po posortowaniu od najmniejszej......");
            Map<String, Integer> sortedMapAsc = sortByValue(howManyWords, ASC);
            printMap(sortedMapAsc);

            System.out.println("Po posortowaniu od najwiekszej......");
            Map<String, Integer> sortedMapDesc = sortByValue(howManyWords, DESC);
            printMap(sortedMapDesc);

            numberOfWords(sortedMapAsc);

        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

    private static Map<String, Integer> sortByValue(Map<String, Integer> number, final boolean order) {
        List<Entry<String, Integer>> list = new LinkedList<>(number.entrySet());

        // Sortowanie listy słów w oparciu o wartość liczbową
        list.sort((word, numberValue) -> {
            if (order) {
                return word.getValue().compareTo(numberValue.getValue()) == 0
                        ? word.getKey().compareTo(numberValue.getKey())
                        : word.getValue().compareTo(numberValue.getValue());
            }
            return numberValue.getValue().compareTo(word.getValue()) == 0
                    ? numberValue.getKey().compareTo(word.getKey())
                    : numberValue.getValue().compareTo(word.getValue());
        });
        return list.stream().collect(
                Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    private static void numberOfWords(Map<String, Integer> sortedMapAsc) throws Exception {

        XSSFWorkbook workbook = new XSSFWorkbook();

        XSSFSheet spreadsheet = workbook.createSheet(" Liczba slow ");

        XSSFRow row;

        Map<String, Object[]> wordNumber = new TreeMap<>();

        wordNumber.put("1", new Object[]{"Slowo", "Ilosc"});
        int idx = 2;
        for (Entry<String, Integer> entry : sortedMapAsc.entrySet()) {
            wordNumber.put(idx++ + "", new Object[]{
                    entry.getKey() + "",
                    entry.getValue() + ""});
        }

        Set<String> keyId = wordNumber.keySet();

        int rowId = 0;

        for (String key : keyId) {

            row = spreadsheet.createRow(rowId++);
            Object[] objectArr = wordNumber.get(key);
            int cellId = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue((String) obj);
            }
        }

        FileOutputStream out = new FileOutputStream(
                new File("C:/Wyniki/liczba slow.xlsx"));
        workbook.write(out);
        out.close();

        System.out.println("Melduje ze plik EXCEL(xlsx) zostal wygenerowany");

        try {
            File file = new File("C:/Wyniki/liczba slow.zip");

            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zos = new ZipOutputStream(fos);

            zos.putNextEntry(new ZipEntry("liczba slow.xlsx"));

            byte[] bytes = Files.readAllBytes(Paths.get("C:/Wyniki/liczba slow.xlsx"));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            zos.close();

        } catch (FileNotFoundException ex) {
            System.err.format("The file %s does not exist", "C:/Wyniki/liczba slow.xlsx");
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
        System.out.println("Melduje ze plik ZIP zostal wygenerowany");
    }

    private static void printMap(Map<String, Integer> map) {
        map.forEach((key, value) -> System.out.println(key + " " + value));
    }

}
