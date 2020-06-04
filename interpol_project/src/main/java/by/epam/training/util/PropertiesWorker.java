package by.epam.training.util;


import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesWorker {
    public static Properties createConfig(String pathToProp) throws PropertiesWorkerException {
        try {
            URL urlToProp = PropertiesWorker.class.getClassLoader().getResource(pathToProp);
            File propertiesFile = new File(urlToProp.getFile());
            String path = propertiesFile.getAbsolutePath();
            Properties properties = new Properties();
            try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
                properties.load(inputStream);
            }
            return properties;
        } catch (Exception e) {
            throw new PropertiesWorkerException("cant get properties :", e);
        }
    }
}
