// data/local/database/AppDatabase.kt
package com.khz.madahi.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.khz.madahi.data.local.database.dao.CategoryDAO
import com.khz.madahi.data.local.database.dao.ContentDAO
import com.khz.madahi.data.local.database.dao.FavoriteDAO
import com.khz.madahi.helper.DB_NAME
import com.khz.madahi.models.Category
import com.khz.madahi.models.Content
import com.khz.madahi.models.Favorite

// ⚠️ نسخه ۴: داده‌های محلی کش هستند؛ همه‌چیز از سرور sync می‌شود.
// نسخه‌های ۲→۴ به‌دلیل تغییرات گسترده نوع ستون‌ها (String → Int)
// با fallbackToDestructiveMigration ارتقا یافته‌اند.
// از نسخه ۴ به بعد، هر تغییر schema باید Migration مشخص داشته باشد.
@Database(
    entities = [Category::class, Content::class, Favorite::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDAO(): CategoryDAO
    abstract fun contentDAO(): ContentDAO
    abstract fun favoriteDAO(): FavoriteDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE
                    ?: synchronized(this) {
                        val instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            DB_NAME
                        )
                            // ✅ allowMainThreadQueries() حذف شد.
                            // همه‌ی متدهای DAO الان suspend هستند و باید از
                            // یک CoroutineScope (مثل viewModelScope) فراخوانی شوند
                            // تا روی Dispatchers.IO اجرا شده و از ANR جلوگیری شود.
                            // ✅ Migrationهای شناخته‌شده اعمال می‌شوند.
                            // fallbackToDestructiveMigration فقط برای ارتقاهای
                            // ناشناخته (جهش نسخه بدون Migration) فعال است.
                            .fallbackToDestructiveMigration(true)
                            .build()
                        INSTANCE = instance
                        instance
                    }
        }
    }
}