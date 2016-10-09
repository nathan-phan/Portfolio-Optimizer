package com.cs490.vo;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GoogleStock {
	private String id;
	private String l_fix;
	private String l_curr;
	private String s;
	private String ltt;
	private String lt;
	private String lt_dts;
	private String c_fix;
	private String cp_fix;
	private String ccol;
	private String t; //symbol
	private String e; //exchange
	private String l; //closing price - current price
	private String c; // change
	private String cp; // change %
	private String pcls_fix; //previous closing;
	private String name;
	private String symbol; //symbol
	private String exchange; //exchange
	private BigDecimal price; //closing price - current pice
	private BigDecimal change; // change
	private BigDecimal changePercent; // change %
	private BigDecimal previousClosingPrice;
	
	@SuppressWarnings("unchecked")
	public GoogleStock(String symbol) throws JsonParseException, JsonMappingException, IOException{
		String jsonContent = IOUtils.toString(new URL("http://finance.google.com/finance/info?infotype=infoquoteall&q=" + symbol), Charset.forName("UTF-8"));
		jsonContent = jsonContent.substring(5);
		ObjectMapper objectMapper = new ObjectMapper();
	  Map<String,Object> map = objectMapper.readValue(jsonContent, Map.class);
	  this.id = (String)map.get("id");
		this.l_fix = (String)map.get("l_fix");
		this.l_curr = (String)map.get("l_cur");
		this.s = (String)map.get("s");
		this.ltt = (String)map.get("ltt");
		this.lt = (String)map.get("lt");
		this.lt_dts = (String)map.get("lt_dts");
		this.c_fix = (String)map.get("c_fix");
		this.cp_fix = (String)map.get("cp_fix");
		this.ccol = (String)map.get("ccol");
		this.t = (String)map.get("t"); //symbol
		this.e = (String)map.get("e"); //exchange
		this.l = (String)map.get("l"); //closing price - current pice
		this.c = (String)map.get("c"); // change
		this.cp = (String)map.get("cp"); // change %
		this.pcls_fix = (String)map.get("pcls_fix"); //previous closing;
		this.name = (String)map.get("name");
	}
	
	public String getPcls_fix() {
		return pcls_fix;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getL_fix() {
		return l_fix;
	}
	public void setL_fix(String l_fix) {
		this.l_fix = l_fix;
	}
	public String getL_curr() {
		return l_curr;
	}
	public void setL_curr(String l_curr) {
		this.l_curr = l_curr;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	public String getLtt() {
		return ltt;
	}
	public void setLtt(String ltt) {
		this.ltt = ltt;
	}
	public String getLt() {
		return lt;
	}
	public void setLt(String lt) {
		this.lt = lt;
	}
	public String getLt_dts() {
		return lt_dts;
	}
	public void setLt_dts(String lt_dts) {
		this.lt_dts = lt_dts;
	}
	public String getC_fix() {
		return c_fix;
	}
	public void setC_fix(String c_fix) {
		this.c_fix = c_fix;
	}
	public String getCp_fix() {
		return cp_fix;
	}
	public void setCp_fix(String cp_fix) {
		this.cp_fix = cp_fix;
	}
	public String getCcol() {
		return ccol;
	}
	public void setCcol(String ccol) {
		this.ccol = ccol;
	}
	public void setPcls_fix(String pcls_fix) {
		this.pcls_fix = pcls_fix;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getE() {
		return e;
	}
	public void setE(String e) {
		this.e = e;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	
	public void setSymbol(String symbol) {
		this.t = symbol;
	}
	
	public String getSymbol() {
		return t;
	}
	
	public String getExchange() {
		return e;
	}
	public BigDecimal getPrice() {
		return new BigDecimal(l.replace(",",""));
	}
	public BigDecimal getChange() {
		return new BigDecimal(c);
	}
	public BigDecimal getChangePercent() {
		return new BigDecimal(cp);
	}
	public BigDecimal getPreviousClosingPrice() {
		return new BigDecimal(pcls_fix.replace(",",""));
	}

	public String getName() {
		return name;
	}
}
