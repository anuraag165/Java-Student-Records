# Java Student Records – GPA Priority Queue

## 📌 Overview
This Java program manages student records using a **Priority Queue** where the priority is based on the student's **GPA** (highest GPA first).  
It is designed for a scenario where **SCIMS** allocates a $1000 prize to first-year students with a GPA greater than 4.0, with a maximum of 5 recipients.

The program supports **two modes** of data input:
1. **Hardcoded Data** – Student details are stored directly in the program.
2. **File Upload** – Load student data from a `.txt` or `.csv` file.

---

## 🛠 Features
- **Priority Queue** based on GPA (highest GPA dequeued first).
- **Read from file or use hardcoded data**.
- **Text & CSV support** for file uploads.
- Limit selection to **max 5 recipients**.
- Display student names **alphabetically** without refilling the priority queue.
- Flexible file upload simulation (via console prompt).

---

## 📂 Project Structure
```
Java-Student-Records/
│── students.txt        # Sample student data in text format
│── students.csv        # Sample student data in CSV format
│── GpaPriorityQueueApp.java  # Main Java program
│── README.md           # Project documentation
```

---

## 📥 Sample Data Format

### **students.txt**
```
Alice,4.5
Bob,3.9
Charlie,4.8
David,4.1
Eva,4.6
```

### **students.csv**
```
Name,GPA
Alice,4.5
Bob,3.9
Charlie,4.8
David,4.1
Eva,4.6
```

---

## ▶ How to Run

### **1️⃣ Compile the Java Program**
```sh
javac GpaPriorityQueueApp.java
```

### **2️⃣ Run the Program**
```sh
java GpaPriorityQueueApp
```

### **3️⃣ Follow the prompts**  
- Choose to use **hardcoded data** or **upload a file**.
- If uploading, provide the file name (`students.txt` or `students.csv`) when prompted.

---

## 💡 Example Output
```
Choose data input method:
1. Hardcoded data
2. Load from file
Enter choice: 1

Top 5 Recipients (by GPA):
Charlie - GPA: 4.8
Eva - GPA: 4.6
Alice - GPA: 4.5
David - GPA: 4.1
```

---

## 📜 License
This project is open-source under the **MIT License**.
You are free to use, modify, and distribute it with attribution.
