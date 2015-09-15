package com.edw.indonesiannikcheck;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * <pre>
 *  com.edw.indonesiannikcheck.Main
 * </pre>
 *
 * @author edwin < edwinkun at gmail dot com >
 * Sep 15, 2015 4:39:27 PM
 *
 */
public class Main {

    private final Logger logger = Logger.getLogger(this.getClass());

    public static void main(String[] args) {
        Main main = new Main();

        if (args.length > 0) 
            main.crawl(args[0]);
        else 
            main.crawl(null);
        
    }

    private void crawl(String nik) {
        if (nik == null) 
            nik = "<PUT YOUR NIK HERE>";
        
        Document doc = Jsoup.parse(readURL("https://data.kpu.go.id/ss8.php?cmd=cari&nik=" + nik.trim()));
        Elements labels = doc.getElementsByClass("label");
        Elements fields = doc.getElementsByClass("field");
        Elements fboxheaders = doc.getElementsByClass("fboxheader");

        if (fboxheaders.text().equals("Not Found")) {
            logger.debug("No Result Found for " + nik);
        } else {
            for (int i = 0; i < labels.size(); i++) {
                if (fields.get(i).text().trim().length() > 0) 
                    logger.debug(labels.get(i).text() + "\t : \t" + fields.get(i).text());
            }
        }
    }

    private String readURL(String url) {

        String fileContents = "";
        String currentLine = "";

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            fileContents = reader.readLine();
            while (currentLine != null) {
                currentLine = reader.readLine();
                fileContents += "\n" + currentLine;
            }
            reader.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return fileContents;
    }

}
