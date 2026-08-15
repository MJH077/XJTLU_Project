# ========== 导入依赖 ==========
import pandas as pd
import os
import re
import webbrowser
from pyecharts import options as opts
from pyecharts.charts import Map


# ========== 全局配置 ==========
target_folder = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question1_Collection"  # 文件保存地址
summary_ip = "Question1_summary_ip.csv"
heat_map = "Question1_heat_map.html"
ip_column = "IP"
china_short_names = {
    "北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江",
    "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南",
    "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州",
    "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆",
    "中国香港", "中国澳门", "中国台湾"
}
china_regions = {
    "北京": "北京市", "天津": "天津市", "河北": "河北省", "山西": "山西省",
    "内蒙古": "内蒙古自治区", "辽宁": "辽宁省", "吉林": "吉林省", "黑龙江": "黑龙江省",
    "上海": "上海市", "江苏": "江苏省", "浙江": "浙江省", "安徽": "安徽省",
    "福建": "福建省", "江西": "江西省", "山东": "山东省", "河南": "河南省",
    "湖北": "湖北省", "湖南": "湖南省", "广东": "广东省", "广西": "广西壮族自治区",
    "海南": "海南省", "重庆": "重庆市", "四川": "四川省", "贵州": "贵州省",
    "云南": "云南省", "西藏": "西藏自治区", "陕西": "陕西省", "甘肃": "甘肃省",
    "青海": "青海省", "宁夏": "宁夏回族自治区", "新疆": "新疆维吾尔自治区",
    "中国香港": "香港特别行政区", "中国澳门": "澳门特别行政区", "中国台湾": "台湾省"
}


# ========== 工具函数 ==========
def get_all_csv_files(folder_path):
    if not os.path.exists(folder_path):
        print(f"The target folder does not be found: {folder_path}")
        folder_path = "./"  # 切换到当前文件夹
    csv_files = []
    files = 0
    for file in os.listdir(folder_path):
        if re.match(r"^BV.*\.csv$", file, re.IGNORECASE):
            full_path = os.path.join(folder_path, file)
            csv_files.append(full_path)
            files = files + 1
    if not csv_files:
        print("Couldn't find any files")
        return []
    print(f"\n--> Have already got {files} valid videos on Bilibili")
    return csv_files

"""批量读取CSV文件，合并IP属地数据"""
def batch_read_and_merge(csv_files):
    all_ip_data = []
    for file in csv_files:
        try:
            df = pd.read_csv(file, usecols=[ip_column], encoding="utf-8-sig")
            df = df[df[ip_column].notna()]
            df = df[df[ip_column] != "Unknown IP"]
            df = df[df[ip_column].isin(china_regions)]
            all_ip_data.extend(df[ip_column].tolist())
        except Exception as e:
            print(f"Couldn't access {file}: {str(e)}")
            continue
    if not all_ip_data:
        print("Couldn't access any valid IP address")
        return pd.DataFrame()
    ip_series = pd.Series(all_ip_data)
    ip_summary = ip_series.value_counts().reset_index()
    ip_summary.columns = ["Province/Region", "Comment Count"]
    ip_summary["Province/Region"] =ip_summary["Province/Region"].map(china_regions)
    ip_summary["Comment Count"] = pd.to_numeric(ip_summary["Comment Count"], errors='coerce').fillna(0)
    # 保存汇总表到CSV
    name = os.path.join(target_folder, summary_ip)
    ip_summary.to_csv(name, index=False, encoding="utf-8-sig")
    print(f"\n--> Comments in videos are merged and cleaned successfully, saved to:")
    print(name)
    total_valid = ip_summary["Comment Count"].sum()
    print(f"\n--> Have already got {total_valid} valid comments from videos")
    return ip_summary

"""生成中国IP属地热力图"""
def generate_heatmap(ip_summary):
    if ip_summary.empty:
        print("Failed to access the Heat Map")
        return
    # 1. 数据准备（确保列名匹配）
    data_pair = ip_summary[["Province/Region", "Comment Count"]].values.tolist()
    total_valid = ip_summary["Comment Count"].sum()
    max_count = ip_summary["Comment Count"].max()
    # 2. 绘制热力图
    map_chart = (
        Map(init_opts=opts.InitOpts(
            width="1500px",
            height="1000px",
            page_title="IP Distribution of Bilibili Channel Comments"
        ))
        .add(
            series_name=" ",
            data_pair=data_pair,
            maptype="china",
            zoom=1.1,
            is_map_symbol_show=False
        )
        .set_global_opts(
            title_opts=opts.TitleOpts(
                title="IP Distribution of Bilibili Video Comments",
                subtitle=f"Valid IP Addresses: {total_valid}",
                title_textstyle_opts=opts.TextStyleOpts(
                    font_size=20, font_weight="bold", color="#222222"
                ),
                subtitle_textstyle_opts=opts.TextStyleOpts(
                    font_size=14, color="#666666"
                ),
                pos_left="center",
                pos_top="30px"
            ),
            visualmap_opts=opts.VisualMapOpts(
                is_piecewise=True,
                is_show=True,
                pieces=[
                    {"min": 1, "max": 19, "label": "1-19", "color": "#FFCCCC"},  # 浅红（高饱和）
                    {"min": 20, "max": 39, "label": "20-39", "color": "#FF9999"},  # 中浅红（高饱和）
                    {"min": 40, "max": 59, "label": "40-59", "color": "#FF6666"},  # 中红（高饱和）
                    {"min": 60, "max": max_count, "label": f">=60", "color": "#FF3333"}  # 深红（高饱和）
                ],
                pos_left="25%",
                pos_bottom="25%",
                item_width=30,
                item_height=25,
                textstyle_opts=opts.TextStyleOpts(font_size=11, color="#333333"),
            ),
            tooltip_opts=opts.TooltipOpts(
                trigger="item",
                formatter="{b}: {c} comments"
            ),
            legend_opts=opts.LegendOpts(is_show=False)
        )
        .set_series_opts(
            label_opts=opts.LabelOpts(
                is_show=True,
                font_size=10,
                color="#333333",
                formatter="{b}"
            ),
            itemstyle_opts=opts.ItemStyleOpts(
                border_color="#E0E0E0",
                border_width=0.8
            )
        )
    )
    # 3. 保存并自动打开HTML
    name = os.path.join(target_folder, heat_map)
    map_chart.render(name)
    print(f"\n--> Heat Map is generated successfully, saved to:")
    print(name)
    # 自动弹出Chrome打开
    webbrowser.open_new_tab(name)


# ===================== 主程序 =====================
def main():
    # 步骤1：获取所有BV开头的CSV文件
    csv_files = get_all_csv_files(target_folder)
    if not csv_files:
        return
    # 步骤2：批量读取并汇总IP数据
    ip_summary = batch_read_and_merge(csv_files)
    if ip_summary.empty:
        return
    # 步骤3：生成热力图
    generate_heatmap(ip_summary)

if __name__ == "__main__":
    main()