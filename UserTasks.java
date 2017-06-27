package org.bonitasoft.development.assignUserTasks;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstance;
import org.bonitasoft.engine.bpm.flownode.HumanTaskInstanceSearchDescriptor;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.search.SearchResult;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;

public class UserTasks {
	public static void main(String args[]){
		Logger logger = Logger.getLogger("TestUserList");
		try{		
			Map<String, String> map = new HashMap<String, String>();
			map.put("server.url", "http://localhost:8080");
			map.put("application.name", "bonita");
			APITypeManager.setAPITypeAndParams(ApiAccessType.HTTP, map);
			
			
			// Set the username and password
			final String username = "giovanna.almeida";
			final String password = "bpm";
			// get the LoginAPI using the TenantAPIAccessor
			final LoginAPI loginAPI = TenantAPIAccessor.getLoginAPI();
			// log in to the tenant to create a session
			final APISession session = loginAPI.login(username, password);			
			
			final ProcessAPI processAPI = TenantAPIAccessor.getProcessAPI(session);
			
			//tasks assigned to this user
			//SearchOptions so = new SearchOptionsBuilder(0, 100).filter(HumanTaskInstanceSearchDescriptor.ASSIGNEE_ID, session.getUserId()).done();
			SearchOptions so = new SearchOptionsBuilder(0, 100).done();
			
			//pending tasks
//			SearchResult<HumanTaskInstance> userTaskList = processAPI.searchPendingTasksForUser(session.getUserId(), so);
//			for(int i=0; i< userTaskList.getResult().size(); i++) {
//				System.out.println(userTaskList.getResult().get(i));
//			}
				
			//assigned and pending tasks for processDefinitionId
			SearchResult<HumanTaskInstance> userAssignedTaskList = processAPI.searchAssignedAndPendingHumanTasksFor(4859600331919021568L, session.getUserId(), so);
						
			System.out.println("\nAssigned + Pending: \n");
			for(int i=0; i< userAssignedTaskList.getResult().size(); i++) {
				processAPI.assignUserTask(userAssignedTaskList.getResult().get(i).getId(), 4);
				System.out.println(userAssignedTaskList.getResult().get(i));
			}			
					
			loginAPI.logout(session);
		}
		catch(Exception e){
			logger.log(Level.SEVERE, "ERROR RETRIEVING USERS", e);
		}
	}
}