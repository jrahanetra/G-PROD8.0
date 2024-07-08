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

    public DataManager(Context context)
    {
        super(context, db_name, null, db_vesrion);
    }

    /**
     * CREATE THE BASE AND ALL TABLES IN THIS FUNCTION
     * @param db The database.
     */
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
                "imageUrl_Produit text not null,"+
                "id_Categorie integer,"+
                "foreign key(id_Categorie) references ProduitCategorie(id_Categorie)" +
                ")";
        String createAdmin = "Create Table Admin (" +
                "id_Admin integer primary key autoincrement," +
                "username text not null," +
                "mot_de_passe text not null)";
        db.execSQL(strSql);
        db.execSQL(newStrSql);
        db.execSQL(createAdmin);
        Log.i("DataBase", "onCreate invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * FUNCTION TO READ ALL PRODUCTS IN THE TABLE PRODUITS
     */
    public List<Produit> readAllProduct()
    {
        List<Produit> listProduit = new ArrayList<>();
        String strSql = "SELECT * FROM Produits";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();
        while(! cursor.isAfterLast()){
            Produit produit = new Produit(cursor.getInt(0),cursor.getString(1), cursor.getDouble(2), cursor.getInt(3),cursor.getString(5), cursor.getString(4), cursor.getInt(6));
            listProduit.add(produit);
            cursor.moveToNext();
        }
        cursor.close();
        return listProduit;
    }

    /**
     * FUNCTION TO INSERT PRODUCT IN A CATEGORY
     * @param name the name of the product to insert
     * @param prix the price of the product to insert
     * @param qtt the quantity of the product to insert
     * @param description the description of the product to insert
     * @param imageUrl the image url of the product to insert
     * @param id_CatProduit the id of the catgory that this product belong to
     */
    public void insertProduct(String name, double prix, double qtt, String description, String imageUrl, int id_CatProduit)
    {
        // Échapper les apostrophes
        name = name.replace("'", "''");
        description = description.replace("'", "''");

        String strSql = "Insert into Produits (name_Produit, prix_Produit, quantite_Produit, description_Produit, imageUrl_Produit, id_Categorie) values" +
                "('"+name+"',"+prix+","+qtt+",'"+description+"','"+imageUrl+"',"+id_CatProduit+")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "insertProduit invoked ");
    }

    /**
     * FUNCTION TO UPDATE PRODUCT
     * @param id the id of the product
     * @param name the name of the product
     * @param prix the price of the product
     * @param qtt the quantity of the product
     * @param imageUrl the url image of the product
     * @param description the description of the product
     */
    public void updateProduct(int id, String name, int prix, int qtt, String imageUrl, String description)
    {
        String strSql = "UPDATE Produits SET " +
                "name_Produit = '" + name + "', " +
                "prix_Produit = " + prix + ", " +
                "quantite_Produit = " + qtt + ", " +
                "description_Produit = '" + description +"',"+
                "imageUrl_Produit = '" + imageUrl +"' "+
                "WHERE id_Produit = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "updateProduct invoked for id_Produit: " + id);
    }

    /**
     * FUNCTION TO INSERT PERSONAL USER
     * @param username
     * @param password
     */
    public void insertAdmin(String username,String password)
    {
        String str = "Insert into Admin (username, mot_de_passe) values" +
                "('"+username+"','"+password+"')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(str);
        Log.i("DataBase","Admin create");
    }

    /**
     * FUNCTION TO VERIFY IF ADMIN OR NO
     * @param username
     * @param password
     */
    public boolean isAdmin(String username, String password)
    {
        List<Admin> listAdmin = new ArrayList<>();
        String strSql = "Select* from Admin";
        Cursor cursor = this.getReadableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();
        while (! cursor.isAfterLast()){
            Admin admin = new Admin(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2));
            listAdmin.add(admin);
            cursor.moveToNext();
        }
        cursor.close();

        int check = 0;
        for (Admin admin : listAdmin){
            if(admin.getUsername().equals(username) && admin.getPassword().equals(password)){
                check ++;
            }
        }
        Log.i("DataBase", ""+check);
        if (check > 0)
            return true;
        else
            return false;
    }

    /**
     * FUNCTION TO INSERT DATA PRODUCT
     * @param name_Cat
     * @param image_CatUrl
     */
    public  void insertCatProduct(String name_Cat, String image_CatUrl)
    {
        String strSql = "Insert into ProduitCategorie (name_Categorie, image_CategorieUrl) values"+
                "('"+name_Cat+"','"+image_CatUrl+"')";
        SQLiteDatabase  db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "insertCatégories invoked ");
    }

    /**
     * FUNCTION TO READCATORY
     * @return List<Category>
     */
    public List<Category> readCategory()
    {
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

    /**
     * FUNCTION TO READ ALL PRODUCTS
     * @param id
     * @return List<Category>
     */
    public List<Produit> readProducts(Integer id)
    {
        List<Produit> listProduit = new ArrayList<>();

        String strSql = "Select * from Produits where id_Categorie = "+id+";";
        Cursor cursor = this.getWritableDatabase().rawQuery(strSql, null);
        cursor.moveToFirst();

        while(! cursor.isAfterLast()){
            Produit produit = new Produit(cursor.getInt(0),cursor.getString(1), cursor.getDouble(2), cursor.getInt(3),cursor.getString(5), cursor.getString(4), cursor.getInt(6));
            listProduit.add(produit);
            cursor.moveToNext();
        }
        cursor.close();
        return listProduit;
    }

    /**
     * FUNCTION TO READ ONE SPECIFY PRODUCT BY HIS ID
     * @param idProduct
     * @return Produit
     */
    public Produit readProduct(Integer idProduct)
    {
        String strSql = "Select * from Produits where id_Produit = "+idProduct;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(strSql, null);
        Produit produit = null;
        if(cursor != null && cursor.moveToFirst())
            // Le curseur contient des données, on peut les lire
            produit = new Produit(cursor.getInt(0),cursor.getString(1), cursor.getDouble(2), cursor.getInt(3), cursor.getString(5), cursor.getString(4), cursor.getInt(6));
        else
            Log.i("Database","le curseur ne contient pas de donnee");

        // Fermer le curseur et la base de données
        if (cursor != null)
            cursor.close();
        db.close();
        return  produit;
    }

    /**
     * TO VERIFY IF TABLE CATEGORY ARE EMPTY OR NO
     * @return boolean
     */
    public boolean isCategoryTableEmpty()
    {
        String strSql = "SELECT COUNT(*) FROM ProduitCategorie";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(strSql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count == 0;
    }

    /**
     * FUNCTION TO DELETE PRODUCT
     * @param idProduct : Integer, the integer of the product to delete
     */
    public void deleteProduct(Integer idProduct)
    {
        String strSql = "Delete from Produits where id_Produit= "+idProduct+"";
        SQLiteDatabase  db = this.getWritableDatabase();
        db.execSQL(strSql);
        Log.i("DataBase", "suppressProduct invoked ");
    }
}
