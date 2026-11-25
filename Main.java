import java.io.*;
import java.util.*;
class Bus {
    String id, route;
    int fare, totalSeats, availableSeats;
    Queue<String> waiting = new LinkedList<>();

    Bus(String id, String route, int fare, int seats) {
        this.id = id;
        this.route = route;
        this.fare = fare;
        totalSeats = seats;
        availableSeats = seats;
    }
}

class Ticket {
    String id, name, busId;

    Ticket(String id, String name, String busId) {
        this.id = id;
        this.name = name;
        this.busId = busId;
    }
}

public class Main {
    static ArrayList<Bus> buses = new ArrayList<>();
    static ArrayList<Ticket> tickets = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    static int ticketNo = 101;

    // Load buses from file
    static void loadBuses() {
        try {
            File f = new File("buses.txt");

            if (!f.exists()) {
                PrintWriter p = new PrintWriter(f);
                p.println("B101 Raipur-Bhopal 700 2");
                p.println("B102 Raipur-Nagpur 450 2");
                p.close();
            }

            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                buses.add(new Bus(s.next(), s.next(),
                        s.nextInt(), s.nextInt()));
            }
            s.close();
        } catch (Exception e) {
            System.out.println("File loading error.");
        }
    }

    // Find bus by ID
    static Bus findBus(String id) {
        for (Bus b : buses)
            if (b.id.equalsIgnoreCase(id))
                return b;
        return null;
    }

    // View buses
    static void viewBuses() {
        for (Bus b : buses)
            System.out.println(b.id + " | " + b.route +
                    " | " + b.fare + " | Seats left: " +
                    b.availableSeats);
    }

    // Search by destination
    static void searchBus() {
        System.out.print("Enter destination: ");
        String d = sc.nextLine().toLowerCase();
        boolean found = false;

        for (Bus b : buses) {
            if (b.route.toLowerCase().contains(d)) {
                System.out.println(b.id + " | " + b.route +
                        " | Fare: " + b.fare);
                found = true;
            }
        }
        if (!found)
            System.out.println("Bus not found.");
    }

    // Book ticket
    static void bookTicket() {
        System.out.print("Enter Bus ID: ");
        Bus b = findBus(sc.nextLine());

        if (b == null) {
            System.out.println("Bus not found.");
            return;
        }

        System.out.print("Enter Passenger Name: ");
        String name = sc.nextLine();

        if (b.availableSeats > 0) {
            b.availableSeats--;
            String id = "TKT" + ticketNo++;
            tickets.add(new Ticket(id, name, b.id));

            System.out.println("Ticket booked.");
            System.out.println(id + " | " + name + " | " +
                    b.id + " " + b.route.replace("-", ""));
            System.out.println("Seats left: " + b.availableSeats);
        } else {
            b.waiting.add(name);
            System.out.println("Bus full. " + name +
                    " added to waiting queue.");
        }
    }

    // Cancel ticket
    static void cancelTicket() {
        System.out.print("Enter Ticket ID: ");
        String id = sc.nextLine();

        Ticket t = null;
        for (Ticket x : tickets) {
            if (x.id.equalsIgnoreCase(id)) {
                t = x;
                break;
            }
        }

        if (t == null) {
            System.out.println("Ticket not found.");
            return;
        }

        Bus b = findBus(t.busId);
        tickets.remove(t);
        System.out.println("Ticket " + id + " cancelled.");

        if (!b.waiting.isEmpty()) {
            String name = b.waiting.poll();
            String newId = "TKT" + ticketNo++;
            tickets.add(new Ticket(newId, name, b.id));
            System.out.println("Seat given to " + name +
                    ": " + newId);
        } else {
            b.availableSeats++;
        }
    }

    // Sort buses by fare
    static void sortByFare() {
        buses.sort(Comparator.comparingInt(b -> b.fare));

        for (Bus b : buses)
            System.out.println(b.id + " | " + b.fare);
    }

    // Save data on exit
    static void saveData() {
        try {
            PrintWriter p = new PrintWriter("buses.txt");

            for (Bus b : buses)
                p.println(b.id + " " + b.route + " " +
                        b.fare + " " + b.totalSeats);
            p.close();

            PrintWriter t = new PrintWriter("tickets.txt");
            for (Ticket x : tickets)
                t.println(x.id + " " + x.name + " " + x.busId);
            t.close();

            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.out.println("Saving error.");
        }
    }

    // Main method
    public static void main(String[] args) {
        loadBuses();
        int choice = 0;

        do {
            System.out.println("\n===== BUS RESERVATION SYSTEM =====");
            System.out.println("1. View Buses");
            System.out.println("2. Search by Destination");
            System.out.println("3. Book Ticket");
            System.out.println("4. Cancel Ticket");
            System.out.println("5. Sort by Fare");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1:
                    viewBuses();
                    break;
                case 2:
                    searchBus();
                    break;
                case 3:
                    bookTicket();
                    break;
                case 4:
                    cancelTicket();
                    break;
                case 5:
                    sortByFare();
                    break;
                case 6:
                    saveData();
                    System.out.println("Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 6);

        sc.close();
    }
}
