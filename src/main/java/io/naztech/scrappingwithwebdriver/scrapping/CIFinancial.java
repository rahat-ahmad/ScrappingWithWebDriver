package io.naztech.scrappingwithwebdriver.scrapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import io.naztech.scrappingwithwebdriver.WebDriverInitializer;
import io.naztech.scrappingwithwebdriver.model.Job;



public class CIFinancial {
	
	private static WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;
	private String BaseUrl = "https://workforcenow.adp.com/mascsr/default/mdf/recruitment/recruitment.html?cid=8af61d30-ce97-4d97-af7d-bda5fa023504";

	public CIFinancial() {
		WebDriverInitializer driverInitializer = new WebDriverInitializer();
		driver = driverInitializer.initialize();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		actions = new Actions(driver);
	}
	
	public void getScrapedJobs() throws IOException, InterruptedException {
		driver.get(BaseUrl);
		wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnShowAllJobs")));
		List<WebElement> rowList = getAllJob();
		System.out.println(rowList.size());
		for(int i = 0;i<rowList.size();i++) {
			actions.doubleClick(rowList.get(i)).perform();
			Thread.sleep(5000);
			Job job = getJobDetails();
			driver.navigate().refresh();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.id("btnShowAllJobs")));
			rowList = getAllJob();
			System.out.println(i+1);
		}
		driver.close();
	}
	
	private Job getJobDetails() {
		Job job = new Job();
		try {
			job.setTitle(driver.findElement(By.className("job-description-title")).getText());
			System.out.println(job.getTitle());
			job.setName(job.getTitle());
			String spec = "";
			for(int i = 2;i<=9;i++) {
				try {
					spec = spec+driver.findElement(By.xpath("//div[@class='job-description-data']/div/div[i]")).getText();
				}
				catch(NoSuchElementException e) {
					System.out.println("element not found");
					spec = spec+driver.findElement(By.xpath("//div[@class='job-description-data']")).getText();
					break;
				}
			}
			job.setSpec(spec);
			System.out.println(job.getSpec());
		}catch(NoSuchElementException e) {
			return null;
		}
		
		try {
			job.setType(driver.findElement(By.className("job-description-worker-catergory")).getText());
			System.out.println(job.getType());
			job.setLocation(driver.findElement(By.xpath("//span[@class='job-description-location']/div/span")).getText());
			System.out.println(job.getLocation());
			job.setReferenceId(driver.findElement(By.className("job-description-requisition")).getText().replace("Requisition ID :", "").trim());
			System.out.println(job.getReferenceId());
			return job;
		}catch(NoSuchElementException e) {
			return job;
		}
	}
	
	private List<WebElement> getAllJob() throws InterruptedException{
		WebElement elementLocator = driver.findElement(By.id("btnShowAllJobs"));
		actions.doubleClick(elementLocator).perform();
		Thread.sleep(10000);
		List<WebElement> rowList = driver.findElements(By.xpath("//div[@class='current-openings-list-container']/div/div"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		for(;;) {
			js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
			Thread.sleep(2000);
			List<WebElement> newRowList = driver.findElements(By.xpath("//div[@class='current-openings-list-container']/div/div"));
			if(rowList.size()==newRowList.size()) break;
			rowList = newRowList;
		}
		return rowList;
		
	}
	
}
