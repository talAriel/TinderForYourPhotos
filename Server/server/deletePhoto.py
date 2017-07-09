import MySQLdb
import sys


cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')

args = sys.argv
url = args[1]

cnx.cursor().execute("UPDATE images SET liked = 'D' WHERE url = '" + url + "'")

cnx.commit()
cnx.close()

