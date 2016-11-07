package com.cs490.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cs490.dao.PortfolioDAO;

public class StockStatsGenerator implements ServletContextListener {

	@SuppressWarnings("resource")
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ClassLoader classLoader = getClass().getClassLoader();
		File covFile = null;
		File returnFile = null;
		try {
			covFile = new File(classLoader.getResource("covariances.csv").toURI());
			returnFile = new File(classLoader.getResource("returns.csv").toURI());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		LinkedHashMap<String, Double> covMap = new LinkedHashMap<String, Double>();
		LinkedHashMap<String, Double> returnMap = new LinkedHashMap<String, Double>();
		try {
			ArrayList<String> stocks = new ArrayList<String>();
			br = new BufferedReader(new FileReader(covFile));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] tokens = line.split(cvsSplitBy);
				if(tokens[0].equals("N/A")){
					for(int i=1; i<tokens.length;i++){
						stocks.add(tokens[i]);
					}
					continue;
				}
				String pair="";
				for(int i=1;i<tokens.length;i++){
					pair = tokens[0] + "-" + stocks.get(i-1);
					double cov = Double.parseDouble(tokens[i]);
					covMap.put(pair, cov);
				}
			}
			br = new BufferedReader(new FileReader(returnFile));
			while ((line = br.readLine()) != null) {
				String[] pair = line.split(cvsSplitBy);
				returnMap.put(pair[0], Double.parseDouble(pair[1]));
			}
			sce.getServletContext().setAttribute("returnMap", returnMap);
			sce.getServletContext().setAttribute("covarianceMap", covMap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
