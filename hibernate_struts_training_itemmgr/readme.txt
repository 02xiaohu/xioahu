struts+hibernate
首先运行com.bjsxt.drp.business.util.Java中的main()方法，进行数据库初始化
然后运行该项目

mysql> select * from t_items;
+--------+----------+----------+---------+----------+------+
| itemNo | itemName | spec     | pattern | category | unit |
+--------+----------+----------+---------+----------+------+
| a1     | sdsfsf   | fwefggge | egege   | B01      | C01  |
| a2     | w2rtwt   | egtegeg  | egregg  | B03      | C02  |
| a3     | 234rw3r  | wrwrw    | w4tw4t  | B02      | C02  |
+--------+----------+----------+---------+----------+------+
<hibernate-mapping package="com.bjsxt.drp.business.itemmgr.model">
	<class name="Item" table="t_items">
		<id name="itemNo">
			<generator class="assigned"/>
		</id>
		<property name="itemName" not-null="true"/>
		<property name="spec"/>
		<property name="pattern"/>
		<many-to-one name="category"/>
		<many-to-one name="unit"/>
	</class>   
</hibernate-mapping>

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
<hibernate-mapping package="com.bjsxt.drp.business.itemmgr.model">
	<class name="DataDict" table="t_data_dict">
		<id name="id">
			<generator class="assigned"/>
		</id>
		<discriminator column="category" type="string"/>
		<property name="name" not-null="true"/>
		<subclass name="ItemCategory" discriminator-value="item_category"/>
		<subclass name="ItemUnit" discriminator-value="item_unit"/>
	</class>
</hibernate-mapping>
public class DataDict {

	private String id;
	
	private String name;
}

public class ItemCategory extends DataDict {
}

public class ItemUnit extends DataDict {
	
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
	
}

创建t_data_dict表
create table t_data_dict (id varchar(255) not null, category varchar(255) not null, name varchar(255) not null, primary key (id))
创建t_items表
create table t_items (itemNo varchar(255) not null, itemName varchar(255) not null, spec varchar(255), pattern varchar(255), category varchar(255), unit varchar(255), primary key (itemNo))
alter table t_items add index FKA06D24359F328157 (unit), add constraint FKA06D24359F328157 foreign key (unit) references t_data_dict (id)
alter table t_items add index FKA06D2435FE0C88CB (category), add constraint FKA06D2435FE0C88CB foreign key (category) references t_data_dict (id)

初始化t_data_dict表
insert into t_data_dict (name, category, id) values (?, 'item_category', ?)
insert into t_data_dict (name, category, id) values (?, 'item_category', ?)
insert into t_data_dict (name, category, id) values (?, 'item_category', ?)
insert into t_data_dict (name, category, id) values (?, 'item_unit', ?)
insert into t_data_dict (name, category, id) values (?, 'item_unit', ?)
insert into t_data_dict (name, category, id) values (?, 'item_unit', ?)


查询t_data_dict表
itemCategoryList = session.createQuery("from ItemCategory a order by a.id").list();
select itemcatego0_.id as id0_, itemcatego0_.name as name0_ from t_data_dict itemcatego0_ where itemcatego0_.category='item_category' order by itemcatego0_.id
select itemunit0_.id as id0_, itemunit0_.name as name0_ from t_data_dict itemunit0_ where itemunit0_.category='item_unit' order by itemunit0_.id

	
	
	

