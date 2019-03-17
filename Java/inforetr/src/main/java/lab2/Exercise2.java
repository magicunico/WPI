package lab2;

import org.apache.tika.exception.TikaException;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exercise2
{

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private OptimaizeLangDetector langDetector;

    public static void main(String[] args)
    {
        Exercise2 exercise = new Exercise2();
        exercise.run();
    }

    private void run()
    {
        try
        {
            if (!new File("./outputDocuments").exists())
            {
                Files.createDirectory(Paths.get("./outputDocuments"));
            }

            initLangDetector();

            Path path = Paths.get(System.getProperty("user.dir")).resolve("documents");
            File directory = path.toFile();
            File[] files = directory.listFiles();
            for (File file : files)
            {
                processFile(file);
            }
        } catch (IOException | SAXException | TikaException e)
        {
            e.printStackTrace();
        }

    }

    private void initLangDetector() throws IOException
    {
        this.langDetector = new OptimaizeLangDetector();
        langDetector.loadModels();
    }

    private void processFile(File file) throws IOException, SAXException, TikaException
    {
        // TODO: extract content, metadata and language from given file

        String language;
        String creatorName;
        Date creationDate;
        Date lastModification;
        String mimeType;
        String content;


        try (InputStream inputStream = new FileInputStream(file))
        {
            BodyContentHandler bodyContentHandler = new BodyContentHandler(Integer.MAX_VALUE);
            AutoDetectParser autoDetectParser = new AutoDetectParser();
            Metadata metadata = new Metadata();

            autoDetectParser.parse(inputStream, bodyContentHandler, metadata);

            creationDate = metadata.getDate(TikaCoreProperties.CREATED);
            lastModification = metadata.getDate(TikaCoreProperties.MODIFIED);
            creatorName = metadata.get(TikaCoreProperties.CREATOR);

            content = bodyContentHandler.toString();
            language = langDetector.detect(content).toString();


            mimeType = metadata.get(Metadata.CONTENT_TYPE);
        }

        saveResult(file.getName(), language, creatorName, creationDate, lastModification, mimeType, content); //TODO: fill with proper values
    }

    private void saveResult(String fileName, String language, String creatorName, Date creationDate,
                            Date lastModification, String mimeType, String content)
    {

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        int index = fileName.lastIndexOf(".");
        String outName = fileName.substring(0, index) + ".txt";
        try
        {
            PrintWriter printWriter = new PrintWriter("./outputDocuments/" + outName);
            printWriter.write("Name: " + fileName + "\n");
            printWriter.write("Language: " + (language != null ? language : "") + "\n");
            printWriter.write("Creator: " + (creatorName != null ? creatorName : "") + "\n");
            String creationDateStr = creationDate == null ? "" : dateFormat.format(creationDate);
            printWriter.write("Creation date: " + creationDateStr + "\n");
            String lastModificationStr = lastModification == null ? "" : dateFormat.format(lastModification);
            printWriter.write("Last modification: " + lastModificationStr + "\n");
            printWriter.write("MIME type: " + (mimeType != null ? mimeType : "") + "\n");
            printWriter.write("\n");
            printWriter.write(content + "\n");
            printWriter.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

}