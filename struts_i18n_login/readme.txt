1��struts���ʻ�������
	* ��struts-config.xml�ļ��м��룺<message-resources parameter="MessageResources" />
	
2���ṩ��ͬ�汾�Ĺ��ʻ���Դ�ļ���������Ҫ����native2asciiת����unicode

3����jsp�в���<bean:message>��ǩ����ȡ���ʻ���Ϣ�ı�

4���˽�����strutsĬ�Ͻ�locale�ŵ�session�е����ԣ���ɲ��ñ�̵ķ�ʽ�л���������
	* �μ���ChangeLanguageAction.java
	
5����Ϣ�ı��Ĺ��ʻ����������������裺
	* �������ʻ���Ϣ
	* ���ݹ��ʻ���Ϣ
	* ��ʾ���ʻ���Ϣ
	
��δ������ʻ���Ϣ��
	���ActionMessage��ActionMessages�������������
	
��δ��ݹ��ʻ���Ϣ��
	* ����saveMessage()������ͨ��Ϣ������saveErrors���ݴ�����Ϣ
	
�����ʾ���ʻ���Ϣ��
	ͨ��<html:messages>��ǩ��ʾ��Ϣ��������ʾ��ͨ��Ϣ�ʹ�����Ϣ��
	ͨ��<html:errors>��ʾ��Ϣ��ֻ����ʾ������Ϣ��		