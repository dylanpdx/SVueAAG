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
		Boolean login = false;

		Document document = new Document("");
		Connection.Response loginForm = null; // Lazy fix?

		while (!login) {
			user = scan.next(); // FOR DEBUGGING ONLY
			pass = scan.next(); // FOR DEBUGGING ONLY

			loginForm = Jsoup.connect("https://parentvue.beaverton.k12.or.us/Login_Student_PXP.aspx").method(Connection.Method.GET).execute();

			document = Jsoup.connect("https://parentvue.beaverton.k12.or.us/Login_Student_PXP.aspx").data("username", user) // FOR
																															// DEBUGGING
																															// ONLY
					.data("password", pass) // FOR DEBUGGING ONLY
					.data("__VIEWSTATE", "/wEPDwUKMTk4OTU4MDc2NWRkpO1Yx46hRy/mkPaHIpJ41xe4NWOOH0l6Fqt2KzXrH58=").data("__VIEWSTATEGENERATOR", "C520BE40").data("__EVENTVALIDATION", "/wEdAASxlVz5jDGwq0WlapthbhhRKhoCyVdJtLIis5AgYZ/RYe4sciJO3Hoc68xTFtZGQEgSYOQVAPr9tiF9q7nSHjzop2Rg4jlbiMUFEEk7o+ulgkFZg4aqMI964lA977RcK24=").cookies(loginForm.cookies()).post();
			// System.out.println(document);
			if (document.toString().contains("Invalid user id or password")) {
				System.out.println("Invalid user id or password.");
			} else {
				login = true;
			}

		}

		// System.out.println(document);
		String welcome = document.getElementsByClass("UserHead").text();
		String name = welcome.split(",")[1];
		name = name.substring(1, name.length());
		System.out.println(name);
		System.out.println(loginForm.cookies());
		Document grades = Jsoup.connect("https://parentvue.beaverton.k12.or.us/PXP_Gradebook.aspx?AGU=0").timeout(10 * 1000).cookies(loginForm.cookies()).post();
		// System.out.println(grades);

		Element table = grades.select("table[class=info_tbl]").first();
		table = table.select("tbody").get(0);
		Elements gradeTBL = table.select("tr");
		int classes = gradeTBL.size() - 1;
		// gradeTBL.get(1).getAllElements().last().text()
		
		for (int i = 1; i < classes; i++) { //  (each class I think? this may be where the bug is)
			Elements tbl = gradeTBL.get(i).getAllElements(); // find what's inside it
			Elements tbl2 = tbl.get(i).select("td"); // each param (I think? this may be where the bug is)
				for (int j = 0; j < tbl2.size(); j++) { // for each thing that's inside the class
					System.out.print(tbl2.get(j).text() + "||"); // print it out?
				}
			System.out.println("Selected class " + i + " td");

			System.out.println();
		}

	}

}
