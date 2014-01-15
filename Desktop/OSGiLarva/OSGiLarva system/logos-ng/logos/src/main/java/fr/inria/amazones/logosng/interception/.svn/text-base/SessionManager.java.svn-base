package fr.inria.amazones.logosng.interception;

import fr.inria.amazones.logosng.logging.Session;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class SessionManager {
    private long sessionId=0;
    private long fakeSessionId=0;

    private static SessionManager sessionManager = null;

    private final Map<Long, Session> sessions = new HashMap<Long, Session>();

    private SessionManager(){}

    public static SessionManager getInstance(){
      if (sessionManager == null){
        sessionManager = new SessionManager();
      }
      return sessionManager;
    }

    public void registerSession(Long threadId) {
        final Session session = Session.buildRealSession(sessionId++, threadId);
        sessions.put(threadId, session);
    }

    public void unregisterSession(Long threadId) {
        if (sessions.remove(threadId) == null) {
            throw new IllegalArgumentException(threadId + " is not an active thread identifier");
        }
    }

    public Set<Session> getCurrentSessions() {
        return new HashSet<Session>(sessions.values());
    }

    public Session getSession(Long threadId){
      if (sessions.containsKey(threadId)){
        return sessions.get(threadId);
      } else {
        final Session session = Session.buildFakeSession(fakeSessionId++, threadId);
        sessions.put(threadId, session);
        return session;
      }
    }
}
