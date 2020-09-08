package qtc.project.banhangnhanh.sale.database;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM ProductModelRoom")
    List<ProductModelRoom> getAll();

    @Insert
    void insert(ProductModelRoom productModelRoom);

    @Delete
    void delete(ProductModelRoom productModelRoom);

    @Update
    void update(ProductModelRoom productModelRoom);

//    @Query(("SELECT *,COUNT(productId) AS pro" +
//            " FROM ProductModelRoom GROUP BY productId HAVING COUNT(productId) > 1"))
//    List<ProductModelRoom> getCount();
//
////    @Query(("UPDATE ProductModelRoom SET quantity =?1 "))
////    List<ProductModelRoom> updateProduct();

}
