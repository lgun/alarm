package com.example.gunialarm.classes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.gunialarm.enums.QueryType;

public class QueryWait {
    public QueryType type;
	public Map map;
	public String queryName;
	public int userIdx;
	
	public QueryWait(String _qname, int _userIdx, Map _map,QueryType _type) 
	{
		this.type = _type;
		this.map =_map;
		this.queryName = _qname;
		this.userIdx = _userIdx;
	}
	
	public QueryWait deepCopy(){
		QueryWait qw = new QueryWait(this.queryName, this.userIdx, this.map,this.type);
		return qw;
	}
	
	public boolean isOldUpdate(String queryName, int userIdx){
		if(this.queryName.equals(queryName) && this.userIdx == userIdx){
			return true;
		}
		return false;
	}
	
	private static void oldUpdateDelete(String queryName, int userIdx){
		for(Iterator<QueryWait> iter = SocketHandler.updateQueryList.iterator(); iter.hasNext(); ){
			QueryWait qw = iter.next();
			if(qw.isOldUpdate(queryName, userIdx)){
				iter.remove();
				
			}
		}
	}

	public static void pushQuery(String qname, int userIdx, Map map, QueryType type){
		pushQuery(qname,userIdx,map,type,true);
	}
	
	public static void pushQuery(String qname, int userIdx, Map map, QueryType type, boolean oldDelete){
		Map<String, Object> in = new HashMap<>();
		in.putAll(map); // 인자 map 이 push이후 요소가 바뀔 수 있기 때문에 깊은복사해서 추가해줌
		QueryWait qw = new QueryWait(qname, userIdx, in, type);

		if(type == QueryType.UPDATE){
			synchronized(SocketHandler.updateQueryList){
				if(oldDelete)
					oldUpdateDelete(qname,userIdx);
				SocketHandler.updateQueryList.add(qw);
			}
		}
		else{
			synchronized(SocketHandler.queryList){
				SocketHandler.queryList.add(qw);
			}
		}
	}
}