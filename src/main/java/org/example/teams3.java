package org.example;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Properties;

// 定義 Team3 類別，用於表示一支棒球隊伍
class Team3 {
    String name;        // 球隊名稱
    String stadium;     // 球場名稱
    int capacity;       // 球場容量
    double playoffFillRate;     // 季後賽上座率
    double worldSeriesFillRate; // 世界大賽上座率

    // Team3 類別的建構函數
    public Team3(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        // 使用斷言確保輸入參數的有效性
        assert name != null && !name.isEmpty() : "球隊名稱不能為空";
        assert stadium != null && !stadium.isEmpty() : "球場名稱不能為空";
        assert capacity > 0 : "球場容量必須大於0";
        assert playoffFillRate >= 0 && playoffFillRate <= 1 : "季後賽上座率必須在0到1之間";
        assert worldSeriesFillRate >= 0 && worldSeriesFillRate <= 1 : "世界大賽上座率必須在0到1之間";

        // 初始化物件屬性
        this.name = name;
        this.stadium = stadium;
        this.capacity = capacity;
        this.playoffFillRate = playoffFillRate;
        this.worldSeriesFillRate = worldSeriesFillRate;
    }
}

// 定義 PostseasonRevenue3 類別，用於計算季後賽收益
class PostseasonRevenue3 {
    private final double ticketPricePlayoff;     // 季後賽票價
    private final double ticketPriceWorldSeries; // 世界大賽票價
    private final double revenueShareHome;       // 主場收益分成比例
    private final double revenueShareAway;       // 客場收益分成比例

    // PostseasonRevenue3 類別的建構函數
    public PostseasonRevenue3(Properties props) throws IllegalArgumentException {
        // 從配置檔案讀取各項參數，如果沒有設定則使用預設值
        this.ticketPricePlayoff = getDoubleProperty(props, "ticket.price.playoff", 450);
        this.ticketPriceWorldSeries = getDoubleProperty(props, "ticket.price.worldseries", 800);
        this.revenueShareHome = getDoubleProperty(props, "revenue.share.home", 0.85);
        this.revenueShareAway = getDoubleProperty(props, "revenue.share.away", 0.15);

        // 驗證收益分配比例總和是否為1
        if (Math.abs(this.revenueShareHome + this.revenueShareAway - 1.0) > 0.000001) {
            throw new IllegalArgumentException("收益分配比例總和必須為1");
        }
    }

    // 從 Properties 物件中讀取 double 類型的屬性值
    private double getDoubleProperty(Properties props, String key, double defaultValue) throws IllegalArgumentException {
        String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("無效的數值設定: " + key);
        }
    }

    // 從 JSON 文件讀取球隊數據
    private static List<Team3> readTeamsFromJson(String filename) throws IOException, ParseException {
        List<Team3> teams = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filename)) {
            JSONArray jsonTeams = (JSONArray) parser.parse(reader);

            for (Object obj : jsonTeams) {
                JSONObject jsonTeam = (JSONObject) obj;
                teams.add(new Team3(
                        (String) jsonTeam.get("name"),
                        (String) jsonTeam.get("stadium"),
                        ((Long) jsonTeam.get("capacity")).intValue(),
                        (Double) jsonTeam.get("playoffFillRate"),
                        (Double) jsonTeam.get("worldSeriesFillRate")
                ));
            }
        }

        return teams;
    }

    // 計算單支球隊在特定情況下的收益
    private double calculateRevenue(Team3 team, int homeGames, int awayGames, boolean isWorldSeries) {
        if (homeGames < 0 || awayGames < 0) {
            throw new IllegalArgumentException("比賽場次不能為負數");
        }

        // 根據是否為世界大賽決定使用的票價和上座率
        double ticketPrice = isWorldSeries ? ticketPriceWorldSeries : ticketPricePlayoff;
        double fillRate = isWorldSeries ? team.worldSeriesFillRate : team.playoffFillRate;

        // 計算主場和客場收益
        double homeRevenue = team.capacity * fillRate * ticketPrice * revenueShareHome * homeGames;
        double awayRevenue = team.capacity * fillRate * ticketPrice * revenueShareAway * awayGames;

        return homeRevenue + awayRevenue;
    }

    // 計算並打印所有球隊的收益情況
    public void calculateAndPrintRevenues(List<Team3> teams) {
        for (Team3 team : teams) {
            try {
                // 計算最差情況：在外卡賽被淘汰（客場2場）
                double worstRevenue = calculateRevenue(team, 0, 2, false);

                // 計算最好情況：打到世界大賽第7場（假設是較低種子，主場3場，客場4場）
                double bestRevenue = calculateRevenue(team, 2, 1, false) + // 外卡賽
                        calculateRevenue(team, 2, 3, false) + // 分區系列賽
                        calculateRevenue(team, 3, 4, false) + // 聯盟冠軍賽
                        calculateRevenue(team, 3, 4, true);   // 世界大賽

                // 計算其他情境
                // 在分區系列賽被淘汰
                double eliminatedInDivisionSeries = calculateRevenue(team, 2, 1, false) + // 外卡賽
                        calculateRevenue(team, 0, 3, false);  // 分區系列賽

                // 在聯盟冠軍賽被淘汰
                double eliminatedInChampionshipSeries = calculateRevenue(team, 2, 1, false) + // 外卡賽
                        calculateRevenue(team, 2, 3, false) + // 分區系列賽
                        calculateRevenue(team, 0, 4, false);  // 聯盟冠軍賽

                // 打印結果
                System.out.printf("%nTeam 隊伍: %s%n", team.name);
                System.out.printf("Eliminated in Division Series 在分區系列賽淘汰 : $%.2f 美元%n", eliminatedInDivisionSeries);
                System.out.printf("Eliminated in Wild Card Series 在外卡賽淘汰 : $%.2f 美元%n", worstRevenue);
                System.out.printf("Eliminated in Championship Series 在聯盟冠軍賽淘汰 : $%.2f 美元%n", eliminatedInChampionshipSeries);
                System.out.printf("Reaches World Series Game 7 打到世界大賽第7場 : $%.2f 美元 %n%n", bestRevenue);
            } catch (IllegalArgumentException e) {
                System.err.println("計算 " + team.name + " 的收益時發生錯誤: " + e.getMessage());
            }
        }
    }

    // 主方法
    public static void main(String[] args) {
        try {
            // 讀取配置文件
            Properties props = new Properties();
            try (InputStream input = PostseasonRevenue3.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("無法找到 config.properties 文件");
                }
                props.load(input);
            }

            // 創建 PostseasonRevenue3 物件
            PostseasonRevenue3 revenue = new PostseasonRevenue3(props);

            // 從 JSON 文件讀取球隊數據
            String jsonPath = props.getProperty("teams.json.path", "src/main/resources/teams.json");
            List<Team3> teams = readTeamsFromJson(jsonPath);

            // 計算並打印所有球隊的收益
            revenue.calculateAndPrintRevenues(teams);
        } catch (IOException e) {
            System.err.println("讀取文件時發生錯誤: " + e.getMessage());
        } catch (ParseException e) {
            System.err.println("解析 JSON 文件時發生錯誤: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("配置文件中的參數無效: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("發生未預期的錯誤: " + e.getMessage());
            e.printStackTrace();
        }
    }
}