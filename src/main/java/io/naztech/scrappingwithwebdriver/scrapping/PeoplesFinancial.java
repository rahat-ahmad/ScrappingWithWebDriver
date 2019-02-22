package io.naztech.scrappingwithwebdriver.scrapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.naztech.scrappingwithwebdriver.WebDriverInitializer;
import io.naztech.scrappingwithwebdriver.model.Job;

public class PeoplesFinancial {
	private static WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;
	private String BaseUrl = "https://sjobs.brassring.com/TGnewUI/Search/Home/Home?partnerid=25679&siteid=5313#home";

	public PeoplesFinancial() {
		WebDriverInitializer driverInitializer = new WebDriverInitializer();
		driver = driverInitializer.initialize();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		actions = new Actions(driver);
	}
	
	public void getScrapedJobs() throws IOException, InterruptedException {
		driver.get(BaseUrl);
		wait = new WebDriverWait(driver, 80);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='searchBoxContainer responsiveWidth lightAccentBkg ng-scope searchBoxContainerNoJobs']/h1")));
		Actions actions = new Actions(driver);
		actions.doubleClick(driver.findElement(By.id("searchControls_BUTTON_2"))).perform();
		Thread.sleep(25000);
		List<WebElement> rowList = getAllJob();
		for (int i = 0; i < rowList.size(); i++) {
			try {
				actions.doubleClick(driver.findElement(By.id("Job_" + i))).perform();
			}
			catch(NoSuchElementException e) {
				System.out.println("NoSuchElement found exception..");
				continue;
			}
			Thread.sleep(3000);
			Job job = getJobDetails();
			//saveJob(job, siteMeta);
			driver.findElement(By.xpath("//a[@class='UnderLineLink ng-scope']")).click();
			Thread.sleep(5000);
			System.out.println(i+1);
			//rowList = getAllJob(siteMeta);
			rowList = getAllJob();
		}
		driver.close();
	}
	
	private Job getJobDetails() {
		Job job = new Job();
		try {
			job.setTitle(
					driver.findElement(By.xpath("//h1[@class='answer ng-binding jobtitleInJobDetails']")).getText());
			System.out.println(job.getTitle());
			job.setName(job.getTitle());
			job.setSpec(
					driver.findElement(By.xpath("//p[@class='answer ng-scope jobdescriptionInJobDetails']")).getText());
			System.out.println(job.getSpec());
			job.setUrl(driver.getCurrentUrl());
			System.out.println(job.getUrl());
		} catch (NoSuchElementException e) {
			System.out.println("Required data not found");
			return null;
		}

		try {
			job.setReferenceId(
					driver.findElement(By.xpath("//p[@class='answer ng-scope position1InJobDetails']")).getText());
			System.out.println(job.getReferenceId());
			job.setPrerequisite(driver
					.findElement(
							By.xpath("//p[@class='answer ng-scope section2LeftfieldsInJobDetails jobDetailTextArea']"))
					.getText());
			System.out.println(job.getPrerequisite());
			job.setType(driver
					.findElement(
							By.xpath("//div[@id=\"content\"]/div[1]/div[5]/div[4]/div[2]/div[1]/div[1]/div[6]/p[2]"))
					.getText());
			System.out.println(job.getType());
			job.setLocation(driver
					.findElement(
							By.xpath("//div[@id=\"content\"]/div[1]/div[5]/div[4]/div[2]/div[1]/div[1]/div[9]/p[2]"))
					.getText());
			System.out.println(job.getLocation());
			return job;
		} catch (NoSuchElementException e) {
			return job;
		}
		
	}
	
	private List<WebElement> getAllJob() throws InterruptedException{
		try {
			List<WebElement> rowList = driver.findElements(By.xpath("//ul[@class='jobList ng-scope']/li"));
			int totalJob = Integer.parseInt(driver.findElement(By.xpath("//div[@class='sectionHeading']/h2")).getText()
					.replace(" results", "").trim());
			for (;;) {
				if (rowList.size() == totalJob)
					break;

				Actions actions = new Actions(driver);
				WebElement elementLocator = driver.findElement(By.id("showMoreJobs"));
				actions.doubleClick(elementLocator).perform();
				Thread.sleep(5000);
				rowList = driver.findElements(By.xpath("//ul[@class='jobList ng-scope']/li"));
			}
			return rowList;

		} catch (NoSuchElementException e) {
			System.out.println("NoSuchElementException exception found");
			
			driver.get(BaseUrl);
			Thread.sleep(5000);
			Actions actions = new Actions(driver);
			actions.doubleClick(driver.findElement(By.id("searchControls_BUTTON_2"))).perform();
			Thread.sleep(25000);
			List<WebElement> rowList = driver.findElements(By.xpath("//ul[@class='jobList ng-scope']/li"));
			int totalJob = Integer.parseInt(driver.findElement(By.xpath("//div[@class='sectionHeading']/h2")).getText()
					.replace(" results", "").trim());
			for (;;) {
				if (rowList.size() == totalJob)
					break;
				WebElement elementLocator = driver.findElement(By.id("showMoreJobs"));
				actions.doubleClick(elementLocator).perform();
				Thread.sleep(5000);
				rowList = driver.findElements(By.xpath("//ul[@class='jobList ng-scope']/li"));
			}
			return rowList;
		}
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		PeoplesFinancial peoplesFinancial = new PeoplesFinancial();
		peoplesFinancial.getScrapedJobs();
		
	}

}
