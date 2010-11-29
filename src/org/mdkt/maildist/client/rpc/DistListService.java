package org.mdkt.maildist.client.rpc;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.dto.DistListMember;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc/distListService")
public interface DistListService extends RemoteService {
	
	void deleteDistLists(ArrayList<String> distListIds);
	void deleteDistListMembers(ArrayList<String> distListMemberIds);
	
	ArrayList<DistList> getDistLists();
	ArrayList<DistListMember> getDistListMembers(String distListId);
	
	void createDistList(DistList distList);
	void updateDistListMember(ArrayList<DistListMember> distListMembers);
	
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static DistListServiceAsync instance;
		public static DistListServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(DistListService.class);
			}
			return instance;
		}
	}

	boolean findDistList(String distListName);
}
