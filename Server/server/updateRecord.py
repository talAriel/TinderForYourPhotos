import MySQLdb
import sys
import math

# python updateRecord.py url liked(Y)/unLiked(N)

cnx = MySQLdb.connect(user='root', passwd='tinder',
                              host='127.0.0.1',
                              db='images')

args = sys.argv
url = args[1]
liked = args[2]

# liked can get the value of 'Y' or 'N'
query_update = "UPDATE images SET liked = '" + liked + "' WHERE url = '" + url + "'"
cnx.cursor().execute(query_update)

# Query DB for the url - get features
cursorB = cnx.cursor()
query = "SELECT objectsh, finalrank, facerank, strongcol1percent FROM images WHERE url = '" + url + "'"
cursorB.execute(query)
liked_attr = {}
for row in cursorB:
    liked_attr["objectsh"] = row[0]
    liked_attr["finalrank"] = row[1]
    liked_attr["facerank"] = row[2]
    liked_attr["strongcol1percent"] = row[3]

# update stats - gaussian distribution

# Define Gaussian formula with mean and variance


def Gaussian(x, mu):
    sigma = 1
    return 1 / (math.sqrt((sigma ** 2) * 2 * math.pi)) * math.exp(- (x - mu) ** 2 / (2 * sigma))

gauss_matrix = []
headers = ["objectsh", "finalrank", "facerank", "strongcol1percent"]
for i in range(10):
    lst = []
    for name in headers:
            lst.append(Gaussian(float(i),float(liked_attr[name])))
    gauss_matrix.append(lst)

type = "_good"
if liked == 'N':
    type = "_bad"

query = "SELECT objectsh" + type + ", finalrank" + type + ", facerank" + type + ", strongcol1percent" + type + " FROM stats"
cursorE = cnx.cursor()
cursorE.execute(query)
stat= list(cursorE.fetchall())
stat_matrix = [list(row) for row in stat]

assert(len(stat_matrix) == len(gauss_matrix))

# calculate the new value for the prob
for i in range(len(stat_matrix)):
    for j in range(len(stat_matrix[i])):
        stat_matrix[i][j] = 0.9 * float(stat_matrix[i][j]) + 0.1 * gauss_matrix[i][j]

# normalize the score in stat_matrix
sum_objectsh = 0
sum_finalrank = 0
sum_facerank = 0
sum_strongcol1percent = 0
for i in range(len(stat_matrix)):
    sum_objectsh += stat_matrix[i][0]
    sum_finalrank += stat_matrix[i][1]
    sum_facerank += stat_matrix[i][2]
    sum_strongcol1percent += stat_matrix[i][3]

for i in range(len(stat_matrix)):
    stat_matrix[i][0] = stat_matrix[i][0] / sum_objectsh
    stat_matrix[i][1] = stat_matrix[i][1] / sum_finalrank
    stat_matrix[i][2] = stat_matrix[i][2] / sum_facerank
    stat_matrix[i][3] = stat_matrix[i][3] / sum_strongcol1percent

for i in range(len(stat_matrix)):
    query = ("UPDATE stats SET"
             " objectsh" + type + " = " + str(stat_matrix[i][0]) + ","
             " finalrank" + type + " = " + str(stat_matrix[i][1]) + ","
             " facerank" + type + " = " + str(stat_matrix[i][2]) + ","
             " strongcol1percent" + type + " = " + str(stat_matrix[i][3]) + ""
             " WHERE val = " + str(i) + ""
             )
    cnx.cursor().execute(query)

query = "SELECT url, objectsh, finalrank, facerank, strongcol1percent FROM images"
cursorC = cnx.cursor()
cursorC.execute(query)

# update prob for images in images by naive bayes score
query = "SELECT * FROM stats"
cursorD = cnx.cursor()
cursorD.execute(query)

# stats is a list of tuples, which each tuple defines a row from the table
stat = list(cursorD.fetchall())

for row in cursorC:
    i_url = row[0]
    i_objectsh = float(row[1])
    i_finalrank = float(row[2])
    i_facerank = float(row[3])
    i_strongcol1percent = float(row[4])
    p_objectsh_g = stat[int(i_objectsh + 0.5) % 10][1]
    p_finalrank_g = stat[int(i_finalrank + 0.5) % 10][3]
    p_facerank_g = stat[int(i_facerank + 0.5) % 10][5]
    p_strongcol1percent_g = stat[int(i_strongcol1percent + 0.5) % 10][7]
    p_objectsh_b = stat[int(i_objectsh + 0.5) % 10][2]
    p_finalrank_b = stat[int(i_finalrank + 0.5) % 10][4]
    p_facerank_b = stat[int(i_facerank + 0.5) % 10][6]
    p_strongcol1percent_b = stat[int(i_strongcol1percent + 0.5) % 10][8]
    mul_good = p_objectsh_g * p_finalrank_g * p_facerank_g * p_strongcol1percent_g
    mul_bad = p_objectsh_b * p_finalrank_b * p_facerank_b * p_strongcol1percent_b
    prob = 0 if (((mul_good) + (mul_bad)) == 0) else (mul_good) / ((mul_good) + (mul_bad))
    query_update = "UPDATE images SET prob = " + str(prob) + " WHERE url = '" + i_url + "'"
    cnx.cursor().execute(query_update)

cnx.commit()
cnx.close()

