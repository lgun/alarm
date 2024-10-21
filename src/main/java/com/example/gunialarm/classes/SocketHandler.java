package com.example.gunialarm.classes;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.gunialarm.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

@Component
public class SocketHandler extends TextWebSocketHandler implements InitializingBean{

    public static SocketHandler sh =null;    
    public SocketHandler() {  
        super();
        sh = this; 
    }

    // 접속한 사용자의 세션값을 담음
    public static Set<WebSocketSession> sessionSet = new HashSet<WebSocketSession>();//접속자 소켓들
    public static Queue<UserMsg> msgList = new LinkedList<>();

    public static String chatName = null;
    public static String chatEx = null;

    static public ArrayList<QueryWait> tempList;
    public static LinkedList<QueryWait> queryList = new LinkedList<>();
	public static LinkedList<QueryWait> updateQueryList = new LinkedList<>();

    // 사용자가(브라우저) 웹소켓 서버에 붙게되면 호출
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        synchronized(sessionSet){
            System.out.println("===== afterConnectionEstablished ======");
            sessionSet.add(session);
            userConnect(session);
        }
    }

    // 접속이 끊어진 사용자가 발생하면 호출
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        synchronized(sessionSet){
            sessionSet.remove(session);
        }
    }   

    // CLIENTS 객체에 담긴 세션값들을 가져와서 반복문을 통해 메시지를 발송
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = "" + message.getPayload();
        synchronized(msgList) {
            msgList.add(new UserMsg(session,msg));
        }
    }

    void cmdProcess(){
        UserMsg um = null;
        synchronized (msgList) {
            if (msgList.size() > 0) {
                um = msgList.poll();
            }
        }
        if (um == null)
            return;
        WebSocketSession session = um.session;
        String msg = um.msg;
        // =================================================} msg
        JSONParser p = new JSONParser();
        JSONObject obj = null;
        
        try {
            obj = (JSONObject) p.parse(msg);
        } catch (Exception e) {
            return;
        }
        String protocol = obj.get("protocol").toString();    
        // switch(protocol){
        //     case "login":   OnLogin(session, obj); break;
        //     case "userChatRead" : userChatRead(session,obj);break;
        // }
    }

    private void userConnect(WebSocketSession session){
        JSONObject obj=new JSONObject();
        obj.put("protocol", "doLogin");
        this.sendMessage(session, obj);
    }

    public void sendMessage(WebSocketSession session, JSONObject obj) {
        if(session == null) return;
        
        if (session.isOpen() ) {
            try {
                synchronized(session){
                    session.sendMessage(new TextMessage(obj.toString()));
                }
            } catch (Exception e) {
                Log.print("err sendMessageToMe", 1, "err");
            }
        }
    }

    public void sendMessageToUser(JSONObject obj) { // 유저 한 명에게 전송
        String idx = ""+obj.get("userIdx");
        for (WebSocketSession session : sessionSet) {
            if (session.isOpen()) {
                try {
                    Map<String, Object> m = session.getAttributes();
                    String _idx = "" + m.get("idx");

                    if (idx.equals(_idx)){
                        session.sendMessage(new TextMessage(obj.toString()));
                    }
                } catch (Exception e) {
                    Log.print("err sendMessageToUser",1,"err");
                }
            }
        }           
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
        } catch (Exception e) {
            // TODO: handle exception
        }
        Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10);
						
                        int msize = msgList.size();
                        if(msize > 0){
                            for(int i=0;i<3000;i++)
                                cmdProcess();
                        }
						
					} catch (Exception e) {
					}
				}
			}
		};
		thread.start();    	
    }
}
