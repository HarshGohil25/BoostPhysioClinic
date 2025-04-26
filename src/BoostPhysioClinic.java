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
}
