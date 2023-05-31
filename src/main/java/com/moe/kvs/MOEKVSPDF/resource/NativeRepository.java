package com.moe.kvs.MOEKVSPDF.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.stereotype.Component;

//import com.example.MOERADTEACHER.util.QueryResult;

@Component
public class NativeRepository {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
//	@Autowired
//	 @Qualifier("jdbcTemplate2")
//	 private JdbcTemplate jdbcTemplate2;
	
	  public QueryResult executeQueries(String queryData){    
		  QueryResult queryResult =new QueryResult();
		  try {
//		  // System.out.println("queryData---->"+queryData);
	        MapSqlParameterSource parameters = new MapSqlParameterSource();
	        List<Object> columnList=new ArrayList<Object>();
	        List<Object> columnDataType=new ArrayList<Object>();
	        List<Map<String, Object>> rows = jdbcTemplate.queryForList(queryData);
	     
	        SqlRowSet rs = jdbcTemplate.queryForRowSet(queryData);
	        SqlRowSetMetaData rsmd = rs.getMetaData();
	        int columnNo = rsmd.getColumnCount();
	        for ( int i = 1; i <= columnNo; i++ )
	        {
	         columnList.add(rsmd.getColumnLabel(i));
	         columnDataType.add(rsmd.getColumnTypeName(i));
	        }
	        queryResult.setColumnName(columnList);
	        queryResult.setRowValue(rows);
	        queryResult.setColumnDataType(columnDataType);
		  }catch(Exception ex) {
			  ex.printStackTrace();
		  }
			return queryResult;
	    }
	  
	  public int updateQueries(String queryData){  
//		  // System.out.println("Update Query--->"+queryData);
		  try {
	      return  jdbcTemplate.update(queryData);
		  }catch(Exception ex) {
			  ex.printStackTrace();
			  return 0;
		  }
			
	    }
	  
	  
	  public Map<String,Object> insertQueries(String queryData){    
		  Map<String,Object> mp=new HashMap<String,Object>();
		  try {
	        jdbcTemplate.execute(queryData);
	        mp.put("status", 1);
		  }catch(Exception ex) {
			  ex.printStackTrace();
			  mp.put("status", 0);
		  }
			return mp;
	    }
}
