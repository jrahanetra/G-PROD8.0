package mg.geit.jason;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DataManager extends SQLiteOpenHelper {
    public static final String db_name = "G-PROD8.db";
    public static final int db_vesrion = 1;

    public DataManager(Context context) {
        super(context, db_name, null, db_vesrion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String strSql = "Create TABLE ProduitCategorie (" +
                "id_Categorie integer primary key autoincrement, " +
                "name_Categorie text not null," +
                "icon_Categorie integer not null"+
                ")";
        String newStrSql = "Create Table Produits  (" +
                "id_Produit integer primary key autoincrement," +
                "name_Produit text not null," +
                "prix_Produit integer not null,"+
                "quantite_Produit integer not null,"+
                "description_Produit text not null,"+
                "id_Categorie integer,"+
                "foreign key(id_Categorie) references ProduitCategorie(id_Categorie)" +
                ")";
        db.execSQL(strSql);
        db.execSQL(newStrSql);
        Log.i("DataBase", "onCreate invoked");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void insertProduit(String name, int prix, int qtt, String description, int id_CatProduit)
    {
        String strSql = "Insert into Produits (name_Produit, prix_Produit, quantite_Produit, description_Produit,id_Categorie) values" +
                "('"+name+"',"+prix+","+qtt+",'"+description+"',"+id_CatProduit+")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "insertProduit invoked ");
    }

    public  void insertCatProduit(String name_Cat, int image_Cat){
        String strSql = "Insert into ProduitCategorie (name_Categorie, icon_Categorie) values"+
                "('"+name_Cat+"',"+image_Cat+")";
        SQLiteDatabase  db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "insertCatégories invoked ");
    }

    public List<Category> readCategories(){
        List<Category> listCat = new ArrayList<>();

        String strSql = "Select * from ProduitCategorie";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();

        while(! cursor.isAfterLast())
        {
            Category categoryData = new Category(cursor.getInt(0),cursor.getString(1), cursor.getInt(2));
            listCat.add(categoryData);
            cursor.moveToNext();
        }
        cursor.close();
        return listCat;
    }

    public List<Produit> readProduits(Integer id){
        List<Produit> listProduit = new ArrayList<>();

        String strSql = "Select * from Produits where id_Categorie = "+id+";";
        Cursor cursor = this.getWritableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();

        while(! cursor.isAfterLast()){
            Produit produit = new Produit(cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
            listProduit.add(produit);
            cursor.moveToNext();
        }
        cursor.close();
        return listProduit;
    }
}
