/**
   * ����ʽ����
   * ����Ҫ��
     1.spring-framework-4.3.9.RELEASE
     2.jdk1.6���ϵİ汾
     3.mysql-connector-java-5.0.4-bin
     4.����c3p0���ӳ� c3p0-0.9.2.1
     5.����springȫע�⿪��
   * �������
   * 1�������������
   * 		����Դ�����ݿ�������Spring-jdbcģ��
   * 2����������Դ��JdbcTemplate��Spring�ṩ�ļ����ݿ�����Ĺ��ߣ���������
   * 3���������ϱ�ע @Transactional ��ʾ��ǰ������һ�����񷽷���
   * 4�� @EnableTransactionManagement ��������ע�����������ܣ�
   * 		@EnableXXX
   * 5�������������������������;
   * 		@Bean
   * 		public PlatformTransactionManager transactionManager()
   *  
   * ԭ��
   * 1����@EnableTransactionManagement
   * 			����TransactionManagementConfigurationSelector�������лᵼ�����
   * 			�����������
   * 			AutoProxyRegistrar
   * 			ProxyTransactionManagementConfiguration
   * 2����AutoProxyRegistrar��
   * 			��������ע��һ�� InfrastructureAdvisorAutoProxyCreator �����
   * 			InfrastructureAdvisorAutoProxyCreator����
   * 			���ú��ô����������ڶ��󴴽��Ժ󣬰�װ���󣬷���һ�����������ǿ�������������ִ�з������������������е��ã�
   * 
   * 3����ProxyTransactionManagementConfiguration ����ʲô��
   * 			1����������ע��������ǿ����
   * 				1����������ǿ��Ҫ������ע�����Ϣ��AnnotationTransactionAttributeSource��������ע��
   * 				2����������������
   * 					TransactionInterceptor������������������Ϣ�������������
   * 					����һ�� MethodInterceptor��
   * 					��Ŀ�귽��ִ�е�ʱ��
   * 						ִ������������
   * 						������������
   * 							1�����Ȼ�ȡ������ص�����
   * 							2�����ٻ�ȡPlatformTransactionManager���������û�����ָ���κ�transactionmanger
   * 								���ջ�������а������ͻ�ȡһ��PlatformTransactionManager��
   * 							3����ִ��Ŀ�귽��
   * 								����쳣����ȡ������������������������ع�������
   * 								�������������������������ύ����
   * 			
   */