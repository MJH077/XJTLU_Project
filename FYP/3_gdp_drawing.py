# ========== 导入依赖 ==========
import pandas as pd
import plotly.express as px


# ========== 1. 官方2025年GDP数据（内地31省+港澳台） ==========
data = {
    "Regions": [
        "Guangdong", "Jiangsu", "Shandong", "Zhejiang", "Sichuan", "Henan", "Taiwan",
        "Hubei", "Fujian", "Shanghai", "Hunan", "Anhui", "Beijing", "Hebei", "Shaanxi",
        "Jiangxi", "Chongqing", "Liaoning", "Yunnan", "Hong Kong", "Guangxi", "Inner Mongolia",
        "Shanxi", "Guizhou", "Xinjiang", "Tianjin", "Heilongjiang", "Jilin", "Gansu", "Hainan",
        "Ningxia", "Qinghai", "Macao", "Xizang"
    ],
    "GDP (100M RMB)": [
        145800, 142352, 103197, 94545, 67665, 66633, 65795, 62661, 60200, 56709, 55309,
        52989, 52073, 49305, 36551, 36020, 33758, 33183, 32766, 30452, 29727, 26710,
        25496, 23562, 21462, 18540, 16878, 14974, 13698, 8109, 5696, 4122, 3721, 3032
    ]
}
df = pd.DataFrame(data)
save_html = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question1_Collection\Question1_Region_gdp.html"

# ========== 2. 绘制学术风格折线图 ==========
fig = px.bar(
    df,
    x="Regions",
    y="GDP (100M RMB)",
    title="GDP Economic Aggregate of all provinces/regions in China in 2025\n (Source: http://hnzdhd.stats.gov.cn)",
    template="plotly_white",
    color_discrete_sequence=["#1f4e79"]
)

fig.update_traces(
    text=df["GDP (100M RMB)"],
    textposition="outside",
    textfont=dict(
        size=10,
        color="#2c3e50",
        family="Times New Roman"
    ),
    marker=dict(
        color="#1f4e79",
        line=dict(color="black", width=1)
    ),
    width=0.6
)

fig.update_layout(
    title_font=dict(size=24, weight="bold", family="Times New Roman", color="black"),
    title_x=0.5,
    xaxis_title="Province/Region",
    xaxis_title_font=dict(size=13, weight="bold", family="Times New Roman"),
    xaxis_showline=True,
    xaxis_linewidth=1.5,
    xaxis_linecolor="black",
    xaxis_tickangle=-45,
    yaxis_title="GDP (100M RMB)",
    yaxis_title_font=dict(size=13, weight="bold", family="Times New Roman"),
    yaxis_showline=True,
    yaxis_linewidth=1.5,
    yaxis_linecolor="black",
    plot_bgcolor="white",
    paper_bgcolor="white",
    showlegend=False
)


# ========== 3. 保存为HTML ==========
fig.write_html(save_html)
print(f"\n--> Region Line is generated successfully, saved to:")
print(save_html)