package com.VerifyLinks;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Verifyalllinks {
	
	private WebDriver driver;
	private int invalidLinksCount;

	@BeforeClass
	public void setUp() {

		driver = new FirefoxDriver();
		driver.get("https://s4testing.nic.in/sarathiservice/sarathiHomePublic.do");
		driver.findElement(By.xpath("html/body/div/div/div[3]/form/nav/div/div/ul/li[2]/a")).click();
		driver.findElement(By.id("username")).sendKeys("monika");
		driver.findElement(By.id("password")).sendKeys("abc@123");
		driver.findElement(By.xpath(".//*[@id='fm1']/div[4]/div/input[5]")).click();
	}

	@Test
	public void validateInvalidLinks() {

		try {
			invalidLinksCount = 0;
			List<WebElement> anchorTagsList = driver.findElements(By
					.tagName("a"));
			System.out.println("Total no. of links are "
					+ anchorTagsList.size());
			for (WebElement anchorTagElement : anchorTagsList) {
				
				if (anchorTagElement != null) {
					String url = anchorTagElement.getAttribute("href");
					if (url != null && !url.contains("javascript")) {
						verifyURLStatus(url);
						System.out.println("url  "+ url);
					} else {
						invalidLinksCount++;
					}
				}
			}

			System.out.println("Total no. of invalid links are "
					+ invalidLinksCount);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null)
			driver.quit();
	}

	public void verifyURLStatus(String URL) {

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(URL);
		try {
			HttpResponse response = client.execute(request);
			// verifying response code and The HttpStatus should be 200 if not,
			// increment invalid link count
			////We can also check for 404 status code like response.getStatusLine().getStatusCode() == 404
			if (response.getStatusLine().getStatusCode() != 200)
				invalidLinksCount++;
			System.out.println("StatusCode  "+response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}