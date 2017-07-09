import MySQLdb
'''
    this script delete a whole table, given as an argument
'''
cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')

tables = ["images", "stats"]
# delete query
for table in tables:
    delete_table_query = ("DROP TABLE " + table)
    # execute the query
    cnx.cursor().execute(delete_table_query)

cnx.commit()
cnx.close()

