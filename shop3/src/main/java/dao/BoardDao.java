package dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import logic.Board;
import logic.Sale;

@Repository
public class BoardDao {

	
	private NamedParameterJdbcTemplate template;
	private RowMapper<Sale> mapper = new BeanPropertyRowMapper<Sale>(Sale.class);
	private Map<String ,Object> param = new HashMap<>();
	
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		template = new NamedParameterJdbcTemplate(dataSource);
	}
	public int maxnum() {
		 return template.queryForObject("select ifnull(max(num),0) from board",param,Integer.class);
	}
	public void insert(Board board) {
		SqlParameterSource prop =
				new BeanPropertySqlParameterSource(board);
		String sql = "insert into board (num , name , pass , subject , content , regdate , file1 , readcnt , grp ,grplevel , grpstep) values (:num , :name , :pass , :subject , :content , now() , :fileurl , 0 , :grp , :grplevel , :grpstep)" ;
		template.update(sql, prop);
	}

}
