

# MLB 季後賽收益計算程序分析 | 概述

這個程序旨在計算 MLB（美國職棒大聯盟）球隊在季後賽中的潛在收益。它考慮了不同的比賽階段（季後賽和世界大賽）以及各種因素，如球場容量和上座率。

# team.java 程式碼分析 | 類別結構


### `Team` 類別

```java
class Team {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    public Team(String name, String stadium, int capacity, double playoffFillRate, double worldSeriesFillRate) {
        // 構造函數實現
    }
}
```

#### 特點：
- 使用斷言（assertions）確保輸入參數的有效性。
- 包含球隊的基本信息和相關統計數據。

### `PostseasonRevenue` 類別

```java
class PostseasonRevenue {
    // 常數定義
    private static final double TICKET_PRICE_PLAYOFF = 450;
    private static final double TICKET_PRICE_WORLD_SERIES = 800;
    private static final double REVENUE_SHARE_HOME = 0.85;
    private static final double REVENUE_SHARE_AWAY = 0.15;

    // 方法定義
    private static List<Team> initializeTeams() { ... }
    private static double calculateRevenue(Team team, int playoffGames, int worldSeriesGames) { ... }
    private static void calculateAndPrintRevenues(List<Team> teams) { ... }
    public static void main(String[] args) { ... }
}
```

## 主要方法分析

### `initializeTeams()`

- **功能**：初始化 MLB 球隊列表。
- **實現細節**：
  - 使用 `try-catch` 塊處理可能的 `AssertionError`。
  - 硬編碼球隊信息，包括名稱、球場、容量和上座率。

### `calculateRevenue()`

- **功能**：計算單個球隊的季後賽收益。
- **參數**：
  - `Team team`: 球隊對象
  - `int playoffGames`: 季後賽場次
  - `int worldSeriesGames`: 世界大賽場次
- **返回值**：計算得出的總收益。
- **異常處理**：
  - 拋出 `IllegalArgumentException` 如果比賽場次為負數。

### `calculateAndPrintRevenues()`

- **功能**：計算並打印所有球隊的收益情況。
- **實現細節**：
  - 遍歷球隊列表，計算每支球隊的最佳和最差收益情況。
  - 使用 `try-catch` 塊處理 `calculateRevenue()` 可能拋出的異常。

### `main()`

- **功能**：程序的入口點。
- **實現**：
  1. 調用 `initializeTeams()` 初始化球隊列表。
  2. 調用 `calculateAndPrintRevenues()` 計算並打印結果。

##  preventive programming

1. **斷言（Assertions）**：
   - 在 `Team` 類的構造函數中使用，確保輸入參數的有效性。

2. **異常處理**：
   - 在 `initializeTeams()` 中捕獲 `AssertionError`。
   - 在 `calculateRevenue()` 中拋出 `IllegalArgumentException`。
   - 在 `calculateAndPrintRevenues()` 中捕獲並處理異常。

3. **參數驗證**：
   - 在 `calculateRevenue()` 中檢查比賽場次的有效性。

4. **錯誤報告**：
   - 使用 `System.err.println()` 輸出錯誤信息。


## 





# Team2.java | 程式結構分析 


### 主要類別
1. `Team2`: 表示一支棒球隊伍
2. `PostseasonRevenue2`: 用於計算季後賽收益

### 配置文件
1. `teams.json`: 包含所有球隊的詳細資訊
2. `config.properties`: 包含全局設定和參數

## 程式碼分析

### Team2 類別
```java
class Team2 {
    String name;
    String stadium;
    int capacity;
    double playoffFillRate;
    double worldSeriesFillRate;

    // 構造函數
}
```
- 包含球隊的基本資訊
- 使用公共字段而非私有字段配合 getter/setter，可能存在封裝性問題

### PostseasonRevenue2 類別
```java
class PostseasonRevenue2 {
    private final double ticketPricePlayoff;
    private final double ticketPriceWorldSeries;
    private final double revenueShareHome;
    private final double revenueShareAway;

    // 構造函數、方法等
}
```
- 使用 `Properties` 對象讀取配置
- 包含計算收益的核心邏輯

#### 主要方法
1. `readTeamsFromJson`: 從 JSON 文件讀取球隊數據
2. `calculateRevenue`: 計算單支球隊的收益
3. `calculateAndPrintRevenues`: 計算並打印所有球隊的收益
4. `main`: 程式入口點

## 配置文件分析

### teams.json
- 包含每支球隊的詳細資訊
- 使用 JSON 格式，易於讀取和修改

### config.properties
- 包含全局設定，如票價、收益分配比例等
- 使用 properties 格式，方便程式讀取

