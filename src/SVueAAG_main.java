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
		String user = ""; // FOR DEBUGGING ONLY!!!
		String pass = ""; // FOR DEBUGGING ONLY!!!
		Boolean login = false;

		Document document = new Document("");
		Connection.Response loginForm = null; // Lazy fix?

		while (!login) {
			user = scan.next(); // FOR DEBUGGING ONLY!!!
			pass = scan.next(); // FOR DEBUGGING ONLY!!!

			loginForm = Jsoup.connect("https://parentvue.beaverton.k12.or.us/Login_Student_PXP.aspx").method(Connection.Method.GET).execute(); // Get
																																				// cookies

			document = Jsoup.connect("https://parentvue.beaverton.k12.or.us/Login_Student_PXP.aspx").data("username", user) // FOR DEBUGGING ONLY
					.data("password", pass) // FOR DEBUGGING ONLY!!!
					.data("__VIEWSTATE", "/wEPDwUKMTk4OTU4MDc2NWRkpO1Yx46hRy/mkPaHIpJ41xe4NWOOH0l6Fqt2KzXrH58=")
					.data("__VIEWSTATEGENERATOR", "C520BE40")
					.data("__EVENTVALIDATION", "/wEdAASxlVz5jDGwq0WlapthbhhRKhoCyVdJtLIis5AgYZ/RYe4sciJO3Hoc68xTFtZGQEgSYOQVAPr9tiF9q7nSHjzop2Rg4jlbiMUFEEk7o+ulgkFZg4aqMI964lA977RcK24=")
					.cookies(loginForm.cookies())
					.post();
			// System.out.println(document);
			if (document.toString().contains("Invalid user id or password")) { // Duh.
				System.out.println("Invalid user id or password.");
			} else {
				login = true;
			}

		}

		// System.out.println(document);
		String welcome = document.getElementsByClass("UserHead").text(); // Student's
																			// name
		String name = welcome.split(",")[1]; // Has an annoying space at the
												// end.
		name = name.substring(1, name.length()); // Done
		System.out.println(name);
		System.out.println(loginForm.cookies());
		Document grades = Jsoup.connect("https://parentvue.beaverton.k12.or.us/PXP_Gradebook.aspx?AGU=0").timeout(10 * 1000).cookies(loginForm.cookies()).post(); // Connect
																																									// to
																																									// gradebook
		// System.out.println(grades);

		Element table = grades.select("table[class=info_tbl]").first(); // Grade
																		// table
		table = table.select("tbody").get(0);
		Elements gradeTBL = table.select("tr");
		int classes = gradeTBL.size() - 1;
		// gradeTBL.get(1).getAllElements().last().text()

		for (int i = 1; i < classes; i++) {
			Elements tbl = gradeTBL.get(i).getAllElements();
			for (int k = 0; k < tbl.size(); k++) { //
				Elements tbl2 = tbl.get(k).select("td");
				for (int j = 0; j < tbl2.size(); j++) { //
					if (tbl2.size() == 6) { // Apparently some forms don't have
											// 6
						System.out.println(tbl2.get(j).text());
					}
				}
			}

			// System.out.println("Selected class " + i + " td");

			System.out.println();
		}

	}

}
