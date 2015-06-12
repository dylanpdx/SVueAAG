import java.io.IOException;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SVueAAG_main {
 // 
	static String Dlink = "https://parentvue.beaverton.k12.or.us/Login_Student_PXP.aspx";
	
	public static Connection.Response logIn(String user,String pass, String loginLink) throws IOException{
		Document document = new Document("");
		Connection.Response loginForm = null; // Lazy fix?
		loginForm = Jsoup.connect(loginLink).method(Connection.Method.GET).execute(); // Get
		Document loginFormParsed = loginForm.parse();
		String viewState=loginFormParsed.select("#__VIEWSTATE").attr("value");
		String eventValidate=loginFormParsed.select("#__EVENTVALIDATION").attr("value");
		String viewStateGen=loginFormParsed.select("#__VIEWSTATEGENERATOR").attr("value");
		document = Jsoup.connect(loginLink)
				.data("username", user) // FOR DEBUGGING ONLY
				.data("password", pass) // FOR DEBUGGING ONLY!!!
				.data("__VIEWSTATE", viewState) // __VIEWSTATEGENERATOR
				.data("__VIEWSTATEGENERATOR", viewStateGen)
				.data("__EVENTVALIDATION", eventValidate)
				.cookies(loginForm.cookies())
				.post();
		// System.out.println(document);
		if (document.toString().contains("Invalid user id or password")) { // Duh.
			return null;
		} else {
			return loginForm;
		}

	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Scanner scan = new Scanner(System.in);
		String user = ""; // FOR DEBUGGING ONLY!!!
		String pass = ""; // FOR DEBUGGING ONLY!!!
		Boolean login = false;
		Document document = new Document("");
		Response res = null;


		while (!login) {
			user = scan.next(); // FOR DEBUGGING ONLY!!!
			pass = scan.next(); // FOR DEBUGGING ONLY!!!
			res = logIn(user,pass,Dlink);
			if (res != null){
				document = res.parse();
				login = true;
			}else{
				System.out.println("Bad login");
			}
			
		}

		System.out.println(document);
		String welcome = document.getElementsByClass("UserHead").text(); // Student's
																			// name
		String name = welcome.split(",")[1];
		name = name.substring(1, name.length());
		System.out.println(name);
		//System.out.println(document.cookies());
		Document grades = Jsoup.connect("https://parentvue.beaverton.k12.or.us/PXP_Gradebook.aspx?AGU=0")
				.timeout(10 * 1000)
				.cookies(res.cookies()).post(); // Connect
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
					if (tbl2.size() == 6) { // Apparently some forms don't have 6
						String txt = tbl2.get(j).text();
						if (!txt.equals(" ")){
							System.out.println(txt);
						}
						
					}
				}
			}

			// System.out.println("Selected class " + i + " td");

			System.out.println();
		}

	}

}
