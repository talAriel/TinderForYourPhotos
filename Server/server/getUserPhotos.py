import MySQLdb
import createJson


query_get = "SELECT url FROM images WHERE liked<>'D'"

cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')
cursorA = cnx.cursor()

# get the answer
cursorA.execute(query_get)
url_list = []
for url in cursorA:
    url_list.append(url[0])

cnx.commit()
cnx.close()
print(createJson.convertToJson(url_list))

