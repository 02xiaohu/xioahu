struts 
��������com.bjsxt.drp.business.util.Java�е�main()�������������ݿ��ʼ��
Ȼ�����и���Ŀ

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
 
public class ItemCategory  
{  //����id
	private String id;
	
	//�����������
	private String name;
}

public class ItemUnit  
 {  //����id
	private String id;
	
	//������λ����
	private String name;
	
}

public class Item {
	
    //���ϴ���
	private String itemNo;
	
	//��������
	private String itemName;
	
	//���
	private String spec;
	
	//�ͺ�
	private String pattern;
	
	//����
	private ItemCategory category;
	
	//��λ
	private ItemUnit unit;

