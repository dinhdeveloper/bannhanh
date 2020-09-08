package qtc.project.banhangnhanh.sale.database;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ProductModelRoom.class}, version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao taskProduct();
}
