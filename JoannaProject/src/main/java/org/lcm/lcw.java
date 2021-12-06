package org.lcm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

public class lcw {

  private static boolean ASC = true;
  private static boolean DESC = false;

  public static void main(String[] args) {
    Scanner odczyt = new Scanner(System.in);

    System.out.println("Podaj sciezke pliku:");
    String plik = odczyt.nextLine();

    FileReader fr = null;
    try {
      fr = new FileReader(plik);
    } catch (FileNotFoundException e) {
      System.out.println("Pliku nie ma w domu");
    }

    double fileSizeInBytes = plik.length();
    double fileSizeInKB = fileSizeInBytes / 1024;
    double fileSizeInMB = fileSizeInKB / 1024;

    if (fileSizeInMB <= 5) {
      System.out.println(fr);
    } else {
      System.out.println("Za gruby, nie przejdzie");
    }

    BufferedReader br = new BufferedReader(fr);

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

    } catch (
        IOException e) {
      e.printStackTrace();
    }
  }

  private static Map<String, Integer> sortByValue(Map<String, Integer> ilosc,
      final boolean porzadek) {
    List<Entry<String, Integer>> lista = new LinkedList<>(ilosc.entrySet());

    // Sortowanie listy słów w oparciu o wartość liczbową
    lista.sort((slowa, liczba) -> porzadek ? slowa.getValue().compareTo(liczba.getValue()) == 0
        ? slowa.getKey().compareTo(liczba.getKey())
        : slowa.getValue().compareTo(liczba.getValue()) : liczba.getValue().compareTo(slowa.getValue()) == 0
        ? liczba.getKey().compareTo(slowa.getKey())
        : liczba.getValue().compareTo(slowa.getValue()));
    return lista.stream().collect(
        Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> b, LinkedHashMap::new));

  }

  private static void printMap(Map<String, Integer> map) {
    map.forEach((key, value) -> System.out.println(key + " " + value));
  }
}

//https://www.geeksforgeeks.org/how-to-write-data-into-excel-sheet-using-java/

//https://stackoverflow.com/questions/30135152/how-list-has-map-values-write-to-excel-file-using-apache-poi