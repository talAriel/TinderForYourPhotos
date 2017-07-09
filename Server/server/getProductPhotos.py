import MySQLdb
import unicodedata
import createJson


cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')

cursorA = cnx.cursor()
cursorA.execute("SELECT url, MAX((prob)) as score from images WHERE liked = 'I' AND retrieved = FALSE GROUP BY url ORDER BY score DESC LIMIT 17")

url_list = []
for url in cursorA:
    url_list.append(url[0])

cnx.commit()
cnx.close()
print(createJson.convertToJson(url_list))

