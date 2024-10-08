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

// 定義 Team 類別，用於表示一支棒球隊伍
class Team3 {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team3(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        // 使用斷言確保參數有效性
        assert name != null && !name.isEmpty() : "球隊名稱不能為空";
        assert stadium != null && !stadium.isEmpty() : "球場名稱不能為空";
        assert capacity > 0 : "球場容量必須大於0";
        assert playoffFillRate >= 0 && playoffFillRate <= 1 : "季後賽上座率必須在0到1之間";
        assert worldSeriesFillRate >= 0 && worldSeriesFillRate <= 1 : "世界大賽上座率必須在0到1之間";

        this.name = name;
        this.stadium = stadium;
        this.capacity = capacity;
        this.playoffFillRate = playoffFillRate;
        this.worldSeriesFillRate = worldSeriesFillRate;
    }
}

// 定義 PostseasonRevenue 類別，用於計算季後賽收益
class PostseasonRevenue3 {
    private final double ticketPricePlayoff;
    private final double ticketPriceWorldSeries;
    private final double revenueShareHome;
    private final double revenueShareAway;
    private final int playoffWorstGames;
    private final int playoffBestGames;
    private final int worldSeriesWorstGames;
    private final int worldSeriesBestGames;

    public PostseasonRevenue3(Properties props) throws IllegalArgumentException {
        this.ticketPricePlayoff = getDoubleProperty(props, "ticket.price.playoff", 450);
        this.ticketPriceWorldSeries = getDoubleProperty(props, "ticket.price.worldseries", 800);
        this.revenueShareHome = getDoubleProperty(props, "revenue.share.home", 0.85);
        this.revenueShareAway = getDoubleProperty(props, "revenue.share.away", 0.15);
        this.playoffWorstGames = getIntProperty(props, "games.playoff.worst", 2);
        this.playoffBestGames = getIntProperty(props, "games.playoff.best", 12);
        this.worldSeriesWorstGames = getIntProperty(props, "games.worldseries.worst", 0);
        this.worldSeriesBestGames = getIntProperty(props, "games.worldseries.best", 4);

        // 驗證收益分配比例總和為1
        if (Math.abs(this.revenueShareHome + this.revenueShareAway - 1.0) > 0.000001) {
            throw new IllegalArgumentException("收益分配比例總和必須為1");
        }
    }

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

    private int getIntProperty(Properties props, String key, int defaultValue) throws IllegalArgumentException {
        String value = props.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("無效的整數設定: " + key);
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

    // 計算單支球隊收益的方法
    private double calculateRevenue(Team3 team, int playoffGames, int worldSeriesGames) throws IllegalArgumentException {
        if (playoffGames < 0 || worldSeriesGames < 0) {
            throw new IllegalArgumentException("比賽場次不能為負數");
        }

        double playoffRevenue = team.capacity * team.playoffFillRate * ticketPricePlayoff * revenueShareAway * playoffGames;
        double worldSeriesRevenue = team.capacity * team.worldSeriesFillRate * ticketPriceWorldSeries * revenueShareAway * worldSeriesGames;
        return playoffRevenue + worldSeriesRevenue;
    }

    // 計算並打印所有球隊收益的方法
    public void calculateAndPrintRevenues(List<Team3> teams) {
        for (Team3 team : teams) {
            try {
                double worstRevenue = calculateRevenue(team, playoffWorstGames, worldSeriesWorstGames);
                double bestRevenue = calculateRevenue(team, playoffBestGames, worldSeriesBestGames);

                System.out.printf("Team: %s%n", team.name);
                System.out.printf("Worst scenario revenue: $%.2f (Eliminated in Wild Card Series)%n", worstRevenue);
                System.out.printf("Best scenario revenue: $%.2f (Reaches World Series Game 7)%n%n", bestRevenue);
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
            try (InputStream input = PostseasonRevenue2.class.getClassLoader().getResourceAsStream("config.properties")) {
                if (input == null) {
                    throw new IOException("無法找到 config.properties 文件");
                }
                props.load(input);
            }

            // 初始化 PostseasonRevenue 對象
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