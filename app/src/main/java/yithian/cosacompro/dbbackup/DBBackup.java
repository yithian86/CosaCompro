package yithian.cosacompro.dbbackup;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import yithian.cosacompro.R;
import yithian.cosacompro.db.DBHandler;

/**
 * Created by Yithian 2016.
 */
public class DBBackup {
    private Activity main_activity;
    private DBHandler dbHandler;

    public DBBackup(Activity main_activity) {
        this.main_activity = main_activity;
        this.dbHandler = DBHandler.getInstance(main_activity);
    }

    public void exportDB() {
        String backupFileName = "cosacompro.db"; // TODO: Crea un nome diverso per ogni backup (ad es.: "cosacompro_db_backup_20160320.db"
        String dbPath = dbHandler.getDBPath();
        if (dbPath == null) {
            Toast.makeText(main_activity, "dbPath è null!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Destination directory
            File backupDirectory = Environment.getExternalStoragePublicDirectory("CosaCompro");
            backupDirectory.mkdirs();

            if (isExternalStorageAvailable()) {
                // Source DB file path
                File currentDB = new File(dbPath);
                // Destination DB file path
                File backupDB = new File(backupDirectory, backupFileName);
                Log.i("backup dest", backupDirectory.getAbsolutePath());
                // Create Source and Destination channels for transfer
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                // 'DestFileSize' is the number of bytes that are transferred
                long fileSize = dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i("Fatto", main_activity.getString(R.string.dbbackup_export_ok) + String.valueOf(fileSize) + " Bytes!");
                Toast.makeText(main_activity, main_activity.getString(R.string.dbbackup_export_ok), Toast.LENGTH_LONG).show();
            } else {
                Log.e("Permission denied", "Can't write to SD card, add permission");
                Toast.makeText(main_activity, "Can't write to SD card, add permission", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(main_activity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void importDB() {
        String backupFileName = "cosacompro.db"; // TODO: Crea un nome diverso per ogni backup (ad es.: "cosacompro_db_backup_20160320.db"
        String dbPath = dbHandler.getDBPath();
        if (dbPath == null) {
            Toast.makeText(main_activity, "dbPath è null!", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Destination directory
            File backupDirectory = Environment.getExternalStoragePublicDirectory("CosaCompro");
            backupDirectory.mkdirs();

            if (isExternalStorageAvailable()) {
                // Source DB file path
                File currentDB = new File(dbPath);
                // Destination DB file path
                File backupDB = new File(backupDirectory, backupFileName);
                Log.i("backup dest", backupDirectory.getAbsolutePath());
                // Create Source and Destination channels for transfer
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                // 'DestFileSize' is the number of bytes that are transferred
                long fileSize = dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.i("Fatto", main_activity.getString(R.string.dbbackup_import_ok) + String.valueOf(fileSize) + " Bytes!");
                Toast.makeText(main_activity, main_activity.getString(R.string.dbbackup_import_ok), Toast.LENGTH_LONG).show();
            } else {
                Log.e("Permission denied", "Can't write to SD card, add permission");
                Toast.makeText(main_activity, "Can't write to SD card, add permission", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(main_activity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public boolean isExternalStorageAvailable() {
        // Retrieving the external storage state
        String state = Environment.getExternalStorageState();
        // Check if available
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
}
