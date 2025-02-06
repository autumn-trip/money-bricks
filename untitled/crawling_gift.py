import time
import pandas as pd
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.chrome.options import Options


# 크롬 드라이버 옵션 설정
chrome_options = Options()
chrome_options.add_experimental_option("detach", True)
chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])
driver = webdriver.Chrome(options=chrome_options)


# ✅ WebDriver 실행 옵션 설정
options = webdriver.ChromeOptions()
options.add_argument("--headless")  # 화면 없이 실행
options.add_argument("--no-sandbox")  # 보안 샌드박스 비활성화
options.add_argument("--disable-dev-shm-usage")  # 메모리 공유 비활성화
options.add_argument("--disable-gpu")  # GPU 가속 비활성화
options.add_argument("--remote-debugging-port=9222")  # 원격 디버깅 포트 설정
# options.binary_location = "/usr/bin/chromium-browser"  # Chrome 위치 지정


# 🛠 크롤링할 URL
url = "https://biz.giftishow.com/bizshop?rows=100"
driver.get(url)

# ⏳ 페이지 로딩 대기
time.sleep(5)  # 동적 페이지 로딩을 기다림

# 🛠 상품 정보 가져오기
products = driver.find_elements(By.CLASS_NAME, "goods-item-area")

product_list = []
for product in products:
    try:
        brand = product.find_element(By.CLASS_NAME, "brand-name").text.strip()
        name = product.find_element(By.CLASS_NAME, "goods-name").text.strip()
        img_url = product.find_element(By.TAG_NAME, "img").get_attribute("src")
        discount_rate = product.find_element(By.CLASS_NAME, "txt-discount-rate").text.strip()
        discount_price = product.find_element(By.CLASS_NAME, "discount-price").text.strip()
        original_price = product.find_element(By.CLASS_NAME, "sale-price").text.strip()

        product_list.append([brand, name, img_url, discount_rate, discount_price, original_price])
    except Exception as e:
        print(f"❌ 오류 발생: {e}")
        continue

# 🛠 DataFrame 변환
df = pd.DataFrame(product_list, columns=["브랜드명", "상품명", "이미지 URL", "할인율", "할인가(원)", "원가(원)"])

# ✅ CSV 저장
df.to_csv("giftishow_products.csv", index=False, encoding="utf-8-sig")

# ✅ 크롤링 완료 및 출력
print("✅ 크롤링 완료! CSV 파일이 저장되었습니다.")
df.head()