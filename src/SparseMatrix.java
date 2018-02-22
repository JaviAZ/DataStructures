/* Student number: c1628112
 * Student name: Javier Alcazar-Zafra
 * Please respect the format for both matrices and vectors text files since they are assumed to be correct
 * and therefore not checked.
 * Adding matrices method might not be efficient enough O(n1+n2)
 * DID THIS WORK?
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
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

    public static void main(String [] args) throws Exception {
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
                entries.add(new ArrayList <>());
            }

            while(sc.hasNextInt()) {
                // Read the row index, column index, and value of an element
                int row = sc.nextInt();
                int col = sc.nextInt();
                int val = sc.nextInt();
                Entry ntr=new Entry(col,val);
                if(entries.get(row).isEmpty() ||entries.get(row)==null || col>entries.get(row).get(entries.get(row).size()-1).getColumn()){
                    entries.get(row).add(ntr);
                }else{
                    for(int j=0;j<entries.get(row).size();j++){
                        if(entries.get(row).get(j).getColumn()>col){
                            entries.get(row).add(j, ntr);
                            break;
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
        SparseMatrix mTotal=new SparseMatrix();
        mTotal.entries=new ArrayList<>();
        mTotal.numCols=M.numColumns();
        for(int i = 0;i < M.entries.size();i++) {
            mTotal.entries.add(new ArrayList <>());
        }
        if(numColumns()!=M.numColumns() || numRows()!=M.numRows()) {
            System.err.println("Matrices sizes are not equal.");
        }else {
            for(int i=0;i<numRows();i++){
                List<Integer> colsFirstM=new ArrayList <>();
                for(int j=0;j<M.entries.get(i).size();j++){
                    mTotal.entries.get(i).add(new Entry(M.entries.get(i).get(j).getColumn(),M.entries.get(i).get(j).getValue()));
                    colsFirstM.add(mTotal.entries.get(i).get(j).getColumn());
                }
                for(int j=0;j<entries.get(i).size();j++){
                    int currCol=entries.get(i).get(j).getColumn();
                    int curVal=entries.get(i).get(j).getValue();
                    if(colsFirstM.contains(currCol)){
                        int col=colsFirstM.indexOf(currCol);
                        mTotal.entries.get(i).get(col).setValue(mTotal.entries.get(i).get(col).getValue()+curVal);
                    }else if(colsFirstM.size()==0 || currCol>colsFirstM.get(colsFirstM.size()-1)){
                        mTotal.entries.get(i).add(new Entry(currCol,curVal));
                        colsFirstM.add(currCol);
                    }else{
                        int k;
                        for(k=0;k<colsFirstM.size();k++){
                            if(colsFirstM.get(k)>currCol){
                                break;
                            }
                        }
                        mTotal.entries.get(i).add(k, new Entry(currCol,curVal));
                        colsFirstM.add(k,currCol);
                    }
                }
            }
        }
        return mTotal;
    }

    // Transposing a matrix
    public SparseMatrix transpose() {
        SparseMatrix trans=new SparseMatrix();
        trans.entries = new ArrayList <>();
        for(int i = 0;i < numCols;i++) {
            trans.entries.add(new ArrayList <>());
        }
        for(int i=0;i<entries.size();i++) {
            trans.numCols++;
            for(int j = 0;j < entries.get(i).size();j++) {
                int col=i;
                int row=entries.get(i).get(j).getColumn();
                int val=entries.get(i).get(j).getValue();
                trans.entries.get(row).add(new Entry(col,val));
            }
        }
        return trans;
    }

    // Matrix-vector multiplication
    public DenseVector multiply(DenseVector v) {
        DenseVector vTemp = new DenseVector(entries.size());
        if(v.size() != numColumns()) {
            System.err.println("Vector size is not suitable for this matrix, please try again.");
        }else {
            for (int i = 0; i < entries.size(); i++) {
                for (int j = 0; j < entries.get(i).size(); j++) {
                    int currCol = entries.get(i).get(j).getColumn();
                    int currVal = entries.get(i).get(j).getValue();
                    int result = currVal * v.getElement(currCol);
                    vTemp.setElement(i, (vTemp.getElement(i) + result));
                }
            }
        }
        return vTemp;
    }

    // Count the number of non-zeros
    public int numNonZeros() {
        int count=0;
        for(int i=0;i<entries.size();i++){
            count+=entries.get(i).size();
        }
        return count;
    }

    // Multiply the matrix by a scalar, and update the matrix elements
    public void multiplyBy(int scalar) {
        for(int i = 0;i < entries.size();i++) {
            for(int j = 0;j < entries.get(i).size();j++) {
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