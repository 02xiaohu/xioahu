
  package spring.annotation.config;
  
  import javax.sql.DataSource;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.ComponentScan;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.jdbc.core.JdbcTemplate;
  import org.springframework.jdbc.datasource.DataSourceTransactionManager;
  import org.springframework.transaction.PlatformTransactionManager;
  import org.springframework.transaction.annotation.EnableTransactionManagement;
  import com.mchange.v2.c3p0.ComboPooledDataSource;
  
  //����������ע���֧��
  @EnableTransactionManagement
  @ComponentScan("spring.annotation")
  @Configuration
  public class TxConfig {

	//����Դ
		@Bean
		public DataSource dataSource() throws Exception{
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setUser("root");
			dataSource.setPassword("mysql");
			dataSource.setDriverClass("com.mysql.jdbc.Driver");
			dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test");
			return dataSource;
		}
		@Bean
		public JdbcTemplate jdbcTemplate() throws Exception{
			//Spring��@Configuration������⴦���������м�����ķ�������ε��ö�ֻ�Ǵ������������
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
			return jdbcTemplate;
		}
		
		//ע�������������������
		@Bean
		public PlatformTransactionManager transactionManager() throws Exception{
			return new DataSourceTransactionManager(dataSource());
		}
  
  }
