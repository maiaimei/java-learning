import sys
import requests
import time
import random
import logging
from bs4 import BeautifulSoup

sys.stdout.reconfigure(encoding='utf-8')  # 修改标准输出为 UTF-8

# 配置日志
logging.basicConfig(
    level=logging.INFO,  # 设置日志级别为 INFO
    format="%(asctime)s - %(levelname)s - %(message)s",  # 日志格式
    handlers=[
        logging.StreamHandler()  # 输出到控制台
    ]
)

def file_lines_to_urls(file_path):
    """
    读取文件内容，将每一行以 ": " 分隔，
    将第一个数组值拼接到 "https://openjdk.org/jeps/" 末尾，返回结果列表。
    """
    result = []
    try:
        with open(file_path, "r", encoding="utf-8") as file:
            for line in file:
                # 去除行首尾的空白字符
                line = line.strip()
                # 跳过空行
                if not line:
                    continue
                # 按 ": " 分隔
                parts = line.split(": ", 1)
                if len(parts) > 1:  # 确保有分隔符
                    # 拼接 URL
                    url = f"https://openjdk.org/jeps/{parts[0]}"
                    result.append(url)
    except FileNotFoundError:
        logging.error(f"File not found: {file_path}")
    except Exception as e:
        logging.error(f"An error occurred: {e}")
    return result

def file_lines_to_dict(file_path):
    """
    将文件的每一行转换为字典，结构为：
    {"jep": "158", "feature": "Unified JVM Logging", "url": "https://openjdk.org/jeps/158"}
    """
    result = []
    try:
        with open(file_path, "r", encoding="utf-8") as file:
            for line in file:
                # 去除行首尾的空白字符
                line = line.strip()
                logging.info(f"Processing file line: {line}")
                # 跳过空行
                if not line:
                    continue
                # 按 ": " 分隔
                parts = line.split(": ", 1)
                if len(parts) == 2:  # 确保分隔成功
                    jep = parts[0]
                    desc = parts[1]
                    url = f"https://openjdk.org/jeps/{jep}"
                    result.append({"jep": jep, "desc": desc, "url": url})
    except FileNotFoundError:
        logging.error(f"File not found: {file_path}")
    except Exception as e:
        logging.error(f"An error occurred: {e}")
    return result

def extract_jep_component(url):
    """
    下载网页内容并解析表格，提取 Component 的信息。
    """
    try:
        # 下载网页内容
        logging.info(f"Fetching content from: {url}")
        response = requests.get(url)
        response.raise_for_status()  # 检查请求是否成功
        html_content = response.text

        # 使用 BeautifulSoup 解析 HTML 内容
        soup = BeautifulSoup(html_content, 'html.parser')

        # 找到包含表格的部分
        table = soup.find('table', class_='head')
        if not table:
            logging.info("Table not found in the webpage.")
            return None

        # 提取表格中的所有行
        rows = table.find_all('tr')
        result = {}

        # 遍历每一行，查找 Component 为指定值的内容
        for row in rows:
            cells = row.find_all('td')
            if len(cells) == 2:  # 确保是两列的行
                key = cells[0].get_text(strip=True)
                value = cells[1].get_text(strip=True)
                result[key] = value

        return result

    except requests.exceptions.RequestException as e:
        logging.error(f"Error fetching the URL: {e}")
        return None

def main():
    """
    主函数：用户输入文件路径，解析文件内容并提取目标信息。
    """
    logging.info("Welcome to the Web Content Extractor!")
    logging.info("This program extracts specific information from multiple webpages.")
    
    # 用户输入文件路径
    file_path = input("Please enter the file path: ").strip()
    logging.info(f"Processing file: {file_path}")

    # 读取文件内容并转换为字典列表, 结构为：[{'jep': '102', 'feature': 'Process API Updates', 'url': 'https://openjdk.org/jeps/102'}]
    dict_list = file_lines_to_dict(file_path)
    logging.info(f"Loaded {len(dict_list)} entries from the file.")

    # 遍历字典列表并调用解析函数
    for entry in dict_list:
        url = entry['url']
        logging.info(f"Processing URL: {url}")

        # 随机延迟，避免频繁请求
        delay = random.uniform(1, 5)  # 随机延迟 1 到 5 秒
        logging.info(f"Waiting for {delay:.2f} seconds before making the request...")
        time.sleep(delay)  # 延迟执行

        result = extract_jep_component(url)
        
        # 打印结果
        if result:
            for key, value in result.items():
                if key == 'Component':
                    entry['component'] = value.replace("\u2009", " ")  # 将提取的 Component 信息且替换窄空格为普通空格再添加到字典中
                    logging.info(f"Extracted Component: {entry['component']}")
                    entry['type'] = get_type_by_component(entry['component'])  # 获取组件类型并添加到字典中
                    entry['link'] = f"=HYPERLINK(\"https://openjdk.org/jeps/{entry['jep']}\",\"JEP {entry['jep']}: {entry['desc']}\")" # 添加链接到字典中
                    entry['title'] = f"JEP {entry['jep']}: {entry['desc']}" # 添加标题到字典中
        else:
            logging.warning(f"No relevant information found for URL: {url}")

    # 对 dict_list 进行排序
    dict_list.sort(key=lambda x: (x.get('type', ''), x.get('component', ''), x.get('title', '')))
    
    # 打印结果
    print("~".join(['Type', 'Component', 'JEP', 'Feature', 'Link', 'Title']))  # 打印表头
    for entry in dict_list:
        print("~".join(str(entry.get(key, "")) for key in ['type', 'component', 'jep', 'desc', 'link', 'title']))

# 处理不同的组件类型
def get_type_by_component(component):
    if component == 'core-libs / java.lang':
        return "Language"
    elif component.startswith("core-libs"):
        return "API"
    elif component.startswith("security-libs"):
        return "API"
    elif component == 'hotspot / gc':
        return "GC"   
    elif component.startswith("hotspot"):
        return "JVM"
    elif component.startswith("tools"):
        return "Tool"
    else:
        return "Other"

if __name__ == "__main__":
    main()

# file_lines_to_urls示例调用
# file_path = "C:/Users/lenovo/Desktop/jeps.txt"  # 替换为你的文件路径
# urls = file_lines_to_urls(file_path)
# print(urls)

# file_lines_to_dict示例调用
# file_path = "C:/Users/lenovo/Desktop/jeps.txt"  # 替换为你的文件路径
# dict_list = file_lines_to_dict(file_path)
# print(dict_list)