import MySQLdb
import createJson

cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')
url_list = []

cursorA = cnx.cursor()
cursorB = cnx.cursor()
cursorC = cnx.cursor()
cursorD = cnx.cursor()

# Query 1
query1 = "SELECT url FROM images WHERE liked = 'I' AND retrieved = FALSE GROUP BY url ORDER BY objectsh DESC LIMIT 1"
cursorA.execute(query1)
url1 = ""
for url in cursorA:
    url1 = url[0]
    url_list.append(url1)

# Query 2
query2 = "SELECT url FROM images WHERE liked = 'I' AND retrieved = FALSE AND url <> '" + url1 + "' GROUP BY url ORDER BY finalrank DESC LIMIT 1"
cursorB.execute(query2)
url2 = ""
for url in cursorB:
    url2 = url[0]
    url_list.append(url2)

# Query 3
query3 = "SELECT url FROM images WHERE liked = 'I' AND retrieved = FALSE AND url <> '" + url1 + "' AND url <> '" + url2 + "' GROUP BY url ORDER BY facerank DESC LIMIT 1"
cursorC.execute(query3)
url3 = ""
for url in cursorC:
    url3 = url[0]
    url_list.append(url3)
# Query 4
query4 = "SELECT url FROM images WHERE liked = 'I' AND retrieved = FALSE AND url <> '" + url1 + "' AND url <> '" + url2 + "' AND url <> '" + url3 + "' GROUP BY url ORDER BY strongcol1percent DESC LIMIT 1"
cursorD.execute(query4)
url4 = ""
for url in cursorD:
    url4 = url[0]
    url_list.append(url4)

cnx.cursor().execute("UPDATE images SET retrieved = TRUE WHERE url = '" + url1 + "' OR url = '" + url2 + "' OR url = '" + url3 + "' OR url = '" + url4 + "'")

cnx.commit()
cnx.close()
print(createJson.convertToJson(url_list))

