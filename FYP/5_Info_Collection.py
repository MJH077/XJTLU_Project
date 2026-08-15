# ========== 导入依赖 ==========
import os.path
import requests
import re
import csv
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options


# ========== 全局配置 ==========
chrome_driver_path = r"C:\Users\ASUS\AppData\Local\Programs\Python\Python313\chromedriver.exe"  # ChromeDriver 路径
base_save_path = r"Question2_Collection/Question2_summary_info.csv"  # 文件保存地址
request_timeout = 10  # 请求超时时间
request_delay = 0.3  # 请求间隔


# ========== 自动登录获取Cookie ==========
def get_cookie():
    opt = Options()
    opt.add_argument("--start-maximized")
    opt.add_experimental_option("excludeSwitches", ["enable-automation"])
    driver = webdriver.Chrome(service=Service(chrome_driver_path), options=opt)
    driver.get("https://www.bilibili.com/")
    print("\n--> Please press 'Enter' after you login Bilibili")
    input()
    cookies = driver.get_cookies()
    driver.quit()
    cookie_str = "; ".join([f"{c['name']}={c['value']}" for c in cookies])
    return cookie_str


# ========== 抓取视频基本信息函数 ==========
"""从B站视频链接中提取BV号"""
def get_bv(url):
    bv_pattern = re.compile(r"BV[a-zA-Z0-9]+")
    match = bv_pattern.search(url)
    return match.group() if match else None

"""通过BV号获取视频基本信息"""
def get_video_base_info(bv_id, cookie):
    url = f"https://api.bilibili.com/x/web-interface/view?bvid={bv_id}"
    headers = {"User-Agent": "Mozilla/5.0", "Cookie": cookie}
    try:
        # 发送请求获取视频信息
        response = requests.get(url, headers=headers, timeout=request_timeout)
        response.raise_for_status()
        data = response.json()
        if data.get("code") == 0:
            video_data = data["data"]
            # 解析核心基本信息
            base_info = {
                "bv_id": bv_id,
                "title": video_data["title"],  # 视频标题
                "up_name": video_data["owner"]["name"],  # UP主昵称
                "up_id": video_data["owner"]["mid"],  # UP主唯一ID
                "description": video_data["desc"].replace("\n", " ").strip(),  # 视频简介/笔记
                "like": video_data["stat"]["like"],  # 点赞数
                "coin": video_data["stat"]["coin"],  # 投币数
                "collect": video_data["stat"]["favorite"],  # 收藏数
                "comment": video_data["stat"]["reply"],  # 评论总数
                "view": video_data["stat"]["view"],  # 播放量
                "duration(s)": video_data["duration"],  # 视频时长（原始秒数）
                "release": time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(video_data["pubdate"])), # 发布时间格式化
            }
            time.sleep(request_delay)  # 请求间隔防反爬
            return base_info
        else:
            print(f"Fail to get video info：{data.get('message')}")
            return None
    except Exception as e:
        print(f"Exception of getting video info：{str(e)}")
        return None

"""保存视频基本信息到指定路径"""
def save_video_info_to_csv(video_info):
    if not os.path.exists(base_save_path):
        os.makedirs(base_save_path)
    file_exists = os.path.exists(base_save_path)
    with open(base_save_path, "a+", newline="", encoding="utf-8-sig") as f:
        # 定义CSV列名
        fieldnames = ["bv_id", "title", "up_name", "up_id", "description", "like",
                      "coin", "collect", "comment", "view", "duration(s)", "release"]
        w = csv.DictWriter(f, fieldnames=fieldnames)
        if not file_exists or os.path.getsize(base_save_path) == 0:
            w.writeheader()
        w.writerow(video_info)
    print(f"--> Have already saved to:")
    print(base_save_path)


# ========== 运行主程序 ==========
if __name__ == "__main__":
    # 1. 登录B站获取Cookie
    cookie = get_cookie()
    # 2. 输入视频链接
    url = input("--> Please enter the link of video: ").strip()
    print()
    # 3. 提取BV号并抓取视频基本信息
    bv_id = get_bv(url)
    if not bv_id:
        print("Failed to extract BV number from the link")
    else:
        video_info = get_video_base_info(bv_id, cookie)
        # 4. 输出视频基本信息
        if video_info:
            for key, value in video_info.items():
                print(f"{key}: {value}")
            print()
            save_video_info_to_csv(video_info)
        else:
            print("Failed to get video basic information")