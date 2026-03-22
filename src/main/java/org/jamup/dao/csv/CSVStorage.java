package org.jamup.dao.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class CSVStorage {

    private static final String CSV_DIR = "src/main/resources/org/jamup/dao/csv/";

    /**
     * Reads all rows from a CSV file, skipping the header row.
     *
     * @param filename the name of the CSV file to read (must be located in the predefined CSV directory).
     * @return a list of string arrays, where each array represents a row in the CSV file.
     * @throws RuntimeException if an error occurs while reading the file.
     */
    public static List<String[]> read(String filename) {
        List<String[]> rows = new ArrayList<>();
        try {
            File fd = new File(CSV_DIR + filename);
            CSVReader reader = new CSVReader(new BufferedReader(new FileReader(fd)));
            String[] record;
            boolean firstRow = true;
            while ((record = reader.readNext()) != null) {
                if (firstRow) {
                    firstRow = false;
                    continue;
                }
                rows.add(record);
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV: " + filename, e);
        }
        return rows;
    }

    /**
     * Appends a single row to the end of a CSV file.
     *
     * @param filename the name of the CSV file to append to (must be located in the predefined CSV directory).
     * @param row an array of strings representing the data to append.
     * @throws RuntimeException if an error occurs while appending to the file.
     */
    public static void append(String filename, String[] row) {
        try {
            File fd = new File(CSV_DIR + filename);
            CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(fd, true)));
            writer.writeNext(row);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Error appending to CSV: " + filename, e);
        }
    }

    /**
     * Rewrites an entire CSV file with a new header and a list of rows.
     * This method writes to a temporary file first and then atomically replaces the target file.
     *
     * @param filename the name of the CSV file to rewrite (must be located in the predefined CSV directory).
     * @param header an array of strings representing the header row.
     * @param rows a list of string arrays representing the data rows.
     * @throws RuntimeException if an error occurs while rewriting the file.
     */
    public static void rewrite(String filename, String[] header, List<String[]> rows) {
        try {
            File tmpFD = File.createTempFile("dao", "tmp");
            CSVWriter writer = new CSVWriter(new BufferedWriter(new FileWriter(tmpFD, true)));
            writer.writeNext(header);
            for (String[] row : rows) {
                writer.writeNext(row);
            }
            writer.flush();
            writer.close();
            Files.move(tmpFD.toPath(), Paths.get(CSV_DIR + filename), REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error rewriting CSV: " + filename, e);
        }
    }
}