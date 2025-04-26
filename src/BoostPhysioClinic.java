import java.util.ArrayList;
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
}
