package com.khz.madahi.helper

const val DB_NAME: String = "Madahi.db"
const val BASE_URL: String = "https://api.madahinote.ir/api/"

// ✅ SKU محصول «نسخه پرو» در پنل پرداخت بازار (باید با BAZAAR_PRO_PRODUCT_ID سرور یکی باشد)
const val BAZAAR_PRO_PRODUCT_ID: String = "pro_version"

const val TABLE_NAME_CONTENT: String = "table_name_content"
const val TABLE_NAME_CATEGORIES: String = "table_name_categories"
const val TABLE_NAME_FAVORITE: String = "table_name_favorite"

const val TABLE_NAME_CONTENT_TYPE: String = "table_name_content_type"
const val TABLE_NAME_MESSAGE: String = "table_name_message"

// ✅ شناسه‌ی کاربر مهمان/پیش‌فرض که در چند فایل (CategoryItem, ContentItem,
// ViewModel ها) به‌صورت رشته‌ی جادویی "0" پخش شده بود، الان یک‌جا تعریف شده.
// در فایل‌های دیگر به‌جای "0" از GUEST_USER_ID استفاده کنید.
const val GUEST_USER_ID: Int = 0
