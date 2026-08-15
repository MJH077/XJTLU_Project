# ========== 导入依赖 ==========
import os
import csv
import re


# ========== 全局配置 ==========
input_path = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection\Question2_summary_info.csv"  # 存放视频基础信息的文件
output_path = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection/Question2_classified_videos.csv"  # 分类结果保存的文件
classify_keywords = {
    "Foundation": [
        "微积分", "线性代数", "高等数学", "统计", "概率论", "离散数学",
        "贝叶斯公式", "复变函数", "数学原理", "数学基础", "数值分析"
    ],
    "Method": [
        "机器学习", "自然语言处理", "神经网络", "深度学习", "大模型", "python",
        "算法", "特征工程", "Transformer", "聚类", "分类", "回归", "NLP", "CNN", "RNN",
        "卷积神经网络", "循环神经网络", "强化学习", "监督学习", "无监督学习"
    ],
    "Application": ["金融", "体育", "教育", "电商", "商业", "动漫", "电影",
                    "医疗", "动漫", "企业", "健身", "生态环境", "养老", "留学"
    ]
}


# ========== 视频分类 ==========
"""读取原始CSV中的视频数据（bv_id, title, description）"""
def load_video_data_from_csv(csv_path):
    video_data = []
    if not os.path.exists(csv_path):
        print(f"File doesn't be found: {csv_path}")
        return video_data
    with open(csv_path, "r", encoding="utf-8-sig") as f:
        reader = csv.DictReader(f)
        required_cols = ["bv_id", "title", "description"]  # 检查必要列是否存在
        if not all(col in reader.fieldnames for col in required_cols):
            print(f"File lacks of: {required_cols}")
            return video_data
        for row in reader:
            video_data.append({
                "bv_id": row["bv_id"].strip(),
                "title": row["title"].strip().lower(),
                "description": row["description"].strip().lower()
            })
    return video_data

"""根据title/description关键字分类视频"""
def classify_video(video):
    content = (video["title"] + " " + video["description"]).lower()
    # 1. 匹配application类
    for keyword in classify_keywords["Application"]:
        if re.search(keyword.lower(), content):
            return "Application"
    # 2. 匹配foundation类
    for keyword in classify_keywords["Foundation"]:
        if re.search(keyword.lower(), content):
            return "Foundation"
    # 3. 匹配method类
    for keyword in classify_keywords["Method"]:
        if re.search(keyword.lower(), content):
            return "Method"
    return "Method"

"""将分类后的视频保存到新文件夹"""
def classified_videos_to_csv(classified_videos):
    all_data = []
    for video in classified_videos:
        category = video["category"]
        all_data.append({
            "bv_id": video["bv_id"],
            "category": category
        })
    with open(output_path, "w", newline="", encoding="utf-8-sig") as f:
        fieldnames = ["bv_id", "category"]
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(all_data)
    # 打印分类统计
    foundation_count = len([v for v in classified_videos if v["category"] == "Foundation"])
    method_count = len([v for v in classified_videos if v["category"] == "Method"])
    application_count = len([v for v in classified_videos if v["category"] == "Application"])
    print(f"\n--> Grouping result has already saved to:")
    print(f"{output_path}")
    print(f"\nFoundation：{foundation_count}")
    print(f"Method: {method_count}")
    print(f"Application: {application_count}")


# ========== 主程序 ==========
if __name__ == "__main__":
    # 步骤1：读取原始CSV视频数据
    video_data = load_video_data_from_csv(input_path)
    if not video_data:
        exit(1)
    # 步骤2：对每个视频进行分类
    classified_videos = []
    for video in video_data:
        category = classify_video(video)
        classified_videos.append({
            "bv_id": video["bv_id"],
            "category": category
        })
    # 步骤3：保存到新文件夹
    classified_videos_to_csv(classified_videos)