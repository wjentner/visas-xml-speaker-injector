package io.lingvis.visas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Wolfgang Jentner (University of Konstanz) [wolfgang.jentner@uni.kn] on 3/18/2019.
 */
public class SpeakerEncoder {

    private static Logger LOGGER = LoggerFactory.getLogger(SpeakerEncoder.class);

    private static File input = new File("./data/input");

    private static File out = new File("./data/output");

    public static void main(String[] args) throws IOException {
        LOGGER.info("started");

        SpeakerEncoder speakerEncoder = new SpeakerEncoder();

        speakerEncoder.recursiveWalk(input.toPath(), "studentId", metadata -> metadata.studentId);

        speakerEncoder.recursiveWalk(input.toPath(), "course", metadata -> metadata.courseName);

        LOGGER.info("finished");
    }

    private void recursiveWalk(Path path, String outputFolder, Function<Metadata, String> conversion) throws IOException {
        Files.list(path).forEach(p -> {
            File f = p.toFile();
            if(f.isDirectory()) {
                try {
                    recursiveWalk(p, outputFolder, conversion);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(f.getName().endsWith(".xml")) {
                try {
                    processFile(f, outputFolder, conversion);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void processFile(File f, String outputFolder, Function<Metadata, String> conversion) throws IOException {
        Metadata m = getInformation(f.getName());

        String contents = new String(Files.readAllBytes(f.toPath()));

//        Pattern p = Pattern.compile("/name=\"(.+?)\"/");

        String newSpeaker = conversion.apply(m);

        contents = contents.replaceAll("name=\"(.+?)\"", "name=\""+newSpeaker+"\"");

        File outFolder = new File(new File(out, outputFolder), m.studentId);
        outFolder.mkdirs();

        File outFile = new File(outFolder, f.getName());

        Files.write(outFile.toPath(), contents.getBytes());
    }

    private Metadata getInformation(String filename) {
        //216831_S98ECON162[1]_parsed_dus_infer.xml
        //
        LOGGER.debug("Parsing filename " + filename);

        Pattern p = Pattern.compile("^(\\d+?)_([SFX])(\\d{2})(\\w{3,4})(\\d{3}\\w|\\d{3})-?(\\d*?)-?(?:\\[(\\d{1,2})]|(\\d{1,2})D)_parsed_dus_infer\\.xml$");

        Matcher m = p.matcher(filename);

        if(!m.matches()) {
            throw new RuntimeException("No match found for " + filename);
        }

        Metadata metadata = new Metadata();
        metadata.studentId = m.group(1);
        metadata.semester = m.group(2);
        metadata.year = m.group(3);
        metadata.courseName = m.group(4);
        metadata.courseNr = m.group(5);
        metadata.essayNr = m.group(6);
        metadata.numOfPages = m.group(7);

        LOGGER.debug("parsed metadata: {}", metadata);

        return metadata;
    }

    class Metadata {
        String studentId;
        String semester;
        String year;
        String courseName;
        String courseNr;
        String essayNr;
        String numOfPages;

        @Override
        public String toString() {
            return "Metadata{" +
                    "studentId='" + studentId + '\'' +
                    ", semester='" + semester + '\'' +
                    ", year='" + year + '\'' +
                    ", courseName='" + courseName + '\'' +
                    ", courseNr='" + courseNr + '\'' +
                    ", essayNr='" + essayNr + '\'' +
                    ", numOfPages='" + numOfPages + '\'' +
                    '}';
        }
    }
}
