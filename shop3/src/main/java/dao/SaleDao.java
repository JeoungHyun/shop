package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Cart;
import logic.Item;
import logic.Sale;
import logic.User;

@Repository //@Component(객체화) + db관련 클래스 데이베이스관련 모델클래스겠군
public class SaleDao {

	private NamedParameterJdbcTemplate template;
	private RowMapper<Sale> mapper = new BeanPropertyRowMapper<Sale>(Sale.class);
	private Map<String ,Object> param = new HashMap<>();
	
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	


	public  int getMaxSaleid() {
		 return template.queryForObject("select ifnull(max(saleid),0) from sale",param,Integer.class);
	} //Integer.class 리턴하는타입이 Integer타입이야
	
	public void insert (Sale sale) {
		String sql = "insert into sale (saleid,userid,saledate) values (:saleid,:userid,now())";
		SqlParameterSource prop =new BeanPropertySqlParameterSource(sale);
		template.update(sql, prop);
	}



	public List<Sale> list(String id) {
		String sql = "select * from sale where userid = :userid";
		param.clear();
		param.put("userid", id);
		return template.query(sql,param,mapper);
	}

}
