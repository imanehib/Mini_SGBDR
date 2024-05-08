package Test;
import Main.DBParams;
import Main.DiskManager;
import Main.PageId;

/*
import java.io.IOException;

public class DiskManagerTests {
    public static void main(String[] args) throws IOException {
        DBParams.DBPath = "C:\\Users\\imane\\Desktop\\IMANE\\L3\\BDDA\\DB\\"; 
        
        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;

        DiskManager dm = DiskManager.getInstance();
        PageId testPageId = dm.allocPage();
        System.out.println(testPageId.toString());
    }
}*/


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DiskManagerTests {

    public static void TestAllocPage() throws IOException {
        DiskManager dm = DiskManager.getInstance();
        PageId testPageId = dm.allocPage();
       
        System.out.println(testPageId);
    }


    public static void TestReadPage() throws IOException {
        DiskManager dm = DiskManager.getInstance();
        ByteBuffer buff = ByteBuffer.allocate(DBParams.SGBDPageSize);
        dm.readPage(new PageId(0, 0), buff);
        buff.flip(); // Prépare le ByteBuffer pour la lecture
        System.out.println(Arrays.toString(buff.array()));
    }

    public static void TestWritePage() throws IOException {
        DiskManager dm = DiskManager.getInstance();
        ByteBuffer buff = ByteBuffer.allocate(DBParams.SGBDPageSize);
        buff.putInt(100);
        buff.rewind();
        dm.writePage(new PageId(0, 0), buff);
    }

    /* public static void TestDeAllocPage(PageId pid) {
        DiskManager dm = DiskManager.getInstance();
        System.out.println("Voici la liste des pages disponibles après désallocation :");
        dm.deallocPage(pid);
    }

    public static void TestCountAllocDealloc() {
        DiskManager dm = DiskManager.getInstance();
        System.out.println("Deux allocations et une désallocation :");
        if (dm.getCurrentCountAllocPages() == 1) {
            System.out.println("Test réussi");
        } else {
            System.out.println("Test échoué");
        }
    }*/

    public static void main(String[] args) throws IOException {
        DBParams.DBPath = "C:\\Users\\NIS\\Documents\\projet-bdda-2023-main\\DB\\";
        DBParams.SGBDPageSize = 4096;
        DBParams.DMFileCount = 4;

        TestAllocPage();
        TestReadPage();
        TestWritePage();
        /*TestDeAllocPage(new PageId(0, 1));
        TestCountAllocDealloc();*/
    }
}


