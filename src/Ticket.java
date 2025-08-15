public class Ticket {
    private int ticketId;
    private int eventId;
    private int customerId;
    private String seatNo;
    private double price;

    public Ticket(int ticketId, int eventId, int customerId, String seatNo, double price) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.customerId = customerId;
        this.seatNo = seatNo;
        this.price = price;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getSeatNo() {
        return seatNo;
    }

    public double getPrice() {
        return price;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
