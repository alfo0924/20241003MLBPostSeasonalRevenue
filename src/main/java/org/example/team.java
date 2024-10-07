package org.example;

import java.util.*;

// 定義 Team 類別，用於表示一支棒球隊伍
class Team {
    String name;            // 球隊名稱
    String stadium;         // 主場球場名稱
    int capacity;           // 球場容量
    double playoffFillRate; // 季後賽上座率
    double worldSeriesFillRate; // 世界大賽上座率

    // Team 類別的構造函數，用於創建新的 Team 對象
    public Team(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        this.name = name;
        this.stadium = stadium;
        this.capacity = capacity;
        this.playoffFillRate = playoffFillRate;
        this.worldSeriesFillRate = worldSeriesFillRate;
    }
}

// 定義 PostseasonRevenue 類別，用於計算季後賽收益
class PostseasonRevenue {
    // 定義常數
    private static final double TICKET_PRICE_PLAYOFF = 450;       // 季後賽門票價格
    private static final double TICKET_PRICE_WORLD_SERIES = 800;  // 世界大賽門票價格
    private static final double REVENUE_SHARE_HOME = 0.85;        // 主場球隊收益份額
    private static final double REVENUE_SHARE_AWAY = 0.15;        // 客場球隊收益份額

    // 初始化球隊列表的方法
    private static List<Team> initializeTeams() {
        List<Team> teams = new ArrayList<>();
        // 添加各支球隊，包括名稱、球場、容量、季後賽上座率和世界大賽上座率
        teams.add(new Team("LAD 洛杉磯道奇", "Dodger Stadium 道奇體育場", 56000, 1.0, 1.0));
        teams.add(new Team("SD 聖地牙哥教士", "Petco Park 沛可球場", 40000, 1.0, 1.0));
        teams.add(new Team("MIL 密爾瓦基釀酒人", "American Family Field 美國家庭球場", 42000, 0.97, 1.0));
        teams.add(new Team("NYM 紐約大都會", "Citi Field 花旗球場", 41800, 1.0, 1.0));
        teams.add(new Team("ATL 亞特蘭大勇士", "Truist Park 信託公園", 41000, 1.0, 1.0));
        teams.add(new Team("BAL 巴爾的摩金鶯", "Oriole Park 金鶯公園", 45000, 1.0, 1.0));
        teams.add(new Team("NYY 紐約洋基", "Yankee Stadium 洋基體育場", 47000, 1.0, 1.0));
        teams.add(new Team("CLE 克里夫蘭守護者", "Progressive Field 進步球場", 34800, 0.98, 1.0));
        teams.add(new Team("HOU 休士頓太空人", "Minute Maid Park 美粒果棒球場", 41000, 1.0, 1.0));
        teams.add(new Team("KS 堪薩斯市皇家", "Kauffman Stadium 考夫曼體育場", 37000, 1.0, 1.0));
        teams.add(new Team("DET 底特律老虎隊", "Comerica Park 聯信球場", 41000, 1.0, 1.0));
        return teams;
    }

    // 計算單支球隊收益的方法
    private static double calculateRevenue(Team team, int playoffGames, int worldSeriesGames) {
        // 計算季後賽收益
        double playoffRevenue = team.capacity * team.playoffFillRate * TICKET_PRICE_PLAYOFF * REVENUE_SHARE_AWAY * playoffGames;
        // 計算世界大賽收益
        double worldSeriesRevenue = team.capacity * team.worldSeriesFillRate * TICKET_PRICE_WORLD_SERIES * REVENUE_SHARE_AWAY * worldSeriesGames;
        // 返回總收益
        return playoffRevenue + worldSeriesRevenue;
    }

    // 計算並打印所有球隊收益的方法
    private static void calculateAndPrintRevenues(List<Team> teams) {
        for (Team team : teams) {
            // 計算最差情況收益（在外卡系列賽中被淘汰）
            double worstRevenue = calculateRevenue(team, 2, 0);
            // 計算最佳情況收益（打到世界大賽第7場）
            double bestRevenue = calculateRevenue(team, 12, 4);

            // 打印球隊名稱
            System.out.printf("Team: %s%n", team.name);
            // 打印最差情況收益
            System.out.printf("Worst scenario revenue: $%.2f (Eliminated in Wild Card Series)%n", worstRevenue);
            // 打印最佳情況收益
            System.out.printf("Best scenario revenue: $%.2f (Reaches World Series Game 7)%n%n", bestRevenue);
        }
    }

    // 主方法
    public static void main(String[] args) {
        // 初始化球隊列表
        List<Team> teams = initializeTeams();
        // 計算並打印所有球隊的收益
        calculateAndPrintRevenues(teams);
    }
}