import sys
import requests
from bs4 import BeautifulSoup

sys.stdout.reconfigure(encoding='utf-8')  # 修改标准输出为 UTF-8

def extract_jep_component(url):
    """
    下载网页内容并解析表格，提取 Component 的信息。
    """
    try:
        # 下载网页内容
        print(f"Fetching content from: {url}")
        response = requests.get(url)
        response.raise_for_status()  # 检查请求是否成功
        html_content = response.text

        # 使用 BeautifulSoup 解析 HTML 内容
        soup = BeautifulSoup(html_content, 'html.parser')

        # 找到包含表格的部分
        table = soup.find('table', class_='head')
        if not table:
            print("Table not found in the webpage.")
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
        print(f"Error fetching the URL: {e}")
        return None

def main():
    """
    主函数：用户输入网页地址，解析网页内容并提取目标信息。
    """
    print("Welcome to the Web Content Extractor!")
    print("This program extracts specific information from a webpage.")
        
    # 用户输入网页地址
    url = input("Please enter the webpage URL: ").strip()
    
    # 调用解析函数
    result = extract_jep_component(url)
    
    # 打印结果
    if result:
        print("\nExtracted Information:")
        for key, value in result.items():
            if key == 'Component':
                print(f"{key}: {value}")
    else:
        print("\nNo relevant information found.")

if __name__ == "__main__":
    main()