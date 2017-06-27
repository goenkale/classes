package org.bonitasoft.development.assignUserTasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.engine.api.ApiAccessType;
import org.bonitasoft.engine.api.LoginAPI;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.api.TenantAPIAccessor;
import org.bonitasoft.engine.bpm.document.ArchivedDocument;
import org.bonitasoft.engine.bpm.document.Document;
import org.bonitasoft.engine.search.SearchOptions;
import org.bonitasoft.engine.search.SearchOptionsBuilder;
import org.bonitasoft.engine.session.APISession;
import org.bonitasoft.engine.util.APITypeManager;

public class DocumentRemoval {
	public static void main(String args[]){
		Logger logger = Logger.getLogger("DocumentRemoval");
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
			
			
			SearchOptions globalSearch = new SearchOptionsBuilder(0, 100).done();
			List<ArchivedDocument> allDocs = processAPI.searchArchivedDocuments(globalSearch).getResult();
			
			for(Document doc : allDocs){
				//archive documents
				processAPI.deleteContentOfArchivedDocument(doc.getId());
				System.out.println("document id: " + doc.getId() + " has been deleted from DOCUMENT table!");
				//not archived
				//processAPI.removeDocument(doc.getId());
			}
					    					
			loginAPI.logout(session);
		}
		catch(Exception e){
			logger.log(Level.SEVERE, "ERROR DELETING ARCHIVE DOCUMENTS", e);
		}
	}
}