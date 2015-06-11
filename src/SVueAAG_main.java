import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class SVueAAG_main {

	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		String user = ""; // FOR DEBUGGING ONLY
		String pass = ""; // FOR DEBUGGING ONLY
		
		user = scan.next(); // FOR DEBUGGING ONLY
		pass = scan.next(); // FOR DEBUGGING ONLY
		
        Connection.Response loginForm = Jsoup.connect("https://parentvue.beaverton.k12.or.us/Login_Student_PXP.aspx")
                .method(Connection.Method.GET)
                .execute();
		
		Document document = Jsoup.connect("https://parentvue.beaverton.k12.or.us/Login_Student_PXP.aspx")
				.data("username",user) // FOR DEBUGGING ONLY
				.data("password",pass) // FOR DEBUGGING ONLY
				.data("__VIEWSTATE","/wEPDwUKMTk4OTU4MDc2NWRkpO1Yx46hRy/mkPaHIpJ41xe4NWOOH0l6Fqt2KzXrH58=")
				.data("__VIEWSTATEGENERATOR","C520BE40")
				.data("__EVENTVALIDATION","/wEdAASxlVz5jDGwq0WlapthbhhRKhoCyVdJtLIis5AgYZ/RYe4sciJO3Hoc68xTFtZGQEgSYOQVAPr9tiF9q7nSHjzop2Rg4jlbiMUFEEk7o+ulgkFZg4aqMI964lA977RcK24=")
				.cookies(loginForm.cookies())
				.post();
		
		//System.out.println(document);
		String welcome = document.getElementsByClass("UserHead").text();
		String name = welcome.split(",")[1];
		name = name.substring(1, name.length());
		System.out.println(name);
		System.out.println(loginForm.cookies());
		
		Document grades = Jsoup.connect("https://parentvue.beaverton.k12.or.us/PXP_Gradebook.aspx")
				.cookies(loginForm.cookies())
				.post();
		System.out.println(grades);
		
	}

}
