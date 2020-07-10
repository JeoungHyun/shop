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

import logic.User;


@Repository
public class UserDao {

	private NamedParameterJdbcTemplate template;
	private RowMapper<User> mapper = new BeanPropertyRowMapper<User>(User.class);
	private Map<String ,Object> param = new HashMap<>();


	@Autowired
	public void setDataSource(DataSource dataSource) {
		
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}


	public void insert(User user) {
		
		SqlParameterSource prop = new BeanPropertySqlParameterSource(user);
		String sql = "insert into useraccount (userid,username,password,birthday,phoneno,postcode,address,email) values (:userid,:username,:password,:birthday,:phoneno,:postcode,:address,:email)";
		template.update(sql,prop);
	}


	public User selectOne(String userid) {
		param.clear();
		param.put("userid",userid);
		return template.queryForObject("select * from useraccount where userid=:userid", param, mapper);
	}


	public void update(User user) {
		String sql="update useraccount set username = :username , phoneno=:phoneno , postcode=:postcode, address=:address ,email=:email , birthday=:birthday where userid=:userid" ;
		SqlParameterSource prop = new BeanPropertySqlParameterSource(user);
		template.update(sql,prop);
		
	}


	public void delete(String userid) {
		String sql = "delete from useraccount where userid=:userid";
		param.clear();
		param.put("userid",userid);
		template.update(sql,param);
		
	}


	public List<User> userList() {
	
		 return template.query("select * from useraccount where userid not like 'admin' ",mapper);
		
	}


	public List<User> userList(String[] idchks) {
		//select * from useraccount where user id in('test1','tset3')
		
		String sql="select * from useraccount where userid in(";
		for(int i= 0 ; i< idchks.length ;i++) {
			sql+="'";
			String a = idchks[i];
			sql+=a+"'";
			if(i!=idchks.length-1) sql+=","; 
		}
		sql+= ")";
		System.out.println(sql);
		
//		String ids="";
//		for(int i = 0 ; i<idchks.length ;i++) {
//			ids +="'" + idchks[i]+ ((i==idchks.length-1)?"'":",");
//		}
//		String sql ="select * from useraccount where userid in(" + ids+")";
//		
//		
		
		return template.query(sql,mapper);
	}
	
	
}
