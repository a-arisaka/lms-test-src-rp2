package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト レポート機能
 * ケース08
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース08 受講生 レポート修正(週報) 正常系")
public class Case08 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() throws Exception {
		goTo("http://localhost:8080/lms/");
		String pageTitle = webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitle);
		//ログインIDとパスワード入力欄が表示されているかチェック
		WebElement elemUser = webDriver.findElement(By.id("loginId"));
		WebElement elemPass = webDriver.findElement(By.id("password"));
		assertTrue(elemUser.isDisplayed());
		assertTrue(elemPass.isDisplayed());

		Thread.sleep(5000);

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() throws Exception {
		//初回ログイン済みのパスワードでログイン
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA01");
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA01a");
		//ログインを実行
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[3]/div/input")).click();

		//メッセージ「ようこそ受講生AA1さん」が表示されることを確認
		WebElement welcomeUser = webDriver
				.findElement(By.xpath("//*[@id=\"nav-content\"]/ul[2]/li[2]/a"));

		assertTrue(welcomeUser.isDisplayed());

		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 提出済の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() throws Exception {
		WebElement button = webDriver.findElement(By.xpath("//tr[td[contains(text(), '提出済み')]]//input[@value='詳細']"));
		button.click();
		Thread.sleep(300);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「確認する」ボタンを押下しレポート登録画面に遷移")
	void test04() throws Exception {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
		By confirmButtonLocator = By.xpath("//input[@value='提出済み週報【デモ】を確認する']");
		WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(confirmButtonLocator));

		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].click();", confirmButton);
		assertEquals("レポート登録 | LMS", webDriver.getTitle());
		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しセクション詳細画面に遷移")
	void test05() throws Exception {
		//それぞれのテキストボックスを取得
		WebElement achievementLevel = webDriver.findElement(By.xpath("//*[@id=\"content_0\"]"));
		WebElement impressions = webDriver.findElement(By.xpath("//*[@id=\"content_1\"]"));
		WebElement lookBack = webDriver.findElement(By.xpath("//*[@id=\"content_2\"]"));
		assertTrue(achievementLevel.isDisplayed());
		assertTrue(impressions.isDisplayed());
		assertTrue(lookBack.isDisplayed());

		//今回は所感を編集
		impressions.clear();
		impressions.sendKeys("週報のサンプルのテストです。");
		File file1 = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath1 = "evidence/case08_05_01" + ".png";
		File destFile1 = new File(destinationPath1);

		FileUtils.copyFile(file1, destFile1);

		//提出ボタンを押下する
		WebElement submissionButton = webDriver.findElement(By.xpath("//button[text()='提出する']"));
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		js.executeScript("arguments[0].click();", submissionButton);

		Thread.sleep(300);
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());
		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test06() throws Exception {
		webDriver.findElement(By.xpath("//a[@href='/lms/user/detail']")).click();
		assertEquals("ユーザー詳細", webDriver.getTitle());
		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 該当レポートの「詳細」ボタンを押下しレポート詳細画面で修正内容が反映される")
	void test07() throws Exception {
		//2022年10月2日(日)の週報【デモ】の詳細ボタンを取得
		WebElement weeklyReport = webDriver
				.findElement(By.xpath("//tr[td[contains(text(), '週報【デモ】')]]//input[@value='詳細']"));
		//ページを該当箇所までスクロールさせ、詳細ボタンを押下
		JavascriptExecutor js = (JavascriptExecutor) webDriver;
		//スクリーンショットをevidenceフォルダに保存

		File file1 = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
		String destinationPath1 = "evidence/case08_07_01" + ".png";
		File destFile1 = new File(destinationPath1);

		FileUtils.copyFile(file1, destFile1);
		js.executeScript("arguments[0].click();", weeklyReport);

		//所感テキストを取得
		WebElement impressions = webDriver.findElement(By.xpath("//td[normalize-space()='週報のサンプルのテストです。']"));
		assertTrue(impressions.isDisplayed());

		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});

	}

}
