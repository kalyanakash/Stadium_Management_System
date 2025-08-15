public class Event {
    private int eventId;
    private String eventName;
    private String eventDate;
    private String stadiumName;

    public Event(int eventId, String eventName, String eventDate, String stadiumName) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.stadiumName = stadiumName;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getStadiumName() {
        return stadiumName;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setStadiumName(String stadiumName) {
        this.stadiumName = stadiumName;
    }
}
