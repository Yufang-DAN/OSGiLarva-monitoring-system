package fr.inria.amazones.logosng.logging;

import java.util.Date;

public final class Session implements java.io.Serializable {


    private final String sessionId;
    private final Date date;
    private final long startingThreadId;

    public static Session buildRealSession(long sessionId, long startingThreadId){
      return new Session ("S", sessionId, startingThreadId);
    }

    public static Session buildFakeSession(long sessionId, long startingThreadId){
      return new Session ("F", sessionId, startingThreadId);
    }

    private Session(String kindOf, long sessionId, long startingThreadId) {
        this.sessionId = kindOf+sessionId;
        this.date = new Date();
        this.startingThreadId = startingThreadId;
    }

    public String getSessionId() {
      return this.sessionId;
    }

    public Date getDate() {
        return date;
    }

    public long getStartingThreadId() {
        return startingThreadId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Session session = (Session) o;
        return startingThreadId == session.startingThreadId && date.equals(session.date);
    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + (int) (startingThreadId ^ (startingThreadId >>> 32));
        return result;
    }

    @Override
    public String toString() {
      return this.sessionId;
    }

    public String toDetailedString() {
        return "Session{"
                + "date=" + date
                + ", startingThreadId=" + startingThreadId
                + '}';
    }
}
