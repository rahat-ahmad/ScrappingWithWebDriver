package io.naztech.scrappingwithwebdriver;

import java.util.List;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.naztech.scrappingwithwebdriver.model.Job;

public class ScrappingWebDriver {


	public void waitForLoad(WebDriver driver) {
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		};
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(pageLoadCondition);

	}

	public String nextPage(int number) {
		return "https://career.deutsche-boerse.com/search/?q=&sortColumn=referencedate&sortDirection=desc&startrow="
				+ (number + 25);
	}

	public int getPage(String currentPage) {

		String[] stringOfPageNumber = currentPage.split(" ", 3);
		System.out.println(currentPage);
		String lastPage = stringOfPageNumber[stringOfPageNumber.length - 1].trim();
		System.out.println(lastPage);
		return Integer.parseInt(lastPage);

	}
	
	public Job setDetail(String url , Job job , WebDriver driver) {
		driver.get(url);
		job.setDate(driver.findElement(By.xpath("//p[@id=\"job-date\"]/span")).getText().trim());
		return job;
	}
	
	public Job setJob(WebElement webElement , WebDriver driver) {
		Job job = new Job();
		job.setJobTitle(webElement.findElement(By.xpath("//a[@class=\"jobTitle-link\"]")).getText().trim());
		job.setLocation(webElement.findElement(By.xpath("//span[@class=\"jobLocation\"]")).getText().trim());
		job.setCompany(webElement.findElement(By.xpath("//span[@class=\"jobFacility\"]")).getText().trim());
		String url = webElement.findElement(By.xpath("//a[@class=\"jobTitle-link\"]")).getAttribute("href").trim();
		job = setDetail(url, job , driver);
		return job;
	}

	public static void main(String[] args) throws InterruptedException {
		ScrappingWebDriver scrappingWebDriver = new ScrappingWebDriver();
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\rahat.ahmad\\Desktop\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		String baseUrl = "https://career.deutsche-boerse.com/search/?q=&sortColumn=referencedate&sortDirection=desc";
		driver.get(baseUrl);
		scrappingWebDriver.waitForLoad(driver);
		

		for (int i = 0;; i+=25) {
			int count =1;
			List<WebElement> pageNumber = driver.findElements(By.xpath("//span[@class=\"paginationLabel\"]/b"));
			int lastPage = Integer.parseInt(pageNumber.get(1).getText().trim());
			String currentPage = pageNumber.get(0).getText().trim();
			List<WebElement> jobTable = driver.findElements(By.xpath("//table[@id='searchresults']/tbody/tr"));
			for(int j = 0;jobTable.size()<j;j++) {
				Job job = new Job();
				job = scrappingWebDriver.setJob(jobTable.get(j),driver);
				System.out.println(job.getCompany());
				System.out.println(job.getDesignation());
				System.out.println(job.getJobTitle());
				System.out.println(job.getDate());
				System.out.println("Job Number "+ count);
				count++;
				driver.get(baseUrl);
			}
			
			baseUrl = scrappingWebDriver.nextPage(i);
			driver.get(baseUrl);
			if (lastPage == scrappingWebDriver.getPage(currentPage))
				break;
		}
		

		driver.close();

	}

}
