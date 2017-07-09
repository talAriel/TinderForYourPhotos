def convertToJson(url_list):
    urls = "[\n"
    for line in url_list:
        urls += "\t{\n"
        urls += "\t\t" + "\"url\" : \"" + line + "\"\n"
        urls += "\t},\n"
    urls=urls[::-1].replace(",","",1)[::-1]
    urls += "]"
    return urls
