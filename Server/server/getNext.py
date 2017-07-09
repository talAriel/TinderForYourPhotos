import MySQLdb
import createJson


cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')

cursorA = cnx.cursor()
query_get = "SELECT url, MIN(ABS(0.5 - prob)) as distance2half from images WHERE liked = 'I' AND retrieved = FALSE GROUP BY url ORDER BY distance2half ASC LIMIT 1"
cursorA.execute(query_get)

url_list = []
for url in cursorA:
    url_list.append(url[0])

# update that the photo was retrieved
cnx.cursor().execute("UPDATE images SET retrieved = TRUE WHERE url ='" + url_list[0] + "'")

cnx.commit()
cnx.close()
print(createJson.convertToJson(url_list))


