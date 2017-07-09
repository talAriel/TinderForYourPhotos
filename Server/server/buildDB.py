import MySQLdb
'''
    this script builds the entire database from a csv file
    the file name is given as an argument
'''

cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')

########################################## init main table ##########################################

create_table_query = ("CREATE TABLE images "
                      "("
                      "url VARCHAR(256) NOT NULL PRIMARY KEY, "
                      "objectsh INTEGER NOT NULL, "
                      "finalrank DECIMAL(6,5) NOT NULL,"
                      "facerank INTEGER NOT NULL, "
                      "strongcol1percent INTEGER NOT NULL"
                      ")"
                     )

init_query = ("LOAD DATA INFILE '/var/lib/mysql-files/photos.csv'"
                "INTO TABLE images "
                "FIELDS TERMINATED BY ',' "
                "LINES TERMINATED BY '\n' "
                "IGNORE 1 LINES"
              )

add_col_query = "ALTER TABLE images \n" \
                "ADD liked VARCHAR(1) NOT NULL DEFAULT 'I'," \
                "ADD retrieved BOOLEAN NOT NULL DEFAULT FALSE," \
                "ADD prob DECIMAL(6,5) NOT NULL DEFAULT 0"

# create the table
cnx.cursor().execute(create_table_query)

# insert the data
cnx.cursor().execute(init_query)

# update to new cols in the new table above
cnx.cursor().execute(add_col_query)

########################################## statistics ##########################################

create_table_query = ("CREATE TABLE stats ("
                     "val INTEGER NOT NULL PRIMARY KEY, "
                     "objectsh_good DECIMAL(6,5) NOT NULL, "
                     "objectsh_bad DECIMAL(6,5) NOT NULL, "
                     "facerank_good DECIMAL(6,5) NOT NULL, "
                     "facerank_bad DECIMAL(6,5) NOT NULL, "
                     "finalrank_good DECIMAL(6,5) NOT NULL, "
                     "finalrank_bad DECIMAL(6,5) NOT NULL, "
                     "strongcol1percent_good DECIMAL(6,5) NOT NULL, "
                     "strongcol1percent_bad DECIMAL(6,5) NOT NULL"
                     ")"
                    )

init_query = "LOAD DATA INFILE '/var/lib/mysql-files/stats.csv' INTO TABLE stats FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' IGNORE 1 LINES"

cnx.cursor().execute(create_table_query)
cnx.cursor().execute(init_query)


cnx.commit()
cnx.close()

