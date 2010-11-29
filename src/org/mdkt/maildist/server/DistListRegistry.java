/**
 * 
 */
package org.mdkt.maildist.server;

import java.util.ArrayList;

import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.dto.DistListMember;

/**
 * 
 * All in one service for dist list and member
 * @author trung
 *
 */
public interface DistListRegistry {

	void deleteDistLists(ArrayList<String> distListIds);

	ArrayList<DistList> findAllDistLists(String userId);

	void saveDistList(DistList distList);
	
	String findDistList(String distListId);

	void deleteDistListMembers(ArrayList<String> distListMemberIds);

	ArrayList<DistListMember> findDistListMembers(String distListId);

	void updateDistListMembers(ArrayList<DistListMember> distListMembers);

	ArrayList<DistListMember> findDistListMembers(String distListId,
			String userEmail);
}
