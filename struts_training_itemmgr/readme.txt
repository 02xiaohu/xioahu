struts
 

mysql> select * from t_items;
+--------+----------+----------+---------+----------+------+
| itemNo | itemName | spec     | pattern | category | unit |
+--------+----------+----------+---------+----------+------+
| a1     | sdsfsf   | fwefggge | egege   | B01      | C01  |
| a2     | w2rtwt   | egtegeg  | egregg  | B03      | C02  |
| a3     | 234rw3r  | wrwrw    | w4tw4t  | B02      | C02  |
+--------+----------+----------+---------+----------+------+
 

mysql> select * from t_data_dict;
+-----+---------------+---------------+
| id  | category      | name          |
+-----+---------------+---------------+
| B01 | item_category | yiliaoqixie   |
| B02 | item_category | zhongchengyao |
| B03 | item_category | xiyao         |
| C01 | item_unit     | he            |
| C02 | item_unit     | pian          |
| C03 | item_unit     | xiang         |
+-----+---------------+---------------+

public class DataDict {

	private String id;
	
	private String name;
}

public class ItemCategory {
     //id
	private String id;
	
	//物料类别名称
	private String name;
}

public class ItemUnit  {
     //id
	private String id;
	
	//计量单位名称
	private String name;	

}

public class Item {
	
    //物料代码
	private String itemNo;
	
	//物料名称
	private String itemName;
	
	//规格
	private String spec;
	
	//型号
	private String pattern;
	
	//类型
	private ItemCategory category;
	
	//单位
	private ItemUnit unit;

