package controllers;

import java.util.HashMap;

import javafx.application.Platform;
import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class tokenate<E> implements TeamspeakActionListener{
		
	JTS3ServerQuery query;
	mainCtrl gui;
	
	boolean working;
	int generated;
	int countToGen;
	HashMap<String, String> servers;

	public tokenate(mainCtrl mainCtrl) {
		// TODO Auto-generated constructor stub
		query = new JTS3ServerQuery();
		
		// Connect to TS3 Server, set your server data here
		if (!query.connectTS3Query("localhost", 25639))
		{
			echoError();
			return;
		}
		
		String schandlerid = query.doCommand("currentschandlerid").get("response");
		query.doCommand("clientnotifyregister schandlerid=" + schandlerid.substring(12) + " event=notifytokenadd");
		
		query.setTeamspeakActionListener(this);
		generated = 1;
		
		working = false;
		gui = mainCtrl;
	}

	@Override
	public void teamspeakActionPerformed(String eventType,
			HashMap<String, String> eventInfo) {
		// TODO Auto-generated method stub
//		System.out.println(eventInfo.get("token" ));
		if (eventType.toString().equals("notifytokenadd")) {
			
			Platform.runLater(new Runnable() {
				public void run() {
					gui.txtOut.appendText(generated + " - " + eventInfo.get("token").toString() + System.lineSeparator());	
					gui.barProgress.progressProperty().set((double)generated / countToGen);
					generated++;
					if (generated > countToGen) {
						working = false;
						generated = 1;
					}
				}
			});
//			System.out.println(generated + eventInfo.get("token").toString());
			
		}
		
	}
	
	public void getServers() {
		// TODO Auto-generated method stub 
		System.out.println(gui.srvList.getSelectionModel().selectedItemProperty().getValue());
		String[] ts3 = query.doCommand("serverconnectionhandlerlist").get("response").split("\\|"); 
		servers = new HashMap<String, String>();
//		gui.srvList.getItems().clear();
		for (int i = 0; i < ts3.length; i++) {
//			System.out.println(ts3[i].substring(12));
			
			System.out.println(query.doCommand("use " + ts3[i]));
			echoError();
			String incom = query.doCommand("serverconnectinfo").get("response");
			servers.put(query.parseLine(incom).get("ip"), ts3[i]);

//			System.out.println(servers); 
		}
		Platform.runLater(new Runnable() {
			public void run() {
				gui.srvList.getItems().clear();
				gui.srvList.getItems().addAll(servers.keySet());
				gui.srvList.getSelectionModel().selectFirst();
			}
		});
		
		
		 
//		gui.srvList.getItems().clear();
//		for (int i = 0; i < ts3.length; i++) {
//			System.out.println(ts3[i].substring(12));
//
//			System.out.println(query.doCommand("use " + ts3[i]));
//			echoError();
//			String incom = query.doCommand("serverconnectinfo").get("response");
//
//			Platform.runLater(new Runnable() {
//				public void run() {
//					 gui.srvList.getItems().add(query.parseLine(incom)); 
//				}
//			});
//			System.out.println(); 
//		}
		
		
	}


	public void genTokens(int count, int gid) {
		// TODO Auto-generated method stub
		if(working){
			return;
		}else {
			countToGen = count;
			gui.txtOut.clear();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					working = true;
					for (int i = 0; i < count; i++) {
						query.doCommand("tokenadd tokentype=0 tokenid1=" + gid + " tokenid2=0");
						echoError();
					}
				}
			}).start();			
		}

	}
	
	public void genTokens(int count, int gid, int cid) {
		if(working){
			return;
		}else {
			countToGen = count;
			gui.txtOut.clear();
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					working = true;
					for (int i = 0; i < count; i++) {
						query.doCommand("tokenadd tokentype=1 tokenid1=" + gid + " tokenid2=" + cid);
						echoError();
					}
				}
			}).start();			
		}
	}
	

	
	void echoError()
	{
		String error = query.getLastError();
		if (error != null)
		{
			System.out.println(error);
			if (query.getLastErrorPermissionID() != -1)
			{
				HashMap<String, String> permInfo = query.getPermissionInfo(query.getLastErrorPermissionID());
				if (permInfo != null)
				{
					System.out.println("Missing Permission: " + permInfo.get("permname"));
				}
			}
		}
	}
}
