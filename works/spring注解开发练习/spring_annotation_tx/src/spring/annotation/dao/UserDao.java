
  package spring.annotation.dao;

  import java.util.UUID;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.jdbc.core.JdbcTemplate;
  import org.springframework.stereotype.Repository;
  
  @Repository
  public class UserDao {
	 
	  @Autowired
	  private JdbcTemplate jdbcTemplate;
	  
	  public void insert(){
			String sql = "INSERT INTO `ibatis`(username,password) VALUES(?,?)";
			String username=UUID.randomUUID().toString().substring(0, 7);
			String password="123";
			jdbcTemplate.update(sql, username,password);
			
		}
  }
