# ========== 导入依赖库 ==========
import pandas as pd
import numpy as np
import plotly.express as px
import plotly.io as pio
from scipy.stats import pearsonr, spearmanr


# ========== 全局配置与数据加载 ==========
data = {
    "Province/Region": ["Guangdong", "Jiangsu", "Shandong", "Zhejiang", "Sichuan", "Henan", "Hubei",
                        "Fujian", "Shanghai", "Hunan", "Anhui", "Beijing", "Hebei", "Shaanxi", "Jiangxi",
                        "Chongqing", "Liaoning", "Yunnan", "Guangxi", "Inner Mongolia", "Shanxi", "Guizhou",
                        "Xinjiang", "Tianjin", "Heilongjiang", "Jilin", "Gansu", "Hainan", "Ningxia",
                        "Qinghai", "Xizang", "Taiwan", "Hong Kong", "Macao"],
    "GDP (100M RMB)": [145800, 142352, 103197, 94545, 67665, 66633, 62661, 60200, 56709, 55309,
                    52989, 52073, 49305, 36551, 36020, 33758, 33183, 32766, 29727, 26710,
                    25496, 23562, 21462, 18540, 16878, 14974, 13698, 8109, 5696, 4122,
                    3032, 65795, 30452, 3721],
    "Comment Count (Person)": [164, 83, 58, 91, 58, 52, 29, 30, 78, 62,
                               42, 110, 30, 41, 23, 34, 33, 13, 29, 12,
                               15, 17, 6, 21, 11, 16, 6, 14, 4, 4,
                               1, 6, 7, 0]
}
df = pd.DataFrame(data)


# ========== 数据清洗：与四象限图保持一致 ==========
gdp_trimmed = df["GDP (100M RMB)"].nlargest(len(df)-1).nsmallest(len(df)-2)
part_trimmed = df["Comment Count (Person)"].nlargest(len(df)-1).nsmallest(len(df)-2)
trimmed_indices = gdp_trimmed.index.intersection(part_trimmed.index)
df_trimmed = df.loc[trimmed_indices].reset_index(drop=True)


# ========== 计算相关系数 ==========
pearson_corr, pearson_p = pearsonr(df_trimmed["GDP (100M RMB)"], df_trimmed["Comment Count (Person)"])
spearman_corr, spearman_p = spearmanr(df_trimmed["GDP (100M RMB)"], df_trimmed["Comment Count (Person)"])


# ========== 绘制学术化散点图 ==========
pio.templates.default = "plotly_white"
fig = px.scatter(
    df_trimmed,
    x="GDP (100M RMB)",
    y="Comment Count (Person)",
    text="Province/Region",
    trendline="ols",
    opacity=0.85,
    title=f"Correlation between Regional GDP and AI Learning Engagement",
    labels={
        "GDP (100M RMB)": "GDP (100 Million RMB)",
        "Comment Count (Person)": "AI Learning Engagement (Comment Count, Person)"
    },
    hover_data={
        "Province/Region": True,
        "GDP (100M RMB)": ":,.0f",
        "Comment Count (Person)": ":,.0f"
    }
)


# ========== 修复：兼容所有版本的趋势线颜色修改 ==========
fig.data[1].line.color = "#e74c3c"
fig.data[1].line.width = 2

# ========== 学术化样式 ==========
fig.update_traces(
    textposition="top right",
    textfont=dict(size=10, color="#2c3e50", family="Times New Roman"),
    marker=dict(size=10, color="#1f4e79", line=dict(width=1, color="white"))
)
fig.update_layout(
    title=dict(font=dict(size=16, weight="bold", family="Times New Roman"), x=0.5),
    xaxis=dict(title=dict(font=dict(size=13, weight="bold", family="Times New Roman")),
               showline=True, linewidth=1.5, linecolor="black", gridcolor="#ececec", zeroline=False),
    yaxis=dict(title=dict(font=dict(size=13, weight="bold", family="Times New Roman")),
               showline=True, linewidth=1.5, linecolor="black", gridcolor="#ececec", zeroline=False),
    plot_bgcolor="white", paper_bgcolor="white",
    margin=dict(l=50, r=30, t=80, b=50),
    font=dict(family="Times New Roman"),
    showlegend=False
)


# ========== 导出 ==========
save_file = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question3_Collection\Question3_correlation_analysis.html"
fig.write_html(save_file, include_plotlyjs=True)
print(f"\n--> Diagram is generated successfully, saved to:")
print(save_file)