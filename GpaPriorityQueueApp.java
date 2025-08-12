import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * GPA-based Priority Queue:
 * - Load students from built-in sample (hardcoded) OR by picking a CSV/TXT file via file dialog.
 * - Only GPA > 4.0 are eligible.
 * - Dequeue up to 5 winners by highest GPA.
 * - Also print eligible students alphabetically (without touching the queue).
 *
 * File format (CSV or TXT):
 *   name,gpa
 *   or
 *   name <TAB> gpa
 *
 * Example:
 *   Alice,4.5
 *   Bob,3.9
 *   Charlie,4.2
 */
public class GpaPriorityQueueApp {

    // -------- Domain --------
    static class Student {
        final String name;
        final double gpa;

        Student(String name, double gpa) {
            this.name = name.trim();
            this.gpa  = gpa;
        }
        @Override public String toString() { return name + " (GPA: " + gpa + ")"; }
    }

    // -------- Loaders --------

    /** Option 1: Hardcoded list */
    static List<Student> loadHardcoded() {
        return Arrays.asList(
            new Student("Alice",   4.50),
            new Student("Bob",     3.90),
            new Student("Charlie", 4.20),
            new Student("David",   4.80),
            new Student("Eve",     4.10),
            new Student("Frank",   3.50),
            new Student("Grace",   4.70),
            new Student("Heidi",   4.00),  // 4.0 not eligible (strictly > 4.0)
            new Student("Ivan",    4.35),
            new Student("Judy",    3.80)
        );
    }

    /** Option 2: Pick a CSV/TXT file via native dialog and load it (comma or tab separated). */
    static List<Student> loadFromPickedFile() throws IOException {
        // Show a file chooser (will use native OS dialog)
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select students CSV/TXT (name,gpa)");
        chooser.setAcceptAllFileFilterUsed(true);
        chooser.setFileFilter(new FileNameExtensionFilter("CSV or TXT files", "csv", "txt"));

        int result = chooser.showOpenDialog(null); // parent null → centers on screen
        if (result != JFileChooser.APPROVE_OPTION) {
            // Cancelled
            return Collections.emptyList();
        }

        File file = chooser.getSelectedFile();
        List<String> lines = Files.readAllLines(file.toPath());
        List<Student> out = new ArrayList<>();

        boolean headerSkipped = false;
        for (String raw : lines) {
            if (raw == null) continue;
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = splitLine(line);
            if (parts.length < 2) continue;

            // Heuristic: skip one header row if second token is not a number
            if (!headerSkipped && !isNumeric(parts[1])) {
                headerSkipped = true;
                continue;
            }

            String name = parts[0].trim();
            try {
                double gpa = Double.parseDouble(parts[1].trim());
                if (!name.isEmpty()) out.add(new Student(name, gpa));
            } catch (NumberFormatException nfe) {
                System.err.println("Skipping bad GPA row: " + line);
            }
        }
        return out;
    }

    private static String[] splitLine(String line) {
        if (line.contains(",")) return line.split("\\s*,\\s*");
        if (line.contains("\t")) return line.split("\\t");
        return line.split("\\s+");
    }

    private static boolean isNumeric(String s) {
        try { Double.parseDouble(s.trim()); return true; }
        catch (Exception e) { return false; }
    }

    // -------- Core logic --------

    /** Build a max-heap by GPA (highest first) for eligible students (GPA > 4.0). */
    static PriorityQueue<Student> buildPrizeQueue(Collection<Student> all) {
        PriorityQueue<Student> pq = new PriorityQueue<>(
            (a, b) -> Double.compare(b.gpa, a.gpa)  // descending by GPA
        );
        for (Student s : all) if (s.gpa > 4.0) pq.add(s);
        return pq;
    }

    /** Print up to maxRecipients winners by GPA. */
    static void printTopRecipients(PriorityQueue<Student> pq, int maxRecipients) {
        System.out.println("\n🎓 Top GPA Students (up to " + maxRecipients + ", GPA > 4.0):");
        for (int i = 1; i <= maxRecipients && !pq.isEmpty(); i++) {
            System.out.println(i + ". " + pq.poll());
        }
    }

    /** Print eligible students alphabetically WITHOUT touching the queue. */
    static void printAlphabetical(Collection<Student> all) {
        List<Student> eligible = new ArrayList<>();
        for (Student s : all) if (s.gpa > 4.0) eligible.add(s);
        eligible.sort(Comparator.comparing(s -> s.name.toLowerCase()));
        System.out.println("\n📜 Eligible Students (Alphabetical):");
        for (Student s : eligible) System.out.println(s);
    }

    // -------- Main / CLI --------

    public static void main(String[] args) {
        // Ensure we can show the dialog even if run from some IDE terminals
        System.setProperty("java.awt.headless", "false");

        Scanner sc = new Scanner(System.in);
        List<Student> students;

        System.out.println("=== SCIMS $1000 Prize — GPA Priority Queue ===");
        System.out.println("Load students from:");
        System.out.println("  1) Built-in sample (hardcoded)");
        System.out.println("  2) Pick a CSV/TXT file (name,gpa) via file dialog");
        System.out.print("Choose [1/2]: ");
        String choice = sc.nextLine().trim();

        try {
            if ("2".equals(choice)) {
                students = loadFromPickedFile();
                if (students.isEmpty()) {
                    System.out.println("No rows loaded (cancelled or empty). Falling back to built-in sample.\n");
                    students = loadHardcoded();
                }
            } else {
                students = loadHardcoded();
            }
        } catch (IOException e) {
            System.out.println("Failed to read file. Using built-in sample.\n");
            students = loadHardcoded();
        }

        PriorityQueue<Student> pq = buildPrizeQueue(students);
        printTopRecipients(pq, 5);
        printAlphabetical(students);

        long eligible = students.stream().filter(s -> s.gpa > 4.0).count();
        System.out.println("\nℹ️  Total eligible (GPA > 4.0): " + eligible);
    }
}
