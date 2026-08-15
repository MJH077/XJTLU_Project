# ========== 导入依赖 ==========
import os
import csv
import re
from datetime import datetime


# ========== 全局配置 ==========
"""
视频参数权重配置：信息系统成功模型（权重设计核心依据）
DeLone, W. H., & McLean, E. R. (2003). 
The DeLone and McLean model of information systems success: A ten-year update. 
Journal of Management Information Systems, 19(4), 9–30.
"""
video_weights = {
    "group": 0.60,          # 内容分类（核心维度）60% - 信息质量
    "interaction": 0.20,    # 互动数据 20% - 用户满意度
    "duration": 0.12,       # 视频时长 12% - 内容呈现形式
    "release_time": 0.08    # 发布时间 8% - 时效性
}

"""
内容分类评分（基于知识共享理论：应用型>方法型>基础型）
Wasko, M. M., & Faraj, S. (2005). Why should I share? 
Examining social capital and knowledge contribution in electronic networks of practice. 
MIS Quarterly, 29(1), 35–57.
"""
group_score_map = {
    "Foundation": 50,   # 基础科普类：基准分
    "Method": 75,       # 方法、技术类：高价值知识
    "Application": 100  # 实践、应用类：顶级价值知识
}

"""
视频时长评分标准（基于教育视频最优时长研究）
Müller, M., et al. (2020). Optimal video length in technology-enhanced learning. 
IEEE Transactions on Learning Technologies, 13(1), 227–239.
"""
duration_rules = {
    "optimal_min": 180,      # 3分钟
    "optimal_max": 7200,     # 2小时
    "poor_min_score": 50,    # 过短/过长基础分
    "score_scale": 100       # 满分
}

"""
发布时间衰减模型（基于信息生命周期理论）
Starbuck, W. H. (1976). Organizations and their environments. 
Harvard Business School Press
"""
time_decay_rules = {
    "fresh_days": 180,       # 6个月内：全新
    "valid_days": 365*2,     # 2年内：有效
    "obsolete_days": 365*3,  # 3年以上：过时
    "fresh_score": 100,
    "valid_score": 80,
    "old_score": 50,
    "obsolete_score": 30
}

# 5. 评论关键词评分配置
comment_keywords = {
    # 高价值关键词（+10分/个）
    "high_value": [
        "人工智能", "贝叶斯", "transformer", "机器学习", "深度学习", "神经网络", "AI",
        "算法", "模型", "特征工程", "聚类", "分类", "回归", "卷积神经网络", "循环神经网络",
        "NLP", "CNN", "RNN", "强化学习", "监督学习", "无监督学习", "大模型", "prompt",
        "注意力机制", "梯度下降", "损失函数", "准确率", "微积分", "F1值", "算力", "数据集"
    ],
    # 低价值关键词（-5分/个）
    "low_value": ["路过", "刷到", "互赞", "互关", "抽奖", "福利", "广告", "营销",
                  "水军", "无关", "不懂", "划走", "没意思", "水视频", "恰饭", "取关", "举报"
    ],
    # 中性关键词（+5分）
    "neutral": [
        "三连", "求资料", "私", "已收藏", "求链接", "求资源", "打卡", "学习", "分享",
        "感谢", "有用", "没用", "清晰", "模糊", "干货", "水货", "充电"
    ]
}

"""
评论价值评分（基于评论有用性理论）
Cheung, C. M. K., Lee, M. K. O., & Rabson, C. A. (2008). 
What drives consumers to spread electronic word of mouth in online 
consumer-opinion platforms? Decision Support Systems, 46(1), 461–469
"""
comment_score_rules = {
    "base_score": 50,           # 评论基础分（中性）
    "high_value_score": 10,     # 专业词汇+10分（知识密度权重）
    "low_value_score": -5,      # 低质词汇-5分（社区噪音惩罚）
    "max_score": 100,           # 评论最高分上限
    "min_score": 0              # 评论最低分下限
}

"""
用户参与度权重（基于TAM模型：感知价值>用户表达）
Davis, F. D. (1989). Perceived usefulness, 
perceived ease of use, and user acceptance of information technology. 
MIS Quarterly, 13(3), 319–340.
"""
participation_weights = {
    "video_value": 0.65,        # 视频价值60%权重（内容质量核心）：外因
    "comment_value": 0.35       # 评论价值40%权重（用户反馈补充）：内因
}

# 配置文件路径（需根据实际路径修改）
classification_csv = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection\Question2_classified_videos.csv"
basic_info_csv = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection\Question2_summary_info.csv"
comment_folder = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question1_Collection"
output_csv = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection\Question2_user_engagement.csv"
stats_output_csv = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question2_Collection\Question2_region_engagement.csv"

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
china_short_region_set = set(china_regions.keys())


# ========== 工具函数 ==========
"""加载视频分类数据"""
def load_video_classification(csv_path):
    video_group_map = {}
    with open(csv_path, "r", encoding="utf-8-sig") as f:
        reader = csv.DictReader(f)
        for row in reader:
            bv_id = row.get("bv_id", "").strip()  # 用get方法容错，避免KeyError
            group = row.get("category", "").strip()
            if bv_id and group:  # 跳过空值
                video_group_map[bv_id] = group
    return video_group_map

"""加载视频基础数据"""
def load_video_basic_info(csv_path):
    video_info_map = {}
    # 若文件不存在，返回空字典并提示
    if not os.path.exists(csv_path):
        print(f"Warning: Video basic info {csv_path} doesn't exist, set 40 by default")
        return video_info_map
    with open(csv_path, "r", encoding="utf-8-sig") as f:
        reader = csv.DictReader(f)
        for row in reader:
            bv_id = row.get("bv_id", "").strip()
            if not bv_id:
                continue
            def safe_int(val):
                try:
                    return int(float(val))
                except (ValueError, TypeError):
                    return 0
            video_info_map[bv_id] = {
                "like": safe_int(row.get("like", 0)),
                "coin": safe_int(row.get("coin", 0)),
                "collect": safe_int(row.get("collect", 0)),
                "comment": safe_int(row.get("comment", 0)),
                "view": safe_int(row.get("view", 0)),
                "duration": safe_int(row.get("duration(s)", 0)),
                "release": row.get("release", "")
            }
    return video_info_map

"""数据归一化（0~100分）"""
def normalize_score(value, min_val, max_val):
    if max_val - min_val == 0:
        return 50  # 无波动时给基础分
    normalized = (value - min_val) / (max_val - min_val) * 100
    return max(0, min(100, normalized))  # 限制在0~100

"""计算单视频价值评分（总分100）"""
def calculate_video_value(bv_id, video_group_map, video_info_map):
    # 1. 分组评分（60%权重）
    group = video_group_map.get(bv_id, "Foundation")
    group_score = group_score_map.get(group, 50)
    # 2. 互动数据评分（20%权重）：点赞+投币+收藏+评论+播放 归一化
    info = video_info_map.get(bv_id, {})
    interaction_metrics = [info["like"], info["coin"], info["collect"], info["comment"], info["view"]]
    # 全局互动数据极值（用于归一化）
    all_interaction = []
    for v in video_info_map.values():
        all_interaction.extend([v["like"], v["coin"], v["collect"], v["comment"], v["view"]])
    min_inter = min(all_interaction) if all_interaction else 0
    max_inter = max(all_interaction) if all_interaction else 1
    # 单视频互动总分
    inter_total = sum(interaction_metrics)
    inter_score = normalize_score(inter_total, min_inter, max_inter)
    # 3. 视频时长评分（教育视频最优时长理论）
    duration = info.get("duration", 0)
    if duration_rules["optimal_min"] <= duration <= duration_rules["optimal_max"]:
        duration_score = 100
    elif duration < 60:  # <1分钟
        duration_score = duration_rules["poor_min_score"]
    elif 60 <= duration < duration_rules["optimal_min"]:
        duration_score = 70
    elif duration > duration_rules["optimal_max"]:
        duration_score = 60
    else:
        duration_score = 50
    # 4. 发布时间评分（信息生命周期衰减理论）
    release_score = time_decay_rules["valid_score"]
    try:
        release_dt = datetime.strptime(info["release"], "%Y-%m-%d %H:%M:%S")
        days_diff = (datetime.now() - release_dt).days
        if days_diff <= time_decay_rules["fresh_days"]:
            release_score = time_decay_rules["fresh_score"]
        elif days_diff <= time_decay_rules["valid_days"]:
            release_score = time_decay_rules["valid_score"]
        elif days_diff <= time_decay_rules["obsolete_days"]:
            release_score = time_decay_rules["old_score"]
        else:
            release_score = time_decay_rules["obsolete_score"]
    except:
        pass
    # 5. 视频总分 = 各维度加权求和
    total_video_score = (
            group_score * video_weights["group"] +
            inter_score * video_weights["interaction"] +
            duration_score * video_weights["duration"] +
            release_score * video_weights["release_time"]
    )
    total_video_score = max(0, min(100, round(total_video_score, 2)))
    return {
        "bv_id": bv_id,
        "group": group,
        "group_score": group_score,
        "interaction_score": round(inter_score,2),
        "duration_score": duration_score,
        "release_score": release_score,
        "video_total_score": total_video_score
    }


"""计算单条评论的价值评分"""
def calculate_single_comment_value(comment_content):
    keyword_stats = {
        "high_value": 0,
        "low_value": 0,
        "neutral": 0
    }
    total_score = comment_score_rules["base_score"]
    content = comment_content.lower()
    # 匹配高价值关键词
    for kw in comment_keywords["high_value"]:
        if re.search(kw.lower(), content):
            keyword_stats["high_value"] += 1
            total_score += comment_score_rules["high_value_score"]
    # 匹配低价值关键词
    for kw in comment_keywords["low_value"]:
        if re.search(kw.lower(), content):
            keyword_stats["low_value"] += 1
            total_score += comment_score_rules["low_value_score"]
    # 匹配中性关键词（仅统计）
    for kw in comment_keywords["neutral"]:
        if re.search(kw.lower(), content):
            keyword_stats["neutral"] += 1
    # 限制分数范围
    total_score = max(comment_score_rules["min_score"], min(comment_score_rules["max_score"], total_score))
    return {
        "comment_total_score": total_score,
        "keyword_stats": keyword_stats
    }


# ========== 主程序 ==========
if __name__ == "__main__":
    # 步骤1：加载基础数据
    video_group_map = load_video_classification(classification_csv)
    # 强制检查类型，避免后续报错
    if not isinstance(video_group_map, dict):
        raise TypeError("video_group_map must be type 'map'")
    # 加载视频基础数据
    video_info_map = load_video_basic_info(basic_info_csv)
    if not isinstance(video_info_map, dict):
        raise TypeError("video_info_map must be type 'map'")
    # 步骤2：预计算所有视频的视频价值评分
    video_score_map = {}
    for bv_id in video_group_map.keys():
        v_score = calculate_video_value(bv_id, video_group_map, video_info_map)
        video_score_map[bv_id] = v_score
    # 步骤3：遍历所有评论文件
    all_user_records = []
    for bv_id in video_group_map.keys():
        comment_file = os.path.join(comment_folder, f"{bv_id}.csv")
        if not os.path.exists(comment_file):
            print(f"Warning: Comment file for {bv_id} not found, skipped")
            continue
        # 获取该视频的视频价值评分
        video_score = video_score_map.get(bv_id, {})
        if not video_score:
            continue
        # 读取该视频下的每条评论
        with open(comment_file, "r", encoding="utf-8-sig") as f:
            reader = csv.DictReader(f)
            for row in reader:
                user_id = row.get("User_ID", "unknown_user").strip()
                content = row.get("Content", "").strip()
                ip = row.get("IP", "unknown").strip()
                index = row.get("Index", "").strip()
                if not content:
                    continue  # 跳过空评论
                # 判断地址是否为中国境内
                is_china_ip = False
                for region in china_short_region_set:
                    if region in ip:
                        is_china_ip = True
                        break
                if not is_china_ip:
                    continue  # 跳过境外IP的整条记录
                # 计算本条评论的价值
                comment_score = calculate_single_comment_value(content)
                # 计算用户满意度
                engagement_score = (
                        video_score["video_total_score"] * participation_weights["video_value"] +
                        comment_score["comment_total_score"] * participation_weights["comment_value"]
                )
                engagement_score = max(0, min(100, round(engagement_score, 2)))
                # 组装记录
                all_user_records.append({
                    "bv_id": bv_id,
                    "user_id": user_id,
                    "user_ip": ip,
                    "user_engagement_score": engagement_score,
                    "video_total_score": video_score["video_total_score"],
                    "comment_total_score": comment_score["comment_total_score"],
                    # "group": video_score["group"],
                    # "group_score": video_score["group_score"],
                    # "interaction_score": video_score["interaction_score"],
                    # "duration_score": video_score["duration_score"],
                    # "release_score": video_score["release_score"],
                    # "comment_index": index,
                    # "comment_content": content,
                    # "high_value_keywords": comment_score["keyword_stats"]["high_value"],
                    # "low_value_keywords": comment_score["keyword_stats"]["low_value"],
                    # "neutral_keywords": comment_score["keyword_stats"]["neutral"]
                })
    # 步骤4：保存到CSV
    with open(output_csv, "w", newline="", encoding="utf-8-sig") as f:
        fieldnames = [
            "bv_id", "user_id", "user_ip", "user_engagement_score", "video_total_score",
            "comment_total_score"
        ]
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(all_user_records)
    print(f"\n--> All user engagement records have been saved to:")
    print(output_csv)
    print(f"Total user records: {len(all_user_records)}")
    # 步骤5：计算各ip的用户参与度均值
    region_stats = {}
    for record in all_user_records:
        ip = record["user_ip"]
        engagement = record["user_engagement_score"]
        video = record["video_total_score"]
        comment = record["comment_total_score"]
        # 匹配对应的省份/地区全称
        region_name = "Unknown"
        for short_name, full_name in china_regions.items():
            if short_name in ip:
                region_name = full_name
                break
        # 保存该地区所有分数（用于去极值）
        if region_name not in region_stats:
            region_stats[region_name] = {
                "engagements": [],
                "videos": [],
                "comments": []
            }
        region_stats[region_name]["engagements"].append(engagement)
        region_stats[region_name]["videos"].append(video)
        region_stats[region_name]["comments"].append(comment)
    # 计算：去掉1个最高 + 去掉1个最低 → 平均分
    region_avg_list = []
    for region, data in region_stats.items():
        # 1) 处理参与度
        eng = sorted(data["engagements"])
        n_eng = len(eng)
        if n_eng >= 3:
            eng_trim = eng[1:-1]
            eng_avg = round(sum(eng_trim) / len(eng_trim), 2)
            final_count = len(eng_trim)
        elif n_eng == 2:
            eng_avg = round(sum(eng) / 2, 2)
            final_count = 2
        else:
            eng_avg = round(eng[0], 2) if n_eng == 1 else 0
            final_count = n_eng
        # 2) 处理视频分 video_score
        vid = sorted(data["videos"])
        n_vid = len(vid)
        if n_vid >= 3:
            vid_trim = vid[1:-1]
            vid_avg = round(sum(vid_trim) / len(vid_trim), 2)
        elif n_vid == 2:
            vid_avg = round(sum(vid) / 2, 2)
        else:
            vid_avg = round(vid[0], 2) if n_vid == 1 else 0
        # 3) 处理评论分 comment_score
        com = sorted(data["comments"])
        n_com = len(com)
        if n_com >= 3:
            com_trim = com[1:-1]
            com_avg = round(sum(com_trim) / len(com_trim), 2)
        elif n_com == 2:
            com_avg = round(sum(com) / 2, 2)
        else:
            com_avg = round(com[0], 2) if n_com == 1 else 0
        region_avg_list.append({
            "region": region,
            "user_engagement_avg_score": eng_avg,
            "avg_video_score": vid_avg,
            "avg_comment_score": com_avg,
            "user_count": final_count
        })
    region_avg_list.sort(key=lambda x: x["user_engagement_avg_score"], reverse=True)
    with open(stats_output_csv, "w", newline="", encoding="utf-8-sig") as f:
        stats_fieldnames = ["region", "user_engagement_avg_score", "avg_video_score",
        "avg_comment_score", "user_count"]
        writer = csv.DictWriter(f, fieldnames=stats_fieldnames)
        writer.writeheader()
        writer.writerows(region_avg_list)
    print(f"\n--> Region average engagement stats saved to:")
    print(stats_output_csv)