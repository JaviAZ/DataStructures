/* Student number: c1628112
 * Student name: Javier Alcazar-Zafra
 * Please respect the format for both matrices and vectors text files since they are assumed to be correct
 * and therefore not checked.
 * Adding matrices method is always O(n1+n2) or under it (if there are repeated columns in the same row in the two
 *    matrices. To check, modify the comments in lines: 264, 267, 274, 384, 296, 304, 311, 316, 323.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Integer;
// A class that represents a dense vector and allows you to read/write its elements
class DenseVector{
    private int[] elements;

    public DenseVector(int n) {
        elements = new int[n];
    }

    public DenseVector(String filename) {
        File file = new File(filename);
        ArrayList<Integer> values = new ArrayList<>();
        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNextInt()) {
                values.add(sc.nextInt());
            }
            sc.close();
            elements = new int[ values.size() ];
            for(int i=0;i<values.size();i++) {
                elements[i] = values.get(i);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Read an element of the vector
    public int getElement(int idx) {
        return elements[idx];
    }

    // Modify an element of the vector
    public void setElement(int idx, int value) {
        elements[idx] = value;
    }

    // Return the number of elements
    public int size() {
        return (elements == null) ? 0 : (elements.length);
    }

    // Print all the elements
    public void print() {
        if(elements == null) {
            return;
        }
        for(int i = 0; i < elements.length; ++ i) {
            System.out.println(elements[i]);
        }
    }
}

// A class that represents a sparse matrix
public class SparseMatrix {
    private int numCols;		// Number of columns
    private ArrayList <ArrayList <Entry> > entries;	// Entries for each row

    // Auxiliary function that prints out the command syntax
    public static void printCommandError() {
        System.err.println("ERROR: use one of the following commands");
        System.err.println(" - Read a matrix and print information: java SparseMatrix -i <MatrixFile>");
        System.err.println(" - Read a matrix and print elements: java SparseMatrix -r <MatrixFile>");
        System.err.println(" - Transpose a matrix: java SparseMatrix -t <MatrixFile>");
        System.err.println(" - Add two matrices: java SparseMatrix -a <MatrixFile1> <MatrixFile2>");
        System.err.println(" - Matrix-vector multiplication: java SparseMatrix -v <MatrixFile> <VectorFile>");
    }

    public static void main(String [] args) {
        if(args.length < 2) {
            printCommandError();
            System.exit(-1);
        }
        if(args[0].equals("-i")) {
            if(args.length != 2) {
                printCommandError();
                System.exit(-1);
            }
            SparseMatrix mat = new SparseMatrix();
            mat.loadEntries(args[1]);
            System.out.println("Read matrix from " + args[1]);
            System.out.println("The matrix has " + mat.numRows() + " rows and " + mat.numColumns() + " columns");
            System.out.println("It has " + mat.numNonZeros() + " non-zeros");
        }else if(args[0].equals("-r")) {
            if(args.length != 2) {
                printCommandError();
                System.exit(-1);
            }
            SparseMatrix mat = new SparseMatrix();
            mat.loadEntries(args[1]);
            System.out.println("Read matrix from " + args[1] + ":");
            mat.print();
        }else if(args[0].equals("-t")) {
            if (args.length != 2) {
                printCommandError();
                System.exit(-1);
            }
            SparseMatrix mat = new SparseMatrix();
            mat.loadEntries(args[1]);
            System.out.println("Read matrix from " + args[1]);
            System.out.println("Matrix elements:");
            mat.print();
            System.out.println();
            System.out.println();
            System.out.println("Transposed matrix elements:");
            SparseMatrix transpose_mat = mat.transpose();
            transpose_mat.print();
        }else if(args[0].equals("-a")) {
            if(args.length != 3) {
                printCommandError();
                System.exit(-1);
            }

            SparseMatrix mat1 = new SparseMatrix();
            mat1.loadEntries(args[1]);
            System.out.println("Read matrix 1 from " + args[1]);
            System.out.println("Matrix elements:");
            mat1.print();

            System.out.println();
            SparseMatrix mat2 = new SparseMatrix();
            mat2.loadEntries(args[2]);
            System.out.println("Read matrix 2 from " + args[2]);
            System.out.println("Matrix elements:");
            mat2.print();
            System.out.println();

            System.out.println("Matrix1 + Matrix2 =");
            SparseMatrix mat_sum1 = mat1.add(mat2);
            mat_sum1.print();
            System.out.println();
            System.out.println("Matrix1 * 2 + Matrix2 =");
            mat1.multiplyBy(2);
            SparseMatrix mat_sum2 = mat1.add(mat2);
            mat_sum2.print();
            System.out.println();

            System.out.println("Matrix1 * 10 + Matrix2 =");
            mat1.multiplyBy(5);
            SparseMatrix mat_sum3 = mat1.add(mat2);
            mat_sum3.print();
        }else if(args[0].equals("-v")) {
            if(args.length != 3) {
                printCommandError();
                System.exit(-1);
            }
            SparseMatrix mat = new SparseMatrix();
            mat.loadEntries(args[1]);
            DenseVector vec = new DenseVector(args[2]);

            System.out.println("Read matrix from " + args[1] + ":");
            mat.print();
            System.out.println();

            System.out.println("Read vector from " + args[2] + ":");
            vec.print();
            System.out.println();

            System.out.println("Matrix-vector multiplication:");
            DenseVector mv = mat.multiply(vec);
            mv.print();
        }
    }

    // Loading matrix entries from a text file
    public void loadEntries(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            int numRows = sc.nextInt();
            numCols = sc.nextInt();
            entries = new ArrayList <>();
            for(int i = 0; i < numRows; ++ i) {
                entries.add(new ArrayList<>());
            }
            while(sc.hasNextInt()) {
                // Read the row index, column index, and value of an element
                int row = sc.nextInt();
                int col = sc.nextInt();
                int val = sc.nextInt();
                Entry ntr=new Entry(col,val); // Store entry in an object for simplicity
                // Check if row has no entries or if the column of the entry to be input is larger than the column of the last entry in the row
                if(entries.get(row).isEmpty() || entries.get(row)==null || col>entries.get(row).get(entries.get(row).size()-1).getColumn()){
                    entries.get(row).add(ntr); // Append entry to the end
                }else{
                    for(int i=0;i<entries.get(row).size();i++){ // Iterate throw the row
                        if(entries.get(row).get(i).getColumn()>col){ // Look for the position in which the column to be input should go
                            entries.get(row).add(i, ntr); // Add entry at that position and shift the rest of the row to the right
                            break; // Break out of the loop to avoid infinite loop
                        }
                    }
                }
            }
        }catch (FileNotFoundException e) {
            System.err.println("File was not found, please try again.");
            numCols = 0;
            entries = null;
        }
    }

    // Default constructor
    public SparseMatrix() {
        numCols = 0;
        entries = null;
    }

    // A class representing a pair of column index and elements
    private class Entry{
        private int column;	// Column index
        private int value;	// Element value
        // Constructor using the column index and the element value
        public Entry(int col, int val) {
            this.column = col;
            this.value = val;
        }
        // Copy constructor
        public Entry(Entry entry) {
            this(entry.column, entry.value);
        }
        // Read column index
        int getColumn() {
            return column;
        }
        // Set column index
        void setColumn(int col) {
            this.column = col;
        }
        // Read element value
        int getValue() {
            return value;
        }
        // Set element value
        void setValue(int val) {
            this.value = val;
        }
    }

    // Adding two matrices
    public SparseMatrix add(SparseMatrix M) {
        SparseMatrix mTotal=new SparseMatrix(); // Create new matrix to store the addition of the matrices
        mTotal.entries=new ArrayList<>(entries.size());
        mTotal.numCols=M.numColumns();
        if(numColumns()!=M.numColumns() || numRows()!=M.numRows()) {
            System.err.println("Matrices sizes are not equal.");
        }else {
            for(int i = 0;i < numRows();i++) { // Iterate through rows (both entries ArrayLists have the same size)
                mTotal.entries.add(new ArrayList<>());
                int entries1Size = entries.get(i).size();
                int entries2Size = M.entries.get(i).size();
                int entries1Counter=0;
                int entries2Counter=0;
                Entry entry1, entry2;
// Delete for Big O                int iterationsCounter=0;
                if(entries2Size==0){ // If the are no entries in the second entries ArrayList
                    while (entries1Counter < entries1Size) { // While there are entries to add in the first ArrayList
// Delete for Big O                       iterationsCounter++;
                        entry1=entries.get(i).get(entries1Counter);
                        mTotal.entries.get(i).add(entry1); // Add entries to the ArrayList that stores the addition
                        entries1Counter++; // Increase the counter for the first entries to not do more iterations than needed
                    }
                }else if(entries1Size==0){ // If there are no entries in the first entries ArrayList
                    while (entries2Counter < entries2Size) { // While there are entries to add in the second ArrayList
// Delete for Big O                       iterationsCounter++;
                        entry2=M.entries.get(i).get(entries2Counter);
                        mTotal.entries.get(i).add(entry2); // Add entries to the ArrayList that stores the addition
                        entries2Counter++; // Increase the counter for the second entries to not do more iterations than needed
                    }
                }else { // If both entries ArrayLists have entries in them
                    // Iterate through the size of the row in both entries added up (if there are no entries in any or it has reached the max per ArrayList the loop wont happen)
                    for (int j = 0; (j < (entries2Size + entries1Size - 2)) && (entries1Counter < entries1Size && entries2Counter < entries2Size); j++) {
                        entry1 = entries.get(i).get(entries1Counter);
                        while (entry1.getColumn() == M.entries.get(i).get(entries2Counter).getColumn()) { // While the entries in both ArrayLists have the same column
// Delete for Big O                       iterationsCounter++;
                            entry1 = entries.get(i).get(entries1Counter);
                            entry2 = M.entries.get(i).get(entries2Counter);
                            int totVal = entry1.getValue() + entry2.getValue(); // Add the values in both ArrayLists
                            mTotal.entries.get(i).add(new Entry(entry1.getColumn(), totVal)); // Add the entry to the ArrayList that stores the addition with the above variable
                            entries1Counter++; // Increase the counter for the first entries to not do more iterations than needed
                            entries2Counter++; // Increase the counter for the second entries to not do more iterations than needed
                            if (entries1Counter >= entries1Size) { // If the maximum of entries in first ArrayList is reached
                                while (entries2Counter < entries2Size) { // While there are entries in second ArrayList
                                    entry2 = M.entries.get(i).get(entries2Counter);
                                    mTotal.entries.get(i).add(entry2); // Add the entry to the ArrayList that stores the addition
                                    entries2Counter++; // Increase the counter for the second entries to not do more iterations than needed
// Delete for Big O                       iterationsCounter++;
                                }
                                break; // Break to avoid index out of bounds exception
                            } else if (entries2Counter >= entries2Size) { // If the maximum of entries in second ArrayList is reached
                                while (entries1Counter < entries1Size) { // While there are entries in first ArrayList
                                    entry1 = entries.get(i).get(entries1Counter);
                                    mTotal.entries.get(i).add(entry1); // Add the entry to the ArrayList that stores the addition
                                    entries1Counter++; // Increase the counter for the first entries to not do more iterations than needed
// Delete for Big O                       iterationsCounter++;
                                }
                                break; // Break to avoid index out of bounds exception
                            }
                        }
                        if (entries1Counter < entries1Size && entries2Counter < entries2Size) { // If both ArrayLists still have entries in them
                            while (entries.get(i).get(entries1Counter).getColumn() < M.entries.get(i).get(entries2Counter).getColumn() && entries1Counter < entries1Size) { // While the entry in the first ArrayList has a column smaller than the second ArrayList
// Delete for Big O                      iterationsCounter++;
                                mTotal.entries.get(i).add(entries.get(i).get(entries1Counter)); // Add the entry to the ArrayList that stores the addition
                                entries1Counter++; // Increase the counter for the first entries to not do more iterations than needed
                            }
                            while (M.entries.get(i).get(entries2Counter).getColumn() < entries.get(i).get(entries1Counter).getColumn() && entries2Counter < entries2Size) { // While the entry in the second ArrayList has a column smaller than the first ArrayList
// Delete for Big O                       iterationsCounter++;
                                mTotal.entries.get(i).add(M.entries.get(i).get(entries2Counter)); // Add the entry to the ArrayList that stores the addition
                                entries2Counter++; // Increase the counter for the second entries to not do more iterations than needed
                            }
                        }
                    }
                }
// Delete for Big O                       System.out.println("In row "+i+"\nn1: "+ entries1Size+ " n2: "+entries2Size+"\nn1+n2: "+(entries1Size+entries2Size)+" Iterations: "+iterationsCounter+"\n");
            }
        }
        return mTotal; // Return matrix which stores the addition of the two matrices
    }

    // Transposing a matrix
    public SparseMatrix transpose() {
        SparseMatrix trans=new SparseMatrix(); // Create new matrix to store the transposed matrix
        trans.entries = new ArrayList <>();
        trans.numCols=numRows();
        for (int i=0;i<numColumns();i++){
            trans.entries.add(new ArrayList <>());
        }
        for(int i=0;i<numRows();i++) {
            for(int j = 0;j < entries.get(i).size();j++) {
                int row=entries.get(i).get(j).getColumn();
                int val=entries.get(i).get(j).getValue();
                // Add entries to the transpose matrix entries ArrayList switching the row with the column
                trans.entries.get(row).add(new Entry(i,val));
            }
        }
        return trans;
    }

    // Matrix-vector multiplication
    public DenseVector multiply(DenseVector v) {
        DenseVector vTemp = new DenseVector(entries.size());
        if(v.size() != numColumns()) { // Check vector size matches matrix size
            System.err.println("Vector size is not suitable for this matrix, please try again.");
        }else {
            for (int i = 0; i < entries.size(); i++) {
                for (int j = 0; j < entries.get(i).size(); j++) {
                    int currCol = entries.get(i).get(j).getColumn();
                    int currVal = entries.get(i).get(j).getValue();
                    int result = currVal * v.getElement(currCol); // Multiply current value by the item in the vector
                    result +=vTemp.getElement(i); // Add result to the rest of the results for that row
                    vTemp.setElement(i, result); // Store result in temporary dense vector
                }
            }
        }
        return vTemp; // Return temporary dense vector
    }

    // Count the number of non-zeros
    public int numNonZeros() {
        int count=0; // Create a counter
        for(int i=0;i<numRows();i++){ // Iterate through the rows of entries
            count+=entries.get(i).size(); // Add size of the entries row to the counter
        }
        return count; // return the counter
    }

    // Multiply the matrix by a scalar, and update the matrix elements
    public void multiplyBy(int scalar) {
        for(int i = 0;i < entries.size();i++) {
            for(int j = 0;j < entries.get(i).size();j++) {
                // Get item value in position (i,j), multiply it by the scalar, and store it back in the ArrayList
                entries.get(i).get(j).setValue(entries.get(i).get(j).getValue()*scalar);
            }
        }
    }

    // Number of rows of the matrix
    public int numRows(){
        if(this.entries != null) {
            return this.entries.size();
        }else {
            return 0;
        }
    }

    // Number of columns of the matrix
    public int numColumns() {
        return this.numCols;
    }

    // Output the elements of the matrix, including the zeros
    public void print() {
        int numRows = entries.size();
        for(int i = 0;i < numRows;i++) {
            ArrayList<Entry> currentRow = entries.get(i);
            int currentCol = -1;
            int entryIdx = -1;
            if(currentRow != null && (!currentRow.isEmpty())) {
                entryIdx = 0;
                currentCol = currentRow.get(entryIdx).getColumn();
            }
            for(int j = 0;j < numCols;j++) {
                if(j == currentCol) {
                    System.out.print(currentRow.get(entryIdx).getValue()+" ");
                    entryIdx++;
                    currentCol = (entryIdx < currentRow.size()) ? currentRow.get(entryIdx).getColumn() : (-1);
                }else {
                    System.out.print(0+" ");
                }
            }
            System.out.println();
        }
    }
}