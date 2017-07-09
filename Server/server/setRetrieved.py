import MySQLdb

cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')


query_update = "UPDATE images SET retrieved = FALSE WHERE retrieved = TRUE AND liked = 'I'"
cnx.cursor().execute(query_update)

cnx.commit()
cnx.close()
