package org.lcm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class lcw {

  private static final boolean ASC = true;
  private static final boolean DESC = false;

  public static void main(String[] args) throws Exception {
    Scanner odczyt = new Scanner(System.in);

    System.out.println("Podaj sciezke pliku:");
    String plik = odczyt.nextLine();

    FileReader fr = null;
    try {
      fr = new FileReader(plik);
    } catch (FileNotFoundException e) {
      System.out.println("Pliku nie ma w domu");
      return;
    }

    double fileSizeInBytes = plik.length();
    double fileSizeInKB = fileSizeInBytes / 1024;
    double fileSizeInMB = fileSizeInKB / 1024;

    if (fileSizeInMB <= 5) {
      System.out.println(fr);
    } else {
      System.out.println("Za gruby, nie przejdzie");
    }

    BufferedReader br = null;

    try {
      String line;

      br = new BufferedReader(fr);
      Map<String, Integer> iloscSlow = new HashMap<>();

      String word = null;
      while ((line = br.readLine()) != null) {

        Scanner sc = new Scanner(line);
        while (sc.hasNext()) {
          word = sc.next();
          if (!iloscSlow.containsKey(word)) {
            iloscSlow.put(word, 1);
          } else {
            iloscSlow.put(word, iloscSlow.get(word) + 1);
          }
        }
      }

      System.out.println("Przed posortowaniem......");
      printMap(iloscSlow);

      System.out.println("Po posortowaniu od najmniejszej......");
      Map<String, Integer> sortedMapAsc = sortByValue(iloscSlow, ASC);
      printMap(sortedMapAsc);

      System.out.println("Po posortowaniu od najwiekszej......");
      Map<String, Integer> sortedMapDesc = sortByValue(iloscSlow, DESC);
      printMap(sortedMapDesc);

      liczbaSlow(sortedMapAsc);

    } finally {
      if (br != null) {
        br.close();
      }
    }
  }

  private static Map<String, Integer> sortByValue(Map<String, Integer> ilosc, final boolean porzadek) {
    List<Entry<String, Integer>> lista = new LinkedList<>(ilosc.entrySet());

    // Sortowanie listy słów w oparciu o wartość liczbową
    lista.sort((slowa, liczba) -> {
      if (porzadek) {
        return slowa.getValue().compareTo(liczba.getValue()) == 0
            ? slowa.getKey().compareTo(liczba.getKey())
            : slowa.getValue().compareTo(liczba.getValue());
      }
      return liczba.getValue().compareTo(slowa.getValue()) == 0
          ? liczba.getKey().compareTo(slowa.getKey())
          : liczba.getValue().compareTo(slowa.getValue());
    });
    return lista.stream().collect(
        Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));
  }

  private static void liczbaSlow(Map<String, Integer> sortedMapAsc) throws Exception {

    XSSFWorkbook workbook = new XSSFWorkbook();

    XSSFSheet spreadsheet = workbook.createSheet(" Liczba slow ");

    XSSFRow row;

    Map<String, Object[]> liczbaSlow = new TreeMap<>();

    liczbaSlow.put("1", new Object[]{"Slowo", "Ilosc"});
    int idx = 2;
    for (Entry<String, Integer> entry : sortedMapAsc.entrySet()) {
      liczbaSlow.put(idx++ + "", new Object[]{
          entry.getKey() + "",
          entry.getValue() + ""});
    }

    Set<String> keyid = liczbaSlow.keySet();

    int rowid = 0;

    for (String key : keyid) {

      row = spreadsheet.createRow(rowid++);
      Object[] objectArr = liczbaSlow.get(key);
      int cellid = 0;

      for (Object obj : objectArr) {
        Cell cell = row.createCell(cellid++);
        cell.setCellValue((String) obj);
      }
    }

    FileOutputStream out = new FileOutputStream(
        new File("C:/Wyniki/liczba slow.xlsx"));

    workbook.write(out);
    out.close();
    System.out.println("Plik wygenerowany");
  }

  private static void printMap(Map<String, Integer> map) {
    map.forEach((key, value) -> System.out.println(key + " " + value));
  }
}

//https://www.geeksforgeeks.org/how-to-write-data-into-excel-sheet-using-java/

//https://stackoverflow.com/questions/30135152/how-list-has-map-values-write-to-excel-file-using-apache-poi
