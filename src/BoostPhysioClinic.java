import java.util.*;

public class BoostPhysioClinic {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Patient> patients = new ArrayList<>();
    static ArrayList<Physiotherapist> physios = new ArrayList<>();
    static ArrayList<Appointment> appointments = new ArrayList<>();
    static int nextPatientId = 1;

    // Patient class
    static class Patient {
        int id;
        String name, mobile, address;

        Patient(int id, String name, String mobile, String address) {
            this.id = id;
            this.name = name;
            this.mobile = mobile;
            this.address = address;
        }

        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Mobile: " + mobile + ", Address: " + address;
        }
    }

    // Physiotherapist class
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

    // Appointment class
    static class Appointment {
        Patient patient;
        Physiotherapist physio;
        String date;
        String status; // Added status field

        Appointment(Patient patient, Physiotherapist physio, String date) {
            this.patient = patient;
            this.physio = physio;
            this.date = date;
            this.status = "booked"; // Default status when appointment is created
        }

        public String toString() {
            return patient.name + " with " + physio.name + " on " + date + " [Status: " + status + "]";
        }
    }

    public static void main(String[] args) {
        loadPhysiotherapists();
        while (true) {
            System.out.println("\n=== Boost Physio Clinic ===");
            System.out.println("1. Add Patient");
            System.out.println("2. Remove Patient");
            System.out.println("3. Show All Patients");
            System.out.println("4. Show Physiotherapists");
            System.out.println("5. Book Appointment");
            System.out.println("6. Show All Appointments");
            System.out.println("7. Change/Cancel Appointment");
            System.out.println("8. Print Report");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            int choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addPatient();
                case 2 -> removePatient();
                case 3 -> showPatients();
                case 4 -> showPhysiotherapists();
                case 5 -> bookAppointment();
                case 6 -> showAppointments();
                case 7 -> manageAppointment();
                case 8 -> printReport();
                case 9 -> {
                    System.out.println("Bye Bye...Take Care!");
                    return;
                }
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

        // Check if this patient has any appointments
        boolean hasAppointments = false;
        for (Appointment appt : appointments) {
            if (appt.patient.id == id) {
                hasAppointments = true;
                break;
            }
        }

        if (hasAppointments) {
            System.out.println("⚠️ This patient has appointments booked.");
            System.out.print("Do you want to also remove their appointments? (yes/no): ");
            String confirm = input.nextLine();
            if (confirm.equalsIgnoreCase("yes")) {
                appointments.removeIf(appt -> appt.patient.id == id);
                System.out.println("✅ All related appointments removed.");
            } else {
                System.out.println("❌ Cannot remove patient without deleting appointments.");
                return;
            }
        }

        patients.remove(toRemove);
        System.out.println("✅ Patient removed successfully.");
    }

    // Show all patients
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

    // Show all appointments
    static void showAppointments() {
        if (appointments.isEmpty()) {
            System.out.println("No appointments booked.");
        } else {
            System.out.println("📅 Appointments:");
            int count = 1;
            for (Appointment a : appointments) {
                System.out.println(count++ + ". " + a);
            }
        }
    }

    // Book an appointment
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
    }

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

    // Change the appointment date
    static void changeAppointmentDate(Appointment appt) {
        System.out.println("Current appointment date: " + appt.date);
        System.out.print("Enter new date: ");
        String newDate = input.nextLine();
        appt.date = newDate;
        System.out.println("✅ Appointment date updated.");
    }

    // Mark an appointment as attended
    static void markAsAttended(Appointment appt) {
        if (appt.status.equals("attended")) {
            System.out.println("❌ This appointment is already marked as attended.");
            return;
        }
        appt.status = "attended";
        System.out.println("✅ Appointment marked as attended.");
    }

    // Cancel an appointment
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

    // Print the report (all appointments)
    static void printReport() {
        if (appointments.isEmpty()) {
            System.out.println("📭 No appointments booked.");
            return;
        }

        System.out.println("\n====== Boost Physio Clinic Report ======\n");
        Map<Integer, List<Appointment>> grouped = new HashMap<>();

        for (Appointment appt : appointments) {
            grouped.computeIfAbsent(appt.patient.id, k -> new ArrayList<>()).add(appt);
        }

        for (Map.Entry<Integer, List<Appointment>> entry : grouped.entrySet()) {
            Patient patient = getPatientById(entry.getKey());
            System.out.println("🧑 Patient: " + patient.name + " (ID: " + patient.id + ")");
            for (Appointment appt : entry.getValue()) {
                System.out.println("   📅 " + appt.date + " with Dr. " + appt.physio.name + " [" + appt.physio.expertise + "] [Status: " + appt.status + "]");
            }
            System.out.println("---------------------------------------");
        }
    }

    // Helper method to get a patient by ID
    static Patient getPatientById(int id) {
        for (Patient p : patients) {
            if (p.id == id) return p;
        }
        return null;
    }

    // Helper method to get a physiotherapist by name
    static Physiotherapist getPhysioByName(String name) {
        for (Physiotherapist p : physios) {
            if (p.name.equalsIgnoreCase(name)) return p;
        }
        return null;
    }

    // Load some hardcoded physiotherapists and their availability
    static void loadPhysiotherapists() {
        physios.add(new Physiotherapist(1, "Dr. Asha Singh", "Sports Injuries", generateDates(1)));
        physios.add(new Physiotherapist(2, "Dr. Raj Mehta", "Back Pain", generateDates(2)));
        physios.add(new Physiotherapist(3, "Dr. Neha Desai", "Post-Surgery Rehab", generateDates(3)));
        physios.add(new Physiotherapist(4, "Dr. Aman Khan", "Neck & Shoulder Pain", generateDates(4)));
        physios.add(new Physiotherapist(5, "Dr. Riya Verma", "Arthritis Therapy", generateDates(5)));
    }

    // Generate dummy dates for physiotherapists' availability
    static List<String> generateDates(int offsetStart) {
        List<String> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (int i = offsetStart; i < offsetStart + 30; i += 5) {
            cal.setTime(new Date());
            cal.add(Calendar.DAY_OF_MONTH, i);
            Date date = cal.getTime();
            String formatted = new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
            dates.add(formatted);
        }
        return dates;
    }
}
