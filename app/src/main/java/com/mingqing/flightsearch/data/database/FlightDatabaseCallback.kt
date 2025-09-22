package com.mingqing.flightsearch.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlightDatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // 在数据库首次创建时预填充机场数据
        CoroutineScope(Dispatchers.IO).launch {
            prepopulateAirports(db)
        }
    }

    private fun prepopulateAirports(db: SupportSQLiteDatabase) {
        // 预填充主要国际机场数据
        val airportData = arrayOf(
            // 美国主要机场
            arrayOf(1, "LAX", "Los Angeles International Airport", 84557968),
            arrayOf(2, "JFK", "John F. Kennedy International Airport", 62551253),
            arrayOf(3, "ORD", "O'Hare International Airport", 84372618),
            arrayOf(4, "ATL", "Hartsfield-Jackson Atlanta International Airport", 104171935),
            arrayOf(5, "DFW", "Dallas/Fort Worth International Airport", 75066956),
            arrayOf(6, "SFO", "San Francisco International Airport", 57834658),
            arrayOf(7, "LAS", "McCarran International Airport", 49348460),
            arrayOf(8, "SEA", "Seattle-Tacoma International Airport", 49849520),
            arrayOf(9, "MIA", "Miami International Airport", 45933463),
            arrayOf(10, "EWR", "Newark Liberty International Airport", 46336452),

            // 欧洲主要机场
            arrayOf(11, "LHR", "London Heathrow Airport", 80126320),
            arrayOf(12, "CDG", "Charles de Gaulle Airport", 76150009),
            arrayOf(13, "AMS", "Amsterdam Airport Schiphol", 71707144),
            arrayOf(14, "FRA", "Frankfurt Airport", 70556072),
            arrayOf(15, "IST", "Istanbul Airport", 68192683),
            arrayOf(16, "MAD", "Madrid-Barajas Airport", 61734944),
            arrayOf(17, "BCN", "Barcelona Airport", 52686314),
            arrayOf(18, "LGW", "London Gatwick Airport", 46574786),
            arrayOf(19, "MUC", "Munich Airport", 47959885),
            arrayOf(20, "FCO", "Leonardo da Vinci Airport", 43532573),

            // 亚洲主要机场
            arrayOf(21, "PEK", "Beijing Capital International Airport", 100013642),
            arrayOf(22, "HND", "Tokyo Haneda Airport", 87131973),
            arrayOf(23, "ICN", "Incheon International Airport", 71247728),
            arrayOf(24, "SIN", "Singapore Changi Airport", 68283000),
            arrayOf(25, "BKK", "Suvarnabhumi Airport", 65081392),
            arrayOf(26, "NRT", "Narita International Airport", 43317298),
            arrayOf(27, "KUL", "Kuala Lumpur International Airport", 25068103),
            arrayOf(28, "HKG", "Hong Kong International Airport", 74517402),
            arrayOf(29, "TPE", "Taiwan Taoyuan International Airport", 48360295),
            arrayOf(30, "CGK", "Soekarno-Hatta International Airport", 66908159),

            // 中东和其他地区
            arrayOf(31, "DXB", "Dubai International Airport", 89149387),
            arrayOf(32, "DOH", "Hamad International Airport", 38779492),
            arrayOf(33, "SYD", "Kingsford Smith Airport", 44397736),
            arrayOf(34, "MEL", "Melbourne Airport", 37719897),
            arrayOf(35, "YYZ", "Toronto Pearson International Airport", 50478614),
            arrayOf(36, "YVR", "Vancouver International Airport", 26379784),

            // 中国主要机场
            arrayOf(37, "PVG", "Shanghai Pudong International Airport", 76153455),
            arrayOf(38, "CAN", "Guangzhou Baiyun International Airport", 73387403),
            arrayOf(39, "CTU", "Chengdu Shuangliu International Airport", 55916653),
            arrayOf(40, "KMG", "Kunming Changshui International Airport", 48076296),
            arrayOf(41, "XIY", "Xi'an Xianyang International Airport", 47169722),
            arrayOf(42, "SZX", "Shenzhen Bao'an International Airport", 52934567),
            arrayOf(43, "HAK", "Haikou Meilan International Airport", 24136757),
            arrayOf(44, "TSN", "Tianjin Binhai International Airport", 24100727),
            arrayOf(45, "WUH", "Wuhan Tianhe International Airport", 27290589),
            arrayOf(46, "NKG", "Nanjing Lukou International Airport", 31250788),

            // 更多国际机场
            arrayOf(47, "GRU", "São Paulo/Guarulhos International Airport", 42242817),
            arrayOf(48, "MEX", "Mexico City International Airport", 50308149),
            arrayOf(49, "BOG", "El Dorado International Airport", 32653887),
            arrayOf(50, "JNB", "O. R. Tambo International Airport", 21230824)
        )

        // 批量插入数据
        airportData.forEach { data ->
            db.execSQL(
                "INSERT INTO airport (id, iata_code, name, passengers) VALUES (?, ?, ?, ?)",
                data
            )
        }
    }
}