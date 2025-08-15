
# Stadium Management System using Java Swing and MySQL

## Project Objective

A desktop application to manage stadium events, ticket bookings, seat pricing, and customer information. The system provides user-friendly panels for both administrators and customers, with all data stored securely in a MySQL database.

## Features & Functionalities

### 1. Event Management
- Add, edit, delete, and view events (name, date, stadium).
- Event overview tab for users to browse events and seat details.

### 2. Stadium Management
- View all stadiums and their locations.
- Stadium tab in user panel: select a stadium to view all seats and prices for that stadium.

### 3. Seat & Price Management
- Unique seat numbers for each stadium (e.g., A1-D10 for Stadium 1, E1-H10 for Stadium 2, etc.).
- Seat prices mapped per stadium and seat type.
- Admin panel displays all seat prices with stadium info.
- Customer panel shows seat, stadium, price, and availability in dropdowns.

### 4. Customer Management
- Register, update, and delete customers.
- Search customers by name or ID.

### 5. Ticket Management
- Book tickets for events and seats.
- Assign seat numbers and calculate ticket prices.
- Cancel tickets.
- Display booked tickets with all details.

### 6. Reporting & Viewing
- Display available and booked seats for each event and stadium.
- Search and filter events, customers, tickets, and seats.

## Technology Stack

- **Frontend:** Java Swing (GUI)
- **Backend:** MySQL
- **Database Connectivity:** JDBC
- **IDE:** NetBeans, Eclipse, or VS Code


## Database Design

### Tables

#### Stadium Table
| Column        | Type     | Description         |
|---------------|----------|---------------------|
| stadium_id    | INT PK   | Unique Stadium ID   |
| stadium_name  | VARCHAR  | Stadium Name        |
| location      | VARCHAR  | Stadium Location    |

#### Event Table
| Column       | Type     | Description   |
|--------------|----------|-------------- |
| event_id     | INT PK   | Unique ID     |
| event_name   | VARCHAR  | Name of event |
| date         | DATE     | Date of event |
| stadium      | VARCHAR  | Stadium name  |

#### Customer Table
| Column      | Type     | Description      |
|-------------|----------|-----------------|
| customer_id | INT PK   | Unique ID       |
| name        | VARCHAR  | Customer Name   |
| email       | VARCHAR  | Email Address   |
| phone       | VARCHAR  | Phone Number    |

#### Ticket Table
| Column      | Type     | Description      |
|-------------|----------|-----------------|
| ticket_id   | INT PK   | Unique ID       |
| event_id    | INT FK   | Linked Event    |
| customer_id | INT FK   | Linked Customer |
| seat_no     | VARCHAR  | Seat Number     |
| price       | DECIMAL  | Ticket Price    |

#### Seats Table
| Column      | Type     | Description      |
|-------------|----------|-----------------|
| seat_no     | VARCHAR  | Seat Number     |
| stadium_id  | INT FK   | Linked Stadium  |
| price       | DECIMAL  | Seat Price      |

## Implementation Steps

1. **Setup MySQL Database**
	- Create `stadium_db` database.
	- Run provided SQL scripts in `resources/` to create tables and insert default data.

2. **Java Project Setup**
	- Place all `.java` files in `src/`.
	- Add MySQL JDBC connector (`lib/mysql-connector-j-9.4.0.jar`).

3. **GUI Design**
	- Use Swing components (`JFrame`, `JPanel`, `JTable`, etc.).
	- Admin panel: manage events, tickets, customers, seat prices.
	- User panel: view events, stadiums, seats, book tickets.

4. **CRUD Operations**
	- Implement add, update, delete, and search for all entities.
	- Connect GUI actions to MySQL using JDBC.

5. **Testing & Validation**
	- Test all forms and database connections.
	- Validate user inputs and error handling.

## How to Run

1. Ensure MySQL is running and `stadium_db` is set up.
2. Compile all Java files:
	```
	javac -cp "src;lib/mysql-connector-j-9.4.0.jar" src/*.java
	```
3. Run the application:
	```
	java -cp "src;lib/mysql-connector-j-9.4.0.jar" MainFrame
	```

## Expected Outcome

- A fully functional desktop application for stadium management.
- Clear stadium-wise seat and price display for both admin and users.
- Secure, reliable data storage and retrieval.
