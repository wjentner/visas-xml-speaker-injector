package io.lingvis.visas.xmlConversion;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FileConverter {

    private static final List<String> utteranceIdentifier = Arrays.asList("<new paragraph>", "<skips line>");
    private static final List<String> newLineIdentifier = Collections.singletonList("<new line>");
    private static final List<String> skipIdentifier = Arrays.asList("<bib>","<keywords>", "<begin footnote>", "<end notes>");

    private XmlWriter xmlWriter;

    public FileConverter() {
        xmlWriter = new XmlWriter();
    }


    public static void main(String[] args) throws IOException, XMLStreamException {
        File folder = new File("./data/inputTxt/");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        FileConverter fileConverter = new FileConverter();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                    fileConverter.transformFile(file.toString(), "./data/outputXml/" + file.getName().substring(0, file.getName().lastIndexOf(".")) + ".xml");
                }
            }
        }
    }


    private void transformFile(String inputFile, String outputFile) throws IOException, XMLStreamException {
        BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile));
        xmlWriter.openWriter(new File(outputFile));

        xmlWriter.writeXmlEncoding();

        xmlWriter.writeSectionStart();
        String firstLine = reader.readLine();
        String secondLine = reader.readLine();

        xmlWriter.writeTopicStart(secondLine.split(": ")[1]);


        int id = 1;
        boolean skipNextLines = true;
        while (reader.ready()) {
            String line = reader.readLine();
            if (utteranceIdentifier.stream().anyMatch(line::startsWith)) {
                if (!skipNextLines) {
                    xmlWriter.writeEndElement();
                }
                xmlWriter.writeUtteranceStart(String.valueOf(id), firstLine.split(": ") [1]);

                skipNextLines = false;
            } else if (newLineIdentifier.stream().anyMatch(line::startsWith)) {
                xmlWriter.writeText("\n");
            } else if (skipIdentifier.stream().anyMatch(line::startsWith)) {
                skipNextLines = true;
            } else {
                xmlWriter.writeText(line + " ");
            }
        }
        xmlWriter.writeEndElement();
        xmlWriter.writeEndElement();
        xmlWriter.writeEndElement();

        xmlWriter.closeWriter();
    }
}
