/**
 * 
 */
package org.mdkt.maildist.server;

import java.util.ArrayList;
import java.util.Iterator;

import org.mdkt.maildist.client.dto.DistList;
import org.mdkt.maildist.client.dto.DistListMember;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

/**
 * From google data store
 * 
 * @author trung
 *
 */
public class GaeDistListRegsitry implements DistListRegistry {
	private static final String DIST_LIST_TYPE = "distributionList";
	private static final String DIST_LIST_USER_ID = "userId";
	private static final String DIST_LIST_ID = "distListId";
	
	private static final String DIST_LIST_MEMBER_TYPE = "distributionListMemember";
	private static final String DIST_LIST_MEMBER_ID = "distListMemberId";
	private static final String DIST_LIST_MEMBER_NAME = "name";
	private static final String DIST_LIST_MEMBER_EMAIL = "email";
	
	@Override
	public void deleteDistLists(ArrayList<String> distListIds) {
		ArrayList<Key> keys = new ArrayList<Key>();
    	for (String id : distListIds) {
    		keys.add(KeyFactory.createKey(DIST_LIST_TYPE, id));
    	}
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	datastore.delete(keys);
	}
	
	@Override
	public ArrayList<DistList> findAllDistLists(String userId) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	Query query = new Query(DIST_LIST_TYPE);
    	query.addFilter(DIST_LIST_USER_ID, FilterOperator.EQUAL, userId);
    	// TODO paging???
    	Iterator<Entity> entities = datastore.prepare(query).asIterator();
    	ArrayList<DistList> list = new ArrayList<DistList>();
    	while (entities.hasNext()) {
    		list.add(fromEntity(entities.next()));
    	}
    	return list;
	}

	private DistList fromEntity(Entity entity) {
		DistList dl = new DistList();
		dl.setDistListId((String)entity.getProperty(DIST_LIST_ID));
		dl.setUserId((String)entity.getProperty(DIST_LIST_USER_ID));
		dl.setNoOfMembers(countMember(dl.getDistListId()));
		return dl;
	}

	private DistListMember fromEntityToDistListMember(Entity entity) {
		DistListMember dl = new DistListMember();
		dl.setDistListId((String)entity.getProperty(DIST_LIST_ID));
		dl.setName((String)entity.getProperty(DIST_LIST_MEMBER_NAME));
		dl.setEmail((String)entity.getProperty(DIST_LIST_MEMBER_EMAIL));
		dl.setDistListMemberId((String)entity.getProperty(DIST_LIST_MEMBER_ID));
		return dl;
	}

	private int countMember(String distListId) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	Query query = new Query(DIST_LIST_MEMBER_TYPE);
    	query.addFilter(DIST_LIST_ID, FilterOperator.EQUAL, distListId);
    	return datastore.prepare(query).countEntities(FetchOptions.Builder.withDefaults());
	}
	
	@Override
	public void saveDistList(DistList distList) {
		Key key = KeyFactory.createKey(DIST_LIST_TYPE, distList.getDistListId());
		Entity e = new Entity(key);
		e.setProperty(DIST_LIST_ID, distList.getDistListId());
		e.setProperty(DIST_LIST_USER_ID, distList.getUserId());
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(e);
	}
	
	@Override
	public String findDistList(String distListId) {
		Key key = KeyFactory.createKey(DIST_LIST_TYPE, distListId);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		try {
			Entity e = datastore.get(key);
			return (String) e.getProperty(DIST_LIST_USER_ID);
        } catch (EntityNotFoundException e) {
            return null;
        }
	}
	
	@Override
	public void deleteDistListMembers(ArrayList<String> distListMemberIds) {
		ArrayList<Key> keys = new ArrayList<Key>();
    	for (String id : distListMemberIds) {
    		keys.add(KeyFactory.createKey(DIST_LIST_MEMBER_TYPE, id));
    	}
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    	datastore.delete(keys);
	}
	
	@Override
	public ArrayList<DistListMember> findDistListMembers(String distListId,
			String userEmail) {
		String email = findDistList(distListId);
		if (email != null && email.equalsIgnoreCase(userEmail)) {
			return findDistListMembers(distListId);
		} else {
			return new ArrayList<DistListMember>();
		}
	}
	
	@Override
	public ArrayList<DistListMember> findDistListMembers(String distListId) {
		if (findDistList(distListId) != null) { // make sure user owns this dist list
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	    	Query query = new Query(DIST_LIST_MEMBER_TYPE);
	    	query.addFilter(DIST_LIST_ID, FilterOperator.EQUAL, distListId);
	    	// TODO paging???
	    	Iterator<Entity> entities = datastore.prepare(query).asIterator();
	    	ArrayList<DistListMember> list = new ArrayList<DistListMember>();
	    	while (entities.hasNext()) {
	    		list.add(fromEntityToDistListMember(entities.next()));
	    	}
	    	return list;
		} else {
			return new ArrayList<DistListMember>();
		}
	}
	
	private Entity distListMemberToEntity(DistListMember m) {
		Key key = KeyFactory.createKey(DIST_LIST_MEMBER_TYPE, m.getDistListMemberId());
		Entity e = new Entity(key);
		e.setProperty(DIST_LIST_MEMBER_ID, m.getDistListMemberId());
		e.setProperty(DIST_LIST_MEMBER_NAME, m.getName());
		e.setProperty(DIST_LIST_MEMBER_EMAIL, m.getEmail());
		e.setProperty(DIST_LIST_ID, m.getDistListId());
		return e;
	}
	
	@Override
	public void updateDistListMembers(ArrayList<DistListMember> distListMembers) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		for (DistListMember m : distListMembers) {
			Entity entity = distListMemberToEntity(m);
			datastore.put(entity);
		}
	}
	
}
