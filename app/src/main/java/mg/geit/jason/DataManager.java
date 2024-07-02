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
                "image_CategorieUrl text not null"+
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

    public void insertProduct(String name, int prix, int qtt, String description, int id_CatProduit)
    {
        String strSql = "Insert into Produits (name_Produit, prix_Produit, quantite_Produit, description_Produit,id_Categorie) values" +
                "('"+name+"',"+prix+","+qtt+",'"+description+"',"+id_CatProduit+")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "insertProduit invoked ");
    }

    public void updateProduct(int id, String name, int prix, int qtt, String description) {
        String strSql = "UPDATE Produits SET " +
                "name_Produit = '" + name + "', " +
                "prix_Produit = " + prix + ", " +
                "quantite_Produit = " + qtt + ", " +
                "description_Produit = '" + description +"' "+
                "WHERE id_Produit = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "updateProduct invoked for id_Produit: " + id);
    }

    public  void insertCatProduct(String name_Cat, String image_CatUrl){
        String strSql = "Insert into ProduitCategorie (name_Categorie, image_CategorieUrl) values"+
                "('"+name_Cat+"','"+image_CatUrl+"')";
        SQLiteDatabase  db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "insertCatégories invoked ");
    }

    public List<Category> readCategory(){
        List<Category> listCat = new ArrayList<>();

        String strSql = "Select * from ProduitCategorie";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();

        while(! cursor.isAfterLast())
        {
            Category categoryData = new Category(cursor.getInt(0),cursor.getString(1), cursor.getString(2));
            listCat.add(categoryData);
            cursor.moveToNext();
        }
        cursor.close();
        return listCat;
    }

    public List<Produit> readProducts(Integer id){
        List<Produit> listProduit = new ArrayList<>();

        String strSql = "Select * from Produits where id_Categorie = "+id+";";
        Cursor cursor = this.getWritableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();

        while(! cursor.isAfterLast()){
            Produit produit = new Produit(cursor.getInt(0),cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
            listProduit.add(produit);
            cursor.moveToNext();
        }
        cursor.close();
        return listProduit;
    }

    public Produit readProduct(Integer idProduct){
        String strSql = "Select * from Produits where id_Produit = "+idProduct;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(strSql, null);
        Produit produit = null;
        if(cursor != null && cursor.moveToFirst())
            // Le curseur contient des données, on peut les lire
            produit = new Produit(cursor.getInt(0),cursor.getString(1), cursor.getInt(2), cursor.getInt(3), cursor.getString(4));
        else
            Log.i("Database","le curseur ne contient pas de donnee");

        // Fermer le curseur et la base de données
        if (cursor != null)
            cursor.close();
        db.close();
        return  produit;
    }
}
