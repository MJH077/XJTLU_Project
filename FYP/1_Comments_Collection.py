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
base_save_path = r"E:\XJTLU\Year4\BUS303\FYP research\FYP\Question1_Collection"  # 文件保存地址
comments_amount = 50  # 需要评论的数量
sort = 1   # 热度升序
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


# ========== 抓取信息函数 ==========
"""从B站视频链接中提取BV号"""
def get_bv(url):
    bv_pattern = re.compile(r"BV[a-zA-Z0-9]+")
    match = bv_pattern.search(url)
    return match.group() if match else None

"""通过BV号获取视频的aid （评论api所必需）"""
def get_video_aid_from_bv(bv_id, cookie):
    url = f"https://api.bilibili.com/x/web-interface/view?bvid={bv_id}"
    headers = {"User-Agent": "Mozilla/5.0", "Cookie": cookie}
    try:
        response = requests.get(url, headers=headers, timeout=request_timeout)
        response.raise_for_status()
        data = response.json()
        if data.get("code") == 0:
            return data["data"]["aid"]
        else:
            print(f"Fail to get aid：{data.get('message')}")
            return None
    except Exception as e:
        print(f"Exception of getting aid：{str(e)}")
        return None

"""调用B站评论API获取评论"""
def get_comments(aid, cookie):
    all_reply = []
    for page in range(1, 4):
        comment_api = "https://api.bilibili.com/x/v2/reply"
        params = {"type": 1, "oid": aid, "pn": page, "ps": 20, "sort": sort}
        headers = {"User-Agent": "Mozilla/5.0", "Cookie": cookie, "Referer": "https://www.bilibili.com/"}
        response = requests.get(comment_api, params=params, headers=headers).json()
        reply = response.get("data", {}).get("replies", [])
        if not reply: break
        all_reply.extend(reply)
        time.sleep(request_delay)
    target = all_reply[1:1 + comments_amount]
    res = []
    for index, reply in enumerate(target, 1):
        raw_ip = (
                reply.get("reply_control", {}).get("location")
                or reply.get("ip_location", "")
                or "Unknown IP"
        )
        ip = raw_ip.replace("IP属地：", "").replace("IP属地:", "").strip()
        ip = ip if ip else "Unknown"
        res.append({
            "Index": index,
            "User_ID": reply["member"]["uname"],
            "Content": reply["content"]["message"].replace("\n", " "),
            "IP": ip
        })
    return res

"""保存到指定路径"""
def save_csv(comments, bv_id):
    name = os.path.join(base_save_path, f"{bv_id}.csv")
    with open(name, "w", newline="", encoding="utf-8-sig") as f:
        w = csv.DictWriter(f, fieldnames=["Index", "User_ID", "Content", "IP"])
        w.writeheader()
        w.writerows(comments)
    print(f"--> Have already saved to:")
    print(name)

# ========== 运行主程序 ==========
if __name__ == "__main__":
    # 1. 登录获取Cookie
    cookie = get_cookie()
    # 2. 输入链接
    url = input("--> Please enter the link of video: ").strip()
    print()
    # 3. 抓取
    bv_id = get_bv(url)
    aid = get_video_aid_from_bv(bv_id, cookie)
    comments = get_comments(aid, cookie)
    # 4. 输出
    for c in comments:
        print(f"[{c['Index']}] {c['User_ID']} | {c['IP']}")
        print(f"{c['Content']}\n")
    save_csv(comments, bv_id)