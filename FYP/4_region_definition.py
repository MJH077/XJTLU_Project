# ========== 导入依赖库 ==========
import pandas as pd
import plotly.graph_objects as go
import numpy as np

# ========== 全局配置 ==========
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
# 对GDP去除1个最大、1个最小
gdp_trimmed = df["GDP (100M RMB)"].nlargest(len(df)-1).nsmallest(len(df)-2)
# 对参与度去除1个最大、1个最小
part_trimmed = df["Comment Count (Person)"].nlargest(len(df)-1).nsmallest(len(df)-2)
x_mean = gdp_trimmed.mean()
y_mean = part_trimmed.mean()
save_file = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question1_Collection\Question1_four_quadrant.html"


# ========== 绘制学术风交互式四象限图 ==========
fig = go.Figure()
fig.add_trace(go.Scatter(
    x=df["GDP (100M RMB)"],
    y=df["Comment Count (Person)"],
    text=df["Province/Region"],
    mode="markers+text",
    textposition="top right",
    marker=dict(
        size=10,
        color="#1f4e79",
        opacity=0.85,
        line=dict(width=1, color="white")
    ),
    textfont=dict(size=10, color="#2c3e50"),
    hovertemplate="<b>%{text}</b><br>GDP：%{x} 100M RMB<br>Comment Count：%{y} person",
    showlegend=False
))

# 四象限分界线
fig.add_vline(x=x_mean, line=dict(color="#7f8c8d", width=1.5, dash="solid"))
fig.add_hline(y=y_mean, line=dict(color="#7f8c8d", width=1.5, dash="solid"))

# 四象限标签
quad_labels = [
    dict(x=x_mean*1.45, y=y_mean*1.45, text="I: High GDP & High AI Engagement", color="#3498db"),
    dict(x=x_mean*0.25, y=y_mean*1.45, text="II: Low GDP & High AI Engagement", color="#f39c12"),
    dict(x=x_mean*0.25, y=y_mean*0.35, text="III: Low GDP & Low AI Engagement", color="#95a5a5"),
    dict(x=x_mean*1.45, y=y_mean*0.35, text="IV: High GDP & Low AI Engagement", color="#e74c3c")
]
for q in quad_labels:
    fig.add_annotation(
        x=q["x"], y=q["y"],
        text=q["text"],
        showarrow=False,
        font=dict(size=12, color=q["color"], family="Times New Roman"),
        bgcolor="white",
        bordercolor=q["color"],
        borderwidth=1
    )

fig.update_layout(
    title=dict(
        text="Four Quadrant Analysis (without Max and Min)",
        font=dict(size=18, weight="bold", family="Times New Roman", color="black"),
        x=0.5
    ),
    xaxis=dict(
        title="GDP (100M RMB)",
        title_font=dict(size=13, weight="bold", family="Times New Roman"),
        showline=True, linewidth=1.5, linecolor="black",  # 横轴实线
        showgrid=False,
        zeroline=False
    ),
    yaxis=dict(
        title="Comment Count (Person)",
        title_font=dict(size=13, weight="bold", family="Times New Roman"),
        showline=True, linewidth=1.5, linecolor="black",  # 纵轴实线
        showgrid=False,
        zeroline=False
    ),
    plot_bgcolor="white",
    paper_bgcolor="white",
    margin=dict(l=50, r=30, t=60, b=50),
    font=dict(family="Times New Roman")
)

fig.update_xaxes(showticklabels=True)
fig.update_yaxes(showticklabels=True)


# ========== 保存为 HTML ==========
fig.write_html(save_file)
print(f"\n--> Diagram is generated successfully, saved to:")
print(save_file)