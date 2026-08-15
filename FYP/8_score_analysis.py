# ========== 导入依赖 ==========
import pandas as pd
import plotly.express as px
import plotly.io as pio
import numpy as np


# ========== 全局配置 ==========
csv_path = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection\Question2_region_engagement.csv"
df = pd.read_csv(csv_path)


# ========== 地区英文名映射 ==========
df["region"] = df["region"].astype(str).str.strip()
region_en_map = {
    "北京市": "Beijing", "天津市": "Tianjin", "河北省": "Hebei", "山西省": "Shanxi",
    "内蒙古自治区": "Inner Mongolia", "辽宁省": "Liaoning", "吉林省": "Jilin", "黑龙江省": "Heilongjiang",
    "上海市": "Shanghai", "江苏省": "Jiangsu", "浙江省": "Zhejiang", "安徽省": "Anhui",
    "福建省": "Fujian", "江西省": "Jiangxi", "山东省": "Shandong", "河南省": "Henan",
    "湖北省": "Hubei", "湖南省": "Hunan", "广东省": "Guangdong", "广西壮族自治区": "Guangxi",
    "海南省": "Hainan", "重庆市": "Chongqing", "四川省": "Sichuan", "贵州省": "Guizhou",
    "云南省": "Yunnan", "西藏自治区": "Tibet", "陕西省": "Shaanxi", "甘肃省": "Gansu",
    "青海省": "Qinghai", "宁夏回族自治区": "Ningxia", "新疆维吾尔自治区": "Xinjiang",
    "香港特别行政区": "Hong Kong", "澳门特别行政区": "Macao", "台湾省": "Taiwan"
}
df["region_en"] = df["region"].replace(region_en_map)
quadrant_map = {
    # Quadrant I: High GDP & High AI Engagement
    "Beijing": "I: High GDP & High AI Engagement",
    "Shanghai": "I: High GDP & High AI Engagement",
    "Hunan": "I: High GDP & High AI Engagement",
    "Sichuan": "I: High GDP & High AI Engagement",
    "Henan": "I: High GDP & High AI Engagement",
    # Quadrant II: Low GDP & High AI Engagement
    "Tianjin": "II: Low GDP & High AI Engagement",
    "Jilin": "II: Low GDP & High AI Engagement",
    "Hainan": "II: Low GDP & High AI Engagement",
    "Guizhou": "II: Low GDP & High AI Engagement",
    "Shanxi": "II: Low GDP & High AI Engagement",
    "Guangxi": "II: Low GDP & High AI Engagement",
    "Chongqing": "II: Low GDP & High AI Engagement",
    "Liaoning": "II: Low GDP & High AI Engagement",
    "Yunnan": "II: Low GDP & High AI Engagement",
    "Shaanxi": "II: Low GDP & High AI Engagement",
    "Jiangxi": "II: Low GDP & High AI Engagement",
    "Anhui": "II: Low GDP & High AI Engagement",
    # Quadrant III: Low GDP & Low AI Engagement
    "Xinjiang": "III: Low GDP & Low AI Engagement",
    "Heilongjiang": "III: Low GDP & Low AI Engagement",
    "Inner Mongolia": "III: Low GDP & Low AI Engagement",
    "Gansu": "III: Low GDP & Low AI Engagement",
    "Qinghai": "III: Low GDP & Low AI Engagement",
    "Ningxia": "III: Low GDP & Low AI Engagement",
    "Macao": "III: Low GDP & Low AI Engagement",
    "Hong Kong": "III: Low GDP & Low AI Engagement",
    "Xizang": "III: Low GDP & Low AI Engagement",
    "Tibet": "III: Low GDP & Low AI Engagement",
    # Quadrant IV: High GDP & Low AI Engagement
    "Guangdong": "IV: High GDP & Low AI Engagement",
    "Jiangsu": "IV: High GDP & Low AI Engagement",
    "Zhejiang": "IV: High GDP & Low AI Engagement",
    "Shandong": "IV: High GDP & Low AI Engagement",
    "Fujian": "IV: High GDP & Low AI Engagement",
    "Hubei": "IV: High GDP & Low AI Engagement",
    "Hebei": "IV: High GDP & Low AI Engagement",
    "Taiwan": "IV: High GDP & Low AI Engagement"
}
df["quadrant"] = df["region_en"].map(quadrant_map)
df["quadrant"] = df["quadrant"].fillna("Unclassified")


# ===================== 数据处理 =====================
df = df.rename(columns={
    "avg_video_score": "video_score",
    "avg_comment_score": "comment_score"
})
df["size_score"] = np.exp(df["user_engagement_avg_score"] / 5)
video_median = df["video_score"].median()
comment_median = df["comment_score"].median()


# ========== 绘图 ==========
quadrant_colors = {
    "I: High GDP & High AI Engagement": "#4A90E2",    # 学术蓝
    "II: Low GDP & High AI Engagement": "#50C878",    # 学术绿
    "III: Low GDP & Low AI Engagement": "#B2B2B2",    # 高级灰
    "IV: High GDP & Low AI Engagement": "#FF6B6B",    # 柔和红
    "Unclassified": "#808080"
}
pio.templates.default = "plotly_white"
category_orders = {
    "quadrant": [
        "I: High GDP & High AI Engagement",
        "II: Low GDP & High AI Engagement",
        "III: Low GDP & Low AI Engagement",
        "IV: High GDP & Low AI Engagement",
        "Unclassified"
    ]
}
fig = px.scatter(
    df,
    x="video_score",
    y="comment_score",
    color="quadrant",
    color_discrete_map=quadrant_colors,
    size="size_score",
    size_max=50,
    text="region_en",
    hover_name="region_en",
    hover_data={
        "user_engagement_avg_score": ":,.2f",
        "video_score": ":,.2f",
        "comment_score": ":,.2f",
        "size_score": False,
        "quadrant": True,
        "region_en": False
    },
    title="Four Quadrant Diagram for users' AI Learning Engagement on Bilibili",
    labels={
        "video_score": "Video Score (Engagement Degree)",
        "comment_score": "Comment Score (Engagement Quality)",
        "user_engagement_avg_score": "User Engagement Score",
        "quadrant": "New Region Pattern"
    },
    category_orders=category_orders
)
fig.update_traces(
    textposition="top center",
    textfont=dict(size=11, color="#2C3E50", family="Times New Roman"),
    marker=dict(opacity=0.85, line=dict(color="white", width=1.5))
)
fig.add_vline(x=video_median, line_dash="dash", line_color="#636363", line_width=1.5)
fig.add_hline(y=comment_median, line_dash="dash", line_color="#636363", line_width=1.5)
fig.update_layout(
    title=dict(font=dict(size=18, weight="bold", family="Times New Roman"), x=0.5),
    xaxis=dict(title=dict(font=dict(size=14, weight="bold", family="Times New Roman")), gridcolor="#ECECEC", tickfont=dict(size=11)),
    yaxis=dict(title=dict(font=dict(size=14, weight="bold", family="Times New Roman")), gridcolor="#ECECEC", tickfont=dict(size=11)),
    width=1600, height=1000,
    margin=dict(l=50, r=50, t=90, b=50),
    plot_bgcolor="white",
    paper_bgcolor="white",
    legend=dict(
        orientation="v",
        yanchor="top", y=1,
        xanchor="left", x=1.02,
        font=dict(size=11, family="Times New Roman")
    )
)
x_min, x_max = df["video_score"].min(), df["video_score"].max()
y_min, y_max = df["comment_score"].min(), df["comment_score"].max()
fig.add_annotation(
    x=x_max * 0.98, y=y_max * 0.98,
    text="High Layer Level & High Comment Quality",
    showarrow=False,
    font=dict(size=12, weight="bold", family="Arial"),
    xanchor="right", yanchor="top",
    bordercolor="black",
    borderwidth=1
)
fig.add_annotation(
    x=x_min * 1.02, y=y_max * 0.98,
    text="Low Layer Level & High Comment Quality",
    showarrow=False,
    font=dict(size=12, weight="bold", family="Arial"),
    xanchor="left", yanchor="top",
    bordercolor="black",
    borderwidth=1
)
fig.add_annotation(
    x=x_min * 1.01, y=y_min * 1.01,
    text="Low Layer Level & Low Comment Quality",
    showarrow=False,
    font=dict(size=12, weight="bold", family="Arial"),
    xanchor="left", yanchor="bottom",
    bordercolor="black",
    borderwidth=1
)
fig.add_annotation(
    x=x_max * 0.99, y=y_min * 1.01,
    text="High Layer Level & Low Comment Quality",
    showarrow=False,
    font=dict(size=12, weight="bold", family="Arial"),
    xanchor="right", yanchor="bottom",
    bordercolor="black",
    borderwidth=1
)


# ========== 导出 ==========
html_path = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection\Question2_score_analysis.html"
fig.write_html(html_path, include_plotlyjs=True)
print(f"\n--> Diagram is generated successfully, saved to:")
print(html_path)