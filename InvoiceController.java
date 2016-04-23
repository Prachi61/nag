package controllers;

import java.util.List;
import java.util.ArrayList;

import play.mvc.Result;

import views.html.billing.bill;
//import views.html.billing.ex;
import views.html.commons.template;

import dao.CampaignUserDao;

public class InvoiceController {

	public static Result index(){

		List<String> users = new ArrayList<String>();
		//List<String> users1 = new ArrayList<String>();
		users = CampaignUserDao.getUsers();
		/*users1.add("scjbaj");
		users1.add("sdjdahku");
		return play.mvc.Results.ok(template.render(ex.render(users1),"Billing tab: View and download bill from here"));
		*/
		return play.mvc.Results.ok(template.render(bill.render(users),"Billing tab: View and download bill from here"));
	}
}
