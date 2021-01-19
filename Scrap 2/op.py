from selenium.common.exceptions import StaleElementReferenceException, NoSuchElementException
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support import expected_conditions as EC
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait
import os

direc = os.path.dirname(os.path.realpath(__file__))
print(direc)
direcChrome = os.path.dirname(os.path.realpath(__file__)) + "/chromedriver"
print(direcChrome)
username = 'ga87ken'
password = '-'
query = 'US AND Wahlen'
timeframe_begin = '03.10.2020'
timeframe_end = '24.10.2020'
url = 'https://login.eaccess.ub.tum.de/login?qurl=https://ezb.uni-regensburg.de%2fwarpto.phtml%3fbibid%3dTUM%26colors' \
      '%3d7%26lang%3dde%26jour_id%3d104479%26url%3dhttp%253A%252F%252Flibrarynet.szarchiv.de '
options = Options()
options.add_argument("start-maximized")
options.add_argument("disable-infobars")
options.add_argument("--disable-extensions")
driver = webdriver.Chrome(executable_path=direcChrome, options=options)
driver.get(url)


def login():
    driver.find_element_by_xpath('//*[@id="user"]').send_keys(username)
    driver.find_element_by_xpath('//*[@id="pass"]').send_keys(password)
    loginButton = driver.find_element_by_class_name('csc-mailform-submit')
    loginButton.click()


def search():
    driver.find_element_by_xpath('//*[@id="searchTerm"]').send_keys(query)
    driver.find_element_by_xpath('//*[@id="searchBtn"]').click()


def changeDate():
    driver.find_element_by_xpath('//*[@id="dateChip"]/h4').click()
    if timeframe_begin == '':
        driver.find_element_by_xpath('//*[@id="selectFromTimePoint"]/option[13]').click()
    else:
        driver.find_element_by_xpath('//*[@id="fromDate"]').clear()
        driver.find_element_by_xpath('//*[@id="fromDate"]').send_keys(timeframe_begin)

    if timeframe_end == '':
        driver.find_element_by_xpath('//*[@id="selectToTimePoint"]/option[2]').click()
    else:
        driver.find_element_by_xpath('//*[@id="toDate"]').clear()
        driver.find_element_by_xpath('//*[@id="toDate"]').send_keys(timeframe_end)

def downloadArticle():
    WebDriverWait(driver, 5).until(EC.element_to_be_clickable((By.CLASS_NAME, 'fullTextLeft')))
    textFields = driver.find_element_by_id('articleTextContainer').find_elements_by_tag_name('p')
    date = driver.find_element_by_class_name('fullTextLeftTop').find_element_by_tag_name('span').text
    title = driver.find_element_by_tag_name('h1').find_element_by_tag_name('font').text

    invalid = '<>:"/\|?*'
    filename = title + ' ' + date + ".txt"
    for char in invalid:
        filename = filename.replace(char, '')

    print(filename)
    with open(direc + '/results/' + filename, 'w+', encoding="utf-8") as file:
        for textField in textFields:
            file.write(textField.text + '\n')


def download():
    WebDriverWait(driver, 5).until(EC.element_to_be_clickable((By.CLASS_NAME, 'hitContentTitle ')))

    # Find first article
    article = driver.find_element_by_class_name('hitContentTitle ')

    # Find link for full text and click it
    link = article.find_element_by_tag_name('a')
    link.click()

    title = ""
    tries = 0

    while True:
        try:
            print(tries)
            tries += 1
            newTitle = driver.find_element_by_tag_name('h1').find_element_by_tag_name('font').text
        except (StaleElementReferenceException, NoSuchElementException):
            newTitle = title

        if title != newTitle or tries > 500:
             tries = 0
             title = newTitle
             downloadArticle()
             next = driver.find_elements_by_class_name('iconGoNext')[2]
             next.click()


login()
changeDate()
search()
download()
