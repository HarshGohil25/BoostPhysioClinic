import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoostPhysioClinic {
static Scanner input = new Scanner(System.in);
    static ArrayList<Patient> patients = new ArrayList<>();
    static ArrayList<Physiotherapist> physios = new ArrayList<>();
    static ArrayList<Appointment> appointments = new ArrayList<>();
    static int nextPatientId = 1;

    public static void main(String[] args) {
        loadPhysiotherapists();
        while (true) {
            System.out.println("\n=== Boost Physio Clinic ===");
            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. Show All Patients");
             System.out.println("4. Show Physiotherapists");
            System.out.println("5. Book Appointment");
            
            System.out.print("Choose an option: ");
            int choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addPatient();
                case 2 -> removePatient();
                
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // Add a new patient
    static void addPatient() {
        System.out.print("Enter patient's name: ");
        String name = input.nextLine();
        System.out.print("Enter mobile number: ");
        String mobile = input.nextLine();
        System.out.print("Enter address: ");
        String address = input.nextLine();
        Patient p = new Patient(nextPatientId++, name, mobile, address);
        patients.add(p);
        System.out.println("✅ Patient added: " + p);
    }

    // Remove a patient
    static void removePatient() {
        if (patients.isEmpty()) {
            System.out.println("No patients to remove.");
            return;
        }

        showPatients();
        System.out.print("Enter the Patient ID to remove: ");
        int id = input.nextInt();
        input.nextLine(); // consume newline

        Patient toRemove = getPatientById(id);

        if (toRemove == null) {
            System.out.println("❌ Patient not found.");
            return;
        }
}
static void showPatients() {
    if (patients.isEmpty()) {
        System.out.println("No patients found.");
    } else {
        System.out.println("🧾 Patients:");
        for (Patient p : patients) System.out.println(p);
    }
}
// Show all physiotherapists
static void showPhysiotherapists() {
    System.out.println("\n🏥 Physiotherapists:");
    for (Physiotherapist p : physios) {
        System.out.println(p);
        System.out.println("   Available Dates: " + p.availability);
    }
}
}
static class Physiotherapist {
    int id;
    String name, expertise;
    List<String> availability;

    Physiotherapist(int id, String name, String expertise, List<String> availability) {
        this.id = id;
        this.name = name;
        this.expertise = expertise;
        this.availability = availability;
    }

    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Expertise: " + expertise;
    }
}
static void bookAppointment() {
    if (patients.isEmpty()) {
        System.out.println("⚠️ No patients found. Add a patient first.");
        return;
    }

    System.out.print("Enter patient ID: ");
    int patientId = input.nextInt();
    input.nextLine();

    Patient patient = getPatientById(patientId);
    if (patient == null) {
        System.out.println("❌ Patient not found.");
        return;
    }

    System.out.print("Enter physiotherapist's name: ");
    String physioName = input.nextLine();
    Physiotherapist physio = getPhysioByName(physioName);

    if (physio == null || physio.availability.isEmpty()) {
        System.out.println("❌ Invalid physiotherapist or no available dates.");
        return;
    }

    System.out.println("Available Dates: " + physio.availability);
    System.out.print("Choose a date: ");
    String date = input.nextLine();

    if (!physio.availability.contains(date)) {
        System.out.println("❌ Invalid date selected.");
        return;
    }

    appointments.add(new Appointment(patient, physio, date));
    physio.availability.remove(date);
    System.out.println("✅ Appointment booked.");

    // Manage (Change/Cancel) an appointment
    static void manageAppointment() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments to modify.");
            return;
        }

        showAppointments();
        System.out.print("Enter the appointment number to modify: ");
        int index = input.nextInt() - 1;
        input.nextLine();

        if (index < 0 || index >= appointments.size()) {
            System.out.println("❌ Invalid appointment number.");
            return;
        }

        Appointment appt = appointments.get(index);
        System.out.println("Selected Appointment: " + appt);
        System.out.println("1. Change Appointment Date");
        System.out.println("2. Cancel Appointment");
        System.out.println("3. Mark as Attended");
        System.out.print("Choose an option: ");
        int option = input.nextInt();
        input.nextLine();

        if (option == 1) changeAppointmentDate(appt);
        else if (option == 2) cancelAppointment(index, appt);
        else if (option == 3) markAsAttended(appt);
        else System.out.println("Invalid choice.");
    }
}
static void cancelAppointment(int index, Appointment appt) {
    if (appt.status.equals("cancelled")) {
        System.out.println("❌ This appointment is already cancelled.");
        return;
    }
    appt.status = "cancelled";
    appt.physio.availability.add(appt.date);
    appointments.remove(index);
    System.out.println("❌ Appointment cancelled.");
}


