import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SVueAAG_main {

	public static void main(String[] args) throws IOException, InterruptedException {
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
		Document grades = Jsoup.connect("https://parentvue.beaverton.k12.or.us/PXP_Gradebook.aspx?AGU=0")
				.timeout(10*1000)
				.cookies(loginForm.cookies())
				.post();
		//System.out.println(grades);
		
		Element table = grades.select("table[class=info_tbl]").first();
		table = table.select("tbody").get(0);
		System.out.println(table.select("tr").size() - 1);
		
	}

}