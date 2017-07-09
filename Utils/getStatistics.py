import pandas as pd

objectsh = [0.0]*10
facerank = [0.0]*10
finalrank = [0.0]*10
strongcol1percent = [0.0]*10

def stat(data, hist):
    sum = data.count()
    for num in data:
        hist[(int(num + 0.5) % 10)] += 1.0 / sum

def write2file(name, hist):
    f = open(name + ".csv","w")
    f.write("value,good,bad\n")
    for i in range(len(hist)):
        f.write(str(i) + "," + str(hist[i]) + "," + str(hist[i]) + "\n")
    f.close()


data = pd.read_csv('photos.csv', sep=',')
stat(data["objectsh"], objectsh)
stat(data["facerank"], facerank)
stat(data["finalrank"], finalrank)
stat(data["strongcol1percent"], strongcol1percent)

write2file("objectsh", objectsh)
write2file("facerank", facerank)
write2file("finalrank", finalrank)
write2file("strongcol1percent", strongcol1percent)

new_file = open("stats.csv", "w")
new_file.write("value,objectsh_good,objectsh_bad,facerank_good,facerank_bad,finalrank_good,finalrank_bad,strongcol1percent_good,strongcol1percent_bad\n")
iter = len(objectsh)
for i in range(iter):
    new_file.write(str(i) + "," + str(objectsh[i]) + "," + str(objectsh[i]) + ",")
    new_file.write(str(facerank[i]) + "," + str(facerank[i]) + ",")
    new_file.write(str(finalrank[i]) + "," + str(finalrank[i]) + ",")
    new_file.write(str(strongcol1percent[i]) + "," + str(strongcol1percent[i]) + "\n")

new_file.close()
del data