
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;
import pallaviProjectWithPOM.LandingPage;

public class BaseTest {

	public WebDriver driver;
	public LandingPage landingpage;

	public WebDriver initializeDriver() throws IOException {

		Properties prop = new Properties();
		FileInputStream fis = new FileInputStream(
				"D:\\Pallavi_work\\code\\SeleniumFrameworkDesign\\src\\main\\java\\pallaviProject\\resources\\GlobalData.properties");

		prop.load(fis);
		//Using Ternary Operator
		String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") :prop.getProperty("browser");
		//String browserName = prop.getProperty("browser");
		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();

		} else if (browserName.equalsIgnoreCase("firefox")) {
			// firefox
			
			System.setProperty("webdriver.gecko.driver", "D:\\Pallavi_work\\firefoxdriver\\geckodriver.exe");
			//FirefoxOptions options = new FirefoxOptions();
			//options.addArguments("C:\\Program Files\\Mozilla Firefox\\firefox.exe"); //location where Firefox is installed

			driver = new FirefoxDriver();
		} else if (browserName.equalsIgnoreCase("edge")) {
			// edge
			// System.setProperties(WebDriver.edge.driver,"edge.exe");//need to give the
			// driver path
			// WebDriver driver = new EdgeDriver();

		}
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		return driver;
	}

	@BeforeMethod(alwaysRun = true)
	public LandingPage launchApplication() throws Exception {
		driver = initializeDriver();
		landingpage = new LandingPage(driver);
		landingpage.goTo();
		return landingpage;
	}

	// taking the screenshot for the test failures
	public String getScreenShot(String testcasename,WebDriver driver) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		System.out.println(System.getProperty("user.dir"));
		File file = new File("D:\\Pallavi_work\\code\\SeleniumFrameworkDesign\\reports\\" + testcasename + ".png");
		FileUtils.copyFile(source, file);
		//return System.getProperty(("User.dir") + "\\reports\\" + testcasename + ".png");
		//return System.getProperty("User.dir") + "\\reports\\" + testcasename + ".png";
		return "D:\\Pallavi_work\\code\\SeleniumFrameworkDesign\\reports\\" + testcasename + ".png";
	}

	public List<HashMap<String, String>> getJsonDataToMap(String filePath) throws IOException {

		String jsonContent = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);

		// String to Hashmap using jakson databind(add this dependancy in pom.xml)
		ObjectMapper mapper = new ObjectMapper();
		List<HashMap<String, String>> data = mapper.readValue(jsonContent,
				new TypeReference<List<HashMap<String, String>>>() {

				});
		return data;

	}

	@AfterMethod(alwaysRun = true)
	public void tearDown() {
		driver.close();
	}
}
