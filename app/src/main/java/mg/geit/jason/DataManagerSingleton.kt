package mg.geit.jason

import android.content.Context

/**
 * THIS IS THE ONLY INSTANCE OF THE DATAMANAGER
 */
object DataManagerSingleton {
    private var instance: DataManager? = null

    /**
     * FUnction to get the instance
     * @param context: Context the context
     */
    fun getInstance(context: Context): DataManager {
        if (instance == null) {
            instance = DataManager(context.applicationContext)
        }
        return instance!!
    }
}
