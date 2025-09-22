-- 机场数据预填充脚本
-- 包含主要国际机场的真实数据

INSERT INTO airport (id, iata_code, name, passengers) VALUES
-- 美国主要机场
(1, 'LAX', 'Los Angeles International Airport', 84557968),
(2, 'JFK', 'John F. Kennedy International Airport', 62551253),
(3, 'ORD', 'O''Hare International Airport', 84372618),
(4, 'ATL', 'Hartsfield-Jackson Atlanta International Airport', 104171935),
(5, 'DFW', 'Dallas/Fort Worth International Airport', 75066956),
(6, 'SFO', 'San Francisco International Airport', 57834658),
(7, 'LAS', 'McCarran International Airport', 49348460),
(8, 'SEA', 'Seattle-Tacoma International Airport', 49849520),
(9, 'MIA', 'Miami International Airport', 45933463),
(10, 'EWR', 'Newark Liberty International Airport', 46336452),

-- 欧洲主要机场
(11, 'LHR', 'London Heathrow Airport', 80126320),
(12, 'CDG', 'Charles de Gaulle Airport', 76150009),
(13, 'AMS', 'Amsterdam Airport Schiphol', 71707144),
(14, 'FRA', 'Frankfurt Airport', 70556072),
(15, 'IST', 'Istanbul Airport', 68192683),
(16, 'MAD', 'Madrid-Barajas Airport', 61734944),
(17, 'BCN', 'Barcelona Airport', 52686314),
(18, 'LGW', 'London Gatwick Airport', 46574786),
(19, 'MUC', 'Munich Airport', 47959885),
(20, 'FCO', 'Leonardo da Vinci Airport', 43532573),

-- 亚洲主要机场
(21, 'PEK', 'Beijing Capital International Airport', 100013642),
(22, 'HND', 'Tokyo Haneda Airport', 87131973),
(23, 'ICN', 'Incheon International Airport', 71247728),
(24, 'SIN', 'Singapore Changi Airport', 68283000),
(25, 'BKK', 'Suvarnabhumi Airport', 65081392),
(26, 'NRT', 'Narita International Airport', 43317298),
(27, 'KUL', 'Kuala Lumpur International Airport', 25068103),
(28, 'HKG', 'Hong Kong International Airport', 74517402),
(29, 'TPE', 'Taiwan Taoyuan International Airport', 48360295),
(30, 'CGK', 'Soekarno-Hatta International Airport', 66908159),

-- 中东和其他地区
(31, 'DXB', 'Dubai International Airport', 89149387),
(32, 'DOH', 'Hamad International Airport', 38779492),
(33, 'SYD', 'Kingsford Smith Airport', 44397736),
(34, 'MEL', 'Melbourne Airport', 37719897),
(35, 'YYZ', 'Toronto Pearson International Airport', 50478614),
(36, 'YVR', 'Vancouver International Airport', 26379784),

-- 中国主要机场
(37, 'PVG', 'Shanghai Pudong International Airport', 76153455),
(38, 'CAN', 'Guangzhou Baiyun International Airport', 73387403),
(39, 'CTU', 'Chengdu Shuangliu International Airport', 55916653),
(40, 'KMG', 'Kunming Changshui International Airport', 48076296),
(41, 'XIY', 'Xi''an Xianyang International Airport', 47169722),
(42, 'SZX', 'Shenzhen Bao''an International Airport', 52934567),
(43, 'HAK', 'Haikou Meilan International Airport', 24136757),
(44, 'TSN', 'Tianjin Binhai International Airport', 24100727),
(45, 'WUH', 'Wuhan Tianhe International Airport', 27290589),
(46, 'NKG', 'Nanjing Lukou International Airport', 31250788),

-- 更多国际机场
(47, 'GRU', 'São Paulo/Guarulhos International Airport', 42242817),
(48, 'MEX', 'Mexico City International Airport', 50308149),
(49, 'BOG', 'El Dorado International Airport', 32653887),
(50, 'JNB', 'O. R. Tambo International Airport', 21230824);