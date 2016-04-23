package dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dynamo.CampaignUser;
import model.dynamo.CostExplorerModel;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlQuery;
import com.avaje.ebean.SqlRow;
import com.ibm.icu.text.SimpleDateFormat;

public class CostExplorerDao extends BaseDao {

	public static double prepare(final String startDate ,final String endDate, CampaignUser campaignUser) throws ParseException 
	{

		double TotalCost = 0;
		String planStr = "0,0.015,0.004";
		String[] parsed = planStr.split("\\|");
		List<Plan> plans = new ArrayList<CostExplorerDao.Plan>();
		for(String element:parsed) {
			String[] costElements = element.split(",");
			Plan plan = new Plan();
			plan.timestamp = Long.valueOf(costElements[0]);
			plan.lookupCost = Double.valueOf(costElements[2]);
			plan.emailCost = Double.valueOf(costElements[1]);
			plans.add(plan);
		}

		Collections.sort(plans);
		try {
			TotalCost = persistCost(startDate, endDate, campaignUser, plans);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}
		return TotalCost;
	}

	public static double persistCost(String startDate , String endDate, CampaignUser campaignUser, List<Plan> plans) throws ParseException {
		SqlQuery query = Ebean.createSqlQuery("Select * from email_job where list = '"+campaignUser.getList()+"'"
				+ " and submit_time >= '"+startDate+" 00:00:00' and submit_time <= '"+endDate+" 23:59:59'");
		List<SqlRow> output = query.findList();
		System.out.println(query);
		System.out.println(output.size());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		double totalCost=0;
		

		for(SqlRow row:output) {
			String submitTime = row.getString("submit_time");
			String campaign = row.getString("campaign");
			Integer successRecords = row.getInteger("success_records");
			Integer unique_records = row.getInteger("unique_records");
			
			System.out.println(successRecords+" "+unique_records);
			
			String segment = row.getString("segment");
			Date submitDate = sdf.parse(submitTime);
			double perEmailCost = 0.0;
			double lookupCost = 0.0;
			for(Plan plan:plans) {
				if(submitDate.getTime()<plan.timestamp) 
					break;
				perEmailCost = plan.emailCost;
				lookupCost = plan.lookupCost;
			
			System.out.println(plan.emailCost+" "+plan.lookupCost);
			}
			double cost = 0.0;
			if(unique_records>successRecords)
				cost = (double)successRecords*perEmailCost + (double)(unique_records-successRecords)*lookupCost;
			else
				cost = (double)successRecords*perEmailCost;
			
			System.out.println(cost+ "   " +totalCost);
			totalCost = totalCost + cost;
		}
		return totalCost;
}

	public static class Plan implements Comparable<Plan>{
		public double emailCost;
		public Long timestamp;
		public double lookupCost;

		@Override
		public int compareTo(Plan o) {
			return this.timestamp.compareTo(o.timestamp);
		}
	}

}
