package jp.co.sss.lms.ct.f06_login2;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.Assert.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能②
 * ケース16
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース16 受講生 初回ログイン 変更パスワード未入力")
public class Case16 {

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
		// 指定のURLの画面を開く
		goTo("http://localhost:8080/lms/");
		//遷移したURLが正しいか確認
		String currentUrl = WebDriverUtils.webDriver.getCurrentUrl();
		assertEquals("http://localhost:8080/lms/", currentUrl);
		//タイトルが正しいか確認
		String pageTitleString = WebDriverUtils.webDriver.getTitle();
		assertEquals("ログイン | LMS", pageTitleString);

		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに初期登録された未ログインの受講生ユーザーでログイン")
	void test02() throws Exception {
		//DBに登録された受講生ユーザーでログインする
		WebElement loginId = webDriver.findElement(By.id("loginId"));
		loginId.clear();
		loginId.sendKeys("StudentAA03");
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA03");
		//ログインを実行
		webDriver.findElement(By.xpath("//*[@id=\"main\"]/div[1]/form/fieldset/div[3]/div/input")).click();
		//利用規約画面に遷移できているかどうか確認
		assertEquals("セキュリティ規約 | LMS", WebDriverUtils.webDriver.getTitle());
		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 「同意します」チェックボックスにチェックを入れ「次へ」ボタン押下")
	void test03() throws Exception {
		WebElement checkbox = webDriver.findElement(By.xpath("//input[@type='checkbox']"));
		scrollBy("100");
		checkbox.click();
		WebElement next = webDriver.findElement(By.xpath("//button[text()='次へ']"));
		next.click();
		assertEquals("パスワード変更 | LMS", WebDriverUtils.webDriver.getTitle());
		//スクリーンショットをevidenceフォルダに保存
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 パスワードを未入力で「変更」ボタン押下")
	void test04() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		currentPassword.clear();
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		passwordConfirm.clear();
		WebElement changePassword = webDriver.findElement(By.id("upd-btn"));
		changePassword.click();
		webDriver.findElement(By.xpath("//button[@type='submit']")).submit();
		Thread.sleep(300);
		WebElement errorCurrentPassword = webDriver
				.findElement(By.xpath("//div[@class='error' and text()='現在のパスワードは必須です。']"));
		assertTrue(errorCurrentPassword.isDisplayed());
		WebElement errorNewPassword = webDriver.findElement(
				By.xpath("//div[@class='error' and text()='パスワードは英大文字、英小文字、数字の3文字種を混合させた8文字以上を入力してください。']"));
		assertTrue(errorNewPassword.isDisplayed());
		WebElement errorConfirmationPassword = webDriver
				.findElement(By.xpath("//div[@class='error' and text()='確認パスワードは必須です。']"));
		assertTrue(errorConfirmationPassword.isDisplayed());

		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 20文字以上の変更パスワードを入力し「変更」ボタン押下")
	void test05() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA03");
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA03aaaaaaaaaaa");
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA02aaaaaaaaaaa");
		webDriver.findElement(By.xpath("//button[@type='submit']")).submit();

		WebElement errorNewPassword = webDriver.findElement(
				By.xpath("//div[@class='error' and text()='パスワードは英大文字、英小文字、数字の3文字種を混合させた8文字以上を入力してください。']"));
		assertTrue(errorNewPassword.isDisplayed());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 ポリシーに合わない変更パスワードを入力し「変更」ボタン押下")
	void test06() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA03");
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("A");
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("A");
		webDriver.findElement(By.xpath("//button[@type='submit']")).submit();

		WebElement errorNewPassword = webDriver.findElement(
				By.xpath(
						"//div[@class='error' and text()='「パスワード」には半角英数字のみ使用可能です。また、半角英大文字、半角英小文字、数字を含めた8～20文字を入力してください。']"));
		assertTrue(errorNewPassword.isDisplayed());
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 一致しない確認パスワードを入力し「変更」ボタン押下")
	void test07() throws Exception {
		WebElement currentPassword = webDriver.findElement(By.id("currentPassword"));
		currentPassword.clear();
		currentPassword.sendKeys("StudentAA03");
		WebElement password = webDriver.findElement(By.id("password"));
		password.clear();
		password.sendKeys("StudentAA02a");
		WebElement passwordConfirm = webDriver.findElement(By.id("passwordConfirm"));
		passwordConfirm.clear();
		passwordConfirm.sendKeys("StudentAA02b");
		webDriver.findElement(By.xpath("//button[@type='submit']")).submit();

		WebElement errorNewPassword = webDriver.findElement(
				By.xpath("//div[@class='error' and text()='パスワードと確認パスワードが一致しません。']"));
		assertTrue(errorNewPassword.isDisplayed());
		getEvidence(new Object() {
		});
	}

}
