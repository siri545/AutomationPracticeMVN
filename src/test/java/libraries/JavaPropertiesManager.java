package libraries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import static org.testng.Assert.assertTrue;

public class  JavaPropertiesManager {
    final static Logger logger = LogManager.getLogger(JavaPropertiesManager.class);

    private String propertiesFile;
    private Properties prop;
    private FileInputStream input;
    private FileOutputStream output;

    public JavaPropertiesManager(String propertiesFilePath) {
        try {
            if (propertiesFilePath.length() > 0) {
                propertiesFile = propertiesFilePath;
                prop = new Properties();
            }
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        }
    }

    public String readProperty(String key) {
        String value = null;
        try {
            input = new FileInputStream(propertiesFile);
            prop.load(input);
            value = prop.getProperty(key);

        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                logger.error("Error: ", e);
                assertTrue(false);
            }
        }
        return value;
    }

    public void setProperty(String key, String value) {
        try {
            output = new FileOutputStream(propertiesFile);
            prop.setProperty(key, value);
            prop.store(output, null);
            logger.info("Properties file created ---> " + propertiesFile);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                logger.error("Error: ", e);
                assertTrue(false);
            }
        }
    }

    public void setProperty(String key, String value, String comments) {
        try {
            output = new FileOutputStream(propertiesFile);
            prop.setProperty(key, value);
            prop.store(output, comments);
            logger.info("Properties file created ---> " + propertiesFile);
        } catch (Exception e) {
            logger.error("Error: ", e);
            assertTrue(false);
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                logger.error("Error: ", e);
                assertTrue(false);
            }
        }
    }

    public static void main(String[] args) {
        //JavaPropertiesManager property = new JavaPropertiesManager("src/test/resources/log4j.properties");
        //String data = property.readProperty("log4j.appender.HTML.File");
        //logger.info("data: " + data);

        //JavaPropertiesManager property = new JavaPropertiesManager("src/test/resources/config.properties");
        //property.setProperty("browserType", "FireFox");
        //property.setProperty("demoMode", "false", "Demo mode true enables highlighting WebElement and slows down the test execution.");

    }

}
