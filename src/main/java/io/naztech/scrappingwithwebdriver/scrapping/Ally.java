package io.naztech.scrappingwithwebdriver.scrapping;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.naztech.scrappingwithwebdriver.WebDriverInitializer;
import io.naztech.scrappingwithwebdriver.model.Job;

public class Ally {
	private static WebDriver driver;
	private WebDriverWait wait;
	private Actions actions;
	private String BaseUrl = "https://recruiting.adp.com/srccar/public/RTI.home?d=AllyCareers&c=1125607";

	public Ally() {
		WebDriverInitializer driverInitializer = new WebDriverInitializer();
		driver = driverInitializer.initialize();
		driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
		actions = new Actions(driver);
	}
	
	public void getScrapedJobs() throws IOException, InterruptedException {
		driver.get(BaseUrl);
		wait = new WebDriverWait(driver, 80);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ng-form[@id=\"searchForm\"]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[3]/div")));
		int totalPage = Integer.parseInt(driver.findElement(By.xpath("//div[@class='pageNum']/span[2]")).getText().replace("of ", "").trim());
		System.out.println(totalPage);
		List<WebElement> jobList = getAllJob();
		System.out.println(jobList.size());
		Actions actions = new Actions(driver);
		for(int j = 1 ;j<=totalPage;j++) {
			actions.doubleClick(jobList.get(j).findElement(By.tagName("a"))).perform();
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='field3_left']/div[2]")));
			for (int i = 0; i < jobList.size(); i++) {
				Thread.sleep(1000);
				Job job = getJobDetails();
				//saveJob(job, siteMeta);
				driver.get(BaseUrl);
				wait = new WebDriverWait(driver, 80);
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='currentCount']")));
				driver.findElement(By.id("page_")).sendKeys(String.valueOf(j));
				driver.findElement(By.id("page_")).sendKeys(Keys.ENTER);
				Thread.sleep(5000);
				System.out.println(i+1+((j-1)*10));
				jobList = getAllJob();
			}
		}
		
		driver.close();
	}
	
	private Job getJobDetails() {
		Job job = new Job();
		try {
			job.setTitle(
					driver.findElement(By.xpath("//span[@class='jobTitle']")).getText());
			System.out.println(job.getTitle());
			job.setName(job.getTitle());
			job.setUrl(driver.getCurrentUrl());
			System.out.println(job.getUrl());
			try {
				job.setSpec(
						driver.findElement(By.xpath("//div[@id='field3_left']/div[2]")).getText());
				System.out.println(job.getSpec());
			}
			catch(NoSuchElementException e) {
				System.out.println("Spec not found");
				job.setSpec(
						driver.findElement(By.xpath("//div[@id='field3_right']/div[2]")).getText());
				System.out.println(job.getSpec());
			}
		} catch (NoSuchElementException e) {
			System.out.println("Required data not found");
			return null;
		}

		try {
			job.setReferenceId(
					driver.findElement(By.xpath("//div[@class='field2_right']/div[2]")).getText());
			System.out.println(job.getReferenceId());
			job.setType(driver
					.findElement(
							By.xpath("//div[@class='field1_right']/div[2]"))
					.getText());
			System.out.println(job.getType());
			job.setLocation(driver
					.findElement(
							By.xpath("//div[@class='field2_left']/div[2]"))
					.getText());
			System.out.println(job.getLocation());
			return job;
		} catch (NoSuchElementException e) {
			return job;
		}
		
	}
	
	private List<WebElement> getAllJob() throws InterruptedException{
		List<WebElement> jobList = driver.findElements(By.xpath("//ng-form[@id=\"searchForm\"]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[3]/div"));
		return jobList;
		
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Ally ally = new Ally();
		ally.getScrapedJobs();
		
	}

}
