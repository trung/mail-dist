/**
 * 
 */
package org.mdkt.maildist.client.rpc;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.dto.DistListMember;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author trung
 *
 */
public interface DistListServiceAsync {

	void createDistList(DistList distList, AsyncCallback<Void> callback);

	void deleteDistListMembers(ArrayList<String> distListMemberIds,
			AsyncCallback<Void> callback);

	void deleteDistLists(ArrayList<String> distListIds,
			AsyncCallback<Void> callback);

	void getDistListMembers(String distListId,
			AsyncCallback<ArrayList<DistListMember>> callback);

	void getDistLists(AsyncCallback<ArrayList<DistList>> callback);

	void updateDistListMember(ArrayList<DistListMember> distListMembers,
			AsyncCallback<Void> callback);

	void findDistList(String distListName,
			AsyncCallback<Boolean> callback);
	

}
