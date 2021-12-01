package com.hibernate;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
public class ExportDB {

	public static void main(String[] args) {
    
		//¶ÁÈ¡hibernate.cfg.xmlÎÄ¼þ
		Configuration cfg=new Configuration().configure();
        
		SchemaExport export=new SchemaExport(cfg);
	
		export.create(true, true);
	}

}
