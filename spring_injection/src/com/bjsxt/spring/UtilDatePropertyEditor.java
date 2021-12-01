
 package com.bjsxt.spring;

 import java.beans.PropertyEditorSupport;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.Date;

 /**
  * java.util.Date Ù–‘±‡º≠∆˜ 
  * @author Administrator
  *
  */
 public class UtilDatePropertyEditor extends PropertyEditorSupport {

	private String format ;
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		System.out.println("UtilDatePropertyEditor.saveAsText() -- text=" + text);
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date d = sdf.parse(text);
			this.setValue(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void setFormat(String format) {
		this.format = format;
	}

}
