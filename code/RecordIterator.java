package Main;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RecordIterator {
    private TableInfo tabInfo;
    private PageId pageId;
    private int position;
    private int numRecordsRead;
    private ByteBuffer dataPageBuffer;

    public RecordIterator(TableInfo tabInfo, PageId pageId) {
        this.tabInfo = tabInfo;
        this.pageId = pageId;
        this.position = DataPage.DATA_PAGE_RECORDS_START;
        this.numRecordsRead = 0;
        this.dataPageBuffer = null; // Initialisez-le au besoin lors de la première utilisation
    }

    public Record getNextRecord() {
        if (dataPageBuffer == null) {
            try {
                dataPageBuffer = BufferManager.getInstance().getPage(pageId);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'exception selon vos besoins
                return null;
            }
        }

        dataPageBuffer.position(position);

        if (numRecordsRead < DataPage.readNumEntries(dataPageBuffer)) {
            int entryStart = DataPage.SLOT_DIR_ENTRIES_START + numRecordsRead * DataPage.SLOT_DIR_ENTRY_SIZE;
            int recordStart = DataPage.readSlotEntryStart(dataPageBuffer, entryStart);
            int recordSize = DataPage.readSlotEntrySize(dataPageBuffer, entryStart);

            if (recordSize > 0) {
                ByteBuffer recordBuffer = dataPageBuffer.slice(); // Utilisez le même buffer de la page
                recordBuffer.position(recordStart);
                recordBuffer.limit(recordStart + recordSize);

                // Mettre à jour la position pour le prochain record
                position += DataPage.SLOT_DIR_ENTRY_SIZE;
                numRecordsRead++;

                Record record = new Record(tabInfo);
                record.readFromBuffer(recordBuffer, 0);

                return record;
            }
        }

        return null;
    }

    public void close() {
        if (dataPageBuffer != null) {
            try {
                BufferManager.getInstance().freePage(pageId, false);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                dataPageBuffer = null;
            }
        }
    }

    public void reset() {
        position = DataPage.DATA_PAGE_RECORDS_START;
        numRecordsRead = 0;
        dataPageBuffer = null;
    }
}
